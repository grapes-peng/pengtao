package com.designpattern.proxy.test;

import java.lang.reflect.Proxy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.designpattern.proxy.Hello;
import com.designpattern.proxy.UserDao;
import com.designpattern.proxy.impl.AbstractHello;
import com.designpattern.proxy.impl.Person;
import com.designpattern.proxy.invocationhandler.HelloInvocationHandler;

public class ProxyTest {

	public static void main(String[] args) {

//		ApplicationContext ctx = new ClassPathXmlApplicationContext(
//				"root-context.xml");
//
//		UserDao userDAOImpl = (UserDao) ctx.getBean("userDAOProxy");
//		userDAOImpl.save();

		ApplicationContext aopCtx = new ClassPathXmlApplicationContext(
				"spring-aop.xml");
		Person person = (Person) aopCtx.getBean("personProxy");
		person.showName();

		if (1 == 1) {
			return;
		}
		Hello helloImpl = new AbstractHello();
		HelloInvocationHandler handler = new HelloInvocationHandler(helloImpl);

		Hello hello = (Hello) Proxy.newProxyInstance(helloImpl.getClass()
				.getClassLoader(), helloImpl.getClass().getInterfaces(),
				handler);
		hello.print();

	}
}
