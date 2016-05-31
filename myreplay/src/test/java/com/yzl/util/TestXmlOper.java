package com.yzl.util;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yzl.db.entity.HDiffRule;
import com.yzl.db.entity.HFmtCode;
import com.yzl.db.entity.extend.DiffRule;
import com.yzl.db.entity.extend.FmtCode;
import com.yzl.vo.CompareResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/applicationContext.xml" })
public class TestXmlOper {
	private final static Logger logger = LoggerFactory.getLogger(TestXmlOper.class);

	@Test
	public void testCompare() {
		HFmtCode fmtCode = new FmtCode();
		HDiffRule rule = new DiffRule();

		fmtCode.setUuid("0b574a31-55a2-4a59-a467-5d46f954e315");
		// fmtCode.setTranCode("");
		// rule.setCmpType("1");
		rule.setCmpType("2");
//		rule.setDeSelectFlag(false);
		rule.setDeSelectFlag(true);
//		rule.setEleCode("//*");
		rule.setEleCode("//SvrDate");

		CompareResult compareResult = XmlOper.compare(fmtCode, rule);
		if (compareResult.isDiff()) {
			List<String> compareInfo = compareResult.getCompareInfo();
			logger.info("差异共有[ " + compareInfo.size() + " ]条");
			for (String info : compareInfo) {
				logger.info(info);
			}
		} else {
			logger.info("比对完毕，没有异同");
		}
	}

}