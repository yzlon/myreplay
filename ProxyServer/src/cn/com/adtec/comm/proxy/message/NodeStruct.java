package cn.com.adtec.comm.proxy.message;

import java.util.HashMap;

/**
 * 用于存放解析或者组装报文时的单个节点内容
 * @author tangjb
 *
 */
public class NodeStruct {

	private String nodeName;		//节点名称
	private String nodeValue;		//节点值
	private String parentNodeName;	//父节点名称
	private int parentNodeLevel;	//父节点级数
	private int nodeLevel;			//本节点级数
	private HashMap<String,String> attributes=new HashMap<String,String>();	//属性及 属性值
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getNodeValue() {
		return nodeValue;
	}
	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}
	public String getParentNodeName() {
		return parentNodeName;
	}
	public void setParentNodeName(String parentNodeName) {
		this.parentNodeName = parentNodeName;
	}
	public int getParentNodeLevel() {
		return parentNodeLevel;
	}
	public void setParentNodeLevel(int parentNodeLevel) {
		this.parentNodeLevel = parentNodeLevel;
	}
	public int getNodeLevel() {
		return nodeLevel;
	}
	public void setNodeLevel(int nodeLevel) {
		this.nodeLevel = nodeLevel;
	}
	public HashMap<String, String> getAttributes() {
		return attributes;
	}
	public void addAttribute(String attName, String attVal) {
		this.attributes.put(attName, attVal);
	}
}
