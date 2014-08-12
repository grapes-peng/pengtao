package com.cxf.rest.interceptor;

import org.apache.cxf.jaxrs.interceptor.JAXRSOutInterceptor;
import org.apache.cxf.message.Message;

public class ExceptionInterceptor extends JAXRSOutInterceptor {

	@Override
	public void handleMessage(Message message) {
		super.handleMessage(message);
//		throw new RuntimeException("test");
	}

}
