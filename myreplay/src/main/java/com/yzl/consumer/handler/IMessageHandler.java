package com.yzl.consumer.handler;

import javax.jms.Message;

public interface IMessageHandler {
	public void handler(Message message);
}
