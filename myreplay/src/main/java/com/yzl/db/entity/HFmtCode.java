package com.yzl.db.entity;

import java.io.Serializable;

public class HFmtCode {
	private String diffCode;

	private String flag;

	private int msgSeq;

	private String requestTime;

	private String responseTime;

	private String status;

	private String tranCode;

	private String uuid;

	public HFmtCode() {
	}

	public String getDiffCode() {
		return this.diffCode;
	}

	public void setDiffCode(String diffCode) {
		this.diffCode = diffCode;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public int getMsgSeq() {
		return this.msgSeq;
	}

	public void setMsgSeq(int msgSeq) {
		this.msgSeq = msgSeq;
	}

	public String getRequestTime() {
		return this.requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getResponseTime() {
		return this.responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTranCode() {
		return this.tranCode;
	}

	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}