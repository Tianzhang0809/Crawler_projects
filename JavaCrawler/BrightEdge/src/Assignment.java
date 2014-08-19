/**
 * 
 * Main entrance for the program. Support two query scenarios Query 1: Total
 * number of results Given a keyword, such as "digital camera", return the total
 * number of results found. Query 2: Result Object Given a keyword (e.g.
 * "digital cameras") and page number (e.g. "1"), return the results in a result
 * object and then print results on screen. For each result, return the
 * following information: Title/Product Name (e.g.
 * "Samsung TL100 Digital Camera") Price of the product
 * 
 * 
 * @author jiashengqiu
 * 
 */

public class Assignment {
	public static void main(String[] args) {
		ProductScraper scraper = new ProductScraper();
		String query;

		if (args.length == 1) {// If user enter 1 parameter, query the number of
								// total result.
			query = args[0];

			scraper.parseQuery(query);

		} else if (args.length == 2) {// If user enter 2 parameters, output
										// result for given page to the console
			query = args[0];
			int pagenumber = 0;
			try {
				pagenumber = Integer.valueOf(args[1]);
			} catch (NumberFormatException nfe) {// Verify second input is
													// number.
				System.err
						.println("Bad input: Second input is not a valid number!");
				System.exit(0);
			}
			scraper.parseQuery(query, pagenumber);

		}

		else {// Wrong number of input
			System.out
					.println("Wrong input! Please make sure input length is 1 or 2.");
			System.exit(0);

		}
	}
}
