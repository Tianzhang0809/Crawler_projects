import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Unit test for the parser functionality
 * 
 * @author jiashengqiu
 * 
 */
public class ParserTest extends TestCase {
	Parser parser = new WalmartParser();

	@Test
	public void test() {
		testResultPerPage();
		testEmptyInput();
		testLongInput();
		testTotalResult();
	}

	public void testResultPerPage() {

		// Verify whether the program gets 16 items per page when typing correct
		// page number
		assertEquals("Verify results per page", 16,
				parser.getResult("digital camera", 1).getSize());
		assertEquals("Verify results per page", 16,
				parser.getResult("baby strollers", 2).getSize());
		assertEquals("Verify results per page", 16,
				parser.getResult("baby strollers", -2).getSize());
	}

	public void testTotalResult() {
		try {
			TestCase.assertNotSame(0,
					parser.getTotalResultNumber("digital camera"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testEmptyInput() {
		// Test empty input
		assertEquals("Verify results per page", 0, parser.getResult("", 1)
				.getSize());
	}

	public void testLongInput() {
		assertEquals("Verify results per page", 16,
				parser.getResult("smart phone and laptop", 1).getSize());
		assertEquals("Verify results per page", 16,
				parser.getResult("interesting facts about world history", 1)
						.getSize());
	}
}
