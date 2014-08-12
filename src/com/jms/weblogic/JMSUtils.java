package com.jms.weblogic;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSUtils {

	private static QueueConnection qConnection;
	private static Queue queue;
	private static QueueSession qSession;

	private static String CONNFACTORYJNDI = "jms/ConnectionFactoryTest";
	private static String JNDIFACTORY = "weblogic.jndi.WLInitialContextFactory";
	private static String PROVIDERURL = "t3://localhost:7001";
	private static String QUEUEJNDI = "jms/QueueTest";

	private static void init() throws NamingException, JMSException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, JNDIFACTORY);
		env.put(Context.PROVIDER_URL, PROVIDERURL);
		Context ctx = new InitialContext(env);
		QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx
				.lookup(CONNFACTORYJNDI);
		qConnection = connFactory.createQueueConnection();
		qSession = qConnection.createQueueSession(false,
				Session.AUTO_ACKNOWLEDGE);
		queue = (Queue) ctx.lookup(QUEUEJNDI);

	}

	public static QueueSender getSender() throws NamingException, JMSException {
		if (qSession == null || queue == null)
			init();
		return qSession.createSender(queue);
	}

	public static QueueSession getSession() throws NamingException,
			JMSException {
		if (qSession == null)
			init();
		return qSession;
	}

	public static QueueReceiver getReceiver() throws NamingException,
			JMSException {
		if (qSession == null || queue == null)
			init();
		return qSession.createReceiver(queue);
	}

	public static QueueConnection getConnection() throws NamingException,
			JMSException {
		if (qConnection == null) {
			init();
		}
		return qConnection;

	}

	public static void close() throws JMSException {
		if (qSession != null)
			qSession.close();
		if (qConnection != null)
			qConnection.close();

	}
}
