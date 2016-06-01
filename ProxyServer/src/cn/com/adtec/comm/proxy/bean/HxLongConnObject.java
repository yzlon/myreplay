package cn.com.adtec.comm.proxy.bean;

import org.apache.mina.core.session.IoSession;

/**
 * 存放核心长连接对象信息
 * @author tangjb
 *
 */
public class HxLongConnObject {

	private String recNodeNo;		//代理节点编号
	private int port;		//端口
	IoSession session;		//客户端会话请求，用于返回报文
	private int timeout;	//超时秒数
	private int statusNum;	//状态标志,0-初始值,1-连接可用,2-连接使用中，3-连接失效
	private String recRuleNo;	//录制规则批次号
	private String beginTime;	//开始时间
	private NewMoniObject reqObj;	//请求对象
	private String sessionId;		//请求会话ID,等同于客户端地址
	private String filesvrTestEnv;	//文件服务器测试环境
	private boolean recFlag;		//录制报文标志
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public IoSession getSession() {
		return session;
	}
	public void setSession(IoSession session) {
		this.session = session;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public int getStatusNum() {
		return statusNum;
	}
	public void setStatusNum(int statusNum) {
		this.statusNum = statusNum;
	}
	
	public NewMoniObject getReqObj() {
		return reqObj;
	}
	public void setReqObj(NewMoniObject reqObj) {
		this.reqObj = reqObj;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getRecNodeNo() {
		return recNodeNo;
	}
	public void setRecNodeNo(String recNodeNo) {
		this.recNodeNo = recNodeNo;
	}
	public String getRecRuleNo() {
		return recRuleNo;
	}
	public void setRecRuleNo(String recRuleNo) {
		this.recRuleNo = recRuleNo;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getFilesvrTestEnv() {
		return filesvrTestEnv;
	}
	public void setFilesvrTestEnv(String filesvrTestEnv) {
		this.filesvrTestEnv = filesvrTestEnv;
	}
	public boolean isRecFlag() {
		return recFlag;
	}
	public void setRecFlag(boolean recFlag) {
		this.recFlag = recFlag;
	}
	
}
