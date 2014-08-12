package com.jms.p2s;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class HelloPublisher {

	TopicConnection topicConnection;
	TopicSession topicSession;
	TopicPublisher topicPublisher;
	Topic topic;

	public HelloPublisher(String factoryJNDI, String topicJNDI)
			throws JMSException, NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		// 设置好连接JMS容器的属性，不同的容器需要的属性可能不同，需要查阅相关文档
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put(Context.PROVIDER_URL, "localhost:1099");
		env.put("java.naming.rmi.security.manager", "yes");
		env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming");

		// 创建连接JMS容器的上下文(context)
		Context context = new InitialContext(env);

		// 通过连接工厂的JNDI名查找ConnectionFactory
		TopicConnectionFactory topicFactory = (TopicConnectionFactory) context
				.lookup(factoryJNDI);

		// 用连接工厂创建一个JMS连接
		topicConnection = topicFactory.createTopicConnection();

		// 通过JMS连接创建一个Session
		topicSession = topicConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);

		// 通过上下文查找到一个主题(topic)
		topic = (Topic) context.lookup(topicJNDI);

		// 用session来创建一个特定主题的消息发送者
		topicPublisher = topicSession.createPublisher(topic);
	}
}
