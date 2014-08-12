package com.spring.context.test;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("test")
public class TestSpringContextUtil {
	private static Logger logger = Logger
			.getLogger(TestSpringContextUtil.class);

	public TestSpringContextUtil() {
		logger.info("TestSpringContextUtil is constructed!");
	}

	public void test() {
		logger.info("invoke test method of TestSpringContextUtil.");
	}
}
