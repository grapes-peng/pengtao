package com.cxf.rest.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestClient {
	public final static String METHOD_GET = "GET";
	public final static String METHOD_PUT = "PUT";
	public final static String METHOD_DELETE = "DELETE";
	public final static String METHOD_POST = "POST";

	public static void rest(String serviceUrl, String parameter,
			String restMethod) {
		try {
			URL url = new URL(serviceUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(restMethod);
			// 如果请求方法为PUT,POST和DELETE设置DoOutput为真
			if (!RestClient.METHOD_GET.equals(restMethod)) {
				con.setDoOutput(true);
				if (!RestClient.METHOD_DELETE.equals(restMethod)) { // 请求方法为PUT或POST时执行
					OutputStream os = con.getOutputStream();
					os.write(parameter.getBytes("UTF-8"));
					os.close();
				}
			} else { // 请求方法为GET时执行
				InputStream in = con.getInputStream();
				byte[] b = new byte[1024];
				int result = in.read(b);
				while (result != -1) {
					System.out.write(b, 0, result);
					result = in.read(b);
				}
			}
			System.out.println(con.getResponseCode() + ":"
					+ con.getResponseMessage());
		} catch (Exception e) {

			throw new RuntimeException(e);
		}

	}

	public static void main(String args[]) {
		// GET
		rest("http://localhost:8080/pengtao/webservice//customerservice/customers/123/456",
				null, RestClient.METHOD_GET);

		if (1 == 1) {
			return;
		}
		// PUT
		String put = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><PRODUCT xmlns:xlink=\"http://www.w3.org/1999/xlink\"><NAME>Chair Shoe</NAME>"
				+ "<PRICE>24.8</PRICE></PRODUCT>";
		rest("http://localhost:8081/sqlrest/PRODUCT/395", put,
				RestClient.METHOD_PUT);

		// POST
		String post = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><PRODUCT xmlns:xlink=\"http://www.w3.org/1999/xlink\">"
				+ "<PRICE>98</PRICE></PRODUCT>";
		rest("http://localhost:8081/sqlrest/PRODUCT/395", post,
				RestClient.METHOD_POST);

		// DELETE
		rest("http://localhost:8081/sqlrest/PRODUCT/395", null,
				RestClient.METHOD_DELETE);
	}

}