package com.cxf.rest.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxrs.interceptor.JAXRSOutInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

public class CustomOutFaultInterceptor extends JAXRSOutInterceptor {
	private boolean handleMessageCalled;

	public void handleMessage(Message message) throws Fault {
		if (message.getExchange().get(
				"org.apache.cxf.systest.for-out-fault-interceptor") == null) {
			return;
		}
		handleMessageCalled = true;
		Exception ex = message.getContent(Exception.class);
		if (ex == null) {
			throw new RuntimeException("Exception is expected");
		}
		if (!(ex instanceof Fault)) {
			throw new RuntimeException("Fault is expected");
		}
		// deal with the actual exception : fault.getCause()
		HttpServletResponse response = (HttpServletResponse) message
				.getExchange().getInMessage()
				.get(AbstractHTTPDestination.HTTP_RESPONSE);
		response.setStatus(500);
		try {
			response.getOutputStream().write("<nobook/>".getBytes());
			response.getOutputStream().flush();
			message.getInterceptorChain().abort();
		} catch (IOException ioex) {
			throw new RuntimeException("Error writing the response");
		}

	}

	protected boolean handleMessageCalled() {
		return handleMessageCalled;
	}

}
