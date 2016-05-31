package com.yzl.replay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yzl.db.entity.extend.FmtCode;
import com.yzl.util.Constants;
import com.yzl.util.FileOper;

@Component
public class ReplayServer {
	private final static Logger logger = LoggerFactory.getLogger(ReplayServer.class);

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public void replay() {
		logger.info("fromat replay beging ......");
		int beginNo = 0;
		int qryNum = Constants.QUERY_MAX_NUM;
		String orderRule = "msg_seq asc";
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("beginNo", beginNo);
		condition.put("qryNum", qryNum);
		condition.put("orderRule", orderRule);
		while (true) {
			logger.info("beginNo[ " + beginNo + " ],qryNum[ " + qryNum + " ]");
			List<FmtCode> fmtCodes = sqlSessionTemplate.selectList("FmtCodeMapper.selectReplayList", condition);
			if (fmtCodes.isEmpty() || fmtCodes == null) {
				if (beginNo > 1) {
					logger.info("发送完毕");
				} else {
					logger.info("没有需要发送的报文");
				}
				break;
			}
			logger.info("有[ " + fmtCodes.size() + " ]条数据需要发送");
			for (FmtCode fmtCode : fmtCodes) {
				logger.info(fmtCode.getUuid());
				String requestMsg;
				String responseMsg = null;
				try {
					requestMsg = FileOper.readFile(Constants.FILE_TYPE_REQUEST, fmtCode.getUuid(), null, null);
					requestMsg = fmtCode.getUuid();// 测试
					responseMsg = HostClient.client(requestMsg);
					// 保存应答报文
					FileOper.saveFile(Constants.FILE_TYPE_NEW_RESPONSE, fmtCode.getUuid(), responseMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 修改flag状态
				if (null == responseMsg || responseMsg.isEmpty()) {
					fmtCode.setFlag(Constants.H_FMT_CODE_FLAG_FAIL);
				} else {
					fmtCode.setFlag(Constants.H_FMT_CODE_FLAG_SUCC);
				}
				sqlSessionTemplate.update("FmtCodeMapper.updateHostFlag", fmtCode);
			}//for
		}
	}
}
