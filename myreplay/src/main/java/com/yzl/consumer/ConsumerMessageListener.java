package com.yzl.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.yzl.consumer.handler.MessageHandlerDBImpl;
import com.yzl.util.Constants;

public class ConsumerMessageListener implements MessageListener, ApplicationContextAware {
	private final static Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);
	private ExecutorService handlerPool = null;
	private int handlerPoolNumbers = Constants.DEFAULT_HANDLER_POOL_NUMBERS;
	private ApplicationContext applicationContext;

	private static int msgOrder;

	@Resource
	private SqlSessionTemplate sqlSessionTemplate;

	public ConsumerMessageListener() {
		this(Constants.DEFAULT_HANDLER_POOL_NUMBERS);
	}

	public ConsumerMessageListener(int handlerPoolNumbers) {
		this.handlerPoolNumbers = handlerPoolNumbers;
		this.handlerPool = Executors.newFixedThreadPool(this.handlerPoolNumbers);
	}

	@Override
	public void onMessage(Message message) {
		final Message msg = message;
		if (0 == msgOrder) {// 取数据库中最大的
			Integer maxMsgSeq = sqlSessionTemplate.selectOne("FmtCodeMapper.selectMaxMsgSeq");
			if (maxMsgSeq != null) {
				msgOrder = maxMsgSeq + 1;
			}
		}
		final int msgSeq = msgOrder++;
		
		this.handlerPool.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("listener启动了新的线程:" + Thread.currentThread().getName());
				// new
				// MessageHandlerDBImpl(ConsumerMessageListener.this.sqlSessionTemplate).handler(msg);
				MessageHandlerDBImpl messageHandler = (MessageHandlerDBImpl) ConsumerMessageListener.this.applicationContext
						.getBean("messageHandler");
				// messageHandler.setSqlSessionTemplate(ConsumerMessageListener.this.sqlSessionTemplate);
				messageHandler.handler(msg,msgSeq);
			}
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = arg0;
	}

}
