package com.yzl.db.entity.extend;

import java.io.Serializable;

import com.yzl.db.entity.HDiffRule;

public class DiffRule extends HDiffRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3050685287872669637L;

	@Override
	public String toString() {
		return "DiffRule [isDeSelectFlag()=" + isDeSelectFlag() + ", getCmpType()=" + getCmpType() + ", getEleCode()="
				+ getEleCode() + ", getId()=" + getId() + ", getRemark()=" + getRemark() + ", getTranCode()="
				+ getTranCode() + ", getValue1()=" + getValue1() + ", getValue2()=" + getValue2() + "]";
	}
}