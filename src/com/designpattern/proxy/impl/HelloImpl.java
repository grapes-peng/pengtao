package com.designpattern.proxy.impl;

import org.apache.log4j.Logger;

public class HelloImpl extends AbstractHello {

	private Logger logger = Logger.getLogger(HelloImpl.class);

	@Override
	public void sayHello() {
		logger.info("This is an implimatation of Hello interface for method sayHello()!");

	}

	@Override
	public void print() {
		logger.info("This is an implimatation of Hello interface for method print()!");

	}

}
