package com.yzl.db.entity.extend;

import java.io.Serializable;

import com.yzl.db.entity.HFmtCode;

public class FmtCode extends HFmtCode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 945964156533319468L;

	@Override
	public String toString() {
		return "FmtCode [getDiffInfo()=" + getDiffInfo() + ", getDiffResult()=" + getDiffResult() + ", getFlag()="
				+ getFlag() + ", getMsgSeq()=" + getMsgSeq() + ", getRequestTime()=" + getRequestTime()
				+ ", getResponseTime()=" + getResponseTime() + ", getStatus()=" + getStatus() + ", getTranCode()="
				+ getTranCode() + ", getUuid()=" + getUuid() + "]";
	}

}