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
		// ���ú�����JMS���������ԣ���ͬ��������Ҫ�����Կ��ܲ�ͬ����Ҫ��������ĵ�
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put(Context.PROVIDER_URL, "localhost:1099");
		env.put("java.naming.rmi.security.manager", "yes");
		env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming");

		// ��������JMS������������(context)
		Context context = new InitialContext(env);

		// ͨ�����ӹ�����JNDI������ConnectionFactory
		TopicConnectionFactory topicFactory = (TopicConnectionFactory) context
				.lookup(factoryJNDI);

		// �����ӹ�������һ��JMS����
		topicConnection = topicFactory.createTopicConnection();

		// ͨ��JMS���Ӵ���һ��Session
		topicSession = topicConnection.createTopicSession(false,
				Session.AUTO_ACKNOWLEDGE);

		// ͨ�������Ĳ��ҵ�һ������(topic)
		topic = (Topic) context.lookup(topicJNDI);

		// ��session������һ���ض��������Ϣ������
		topicPublisher = topicSession.createPublisher(topic);
	}
}
