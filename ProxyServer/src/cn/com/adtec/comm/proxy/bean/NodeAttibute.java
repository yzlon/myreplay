package cn.com.adtec.comm.proxy.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class NodeAttibute {
	private String nodeNo;
	private ArrayList<String[]> ruleInfo=new ArrayList<String[]>();
	private ConcurrentHashMap<String, HashMap<String, String>> contents = new ConcurrentHashMap<String, HashMap<String, String>>();
	public String getNodeNo() {
		return nodeNo;
	}
	public void setNodeNo(String nodeNo) {
		this.nodeNo = nodeNo;
	}
	public ArrayList<String[]> getRuleInfo() {
		return ruleInfo;
	}
	public void setRuleInfo(ArrayList<String[]> ruleInfo) {
		this.ruleInfo = ruleInfo;
	}
	public ConcurrentHashMap<String, HashMap<String, String>> getContents() {
		return contents;
	}
	public void setContents(ConcurrentHashMap<String, HashMap<String, String>> contents) {
		this.contents = contents;
	}
	
}
