package Crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	public static void main(String args[]) {
		String res = findAddress("1602 S. El Camino Real, San Mateo CA");
		System.out.println(res);
	}

	public static String findAddress(String addr) {

		String addressPattern = "\\d+\\s+(([a-zA-Z])+.?|([a-zA-Z]+.?\\s+[a-zA-Z]+))(\\s+[a-zA-Z]*)+\\,\\s*(\\n?[a-zA-Z]*\\s*[a-zA-Z]*\\s*[a-zA-Z]*)?\\s*\\d*";
		Pattern p = Pattern.compile(addressPattern);
		Matcher m = p.matcher(addr);
		if (m.find()) {
			return m.group();
		} else {
			return null;
		}
	}

	public static String findPhone(String phone) {

		String phonePattern = "[(]?[0-9][0-9][0-9][)-]?\\s?\\s?[0-9][0-9][0-9][-]?[0-9][0-9][0-9][0-9]";

		Pattern p = Pattern.compile(phonePattern);
		Matcher m = p.matcher(phone);
		if (m.find()) {
			return m.group();
		} else {
			return null;
		}
	}
}
