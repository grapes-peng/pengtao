package com.cxf.rest.interceptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxrs.interceptor.JAXRSInInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.log4j.Logger;

public class AuthInterceptor extends JAXRSInInterceptor {
	private Logger logger = Logger.getLogger(AuthInterceptor.class);

	public void handleMessage(Message message) throws Fault {
		/*
		 * Iterator<Entry<String, Object>> it = message.entrySet().iterator();
		 * while (it.hasNext()) { Entry<String, Object> e = it.next();
		 * System.out.println(e.getKey() + "," + e.getValue()); }
		 */
//		if (1 == 1) {
//			message.put(Message.RESPONSE_CODE, "Token 错误！");
//
//			HttpServletResponse response = (HttpServletResponse) message
//					.getExchange().getInMessage()
//					.get(AbstractHTTPDestination.HTTP_RESPONSE);
//			// response.setStatus(500);
//			try {
//				response.sendError(500, "<Token 错误！/>");
//				// response.getOutputStream().write("<Token 错误！/>".getBytes());
//				// response.getOutputStream().flush();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			// message.getInterceptorChain().abort();
//			// throw new Fault(new RuntimeException("Token 错误！"));
//		}
		String reqParams = null;
		if (message.get(Message.HTTP_REQUEST_METHOD).equals("GET")) {// 采用GET方式请求
			reqParams = (String) message.get(Message.QUERY_STRING);
			String s1 = (String) message.get(Message.INBOUND_MESSAGE);
			String s2 = (String) message.get(Message.ACCEPT_CONTENT_TYPE);
			String s3 = (String) message.get(Message.ACCEPT_CONTENT_TYPE);
			String s4 = (String) message.get(Message.BASE_PATH);
			String s5 = (String) message.get(Message.CONTENT_TYPE);
			String s6 = (String) message.get(Message.DECOUPLED_CHANNEL_MESSAGE);
			String s7 = (String) message.get(Message.INBOUND_MESSAGE);
			String s8 = (String) message.get(Message.INVOCATION_CONTEXT);
			String s9 = (String) message.get(Message.REST_MESSAGE);

			String s = (String) message.get(Message.REQUEST_URI);
			String origin = s.substring(0, s.indexOf("id/") + "id/".length());
			message.put(Message.REQUEST_URI, origin + "555");
			message.remove(Message.QUERY_STRING);
			reqParams = this.getParams(this.getParamsMap(reqParams));
			message.put(Message.QUERY_STRING, reqParams);
		} else if (message.get(Message.HTTP_REQUEST_METHOD).equals("POST")) {// 采用POST方式请求
			try {
				InputStream is = message.getContent(InputStream.class);
				reqParams = this.getParams(this.getParamsMap(is.toString()));
				if (is != null)
					message.setContent(InputStream.class,
							new ByteArrayInputStream(reqParams.getBytes()));
			} catch (Exception e) {
				logger.error("GatewayInInterceptor异常", e);
			}
		}
		logger.info("请求的参数：" + reqParams);

	}

	private Map<String, String> getParamsMap(String strParams) {

		if (strParams == null || strParams.trim().length() <= 0) {
			return null;
		}

		Map<String, String> map = new HashMap<String, String>();
		String[] params = strParams.split("&");
		for (int i = 0; i < params.length; i++) {
			String[] arr = params[i].split("=");
			map.put(arr[0], arr[1]);
		}
		return map;
	}

	public static void main(String[] args) {
		String s = "http://localhost:8080/pengtao/webservice/customerservice/id/123";
		String origin = s.substring(0, s.indexOf("id/") + "id/".length());
		System.out.println(origin + "555");

	}

	private String getParams(Map<String, String> map) {
		if (map == null || map.size() == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = "http://yufenfei.iteye.com/blog/" + map.get(key);
			/*
			 * 这里可以对客户端上送过来的输入参数进行特殊处理。如密文解密；对数据进行验证等等。。。
			 * if(key.equals("content")){ value.replace("%3D", "="); value =
			 * http
			 * ://yufenfei.iteye.com/blog/DesEncrypt.convertPwd(value,"DES"); }
			 */
			if (sb.length() <= 0) {
				sb.append(key + "=" + value);
			} else {
				sb.append("&" + key + "=" + value);
			}
		}
		return sb.toString();
	}

}
