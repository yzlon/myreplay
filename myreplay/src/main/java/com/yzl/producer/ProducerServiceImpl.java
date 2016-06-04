package com.yzl.producer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.yzl.util.Constants;
import com.yzl.util.FileOper;
import com.yzl.vo.TransMessage;

@Component("producer")
public class ProducerServiceImpl implements ProducerService {
	private final static Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);

	@Autowired
	private JmsTemplate jmsTemplate = null;

	@Autowired
	@Qualifier("queueDestination")
	Destination destination = null;

	@Override
	public void sendMessage(final TransMessage transMessage) {
		final TransMessage message = createMessage();
		this.jmsTemplate.send(this.destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				logger.info("send message:" + message.toString());
				return session.createObjectMessage(message);
			}
		});
	}

	private TransMessage createMessage() {
		TransMessage transMessage = new TransMessage();
		transMessage.setUuid(UUID.randomUUID().toString());// Éú³Éuuid
		transMessage.setTranCode(createTranCode());
		try {
			transMessage.setRequestMsg(FileOper.readFile(Constants.FILE_TYPE_REQUEST,
					"0b574a31-55a2-4a59-a467-5d46f954e315", new SimpleDateFormat("yyyyMMdd").parse("20160506"), null));
			transMessage.setResponseMsg(FileOper.readFile(Constants.FILE_TYPE_RESPONSE,
					"0b574a31-55a2-4a59-a467-5d46f954e315", new SimpleDateFormat("yyyyMMdd").parse("20160506"), null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		transMessage.setMsgSeq(new Random().nextInt(10000000));
		transMessage.setRecvTimeStamp(String.valueOf(new Date().getTime()));
		transMessage.setRespTimeStamp(String.valueOf(new Date().getTime()));

		return transMessage;
	}

	private String createTranCode() {
		int max = 9;
		int min = 1;
		String tranCode = null;
		Random random = new Random();
		tranCode = "0001002100" + (random.nextInt(9) % (max - min + 1) + min);
		return tranCode;
	}
}
