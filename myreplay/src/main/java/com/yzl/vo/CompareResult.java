package com.yzl.vo;

import java.util.List;

public class CompareResult {
	private List<String> compareInfo;
	private boolean diff;

	public boolean isDiff() {
		return diff;
	}

	public void setDiff(boolean diff) {
		this.diff = diff;
	}

	public List<String> getCompareInfo() {
		return compareInfo;
	}

	public void setCompareInfo(List<String> compareInfo) {
		this.compareInfo = compareInfo;
	}

	@Override
	public String toString() {
		return "CompareResult [compareInfo=" + compareInfo + "]";
	}

}
