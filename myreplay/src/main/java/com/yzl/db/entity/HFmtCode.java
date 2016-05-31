package com.yzl.db.entity;

public class HFmtCode {
	private String diffResult;
	
	private String diffInfo;

	public String getDiffInfo() {
		return diffInfo;
	}

	public void setDiffInfo(String diffInfo) {
		this.diffInfo = diffInfo;
	}

	private String flag;

	private int msgSeq;

	private String requestTime;

	private String responseTime;

	private String status;

	private String tranCode;

	private String uuid;

	public HFmtCode() {
	}

	public String getDiffResult() {
		return this.diffResult;
	}

	public void setDiffCode(String diffResult) {
		this.diffResult = diffResult;
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