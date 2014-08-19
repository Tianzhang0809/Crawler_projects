package Crawler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Crawler {
	ArrayList<Parser> parsers = new ArrayList<Parser>();

	public void run() throws IOException, SQLException {
		System.out.println("Start crawling the websites...");

		for (Parser p : parsers) {
			p.process();
		}
		System.out.println("Finish!");

	}

	public void addParser(Parser p) {
		parsers.add(p);
	}

	public void clearParser() {
		parsers.clear();
	}

	public void deleteParser(Parser p) {
		parsers.remove(p);
	}

	public static void main(String[] args) throws IOException, SQLException {
		Crawler crawler = new Crawler();
		Parser aparser = new AllspiceParser("Allspice",
				"http://allspicerestaurant.com/");
		Parser bparser = new BombayParser("BombayGarden",
				"http://www.dinebombaygarden.com/");
		crawler.addParser(aparser);
		crawler.addParser(bparser);
		crawler.run();

	}
}
