package com.yzl.compare;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yzl.db.entity.extend.FmtCode;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/applicationContext.xml" })
public class TestCompareService {
	private final static Logger logger = LoggerFactory.getLogger(TestCompareService.class);

	@Autowired
	private CompareService compareService;

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Test
	public void testCompare() {
		logger.info("starting.......");
		compareService.start();
		try {
			Thread.sleep(5 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("compare over.......");
	}
}