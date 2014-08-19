package Search;

import java.sql.ResultSet;
import java.sql.SQLException;

import DataModel.DB;

public class Search {
	DB db = new DB();

	public void searchMenu(String keyword) throws SQLException {
		String sql = "SELECT restaurantapp.Menu.name,restaurantapp.Menu.desc,restaurantapp.Restaurant.name FROM restaurantapp.Menu INNER JOIN restaurantapp.Restaurant ON Menu.resid=Restaurant.id  where restaurantapp.Menu.name like '_%"
				+ keyword
				+ "%' or restaurantapp.Menu.desc like '_%"
				+ keyword
				+ "%';";
		ResultSet rs = db.runSql(sql);
		if (rs.next()) {
			System.out
					.println("##############  Following are the menu items you might be interested: ##############");
			System.out.println();
			System.out.println(rs.getString(1) + " from " + rs.getString(3));
			System.out.println("Description:" + rs.getString(2));
			System.out.println();
			System.out
					.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
			System.out.println();
		} else {
			System.out.println("No menu item found related to key word "
					+ keyword);

		}
		while (rs.next()) {
			System.out.println(rs.getString(1) + " from " + rs.getString(3));
			System.out.println("Description:" + rs.getString(2));
			System.out.println();
			System.out
					.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
			System.out.println();
		}

	}

	public static void main(String[] args) throws SQLException {
		Search s = new Search();
		s.searchMenu("chicken");
	}
}
