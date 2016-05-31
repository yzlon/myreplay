package com.yzl.compare;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.yzl.db.entity.extend.DiffRule;
import com.yzl.db.entity.extend.RuleFiled;

@Component
public class CompareService implements ApplicationContextAware {
	private final static Logger logger = LoggerFactory.getLogger(CompareService.class);
	private ApplicationContext applicationContext;

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	ExecutorService comparePool;

	private int threadNum;// ��Ҫ���ɵ��̵߳ĸ���
	public static Stack<String> tranCodeGroupStack = new Stack<String>();
	public static long threadNo;// �̱߳��
	private static Map<String, List<DiffRule>> rules;

	public void start() {
		// ��ȡ���ݿ��еĽ�����
		getTranCodeGroup();
		if (tranCodeGroupStack.empty()) {
			logger.info("û����Ҫ�ȶԵı���");
			return;
		}
		// ��ȡ�������Ӧ�ıȶԹ���
		getTranCodeDiffRules();
		if (rules.isEmpty()) {
			logger.info("���еĽ����붼û�����ñȶԹ���");
			return;
		}

		if (threadNum < tranCodeGroupStack.size()) {
			threadNum = tranCodeGroupStack.size();
		}
		if (null == comparePool) {
			comparePool = Executors.newFixedThreadPool(this.threadNum);
		}
		logger.info("��Ҫ�����̵߳ĸ���Ϊ:" + threadNum);
		for (int i = 0; i < threadNum; i++) {
			DataCompareThread compareThread = (DataCompareThread) this.applicationContext.getBean("dataCompareThread");
			this.comparePool.execute(compareThread);
		}
	}

	public static List<DiffRule> getRules(String tranCode) {
		if (null == rules || rules.isEmpty()) {
			return null;
		} else {
			return rules.get(tranCode);
		}
	}

	private void getTranCodeDiffRules() {
		rules = new HashMap<String, List<DiffRule>>();
		Enumeration<String> tranCodes = tranCodeGroupStack.elements();
		while (tranCodes.hasMoreElements()) {
			String tranCode = tranCodes.nextElement();
			List<DiffRule> tranRules = sqlSessionTemplate.selectList("DiffRuleMapper.selectByTranCode", tranCode);
			if (tranRules.isEmpty() || null == tranRules) {
				logger.error("������[ " + tranCode + " ]û�����ñȶԹ���");
				// ɾ���˽��׵ıȶ�
				tranCodeGroupStack.remove(tranCode);
				continue;
			}
			rules.put(tranCode, tranRules);
		} // while
	}

	private void getTranCodeGroup() {
		logger.info(sqlSessionTemplate.toString());
		List<String> tranCodeGroup = sqlSessionTemplate.selectList("FmtCodeMapper.selectDistinTranCode");
		logger.info(tranCodeGroup.toString());
		if (tranCodeGroup.isEmpty() || null == tranCodeGroup) {
			logger.error("û����Ҫ�ȶԵ�����");
			return;
		}
		tranCodeGroupStack.addAll(tranCodeGroup);
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = arg0;
	}
}
