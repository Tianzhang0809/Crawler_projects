package Crawler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DataModel.DB;
import DataModel.Restaurant;

public class RestaurantParser implements Parser {
	DB db = new DB();
	String baseurl = "";
	String menuurl = "";
	String contacturl = "";
	Restaurant res;
	String name;
	HashSet<String> visitedurl = new HashSet<String>();

	public RestaurantParser(String name, String baseurl) {
		this.name = name;
		this.baseurl = baseurl;
		res = new Restaurant();
		res.setName(name);
	}

	@Override
	public void process() throws IOException, SQLException {
		getContactUrl();
		if (contacturl.equals("")) {
			dfs(baseurl);
		}
		getMenuUrl();
		if (menuurl.equals("")) {
			dfs(baseurl);
		}
		String address = searchAddress();
		String phone = searchPhone();
		res.setAddress(address);
		res.setPhone(phone);
		db.saveToDB(res);
		// System.out.println(res.getKey());
		searchMenuItem();

	}

	public void searchMenuItem() throws IOException, SQLException {
		Document doc = Jsoup.connect(menuurl).get();

		Elements elements = doc.select("h3");
		for (org.jsoup.nodes.Element e : elements) {
			Elements questions = e.select("a[href]");
			if (questions.first() != null) {
				String menuurl = questions.first().attr("abs:href");

				// searchMenuItem(menuurl);

			}
		}
	}

	public void dfs(String url) {

		visitedurl.add(url);

		try {
			Document doc = Jsoup.connect(url).get();
			String text = doc.text();
			if (contacturl.equals("")) {
				if (text.contains("address") || text.contains("phone")
						|| text.contains("email")) {
					contacturl = url;
				}
			}
			if (menuurl.equals("")) {
				if (text.contains("menu") || text.contains("menus")
						|| text.contains("price")) {
					menuurl = url;
				}
			}
			Elements questions = doc.select("a[href]");
			for (Element link : questions) {
				String newurl = link.attr("abs:href");
				if (newurl != null && !visitedurl.contains(newurl)) {
					if (newurl.contains("http")) {
						if (newurl.contains(baseurl))
							dfs(newurl);
					} else {
						dfs(baseurl + newurl);
					}
				}
			}
		} catch (Exception e) {
		}

	}

	public String getContactUrl() throws IOException {
		Document doc = Jsoup.connect(baseurl).get();
		Elements links = doc.select("ul").select("a[href]");
		for (Element link : links) {
			String newurl = link.attr("abs:href");
			// System.out.println(newurl);

			if (newurl != null && newurl.contains("contact")) {
				contacturl = newurl;
				return newurl;
			}
		}
		return baseurl;
	}

	public String getMenuUrl() throws IOException {
		Document doc = Jsoup.connect(baseurl).get();
		Elements links = doc.select("ul").select("a[href]");
		for (Element link : links) {
			String newurl = link.attr("abs:href");
			// System.out.println(newurl);

			if (newurl != null && newurl.contains("menu")) {
				menuurl = newurl;
				return newurl;
			}
		}

		return baseurl;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return baseurl;
	}

	@Override
	public void setUrl(String url) {
		// TODO Auto-generated method stub
		this.baseurl = url;

	}

	public String searchPhone() throws IOException {
		Document contactdoc = Jsoup.connect(contacturl).get();

		String html = contactdoc.text();
		String res = Regex.findPhone(html);
		return res;
	}

	public String searchAddress() throws IOException {
		Document contactdoc = Jsoup.connect(contacturl).get();

		String html = contactdoc.text();
		String res = Regex.findAddress(html);
		return res;
	}

	public static void main(String[] args) throws IOException, SQLException {
		Document doc = Jsoup.connect(
				"http://www.dinebombaygarden.com/contacts.php").get();
		String html = doc.text();
		// System.out.println(Regex.findPhone(html));
	}

}
