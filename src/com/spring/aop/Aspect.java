package com.spring.aop;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.aop.MethodBeforeAdvice;

public class Aspect implements MethodBeforeAdvice {

	private static final Logger logger = Logger.getLogger(Aspect.class);

	@Override
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		logger.info("method name:" + method.getName());
		int i = 0;
		for (Object o : args) {
			i = i + 1;
			logger.info("arguments " + i + ":" + o.toString());
		}
		logger.info("clazz name: " + target.getClass().getName());

	}
}
