package com.yzl.compare;

import java.util.ArrayList;
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

import com.yzl.db.entity.extend.DiffRule;
import com.yzl.db.entity.extend.FmtCode;
import com.yzl.util.Constants;
import com.yzl.util.XmlOper;
import com.yzl.vo.CompareResult;

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
				tranCode = CompareService.tranCodeGroupStack.pop();// ��ջ���˵�ʱ��Ҫ�׳��쳣EmptyStackException
			} catch (EmptyStackException e) {
				logger.info("û�п��ԱȶԵĽ�������");
				break;
			}
			// ��øý������Ӧ�ıȶԹ���
			List<DiffRule> rules = CompareService.getRules(tranCode);
			if (null == rules) {
				logger.error("������[ " + tranCode + " ]�Ľ��׹���û������");
				continue;
			}

			Map<String, Object> condition = new HashMap<String, Object>();
			int beginNo = 0;
			int maxNo = Constants.QUERY_MAX_NUM;// ÿ�αȶԵ�����
			condition.put("tranCode", tranCode);
			condition.put("status", "0");
			;
			condition.put("beginNo", beginNo);
			condition.put("maxNo", maxNo);
			logger.info("��ʼ�ȶԽ��ף�" + tranCode);
			while (true) {
				logger.info("��ѯ��ֹ��¼��:" + beginNo + " - " + maxNo);
				List<FmtCode> fmtCodes = sqlSessionTemplate.selectList("FmtCodeMapper.selectByTranCode", condition);
				if (null == fmtCodes || fmtCodes.isEmpty()) {
					break;
				}
				logger.info("***************");
				logger.info("�������[ " + fmtCodes.size() + " ]��,���׹���[ " + rules.size() + " ]��");
				for (FmtCode fmtCode : fmtCodes) {
					ArrayList<CompareResult> compareResults = new ArrayList<CompareResult>();
					logger.info("fmtCode:" + fmtCode.toString());
					for (DiffRule rule : rules) {
						logger.info("rule:" + rule.toString());
						CompareResult compareResult = XmlOper.compare(fmtCode, rule);
						if (compareResult.isDiff()) {
							fmtCode.setDiffCode(Constants.H_FMT_CODE_DIFF_CODE_DIFFERENT);
							fmtCode.setDiffInfo(compareResult.getCompareInfo().toString());
							compareResults.add(compareResult);
						} else {
							fmtCode.setDiffCode(Constants.H_FMT_CODE_DIFF_CODE_SAME);
						}
						fmtCode.setStatus(Constants.H_FMT_CODE_STATUS_SUCC);
						sqlSessionTemplate.update("FmtCodeMapper.updateStatus", fmtCode);
					}
					for (CompareResult compareResult : compareResults) {
						logger.info("���յıȶԽ��:" + compareResult.getCompareInfo().toString());
					}
				}
				logger.info("****************");
			}
			break;
		}
	}

}
