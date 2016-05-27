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

	private int threadNum;// ��Ҫ���ɵ��̵߳ĸ���
	public static Stack<String> tranCodesStack;
	public static long threadNo;// �̱߳��

	public void start() {
		// ��ȡ���ݿ��еĽ�����
		getTranCodes();
		if (tranCodesStack.empty()) {
			logger.info("û����Ҫ�ȶԵı���");
			return;
		}
		if (threadNum < tranCodesStack.size()) {
			threadNum = tranCodesStack.size();
		}
		logger.info("��Ҫ�����̵߳ĸ���Ϊ:" + threadNum);
	}

	private void getTranCodes() {
		List<String> tranCodes = sqlSessionTemplate.selectList("HFmtCodeMapper.selectDistinTranCode");
		tranCodesStack.addAll(tranCodes);
	}
}
