package Crawler;

import java.io.IOException;
import java.sql.SQLException;

public interface Parser {

	public String getUrl();

	public void setUrl(String url);

	public void process() throws IOException, SQLException;
}
