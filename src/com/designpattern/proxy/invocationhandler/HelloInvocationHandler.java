package com.designpattern.proxy.invocationhandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.designpattern.proxy.Hello;

public class HelloInvocationHandler implements InvocationHandler {

	private static Logger logger = Logger
			.getLogger(HelloInvocationHandler.class);
	private Hello delegate;

	public HelloInvocationHandler(Hello obj) {
		this.delegate = obj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		logger.info(method + "begin...");
		Object result = method.invoke(delegate, args);
		logger.info(method + "end...");
		return result;
	}

}
