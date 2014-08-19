/**
 * Product Scraper is a class that can be used by the user. User can set the
 * parser and displayer according to their need. For example, user can set the
 * parser as WalmartParser but in the future they can set SearsParser also. It
 * support basic operation for parsing 1 parameter and 2 parameters query.
 * 
 * @author jiashengqiu
 * 
 */
public class ProductScraper {
	private Displayer displayer;
	private Parser parser;

	public void setDisplayer(Displayer displayer) {
		this.displayer = displayer;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public ProductScraper() {
		this.parser = new WalmartParser();
		this.displayer = new Displayer();
	}

	/**
	 * Output the total number of results to console
	 * 
	 * @param query
	 */
	public void parseQuery(String query) {
		Result result = parser.getResult(query, 0);
		displayer.printToConsole(result);
	}

	/**
	 * Output the result in current page to console.
	 * 
	 * @param query
	 * @param pagenum
	 */
	public void parseQuery(String query, int pagenum) {
		Result result = parser.getResult(query, pagenum);
		displayer.printToConsole(result);
	}

	public static void main(String[] args) {
		ProductScraper ws = new ProductScraper();
		ws.parseQuery("digital camera", 3);
	}
}
