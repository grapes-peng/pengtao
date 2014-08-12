package com.cxf.rest.interceptor;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.ext.MessageContext;

@Provider
public class InvokeFaultExceptionMapper implements
		ExceptionMapper<BookException> {
	@Context
	private MessageContext mc;

	public Response toResponse(BookException ex) {
		StackTraceElement[] trace = new StackTraceElement[1];
		trace[0] = ex.getStackTrace()[0];
		ex.setStackTrace(trace);
		System.out.println("处理自定义的异常");
		ResponseBuilder rb = Response
				.status(Response.Status.INTERNAL_SERVER_ERROR);
		rb.type("application/json;charset=UTF-8");
		rb.entity(ex);
		rb.language(Locale.SIMPLIFIED_CHINESE);
		Response r = rb.build();
		return r;
	}

	private String generateErrorMessage(BookException exception) {
		HttpServletRequest request = mc.getHttpServletRequest();
		String errorKey = exception.getMessage();

		Locale locale = request.getLocale();
		ResourceBundle rb = ResourceBundle
				.getBundle("com.fw.cxf.error", locale);
		String message = rb.getString(errorKey);

		return message;
	}

}
