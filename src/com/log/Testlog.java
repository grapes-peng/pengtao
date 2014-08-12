package com.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Testlog {

	private static Log log = LogFactory.getLog(Testlog.class);

	public static void main(String[] args) {
		log.debug("test-------------");
	}
}
