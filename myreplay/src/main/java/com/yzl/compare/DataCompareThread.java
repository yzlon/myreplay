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

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public DataCompareThread() {
		logger.info("生成新的datacompareThread");
	}

	@Override
	public void run() {
		while (true) {
			String tranCode = null;
			try {
				tranCode = CompareService.tranCodeGroupStack.pop();// 当栈空了的时候要抛出异常EmptyStackException
			} catch (EmptyStackException e) {
				logger.info("没有可以比对的交易码了");
				break;
			}
			// 获得该交易码对应的比对规则
			List<DiffRule> rules = CompareService.getRules(tranCode);
			if (null == rules) {
				sqlSessionTemplate.update("FmtCodeMapper.updateNoRules", tranCode);
				logger.error("交易码[ " + tranCode + " ]的交易规则还没有配置");
				continue;
			}

			Map<String, Object> condition = new HashMap<String, Object>();
			int beginNo = 0;
			int qryNum = Constants.QUERY_MAX_NUM;// 每次比对的条数
			condition.put("tranCode", tranCode);
			condition.put("status", "0");
			condition.put("beginNo", beginNo);
			condition.put("qryNum", qryNum);
			logger.info("开始比对交易：" + tranCode);
			while (true) {
				logger.info("查询起止记录数:" + beginNo + " - " + qryNum);
				List<FmtCode> fmtCodes = sqlSessionTemplate.selectList("FmtCodeMapper.selectByTranCode", condition);
				if (null == fmtCodes || fmtCodes.isEmpty()) {
					break;
				}
				logger.info("***************");
				logger.info("查出数据[ " + fmtCodes.size() + " ]条,交易规则[ " + rules.size() + " ]条");
				for (FmtCode fmtCode : fmtCodes) {
					ArrayList<CompareResult> compareResults = new ArrayList<CompareResult>();
					// 比对完成内容
					StringBuilder diffInfo = new StringBuilder();
					// 异同标识
					boolean diff = false;

					logger.info("fmtCode:" + fmtCode.toString());
					for (DiffRule rule : rules) {
						logger.info("rule:" + rule.toString());
						CompareResult compareResult = XmlOper.compare(fmtCode, rule);
						if (compareResult.isDiff()) {
							diff = true;
							diffInfo.append(
									"规则[ " + rule.getCmpType() + " ]比对结果[ " + compareResult.getCompareInfo() + " ];");
							compareResults.add(compareResult);
						} else {
							diffInfo.append("规则[ " + rule.getCmpType() + " ]比对相同;");
						}
					} // for
						// 修改比对状态
					if (diff) {
						fmtCode.setDiffCode(Constants.H_FMT_CODE_DIFF_CODE_DIFFERENT);
					} else {
						fmtCode.setDiffCode(Constants.H_FMT_CODE_DIFF_CODE_SAME);
					}
					fmtCode.setStatus(Constants.H_FMT_CODE_STATUS_SUCC);
					fmtCode.setDiffInfo(diffInfo.toString());
					sqlSessionTemplate.update("FmtCodeMapper.updateStatus", fmtCode);

					for (CompareResult compareResult : compareResults) {
						logger.info("最终的比对结果:" + compareResult.getCompareInfo().toString());
					} // for
				} // for
			} // while
		} // while
	}

}
