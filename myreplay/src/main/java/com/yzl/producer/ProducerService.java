package com.yzl.producer;

import javax.jms.Destination;

import com.yzl.vo.TransMessage;

public interface ProducerService {
	void sendMessage(final TransMessage tranMessage);
}
