package com.spring.context;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	private static Logger logger = Logger.getLogger(SpringContextUtil.class);

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		logger.info("Set application context...");
		// System.out.println("Set application context...");
		applicationContext = arg0;

	}

	public static Object getBean(String name) throws BeansException {
		logger.info("Get application context...");
		return applicationContext.getBean(name);
	}

}
