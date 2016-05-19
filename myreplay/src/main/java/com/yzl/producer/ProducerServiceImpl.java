package com.yzl.producer;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.yzl.vo.TransMessage;

@Component("producer")
public class ProducerServiceImpl implements ProducerService {
	@Autowired
	private JmsTemplate jmsTemplate = null;

	@Autowired
	@Qualifier("queueDestination")
	Destination destination = null;

	@Override
	public void sendMessage(final TransMessage transMessage) {
		this.jmsTemplate.send(this.destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				System.out.println("send message******" + transMessage.toString());
				return session.createObjectMessage(transMessage);
			}
		});
	}
}
