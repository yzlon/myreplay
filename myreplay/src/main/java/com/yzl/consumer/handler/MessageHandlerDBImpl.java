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
		System.out.println("��ǰ��������:" + Thread.currentThread().getName() + " num:" + objNums);
		if (ObjectMessage.class.isInstance(message)) {
			TransMessage transMessage = null;
			try {
				transMessage = (TransMessage) ((ObjectMessage) message).getObject();
				// �������ݿ����
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
				System.out.println("�ɹ��������ݣ�" + i + " ��");
			} catch (JMSException e) {
				e.printStackTrace();
			}
		} else {
			logger.error("not ObjectMessage");
		}
	}

	private synchronized void objNumAdd() {
		objNums++;
		System.out.println("���ɶ�������:" + Thread.currentThread().getName() + " num:" + objNums);
	}
}
