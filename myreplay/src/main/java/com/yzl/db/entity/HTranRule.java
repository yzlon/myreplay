package com.yzl.db.entity;

public class HTranRule {
	private int id;

	private String ruleCode;

	private int ruleLevel;

	private String tranCode;

	public HTranRule() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRuleCode() {
		return this.ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public int getRuleLevel() {
		return this.ruleLevel;
	}

	public void setRuleLevel(int ruleLevel) {
		this.ruleLevel = ruleLevel;
	}

	public String getTranCode() {
		return this.tranCode;
	}

	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

}