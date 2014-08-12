package com.jms.weblogic;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.QueueReceiver;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.naming.NamingException;

public class JMSReceiver {

	public static void main(String[] args) throws NamingException, JMSException {
		QueueReceiver qReceiver = JMSUtils.getReceiver();
		qReceiver.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message msg) {
				String msgText = "";
				double d = 0;
				try {
					if (msg instanceof TextMessage) {
						msgText = ((TextMessage) msg).getText();
					} else if (msg instanceof StreamMessage) {
						msgText = ((StreamMessage) msg).readString();
						d = ((StreamMessage) msg).readDouble();
					} else if (msg instanceof BytesMessage) {
						byte[] block = new byte[1024];
						((BytesMessage) msg).readBytes(block);
						msgText = String.valueOf(block);
					} else if (msg instanceof MapMessage) {
						msgText = ((MapMessage) msg).getString("name");
					}
				} catch (JMSException e) {
					e.printStackTrace();
				}
				System.out.println(msgText + " " + d);
			}
		});
		System.out.println(JMSUtils.getConnection());
		JMSUtils.getConnection().start();
	}
}
