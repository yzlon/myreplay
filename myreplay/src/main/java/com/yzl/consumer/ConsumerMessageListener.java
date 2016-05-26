package com.yzl.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yzl.consumer.handler.IMessageHandler;
import com.yzl.consumer.handler.MessageHandlerDBImpl;
import com.yzl.util.Constants;

public class ConsumerMessageListener implements MessageListener {
	private final static Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);
	private ExecutorService handlerPool = null;
	private int handlerPoolNumbers = Constants.DEFAULT_HANDLER_POOL_NUMBERS;
	private IMessageHandler messageHandler = null;
	private static int num = 0;
	private int messageNums = 0;

	@Resource
	private SqlSessionTemplate sqlSessionTemplate;

	public ConsumerMessageListener() {
		this.handlerPool = Executors.newFixedThreadPool(this.handlerPoolNumbers);
		this.messageHandler = new MessageHandlerDBImpl();
	}

	public ConsumerMessageListener(int handlerPoolNumbers, IMessageHandler messageHandler) {
		this.handlerPoolNumbers = handlerPoolNumbers;
		this.handlerPool = Executors.newFixedThreadPool(this.handlerPoolNumbers);
		this.messageHandler = messageHandler;
	}

	@Override
	public void onMessage(Message message) {
		this.messageNums++;
		final Message msg = message;
		this.handlerPool.execute(new Runnable() {
			@Override
			public void run() {
				// ConsumerMessageListener.this.messageHandler.handler(msg);
				new MessageHandlerDBImpl(ConsumerMessageListener.this.sqlSessionTemplate).handler(msg);
			}
		});
		System.out.println(Thread.currentThread().getName() + " messageNums:" + this.messageNums);
	}

}
