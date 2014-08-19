import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Actual implementation of the parser. It will go to the search url of the
 * WalMart and get the query result back from the internet. getResult method
 * will takes in two parameters, query and page number and return the result
 * object. Jsoup library is used in this class for parsing the HTML and getting
 * the text node.
 * 
 * @author jiashengqiu
 * 
 */
public class WalmartParser implements Parser {
	private int numberperpage = 16;
	private final String baseurl = "http://www.walmart.com/search/search-ng.do?search_query=";

	/**
	 * Set the number of product items per page.
	 * 
	 * @param num
	 */
	public void setNumberPerPage(int num) {
		this.numberperpage = num;
	}

	/**
	 * get the query url string by passing the page number and query.
	 * 
	 * @param query
	 * @param pageNumber
	 * @return
	 */
	public String getURL(String query, int pageNumber) {
		query = query.replace(" ", "+");
		if (pageNumber == 0) {
			return baseurl + query;
		} else {
			return baseurl + query + "&ic=" + numberperpage + "_"
					+ (pageNumber - 1) * numberperpage;
		}
	}

	/**
	 * Get the Document object by connecting to website and fetching the html
	 * which can be used in later parsing.
	 * 
	 * @param query
	 * @param pageNumber
	 * @return
	 */
	public Document getResultPage(String query, int pageNumber) {
		query = query.replace(" ", "+");
		String url = baseurl + query + "&ic=" + numberperpage + "_"
				+ (pageNumber - 1) * numberperpage;
		try {
			Document doc = Jsoup.connect(url).get();
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// Get the number of query result by passing a query string.
	@Override
	public int getTotalResultNumber(String query) {
		try {
			Document doc = getResultPage(query, 0);
			int number = getTotalResultNumber(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return numberperpage;
	}

	// Get the number of query result by passing a document object
	private int getTotalResultNumber(Document doc) {
		if (doc == null) {
			return 0;
		}
		String result = null;
		try {
			Element ele = doc.select("span.floatLeft").first();
			result = ele.text();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] splittext = result.split(" ");
		int totalnum = Integer.valueOf(splittext[0]);
		return totalnum;
	}

	@Override
	/**
	 * Get the Result object by parsing the search result page. The document object is like a tree, 
	 * the Jsoup library is used in parsing the html tag. The information is parsed level by level.
	 */
	public Result getResult(String query, int pageNumber) {
		if (pageNumber < 0) {
			pageNumber = -pageNumber;// Handle the negative page number.
										// possibly caused by typing error.
		}
		if (query == null || query.trim().length() == 0) {
			return new Result(query, pageNumber, baseurl, 0, null);
		}
		String url = getURL(query, pageNumber);
		ArrayList<Product> resultset = new ArrayList<Product>();
		Document doc = getResultPage(query, pageNumber);
		if (doc == null) {
			return null; // If cannot get document object, return null
		}
		int totalnumber = getTotalResultNumber(doc);
		if (pageNumber == 0) {
			return new Result(query, pageNumber, url, totalnumber, null);
		}
		Elements elems = doc.select("div.prodInfo");
		for (Element elem : elems) {

			String title = elem.select("a.prodLink.ListItemLink").text();

			String description = elem.select("table.ProdDescContainer").text();
			Element highlightelem = elem.select("a.prodLink.ListItemLink")
					.select("span.highlight").last();
			// Attempt to process and make shorter title, works for query like
			// "Digital Camera", "Man shoes", but not very scalable to all
			// search key words
			// if (highlightelem != null) {
			// String highlight = highlightelem.text();
			// int index = title.indexOf(highlight);
			// title = title.substring(0, index + highlight.length());
			// }
			String price = elem.select("div.camelPrice").text();
			if (price == null || price.trim().length() == 0) {
				price = elem.select("span.prefixPriceText2").text() + " "
						+ elem.select("span.camelPrice").text();
				if (price == null || price.trim().length() == 0) {
					price = elem.select("span.PriceSItalicStrikethruLtgry")
							.text() + "(List Price)";
				}
			}
			Product product = new Product(title, description, price);
			resultset.add(product);

		}

		Result result = new Result(query, pageNumber, url, totalnumber,
				resultset);

		return result;
	}

	public static void main(String[] args) {
		WalmartParser parser = new WalmartParser();
		Result result = parser.getResult("baby stroller", 1);
		Displayer displayer = new Displayer();
		displayer.printToConsole(result);
		// System.out.println(parser.getTotalResultNumber("digital camera"));
	}
}
