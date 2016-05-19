package com.yzl.consumer;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yzl.producer.ProducerService;
import com.yzl.vo.TransMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/applicationContext.xml" })
public class TestProducer {
	@Autowired
	ProducerService producer = null;

	@Test
	public void testProducer() {
		for (int i = 0; i < 20; i++) {
			TransMessage transMessage = new TransMessage();
			transMessage.setUuid("uuid:" + new Random().nextInt(100000000));
			transMessage.setTranCode("tranCode:" + Math.random() * 10000);
			transMessage.setRequestMsg("requestMsg:请求报文");
			transMessage.setResponseMsg("responseMsg:应答报文");
			transMessage.setMsgSeq(new Random().nextInt(10000000));
			transMessage.setRecvTimeStamp("recvTimeStamp:22:30");
			transMessage.setRespTimeStamp("respTimeStamp:23:30");
			producer.sendMessage(transMessage);
			try {
				Thread.sleep(3 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
