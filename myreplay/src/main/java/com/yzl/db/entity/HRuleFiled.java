package com.yzl.db.entity;

import java.io.Serializable;

public class HRuleFiled {
	private String cmpType;

	private String eleCode;

	private int id;

	private String remark;

	private String ruleCode;

	private String value1;

	private String value2;

	public HRuleFiled() {
	}

	public String getCmpType() {
		return this.cmpType;
	}

	public void setCmpType(String cmpType) {
		this.cmpType = cmpType;
	}

	public String getEleCode() {
		return this.eleCode;
	}

	public void setEleCode(String eleCode) {
		this.eleCode = eleCode;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRuleCode() {
		return this.ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getValue1() {
		return this.value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return this.value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

}