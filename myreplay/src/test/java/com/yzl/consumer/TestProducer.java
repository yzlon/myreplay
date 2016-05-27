package com.yzl.consumer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yzl.producer.ProducerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/applicationContext.xml" })
public class TestProducer {
	private final static Logger logger = LoggerFactory.getLogger(TestProducer.class);

	@Autowired
	ProducerService producer = null;

	@Test
	public void testProducer() {
		boolean flag = true;
		long begTime = new Date().getTime();
		long endTime = begTime;
		try {
			logger.info(new SimpleDateFormat("yyyyMMdd").parse("20160506").toString());
			logger.info(new Date().toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		logger.info(TestProducer.class.getClassLoader().getResource("").getPath());
		while (flag) {
			producer.sendMessage(null);
			endTime = new Date().getTime();
			if (endTime - begTime > 100000 * 1000) {
				flag = false;
			}
		} // while(flag)
	}
}
