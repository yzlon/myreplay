package com.yzl.replay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/applicationContext.xml" })
public class TestReplayServer {
	private final static Logger logger = LoggerFactory.getLogger(TestReplayServer.class);

	@Autowired
	ReplayServer replayServer;

	@Test
	public void testReplay() {
		logger.info("begin。。。");
		replayServer.replay();
		logger.info("end。。。");
	}
}
