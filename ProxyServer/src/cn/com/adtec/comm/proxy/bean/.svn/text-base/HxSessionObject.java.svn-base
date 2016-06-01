package cn.com.adtec.comm.proxy.bean;

import org.apache.mina.core.session.IoSession;

/**
 * 存放核心长连接对象信息
 * @author tangjb
 *
 */
public class HxSessionObject {

	IoSession session;		//到新监控的服务端会话请求，用于发送报文

	private int statusNum;	//状态标志,0-初始值,1-连接可用,2-连接使用中，3-连接失效


	public IoSession getSession() {
		return session;
	}
	public void setSession(IoSession session) {
		this.session = session;
	}

	public int getStatusNum() {
		return statusNum;
	}
	public void setStatusNum(int statusNum) {
		this.statusNum = statusNum;
	}
	
}
