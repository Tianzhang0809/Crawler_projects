import java.io.IOException;

/**
 * Parser interface, define a parser interface that can be used to construct
 * concrete parser, currently WalmartParser can implement this interface, in the
 * future can extend to more parser.
 * 
 * @author jiashengqiu
 * 
 */
public interface Parser {
	public int getTotalResultNumber(String query) throws IOException;

	// Method to get the number of query result.
	public Result getResult(String query, int pageNumber);
	// Method to get the actual result.

}
