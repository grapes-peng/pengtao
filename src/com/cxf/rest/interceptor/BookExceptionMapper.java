package com.cxf.rest.interceptor;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class BookExceptionMapper implements ExceptionMapper<BookException> {
	public Response toResponse(BookException ex) {
		return Response.status(Response.Status.NOT_FOUND).entity(ex)
				.type(MediaType.APPLICATION_JSON).build();
	}
}
