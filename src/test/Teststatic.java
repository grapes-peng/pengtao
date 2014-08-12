package test;

import java.util.HashMap;
import java.util.Map;

public class Teststatic {

	public static Map<String, String> m1 = new HashMap<String, String>();

	static {
		m1.put("1", "abc");
	}

}
