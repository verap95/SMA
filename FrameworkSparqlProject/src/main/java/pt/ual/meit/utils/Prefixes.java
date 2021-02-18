package pt.ual.meit.utils;

import java.util.HashMap;

public class Prefixes {

	private static final HashMap<String, String> prefix = new HashMap<String,String>();

	public HashMap<String, String> setValue(String key, String value) {
		if (getPrefixes(key) == null) {
			prefix.put(key, value);
		}
		return prefix;
	}

	public String getPrefixes(String key) {
		prefix.put("xsd", "<http://www.w3.org/2001/XMLSchema#>");
		String p = prefix.get(key);
		return p;
	}
}
