package com.yzl.compare;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yzl.db.entity.HFmtCode;
import com.yzl.util.Constants;

@Component
@Scope("prototype")
public class DataCompareThread implements Runnable {
	private final static Logger logger = LoggerFactory.getLogger(DataCompareThread.class);

	private long threadSeqNo;

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public DataCompareThread() {
		logger.info("�����µ�datacompareThread");
	}

	public DataCompareThread(long threadSeqNo) {
		this.threadSeqNo = threadSeqNo;
	}

	@Override
	public void run() {
		while (true) {
			String tranCode = null;
			try {
				tranCode = CompareService.tranCodesStack.pop();// ��ջ���˵�ʱ��Ҫ�׳��쳣EmptyStackException
			} catch (EmptyStackException e) {
				logger.info("û�п��ԱȶԵĽ�������");
				break;
			}

			// Map<String, Integer> condition = new HashMap<String, Integer>();
			Map<String, Object> condition = new HashMap<String, Object>();
			int beginNo = 0;
			int maxNo = 10;
			logger.info("��ʼ�ȶԽ��ף�" + tranCode);
			while (true) {
				if (!condition.isEmpty()) {
					condition.replace("beginNo", beginNo);
					condition.replace("beginNo", maxNo);
				} else {
					condition.put("tranCode", tranCode);
					condition.put("beginNo", beginNo);
					condition.put("maxNo", maxNo);
				}

				logger.info("��ѯ��ֹ��¼��:" + beginNo + " - " + maxNo);
				List<HFmtCode> list = sqlSessionTemplate.selectList("HFmtCodeMapper.selectByTranCode", condition);
				if (null == list || list.isEmpty()) {
					break;
				}
				logger.info("***************");
				logger.info("�������[ " + list.size() + " ]��");
				for (HFmtCode fmtCode : list) {
					logger.info(fmtCode.getUuid());
				}
				logger.info("****************");
				beginNo = maxNo + 1;
				maxNo += Constants.QUERY_MAX_NUM;
			}
			break;
		}
	}

}
