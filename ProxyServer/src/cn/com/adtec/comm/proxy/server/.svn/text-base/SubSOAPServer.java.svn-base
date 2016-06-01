package cn.com.adtec.comm.proxy.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cn.com.adtec.comm.proxy.codec.SubSOAPServerCodecFactory;

import cn.com.adtec.comm.proxy.handler.SubSOAPServerBusinessHandler;
import cn.com.adtec.comm.proxy.tools.FileTool;

/**
 * 子服务器-SOAP报文
 * @author tangjb
 *
 */
public class SubSOAPServer extends AbstractSubServer {
	
	private static final Logger logger = LoggerFactory.getLogger(SubSOAPServer.class);
	/**
	 * 刷新服务器属性相关配置
	 * 节点编号|@|节点名称|@|上游系统IP |@|下游系统IP|@|下游系统端口|@|
	 * 下游系统通讯超时秒数|@|代理节点启动端口|@|代理启动开关标志|@|
	 * 代理节点“直连/仿真”模式|@|报文种类|@|下游系统交易报文字符集|@|
	 * TMS仿真服务地址|@|TMS仿真服务端口|@|TMS应用服务名称|@|TMS交易报文字符集|@|
	 * 收取原始（请求响应）报文规则|@|上游系统端口|@|连接模式
	 */
	@Override
	public boolean refreshSubServer(String rule) {
		
		if (null==rule)
			return false;
		String[] items = rule.split(FileTool.getSplitStr());
		
		this.setNodeNo(items[0].trim());
		this.setNodeName(items[1].trim());
		this.setLastSystemIP(items[2].trim());
		this.setNextSystemIP(items[3].trim());
		this.setNextSystemPort(items[4].trim());
		this.setNextSystemTimeOut(items[5].trim());
		this.setNodePort(items[6].trim());
		this.setNodeStartFlag(items[7].trim());
		this.setNodeMode(items[8].trim());
		this.setMsgKind(items[9].trim());
		this.setCharset(items[10].trim());
		this.setTmsIP(items[11].trim());
		this.setTmsPort(items[12].trim());
		//this.setTmsPkgSendBackApp(items[13].trim());
		this.setTmsCharSet(items[14].trim());
		this.setLinkMode(items[17].trim());
		//SOAP报文按标准截取报文
		items = null;
		return true;
	}

	/**
	 * 关闭子服务器
	 */
	@Override
	public boolean shutSubServer() {

		//this.getAcceptor().unbind(new InetSocketAddress(Integer.parseInt(this.getNodePort())));//关不掉
		this.getAcceptor().unbind();
		this.getAcceptor().dispose();
		
		return true;
	}

	/**
	 * 启动子服务器
	 */
	@Override
	public boolean startSubServer() {
		if (this.getAcceptor()==null)
		{
			this.setAcceptor(new NioSocketAcceptor(Runtime.getRuntime().availableProcessors()+1));
			((NioSocketAcceptor)this.getAcceptor()).setReuseAddress(true);
			this.getAcceptor().getSessionConfig().setReadBufferSize(4096);
			((NioSocketAcceptor)this.getAcceptor()).getSessionConfig().setSendBufferSize(10240);
			((NioSocketAcceptor)this.getAcceptor()).getSessionConfig().setTcpNoDelay(true);
			//((NioSocketAcceptor)this.getAcceptor()).getSessionConfig().setReuseAddress(true);
			//((SocketSessionConfig)this.getAcceptor().getSessionConfig()).setReuseAddress(true);
			this.getAcceptor().getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,10);
			try {
				
				//绑定之前将组装过滤器和业务逻辑处理器
				//logger.info("SubSOAPServer,Set business handler...............");
				this.getAcceptor().setHandler(new SubSOAPServerBusinessHandler(this));
				//logger.info("SubSOAPServer,binding protocol codec.............");
				//this.getAcceptor().getFilterChain().addLast("pool",new ExecutorFilter(Executors.newCachedThreadPool()));
				this.getAcceptor().getFilterChain().addLast("codec",
						new ProtocolCodecFilter(
						new SubSOAPServerCodecFactory(
						Charset.forName((this.getCharset()==null||this.getCharset().equals(""))?"UTF-8":this.getCharset())
						)
						));
				this.getAcceptor().getFilterChain().addLast("exector", new ExecutorFilter(Executors.newCachedThreadPool()));
				LoggingFilter filter= new LoggingFilter();
				filter.setMessageReceivedLogLevel(LogLevel.INFO);
				filter.setMessageSentLogLevel(LogLevel.INFO);
				this.getAcceptor().getFilterChain().addLast("log",filter);
				this.getAcceptor().bind(new InetSocketAddress(Integer.parseInt(this.getNodePort())));
				logger.info("____subsever binding port:["+this.getNodePort()+"]");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Start SubSOAPServer fail,the reason is....\n"+e.toString());
				e.printStackTrace();
				return false;
			}
		}//if (acceptor==null)
		else
		{
			//如果之前被关闭了，则直接启动
			try {
				this.getAcceptor().bind(new InetSocketAddress(Integer.parseInt(this.getNodePort())));
				//logger.info("____subsever 再次启动  binding port:["+this.getNodePort()+"]");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return true;
	}

}
