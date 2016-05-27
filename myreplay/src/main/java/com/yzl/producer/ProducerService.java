package com.yzl.producer;

import com.yzl.vo.TransMessage;

public interface ProducerService {
	void sendMessage(final TransMessage tranMessage);
}
