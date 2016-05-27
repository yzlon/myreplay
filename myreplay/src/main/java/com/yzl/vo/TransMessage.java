package com.yzl.vo;

import java.io.Serializable;

public class TransMessage implements Serializable {

	private static final long serialVersionUID = -8673596148950676013L;

	/**
	 * UUID
	 */
	private String uuid;
	/**
	 * ������
	 */
	private String tranCode;
	/**
	 * MQ��Ϣ���
	 */
	private int msgSeq;
	/**
	 * ����������
	 */
	private String requestMsg;
	/**
	 * ������Ӧ����
	 */
	private String responseMsg;
	/**
	 * ���Ľ���ʱ���
	 */
	private String recvTimeStamp;
	/**
	 * ������Ӧʱ���
	 */
	private String respTimeStamp;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTranCode() {
		return tranCode;
	}

	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

	public String getRequestMsg() {
		return requestMsg;
	}

	public void setRequestMsg(String requestMsg) {
		this.requestMsg = requestMsg;
	}

	public int getMsgSeq() {
		return msgSeq;
	}

	public void setMsgSeq(int msgSeq) {
		this.msgSeq = msgSeq;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getRecvTimeStamp() {
		return recvTimeStamp;
	}

	public void setRecvTimeStamp(String recvTimeStamp) {
		this.recvTimeStamp = recvTimeStamp;
	}

	public String getRespTimeStamp() {
		return respTimeStamp;
	}

	public void setRespTimeStamp(String respTimeStamp) {
		this.respTimeStamp = respTimeStamp;
	}

	public TransMessage(String uuid, String tranCode, int msgSeq, String requestMsg, String responseMsg,
			String recvTimeStamp, String respTimeStamp) {
		super();
		this.uuid = uuid;
		this.tranCode = tranCode;
		this.msgSeq = msgSeq;
		this.requestMsg = requestMsg;
		this.responseMsg = responseMsg;
		this.recvTimeStamp = recvTimeStamp;
		this.respTimeStamp = respTimeStamp;
	}

	public TransMessage() {
		super();
	}

	@Override
	public String toString() {
		return "TransMessage [uuid=" + uuid + ", tranCode=" + tranCode + ", msgSeq=" + msgSeq + ", requestMsg="
				+ requestMsg + ", responseMsg=" + responseMsg + ", recvTimeStamp=" + recvTimeStamp + ", respTimeStamp="
				+ respTimeStamp + "]";
	}

}
