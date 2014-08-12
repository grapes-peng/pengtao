package com.cxf.rest.client;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.cxf.rest.CustomerService;
import com.cxf.rest.dto.Customer;

public class ClientWS {
	public static void main(String args[]) throws Exception {

		/*
		 * 方法三
		 */
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		// 注册WebService接口
		factory.setServiceClass(CustomerService.class);
		// 设置WebService地址
		factory.setAddress("http://127.0.0.1:8080/pengtao/webservice/customerservice/123/456");
		CustomerService dfw = (CustomerService) factory.create();
		System.out.println("invoke webservice...");
		String xtczdm = "admin";
		// 根据操作代码得到用户
		Customer user = new Customer();
		if (user != null) {
			// 修改用户密码
			user.setId("sss");
			Response result = dfw.addCustomer(user);
		}

	}

}