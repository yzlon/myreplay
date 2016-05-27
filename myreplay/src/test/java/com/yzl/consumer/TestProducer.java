package com.yzl.consumer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yzl.producer.ProducerService;
import com.yzl.util.FileOper;
import com.yzl.vo.TransMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/applicationContext.xml" })
public class TestProducer {
	@Autowired
	ProducerService producer = null;

	@Test
	public void testProducer() {
		boolean flag = true;
		long begTime = new Date().getTime();
		long endTime = begTime;
		try {
			System.out.println(new SimpleDateFormat("yyyyMMdd").parse("20160506"));
			System.out.println(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(TestProducer.class.getClassLoader().getResource("").getPath());
		while (flag) {
			producer.sendMessage(null);
			endTime = new Date().getTime();
			if (endTime - begTime > 100000 * 1000) {
				flag = false;
			}
		}
	}
}
