package com.jms.weblogic;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueSender;
import javax.naming.NamingException;

public class JMSSender {

	public static void main(String[] args) {
		try {

			QueueSender qSender = JMSUtils.getSender();
			for (int i = 0; i < 10; i++) {
				System.out.println(JMSUtils.getConnection());
				Message msg = JMSUtils.getSession().createTextMessage(
						"Message is from JMS Sender!" + i);
				qSender.send(msg);
			}
			qSender.close();
			JMSUtils.close();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
