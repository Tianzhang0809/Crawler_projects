package Crawler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DataModel.MenuItem;

public class BombayParser extends RestaurantParser {

	public BombayParser(String name, String baseurl) {
		super(name, baseurl);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void searchMenuItem() throws IOException, SQLException {
		Document doc = Jsoup.connect(menuurl).get();
		Elements elements = doc.select("h3");
		for (org.jsoup.nodes.Element e : elements) {
			Elements questions = e.select("a[href]");
			if (questions.first() != null) {
				String menuurl = questions.first().attr("abs:href");

				searchMenuItem(menuurl);

			}
		}
	}

	public boolean searchMenuItem(String url) throws IOException, SQLException {
		Document doc = Jsoup.connect(url).get();

		Elements elements = doc.select("h3");
		Elements despelems = doc.select("p");
		Iterator<Element> desiterator = despelems.iterator();
		desiterator.next();

		for (org.jsoup.nodes.Element e : elements) {
			String text = e.text();
			int index = text.indexOf(".");
			if (index > 0) {
				String menuitem = text.substring(0, index);
				MenuItem mi = new MenuItem();
				mi.setTitle(menuitem);
				Element des = desiterator.next();

				mi.setDescription(des.text());
				mi.setRestaurant(res);
				// System.out.println(res.getKey());
				// System.out.println(mi.getTitle());
				db.saveToDB(mi);
				// System.out.println(mi.getDescription());
			}
		}
		return true;
	}

	public static void main(String[] args) throws IOException, SQLException {
		BombayParser bp = new BombayParser("BombayGarden",
				"http://www.dinebombaygarden.com/");

		bp.process();
		bp.getContactUrl();
		System.out.println(bp.searchAddress());
	}
}
