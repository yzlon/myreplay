package cn.com.adtec.comm.proxy.server;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.service.IoAcceptor;

/**
 * 所有子服务中的服务器必须继承的抽象类
 * @author tangjb
 *
 */
public abstract class AbstractSubServer {
	
	private IoAcceptor acceptor;

	private String charset;				//上游、下游系统字符集

	private String nodeNo;  			//节点编号
	private String nodeName;			//节点名称
	private String lastSystemIP;		//上游系统IP 
	private String nextSystemIP;		//下游系统IP
	private String nextSystemPort;		//下游系统端口
	private String nextSystemTimeOut;	//下游系统通讯超时秒数
	private String nodePort;			//代理节点启动端口
	private String nodeStartFlag;		//代理启动开关标志
	private String nodeMode;			//代理节点“直连/仿真”模式
	private String msgKind;				//报文种类 ,SOAP、XML（纯XML）、FIX（定长）、FIXSEP（带分隔符的流式报文）
	private String tmsIP;				//TMS仿真服务地址
	private String tmsPort;				//TMS仿真服务端口
	private String tmsSimulApp="COMM11";			//TMS“仿真”应用名称
	private String tmsPkgSendBackApp="COMM01";		//TMS“保存报文”应用名称
	private String tmsCharSet;			//与TMS交互时，报文所采用的字符集编码格式
	private String linkMode;			//长连接还是短连接,0-长连接，1-短连接
	
	private String[][] pkgCollectRules;		//报文录制规则
	private ConcurrentHashMap<String,HashMap<String,String>> keyWordVal;	//每条规则包含的关键字KEY-VALUE
	
	private String[][] pkgSimRules;			//报文仿真规则
	private ConcurrentHashMap<String,HashMap<String,String>> keyWordValSim;	//每条仿真规则包含的关键字KEY-VALUE

