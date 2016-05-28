package com.yzl.compare;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class CompareService implements ApplicationContextAware {
	private final static Logger logger = LoggerFactory.getLogger(CompareService.class);
	private ApplicationContext applicationContext;

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	ExecutorService comparePool;

	private int threadNum;// ��Ҫ���ɵ��̵߳ĸ���
	public static Stack<String> tranCodesStack = new Stack<String>();
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
		if (null == comparePool) {
			comparePool = Executors.newFixedThreadPool(this.threadNum);
		}
		logger.info("��Ҫ�����̵߳ĸ���Ϊ:" + threadNum);
		// for (int i = 0; i < threadNum; i++) {
		for (int i = 0; i < 1; i++) {
			DataCompareThread compareThread = (DataCompareThread) this.applicationContext.getBean("dataCompareThread");
			this.comparePool.execute(compareThread);
		}
	}

	private void getTranCodes() {
		logger.info(sqlSessionTemplate.toString());
		List<String> tranCodes = sqlSessionTemplate.selectList("HFmtCodeMapper.selectDistinTranCode");
		logger.info(tranCodes.toString());
		if (tranCodes.isEmpty() || null == tranCodes) {
			logger.error("û����Ҫ�ȶԵ�����");
			return;
		}
		tranCodesStack.addAll(tranCodes);
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = arg0;
	}
}
