package com.yzl.compare;

import java.util.List;
import java.util.Stack;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CompareService {
	private final static Logger logger = LoggerFactory.getLogger(CompareService.class);

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	private int threadNum;// 需要生成的线程的个数
	public static Stack<String> tranCodesStack;
	public static long threadNo;// 线程编号

	public void start() {
		// 获取数据库中的交易码
		getTranCodes();
		if (tranCodesStack.empty()) {
			logger.info("没有需要比对的报文");
			return;
		}
		if (threadNum < tranCodesStack.size()) {
			threadNum = tranCodesStack.size();
		}
		logger.info("需要生成线程的个数为:" + threadNum);
	}

	private void getTranCodes() {
		List<String> tranCodes = sqlSessionTemplate.selectList("HFmtCodeMapper.selectDistinTranCode");
		tranCodesStack.addAll(tranCodes);
	}
}