	//以下字段做为截取报文规则，SOAP报文格式不需要
	private int pkgHeadLen;				//报文头长度   20140724，add by TJB
	private int pkgLengthStartLoc;		//取报文总长度时的起始位置
	private int pkgLengthEndLoc;		//取报文总长度时的结束位置
	private String pkgIncludeSelf;		//是否包括本身长度
	private String pkgZipFlag;			//长度本身是网络字节序压缩格式还是十进制格式
	private String pkgSepChar;			//分隔符十进制值
	
	
	public String getNodeNo() {
		return nodeNo;
	}
	public void setNodeNo(String nodeNo) {
		this.nodeNo = nodeNo;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getLastSystemIP() {
		return lastSystemIP;
	}
	public void setLastSystemIP(String lastSystemIP) {
		this.lastSystemIP = lastSystemIP;
	}
	public String getNextSystemIP() {
		return nextSystemIP;
	}
	public void setNextSystemIP(String nextSystemIP) {
		this.nextSystemIP = nextSystemIP;
	}
	public String getNextSystemPort() {
		return nextSystemPort;
	}
	public void setNextSystemPort(String nextSystemPort) {
		this.nextSystemPort = nextSystemPort;
	}
	public String getNextSystemTimeOut() {
		return nextSystemTimeOut;
	}
	public void setNextSystemTimeOut(String nextSystemTimeOut) {
		this.nextSystemTimeOut = nextSystemTimeOut;
	}
	public String getNodePort() {
		return nodePort;
	}
	public void setNodePort(String nodePort) {
		this.nodePort = nodePort;
	}
	public String getNodeStartFlag() {
		return nodeStartFlag;
	}
	public void setNodeStartFlag(String nodeStartFlag) {
		this.nodeStartFlag = nodeStartFlag;
	}
	public String getNodeMode() {
		return nodeMode;
	}
	public void setNodeMode(String nodeMode) {
		this.nodeMode = nodeMode;
	}
	public String getMsgKind() {
		return msgKind;
	}
	public void setMsgKind(String msgKind) {
		this.msgKind = msgKind;
	}
	public String getTmsIP() {
		return tmsIP;
	}
	public void setTmsIP(String tmsIP) {
		this.tmsIP = tmsIP;
	}
	public String getTmsPort() {
		return tmsPort;
	}
	public void setTmsPort(String tmsPort) {
		this.tmsPort = tmsPort;
	}
	public int getPkgLengthStartLoc() {
		return pkgLengthStartLoc;
	}
	public void setPkgLengthStartLoc(int pkgLengthStartLoc) {
		this.pkgLengthStartLoc = pkgLengthStartLoc;
	}
	public int getPkgLengthEndLoc() {
		return pkgLengthEndLoc;
	}
	public void setPkgLengthEndLoc(int pkgLengthEndLoc) {
		this.pkgLengthEndLoc = pkgLengthEndLoc;
	}
	public String getPkgIncludeSelf() {
		return pkgIncludeSelf;
	}
	public void setPkgIncludeSelf(String pkgIncludeSelf) {
		this.pkgIncludeSelf = pkgIncludeSelf;
	}
	public String getPkgZipFlag() {
		return pkgZipFlag;
	}
	public void setPkgZipFlag(String pkgZipFlag) {
		this.pkgZipFlag = pkgZipFlag;
	}
	public String getPkgSepChar() {
		return pkgSepChar;
	}
	public void setPkgSepChar(String pkgSepChar) {
		this.pkgSepChar = pkgSepChar;
	}
	
	public IoAcceptor getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(IoAcceptor acceptor) {
		this.acceptor = acceptor;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public String[][] getPkgCollectRules() {
		return pkgCollectRules;
	}
	
	/**
	 * 将报文截取规则相关属性全部加载进内存
	 * @param pkgCollectRules
	 */
	public void setPkgCollectRules(String[][] pkgCollectRules) {
		this.pkgCollectRules = pkgCollectRules;
	}
	public ConcurrentHashMap<String, HashMap<String, String>> getKeyWordVal() {
		return keyWordVal;
	}
	/**
	 * 按“报文录制规则批次号”为主键，将报文录制规则拆分成key-value对
	 * @param keyWordVal
	 */
	
	public void setKeyWordVal(ConcurrentHashMap<String, HashMap<String, String>> keyWordVal) {
		this.keyWordVal = keyWordVal;
	}
	
	
	public String getTmsSimulApp() {
		return tmsSimulApp;
	}
	public void setTmsSimulApp(String tmsSimulApp) {
		this.tmsSimulApp = tmsSimulApp;
	}
	public String getTmsPkgSendBackApp() {
		return tmsPkgSendBackApp;
	}
	public void setTmsPkgSendBackApp(String tmsPkgSendBackApp) {
		this.tmsPkgSendBackApp = tmsPkgSendBackApp;
	}
	public String getTmsCharSet() {
		return tmsCharSet;
	}
	public void setTmsCharSet(String tmsCharSet) {
		this.tmsCharSet = tmsCharSet;
	}
	public int getPkgHeadLen() {
		return pkgHeadLen;
	}
	public void setPkgHeadLen(int pkgHeadLen) {
		this.pkgHeadLen = pkgHeadLen;
	}
	//刷新子服务器属性
	public abstract boolean refreshSubServer(String rule);
	
	//启动子服务器
	public abstract boolean startSubServer();
	
	//停止子服务器
	public abstract boolean shutSubServer();
	public String[][] getPkgSimRules() {
		return pkgSimRules;
	}
	public void setPkgSimRules(String[][] pkgSimRules) {
		this.pkgSimRules = pkgSimRules;
	}
	public ConcurrentHashMap<String, HashMap<String, String>> getKeyWordValSim() {
		return keyWordValSim;
	}
	public void setKeyWordValSim(
			ConcurrentHashMap<String, HashMap<String, String>> keyWordValSim) {
		this.keyWordValSim = keyWordValSim;
	}
	public String getLinkMode() {
		return linkMode;
	}
	public void setLinkMode(String linkMode) {
		this.linkMode = linkMode;
	}
	
}
