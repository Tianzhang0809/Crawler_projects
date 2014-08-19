package DataModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	static String username;
	static String password;

	public boolean createDB() throws SQLException {
		String sql1 = "CREATE Database if not EXISTS Restaurantapp;";
		String sql2 = "Use Restaurantapp";
		String sql3 = "CREATE TABLE if not EXISTS `Restaurantapp`.`Restaurant` (`id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(45) NOT NULL, "
				+ "`phone` VARCHAR(45) NULL,`address` VARCHAR(80) NULL,PRIMARY KEY (`id`));";
		String sql4 = "CREATE TABLE if not EXISTS `Restaurantapp`.`Menu` "
				+ "(`id` INT NOT NULL AUTO_INCREMENT,`name` VARCHAR(100) "
				+ "NOT NULL,`desc` TEXT NULL, `resid` INT NOT NULL,"
				+ "PRIMARY KEY (`id`),INDEX `resid_idx` (`resid` ASC), "
				+ "CONSTRAINT `resid` FOREIGN KEY (`resid`)"
				+ "REFERENCES `Restaurantapp`.`Restaurant` (`id`) ON DELETE"
				+ " NO ACTION ON UPDATE NO ACTION);";

		DB db = new DB();
		db.runSql2(sql1);
		db.runSql2(sql2);
		db.runSql2(sql3);
		db.runSql2(sql4);

		return true;
	}

	public static void setDBusernamepassword(String user, String pw) {
		username = user;
		password = pw;
	}

	public Connection conn = null;

	public DB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/";
			conn = DriverManager.getConnection(url, username, password);
			// System.out.println("conn built");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ResultSet runSql(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.executeQuery(sql);
	}

	public boolean runSql2(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.execute(sql);
	}

	@Override
	protected void finalize() throws Throwable {
		if (conn != null || !conn.isClosed()) {
			conn.close();
		}
	}

	public boolean checkInDB(Restaurant res) throws SQLException {
		String sql = "select * from  `Restaurantapp`.`Restaurant` where name = '"
				+ res.getName() + "'";
		ResultSet rs = runSql(sql);

		if (rs.next()) {
			res.setKey(rs.getInt(1));
			return true;
		}
		return false;
	}

	public boolean checkInDB(MenuItem mi) throws SQLException {
		String sql = "select * from  `Restaurantapp`.`Menu` where name = \""
				+ mi.getTitle() + "\"" + " and resid = '"
				+ mi.getRestaurant().getKey() + "'";
		ResultSet rs = runSql(sql);
		if (rs.next()) {
			return true;
		}
		return false;
	}

	public boolean saveToDB(MenuItem mi) throws SQLException {
		if (checkInDB(mi)) {

			return false;
		}
		String sql = "INSERT INTO  `Restaurantapp`.`Menu` "
				+ "(name,Restaurantapp.menu.desc,resid) VALUES (?,?,?);";
		PreparedStatement stmt = this.conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, mi.getTitle());
		stmt.setString(2, mi.getDescription());
		stmt.setInt(3, mi.getRestaurant().key);

		stmt.execute();
		ResultSet keys = stmt.getGeneratedKeys();
		keys.next();
		return true;
	}

	public boolean saveToDB(Restaurant res) throws SQLException {
		if (checkInDB(res)) {
			// System.out.println(res.address);
			return false;
		}
		String sql = "INSERT INTO  `Restaurantapp`.`Restaurant` "
				+ "(name,address,phone) VALUES " + "(?,?,?);";
		PreparedStatement stmt = this.conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, res.getName());
		stmt.setString(2, res.getAddress());
		stmt.setString(3, res.getPhone());
		stmt.execute();
		ResultSet keys = stmt.getGeneratedKeys();
		keys.next();
		res.key = keys.getInt(1);
		System.out.println(res.getKey());
		return true;
	}

	public static void main(String[] args) throws SQLException {
		setDBusernamepassword("root", "qjs");
		DB db = new DB();
		db.createDB();
	}
}
