package com.yzl.consumer.handler;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.activemq.transaction.Synchronization;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.yzl.consumer.ConsumerMessageListener;
import com.yzl.db.entity.HFmtCode;
import com.yzl.db.entity.extend.FmtCode;
import com.yzl.vo.TransMessage;

@Component("messageHandler")
public class MessageHandlerDBImpl implements IMessageHandler {
	private int num = 0;
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
				fmtCode.setDiffCode("0");
				fmtCode.setFlag("0");
				fmtCode.setStatus("0");
				int i = sqlSessionTemplate.insert("HFmtCodeMapper.insert", fmtCode);
				System.out.println("�ɹ��������ݣ�" + i + " ��");
			} catch (JMSException e) {
				e.printStackTrace();
			}
			System.out.println(transMessage.toString());
			System.out.println(message.toString());
		} else {
			System.out.println("not ObjectMessage");
		}
	}

	private synchronized void objNumAdd() {
		objNums++;
		System.out.println("���ɶ�������:" + Thread.currentThread().getName() + " num:" + objNums);
	}
}
