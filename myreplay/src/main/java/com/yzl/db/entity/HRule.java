package com.yzl.db.entity;

import java.io.Serializable;

public class HRule {
	private String funcClass;

	private int id;

	private String remark;

	private String resultMode;

	private String ruleCode;

	private String ruleName;

	private String ruleType;

	public HRule() {
	}

	public String getFuncClass() {
		return this.funcClass;
	}

	public void setFuncClass(String funcClass) {
		this.funcClass = funcClass;
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

	public String getResultMode() {
		return this.resultMode;
	}

	public void setResultMode(String resultMode) {
		this.resultMode = resultMode;
	}

	public String getRuleCode() {
		return this.ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getRuleName() {
		return this.ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleType() {
		return this.ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

}