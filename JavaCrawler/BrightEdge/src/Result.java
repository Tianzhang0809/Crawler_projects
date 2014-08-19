import java.util.ArrayList;

/**
 * Result is a class to store the list of products from the result page given a
 * query and page number.
 * 
 * 
 * @author jiashengqiu
 * 
 */
public class Result {
	private final String query;// Query String for the result page
	private final int pageNum;// Page number of the search page
	private int size;// size of the result set in current page.
	private final String queryURL;// The query url for the result.
	private final int totalNumber;// Total number of the query result.
	private ArrayList<Product> results;// A list of product to store the
										// results.

	public Result(String query, int pageNum, String queryURL, int totalNumber,
			ArrayList<Product> results) {
		this.query = query;
		this.pageNum = pageNum;
		this.queryURL = queryURL;
		this.totalNumber = totalNumber;
		this.results = results;

		if (results == null) {
			this.size = 0;
		} else {
			this.size = results.size();
		}

	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public String getQuery() {// Get the query String
		return query;
	}

	public int getPageNum() {// Get the page Number
		return pageNum;
	}

	public int getSize() {// Get the number of query result
		return size;
	}

	public ArrayList<Product> getResults() {// Get the result list
		return results;
	}

	public String getQueryURL() {// Get the query url string
		return queryURL;
	}

	public void updateResult(ArrayList<Product> results) {
		this.results = results;
	}

}
