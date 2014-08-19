import java.util.Scanner;

import Crawler.AllspiceParser;
import Crawler.BombayParser;
import Crawler.Crawler;
import Crawler.Parser;
import DataModel.DB;
import Search.Search;

public class Assignment {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your database username:");
		String user = scanner.nextLine();
		System.out.println("Enter your database password:");
		String pw = scanner.nextLine();
		DB.setDBusernamepassword(user, pw);
		DB db = new DB();
		try {
			db.createDB();
			Crawler crawler = new Crawler();
			Parser aparser = new AllspiceParser("Allspice",
					"http://allspicerestaurant.com/");
			Parser bparser = new BombayParser("BombayGarden",
					"http://www.dinebombaygarden.com/");
			crawler.addParser(aparser);
			crawler.addParser(bparser);
			crawler.run();
			while (true) {
				System.out
						.println("Enter key word to search the menu or enter q! to quit (e.g. chicken) : ");
				String input = scanner.nextLine();
				if (input.trim().equals("q!")) {
					System.exit(0);
				} else {
					Search search = new Search();
					search.searchMenu(input.trim());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
