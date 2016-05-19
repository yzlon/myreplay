package com.yzl.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import com.yzl.vo.TransMessage;

public class ConsumerMessageListener implements MessageListener {
	private final static Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);

	@Override
	public void onMessage(Message message) {
		System.out.println("收到消息了啊");
		if (ObjectMessage.class.isInstance(message)) {
			TransMessage transMessage = null;
			try {
				transMessage = (TransMessage) ((ObjectMessage)message).getObject();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(transMessage.toString());
			System.out.println(message.toString());
		} else {
			System.out.println("not ObjectMessage");
		}

	}

}
