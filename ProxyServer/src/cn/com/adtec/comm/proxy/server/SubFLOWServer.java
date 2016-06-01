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


import cn.com.adtec.comm.proxy.codec.SubFLOWServerCodecFactory;


import cn.com.adtec.comm.proxy.handler.SubFLOWServerBusinessHandler;

import cn.com.adtec.comm.proxy.tools.FileTool;

/**
 * 子服务器-流式报文
 * @author tangjb
 *
 */
public class SubFLOWServer extends AbstractSubServer {
	
	private static final Logger logger = LoggerFactory.getLogger(SubFLOWServer.class);
	/**
	 * 刷新服务器属性相关配置
	 * 节点编号|@|节点名称|@|上游系统IP |@|下游系统IP|@|下游系统端口|@|
	 * 下游系统通讯超时秒数|@|代理节点启动端口|@|代理启动开关标志|@|
	 * 代理节点“直连/仿真”模式|@|报文种类|@|下游系统交易报文字符集|@|
	 * TMS仿真服务地址|@|TMS仿真服务端口|@|TMS应用服务名称|@|TMS交易报文字符集|@|
	 * 收取原始（请求响应）报文规则
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
		//流式报文按指定方式截取报文
		/*
		 * PkgLen=[m,n](从第m位到第n位前取报文总长度，m最小从1开始，n必须大于m);
		 * ;includeSelf=0(是否包括本身长度);CFiller=32(填充字符ASCII表十进制值)，fillRegulation=L(填充方向)，zip=0|1(长度本身是网络字节序压缩格式还是十进制格式)”
		 * 181|@|moni_clt|@| 10.181.80.18|@| 10.181.80.18|@| 7777|@| 10|@| 9996|@| 1|@| 0|@| FIX|@|GBK|@|10.181.80.18|@|12306|@| |@|UTF-8|@| 
		 * PkgLen=[1,5];includeSelf=0;zip=0|@| 12122
		 */
		String[] scratchPkgRule = items[15].trim().split(";");
		//总共有三大项:报文长度存放起始位置,是否包含自身长度，是否压缩格式
		
		//1.报文长度存放起始位置
		if (scratchPkgRule.length>0)
		{
			if (scratchPkgRule[0].indexOf("PkgLen")!=-1)
			{
				//指定长度位置
				String[] temp = scratchPkgRule[0].split("=");
				if (temp.length==2)
				{
					temp[1] = temp[1].replaceAll("\\[", "").replaceAll("\\]", "");
					String[] locArr = temp[1].split("\\,");
					if (locArr.length>=2)
					{
						int minLoc = Integer.parseInt(locArr[0].trim());
						this.setPkgLengthStartLoc(minLoc-1);
						int maxLoc = Integer.parseInt(locArr[1].trim());
						this.setPkgLengthEndLoc(maxLoc-1);
						this.setPkgHeadLen(maxLoc-1);
					}
					locArr = null;
				}
				temp = null;
			}
		}
		
		//2.是否包含自身长度
		if (scratchPkgRule.length>1)
		{
			if (scratchPkgRule[1].indexOf("includeSelf")!=-1)
			{
				//是否包括本身长度
				String[] temp = scratchPkgRule[1].split("=");
				if (temp.length==2)
				{
					this.setPkgIncludeSelf(temp[1].trim());
					
				}
				temp = null;
			}
		}
		
		//3.是否压缩格式
		if (scratchPkgRule.length>2)
		{
			if (scratchPkgRule[2].indexOf("zip")!=-1)
			{
				//长度本身是网络字节序压缩格式还是十进制格式
				String[] temp = scratchPkgRule[2].split("=");
				if (temp.length==2)
				{
					this.setPkgZipFlag(temp[1].trim());
				}
				temp = null;
			}
		}
		
		items = null;
		scratchPkgRule = null;
		return true;
	}

	/**
	 * 关闭子服务器
	 */
	@Override
	public boolean shutSubServer() {

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
			this.getAcceptor().getSessionConfig().setReadBufferSize(2048);
			((NioSocketAcceptor)this.getAcceptor()).getSessionConfig().setSendBufferSize(10240);
			((NioSocketAcceptor)this.getAcceptor()).getSessionConfig().setTcpNoDelay(true);
			((NioSocketAcceptor)this.getAcceptor()).getSessionConfig().setReuseAddress(true);
			//((SocketSessionConfig)this.getAcceptor().getSessionConfig()).setReuseAddress(true);
			this.getAcceptor().getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,10);
			try {
				
				//绑定之前将组装过滤器和业务逻辑处理器
				//logger.info("SubFLOWServer,Set business handler...............");
				this.getAcceptor().setHandler(new SubFLOWServerBusinessHandler(this));
				//logger.info("SubFLOWServer,binding protocol codec.............");
				this.getAcceptor().getFilterChain().addLast("codec",
						new ProtocolCodecFilter(
						new SubFLOWServerCodecFactory(
						Charset.forName((this.getCharset()==null||this.getCharset().equals(""))?"UTF-8":this.getCharset()),this
						)
						));
				this.getAcceptor().getFilterChain().addLast("exector", new ExecutorFilter(Executors.newCachedThreadPool()));
				LoggingFilter filter= new LoggingFilter();
				filter.setMessageReceivedLogLevel(LogLevel.INFO);
				filter.setMessageSentLogLevel(LogLevel.INFO);
				this.getAcceptor().getFilterChain().addLast("logger",filter);
				this.getAcceptor().bind(new InetSocketAddress(Integer.parseInt(this.getNodePort())));
				logger.info("____subsever binding port:["+this.getNodePort()+"]");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Start SubFLOWServer fail,the reason is....\n"+e.toString());
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
