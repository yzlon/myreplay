package com.yzl.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yzl.consumer.handler.MessageHandlerDBImpl;
import com.yzl.util.Constants;

public class ConsumerMessageListener implements MessageListener {
	private final static Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);
	private ExecutorService handlerPool = null;
	private int handlerPoolNumbers = Constants.DEFAULT_HANDLER_POOL_NUMBERS;

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
		this.handlerPool.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("listener启动了新的线程:" + Thread.currentThread().getName());
				new MessageHandlerDBImpl(ConsumerMessageListener.this.sqlSessionTemplate).handler(msg);
			}
		});
	}

}
