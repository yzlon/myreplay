package com.yzl.consumer.handler;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.yzl.db.entity.HFmtCode;
import com.yzl.db.entity.extend.FmtCode;
import com.yzl.util.Constants;
import com.yzl.util.FileOper;
import com.yzl.vo.TransMessage;

@Component("messageHandler")
@Scope("prototype")
public class MessageHandlerDBImpl implements IMessageHandler {
	private final static Logger logger = LoggerFactory.getLogger(MessageHandlerDBImpl.class);
	private static int objNums = 0;

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public MessageHandlerDBImpl(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
		objNumAdd();
	}

	public MessageHandlerDBImpl() {
		objNumAdd();
	}

	@Override
	public void handler(Message message) {
		logger.info("当前对象的序号:" + Thread.currentThread().getName() + " num:" + objNums);
		if (ObjectMessage.class.isInstance(message)) {
			TransMessage transMessage = null;
			try {
				transMessage = (TransMessage) ((ObjectMessage) message).getObject();
				// 写入文件

				try {
					FileOper.saveFile(Constants.FILE_TYPE_REQUEST, transMessage.getUuid(),
							transMessage.getRequestMsg());
					FileOper.saveFile(Constants.FILE_TYPE_RESPONSE, transMessage.getUuid(),
							transMessage.getResponseMsg());
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 插入数据库操作
				HFmtCode fmtCode = new FmtCode();
				fmtCode.setUuid(transMessage.getUuid());
				fmtCode.setTranCode(transMessage.getTranCode());
				fmtCode.setMsgSeq(transMessage.getMsgSeq());
				fmtCode.setResponseTime(transMessage.getRespTimeStamp());
				fmtCode.setRequestTime(transMessage.getRecvTimeStamp());
				fmtCode.setDiffCode(Constants.H_FMT_CODE_STATUS_DEFAULT);
				fmtCode.setDiffInfo("还没有开始比对");
				fmtCode.setFlag(Constants.H_FMT_CODE_FLAG_DEFAULT);
				fmtCode.setStatus(Constants.H_FMT_CODE_STATUS_DEFAULT);
				int i = sqlSessionTemplate.insert("FmtCodeMapper.insert", fmtCode);
				logger.info("成功插入数据：" + i + " 条");
			} catch (JMSException e) {
				e.printStackTrace();
			}
		} else {
			logger.error("not ObjectMessage");
		}
	}

	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	private synchronized void objNumAdd() {
		objNums++;
		logger.info("生成对象的序号:" + Thread.currentThread().getName() + " num:" + objNums);
	}
}
