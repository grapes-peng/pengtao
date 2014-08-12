/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.cxf.jaxrs.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ProviderInfo;
import org.apache.cxf.jaxrs.model.wadl.WadlGenerator;
import org.apache.cxf.jaxrs.provider.ProviderFactory;
import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.apache.cxf.message.Message;

public class RequestPreprocessor {
	private static final String ACCEPT_QUERY = "_type";
	private static final String CTYPE_QUERY = "_ctype";
	private static final String METHOD_QUERY = "_method";
	private static final String METHOD_HEADER = "X-HTTP-Method-Override";
	private static final String SCHEMA_EXTENSION = ".xsd";
	private static final String WADL_EXTENSION = ".wadl";
	private static final Map<String, String> SHORTCUTS = new HashMap();
	private Map<Object, Object> languageMappings;
	private Map<Object, Object> extensionMappings;

	public RequestPreprocessor() {
		this(null, null);
	}

	public RequestPreprocessor(Map<Object, Object> languageMappings,
			Map<Object, Object> extensionMappings) {
		this.languageMappings = ((languageMappings == null) ? Collections
				.emptyMap() : languageMappings);

		this.extensionMappings = ((extensionMappings == null) ? Collections
				.emptyMap() : extensionMappings);
	}

	public String preprocess(Message m, UriInfo u) {
		handleExtensionMappings(m, u);
		handleLanguageMappings(m, u);

		MultivaluedMap queries = u.getQueryParameters();
		handleTypeQuery(m, queries);
		handleCType(m, queries);
		handleMethod(m, queries, new HttpHeadersImpl(m));
		Response r = checkMetadataRequest(m, u);
		if (r != null) {
			m.getExchange().put(Response.class, r);
		}
		return new UriInfoImpl(m, null).getPath();
	}

	private void handleLanguageMappings(Message m, UriInfo uriInfo) {
		String path = uriInfo.getPath(false);
		for (Map.Entry entry : this.languageMappings.entrySet())
			if (path.endsWith("." + entry.getKey())) {
				updateAcceptLanguageHeader(m, entry.getValue().toString());
				updatePath(m, path, entry.getKey().toString());
				return;
			}
	}

	private void handleExtensionMappings(Message m, UriInfo uriInfo) {
		String path = uriInfo.getPath(false);
		for (Map.Entry entry : this.extensionMappings.entrySet()) {
			String key = entry.getKey().toString();
			if (path.endsWith("." + key)) {
				updateAcceptTypeHeader(m, entry.getValue().toString());
				updatePath(m, path, key);
				if (!("wadl".equals(key))) {
					return;
				}
				String query = (String) m.get(Message.QUERY_STRING);
				if (StringUtils.isEmpty(query))
					query = "_wadl";
				else if (!(query.contains("_wadl"))) {
					query = query + "&_wadl";
				}
				m.put(Message.QUERY_STRING, query);
				return;
			}
		}
	}

	private void updateAcceptLanguageHeader(Message m, String anotherValue) {
		List acceptLanguage = (List) ((Map) m.get(Message.PROTOCOL_HEADERS))
				.get("Accept-Language");

		if (acceptLanguage == null) {
			acceptLanguage = new ArrayList();
		}

		acceptLanguage.add(anotherValue);
		((Map) m.get(Message.PROTOCOL_HEADERS)).put("Accept-Language",
				acceptLanguage);
	}

	private void updatePath(Message m, String path, String suffix) {
		String newPath = path.substring(0, path.length()
				- (suffix.length() + 1));
		HttpUtils.updatePath(m, newPath);
	}

	private void handleMethod(Message m,
			MultivaluedMap<String, String> queries, HttpHeaders headers) {
		String method = (String) queries.getFirst("_method");
		if (method == null) {
			List values = headers.getRequestHeader("X-HTTP-Method-Override");
			if (values.size() == 1) {
				method = (String) values.get(0);
			}
		}
		if (method != null)
			m.put("org.apache.cxf.request.method", method);
	}

	private void handleTypeQuery(Message m,
			MultivaluedMap<String, String> queries) {
		String type = (String) queries.getFirst("_type");
		if (type != null) {
			if (SHORTCUTS.containsKey(type)) {
				type = (String) SHORTCUTS.get(type);
			}
			updateAcceptTypeHeader(m, type);
		}
	}

	private void handleCType(Message m, MultivaluedMap<String, String> queries) {
		String type = (String) queries.getFirst("_ctype");
		if (type != null) {
			if (SHORTCUTS.containsKey(type)) {
				type = (String) SHORTCUTS.get(type);
			}
			m.put("Content-Type", type);
		}
	}

	private void updateAcceptTypeHeader(Message m, String acceptValue) {
		m.put("Accept", acceptValue);
		((Map) m.get(Message.PROTOCOL_HEADERS)).put("Accept",
				Collections.singletonList(acceptValue));
	}

	public Response checkMetadataRequest(Message m, UriInfo ui) {
		String originalRequestURI = (String) m
				.get("org.apache.cxf.request.uri");
		String query = (String) m.get(Message.QUERY_STRING);
		if ((query != null) && (query.contains("_wadl"))) {
			String requestURI = getValueWithoutSlash(originalRequestURI);
			String baseAddress = getValueWithoutSlash(HttpUtils
					.getBaseAddress(m));
			if (baseAddress.equals(requestURI))
				return handleMetadataRequest(m);
		} else if ((originalRequestURI != null)
				&& (((originalRequestURI.endsWith(".xsd")) || (originalRequestURI
						.endsWith(".wadl"))))) {
			return handleMetadataRequest(m);
		}
		return null;
	}

	private Response handleMetadataRequest(Message m) {
		List shs = ProviderFactory.getInstance(m).getRequestHandlers();

		if ((shs.size() > 0)
				&& (((ProviderInfo) shs.get(0)).getProvider() instanceof WadlGenerator)) {
			return ((RequestHandler) ((ProviderInfo) shs.get(0)).getProvider())
					.handleRequest(m, null);
		}
		return null;
	}

	private static String getValueWithoutSlash(String value) {
		return ((value.endsWith("/")) ? value.substring(0, value.length() - 1)
				: value);
	}

	static {
		SHORTCUTS.put("json", "application/json");
		SHORTCUTS.put("text", "text/*");
		SHORTCUTS.put("xml", "application/xml");
		SHORTCUTS.put("atom", "application/atom+xml");
		SHORTCUTS.put("html", "text/html");
		SHORTCUTS.put("wadl", "application/vnd.sun.wadl+xml");
	}
}