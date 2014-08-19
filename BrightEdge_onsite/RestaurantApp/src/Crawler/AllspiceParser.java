package Crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DataModel.MenuItem;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AllspiceParser extends RestaurantParser {

	public AllspiceParser(String name, String baseurl) {
		super(name, baseurl);
		// TODO Auto-generated constructor stub
	}

	@Override
	@SuppressWarnings("fallthrough")
	public void searchMenuItem() throws IOException, SQLException {
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_10);
		// webClient.getOptions().setJavaScriptEnabled(false);

		HtmlPage page = null;
		webClient.setThrowExceptionOnFailingStatusCode(false);
		webClient.setThrowExceptionOnScriptError(false);
		try {
			page = webClient.getPage(menuurl);
			Document doc = Jsoup.parse(page.asXml());
			Elements elems = doc.select("div.locu-menu-item");
			for (Element e : elems) {
				String title = e.select("div.locu-menu-item-name").first()
						.text();
				String desc = e.select("div.locu-menu-item-description")
						.first().text();
				title = title.replace("\"", "");
				desc = desc.replace("\"", "");
				MenuItem mi = new MenuItem();
				mi.setTitle(title);
				mi.setDescription(desc);
				mi.setRestaurant(res);
				// System.out.println(title);
				db.saveToDB(mi);
			}

		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException, SQLException {
		AllspiceParser ap = new AllspiceParser("Allspice",
				"http://allspicerestaurant.com/");
		ap.process();

	}
}
