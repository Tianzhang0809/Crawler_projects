/**
 * This displayer class is to handle the output to console. From design
 * perspective, A Scraper can different displayer. So this class is designed for
 * future extension.
 * 
 * @author jiashengqiu
 * 
 */
public class Displayer {
	/**
	 * This method takes a result parameter and display the result set in the
	 * right format to the console.
	 * 
	 * @param result
	 */
	public void printToConsole(Result result) {
		if (result == null) {
			System.out
					.println("No results found! Please check the url, query or internet connection.");
		} else {
			System.out.println("Query input: " + result.getQuery());
			System.out.println("Query URL: " + result.getQueryURL());
			System.out.println("Number of total restults:"
					+ result.getTotalNumber());
			System.out.println("------------------------------------------");

			if (result.getPageNum() > 0) {
				System.out.println("Current page:" + result.getPageNum());
				System.out.println();
				for (Product product : result.getResults()) {
					System.out.println(product);
				}
			}
		}
	}

}
