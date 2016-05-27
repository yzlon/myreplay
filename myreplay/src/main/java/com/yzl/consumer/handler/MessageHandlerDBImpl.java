package com.yzl.consumer.handler;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.yzl.db.entity.HFmtCode;
import com.yzl.db.entity.extend.FmtCode;
import com.yzl.util.Constants;
import com.yzl.vo.TransMessage;

@Component("messageHandler")
public class MessageHandlerDBImpl implements IMessageHandler {
	private final static Logger logger = LoggerFactory.getLogger(MessageHandlerDBImpl.class);
	private static int objNums = 0;
	private SqlSessionTemplate sqlSessionTemplate;

	public MessageHandlerDBImpl(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
		 objNumAdd();
	}

	public MessageHandlerDBImpl() {
	}

	@Override
	public void handler(Message message) {
		System.out.println("当前对象的序号:" + Thread.currentThread().getName() + " num:" + objNums);
		if (ObjectMessage.class.isInstance(message)) {
			TransMessage transMessage = null;
			try {
				transMessage = (TransMessage) ((ObjectMessage) message).getObject();
				// 插入数据库操作
				HFmtCode fmtCode = new FmtCode();
				fmtCode.setUuid(transMessage.getUuid());
				fmtCode.setTranCode(transMessage.getTranCode());
				fmtCode.setMsgSeq(transMessage.getMsgSeq());
				fmtCode.setResponseTime(transMessage.getRespTimeStamp());
				fmtCode.setRequestTime(transMessage.getRecvTimeStamp());
				fmtCode.setDiffCode(Constants.H_FMT_CODE_STATUS_DEFAULT);
				fmtCode.setFlag(Constants.H_FMT_CODE_FLAG_DEFAULT);
				fmtCode.setStatus(Constants.H_FMT_CODE_STATUS_DEFAULT);
				int i = sqlSessionTemplate.insert("HFmtCodeMapper.insert", fmtCode);
				System.out.println("成功插入数据：" + i + " 条");
			} catch (JMSException e) {
				e.printStackTrace();
			}
		} else {
			logger.error("not ObjectMessage");
		}
	}

	private synchronized void objNumAdd() {
		objNums++;
		System.out.println("生成对象的序号:" + Thread.currentThread().getName() + " num:" + objNums);
	}
}
