import java.io.IOException;

import Crawler.AllspiceParser;
import Crawler.BombayParser;
import Crawler.Crawler;
import Crawler.Parser;
import DataModel.DB;
import Search.Search;

public class Main {
	public static void main(String[] args) throws IOException {
		DB.setDBusernamepassword("root", "qjs");
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
			Search search = new Search();
			search.searchMenu("chicken");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
