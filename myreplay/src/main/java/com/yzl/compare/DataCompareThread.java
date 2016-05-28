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
		logger.info("生成新的datacompareThread");
	}

	public DataCompareThread(long threadSeqNo) {
		this.threadSeqNo = threadSeqNo;
	}

	@Override
	public void run() {
		while (true) {
			String tranCode = null;
			try {
				tranCode = CompareService.tranCodesStack.pop();// 当栈空了的时候要抛出异常EmptyStackException
			} catch (EmptyStackException e) {
				logger.info("没有可以比对的交易码了");
				break;
			}

			// Map<String, Integer> condition = new HashMap<String, Integer>();
			Map<String, Object> condition = new HashMap<String, Object>();
			int beginNo = 0;
			int maxNo = 10;
			logger.info("开始比对交易：" + tranCode);
			while (true) {
				if (!condition.isEmpty()) {
					condition.replace("beginNo", beginNo);
					condition.replace("beginNo", maxNo);
				} else {
					condition.put("tranCode", tranCode);
					condition.put("beginNo", beginNo);
					condition.put("maxNo", maxNo);
				}

				logger.info("查询起止记录数:" + beginNo + " - " + maxNo);
				List<HFmtCode> list = sqlSessionTemplate.selectList("HFmtCodeMapper.selectByTranCode", condition);
				if (null == list || list.isEmpty()) {
					break;
				}
				logger.info("***************");
				logger.info("查出数据[ " + list.size() + " ]条");
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
