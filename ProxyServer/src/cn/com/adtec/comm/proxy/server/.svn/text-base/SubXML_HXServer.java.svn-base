package cn.com.adtec.comm.proxy.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;


import org.apache.mina.core.session.IdleStatus;


import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.adtec.comm.proxy.bean.HxLongConnObject;
import cn.com.adtec.comm.proxy.bean.HxSessionObject;
import cn.com.adtec.comm.proxy.codec.NewMoniServerCodecFactory;

import cn.com.adtec.comm.proxy.handler.SubXML_HXServerBusinessHandler;

import cn.com.adtec.comm.proxy.tools.FileTool;


/**
 * 子服务器-XML报文(核心)
 * @author tangjb
 *
 */
public class SubXML_HXServer extends AbstractSubServer {
	private ArrayList<String> arrayNextSvrIp=new ArrayList<String>();		//存放下游系统IP
	private ArrayList<String> arrayNextSvrPort=new ArrayList<String>();		//存放下游系统端口
	private ArrayList<String> arrayNodePort=new ArrayList<String>();		//代理服务器侦听端口
	private ArrayList<String> arrayLastSvrIp=new ArrayList<String>();		//上游系统IP
	private ArrayList<String> arrayLastSvrPort=new ArrayList<String>();		//上游系统端口
	public static ConcurrentLinkedQueue<HxLongConnObject> hxQueue = null;
		//new ConcurrentLinkedQueue<HxLongConnObject>();
	
	/*可用连接，采用IP+PORT作为key,HxLongConnObject为value*/
	private ConcurrentHashMap<String,ArrayList<HxSessionObject>> availableConn = new ConcurrentHashMap<String,ArrayList<HxSessionObject>>();	//可用连接数
	
	
	private static final Logger logger = LoggerFactory.getLogger(SubXML_HXServer.class);
	/**
	 * 从单独的文件中读取
	 * 节点编号|@|节点名称|@|上游系统IP |@|下游系统IP|@|下游系统端口|@|下游系统通讯超时秒数|@|代理节点启动端口|@|
	 * 代理启动开关标志|@|代理节点“直连/仿真”模式|@|报文种类|@|下游系统交易报文字符集|@|TMS仿真服务地址|@|
	 * TMS仿真服务端口|@|TMS应用服务名称|@|TMS交易报文字符集|@|收取原始（请求响应）报文规则|@|上游系统端口
	 */
	@Override
	public boolean refreshSubServer(String rule) {
		
		if (null==rule)
			return false;
		String[] items = rule.split(FileTool.getSplitStr());
		
		this.setNodeNo(items[0].trim());
		this.setNodeName(items[1].trim());
		//上游系统IP，顺序与上游系统端口一致  this.setLastSystemIP(items[2].trim());
		String[] tmp = items[2].trim().split("\\,");
		for (int i=0;i<tmp.length;i++)
		{
			if (!tmp[i].trim().equals(""))
			{
				this.getArrayLastSvrIp().add(tmp[i]);
			}
		}
		//下游系统IP  this.setNextSystemIP(items[3].trim());
		tmp = items[3].trim().split("\\,");
		for (int i=0;i<tmp.length;i++)
		{
			if (!tmp[i].trim().equals(""))
			{
				this.getArrayNextSvrIp().add(tmp[i]);
			}
		}
		//下游系统端口，顺序与下游系统IP一致  this.setNextSystemPort(items[4].trim());
		tmp = items[4].trim().split("\\,");
		for (int i=0;i<tmp.length;i++)
		{
			if (!tmp[i].trim().equals(""))
			{
				this.getArrayNextSvrPort().add(tmp[i]);
			}
		}
		
		if (this.getArrayNextSvrPort().size()!=0 && this.getArrayNextSvrPort().size()!=this.getArrayNextSvrIp().size())
		{
			logger.error("下游系统IP个数和端口数无法一一对应！IP["+items[3].trim()+"]port["+items[4].trim()+"]");
			tmp = null;
			items = null;
			return false;
		}
		
		this.setNextSystemTimeOut(items[5].trim());
		//本机启动服务端口,this.setNodePort(items[6].trim());
		tmp = items[6].trim().split("\\,");
		for (int i=0;i<tmp.length;i++)
		{
			if (!tmp[i].trim().equals(""))
			{
				this.getArrayNodePort().add(tmp[i]);
			}
		}
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
		
		//上游系统端口
		if (items.length>16)
		{
			tmp = items[16].trim().split("\\,");
			for (int i=0;i<tmp.length;i++)
			{
				if (!tmp[i].trim().equals(""))
				{
					this.getArrayLastSvrPort().add(tmp[i]);
				}
			}
			if (this.getArrayLastSvrPort().size()!=0 && this.getArrayLastSvrIp().size()!=this.getArrayLastSvrPort().size())
			{
				logger.error("上游系统IP个数和端口数无法一一对应！IP["+items[2].trim()+"]port["+items[16].trim()+"]");
				tmp = null;
				items = null;
				return false;
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
		//this.availableConn.clear();
		Set<String> keys = (Set<String>) this.availableConn.keys();
		for (String tmp:keys)
		{
			this.availableConn.remove(tmp);
		}
		keys = null;
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
			this.getAcceptor().getSessionConfig().setReadBufferSize(3072);
			((NioSocketAcceptor)this.getAcceptor()).getSessionConfig().setSendBufferSize(10240);
			((NioSocketAcceptor)this.getAcceptor()).getSessionConfig().setTcpNoDelay(true);
			((NioSocketAcceptor)this.getAcceptor()).getSessionConfig().setReuseAddress(true);
			
			this.getAcceptor().getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,60);
			
			try {
				
				//绑定之前将组装过滤器和业务逻辑处理器
				//logger.info("SubFLOWServer,Set business handler...............");
				this.getAcceptor().setHandler(new SubXML_HXServerBusinessHandler(this));
				//this.getAcceptor().getFilterChain().addLast("pool",new ExecutorFilter(Executors.newCachedThreadPool()));
				//logger.info("SubFLOWServer,binding protocol codec.............");
				
				this.getAcceptor().getFilterChain().addLast("codec",
						new ProtocolCodecFilter(
						new NewMoniServerCodecFactory(
								Charset.forName((null==this.getCharset()|| "".equals(this.getCharset()))?"UTF-8":this.getCharset())
						)
						));
				this.getAcceptor().getFilterChain().addLast("exector", new ExecutorFilter(Executors.newCachedThreadPool()));
				
				LoggingFilter filter= new LoggingFilter();
				filter.setMessageReceivedLogLevel(LogLevel.INFO);
				filter.setMessageSentLogLevel(LogLevel.INFO);
				this.getAcceptor().getFilterChain().addLast("logger",filter);
				
				ArrayList<InetSocketAddress> ports = new ArrayList<InetSocketAddress>();
				for (int i=0;i<this.getArrayNodePort().size();i++)
				{
					ports.add(new InetSocketAddress(Integer.parseInt(this.getArrayNodePort().get(i))));
				}
				this.getAcceptor().bind(ports);
				
				
				logger.info("____subsever binding port:["+ports.toString()+"]");
				ports = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Start SubXML_HXServer fail,the reason is....\n"+e.toString());
				e.printStackTrace();
				return false;
			}
		}//if (acceptor==null)
		else
		{
			//如果之前被关闭了，则直接启动
			try {
				ArrayList<InetSocketAddress> ports = new ArrayList<InetSocketAddress>();
				for (int i=0;i<this.getArrayNodePort().size();i++)
				{
					ports.add(new InetSocketAddress(Integer.parseInt(this.getArrayNodePort().get(i))));
				}
				this.getAcceptor().bind(ports);
				
				logger.info("____subsever 再次启动  binding port:["+ports.toArray()+"]");
				ports = null;
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		//启动与下一级服务的长连接
		//buildConnect()
		
		return true;
	}



	public ArrayList<String> getArrayNextSvrIp() {
		return arrayNextSvrIp;
	}

	public void setArrayNextSvrIp(ArrayList<String> arrayNextSvrIp) {
		this.arrayNextSvrIp = arrayNextSvrIp;
	}

	public ArrayList<String> getArrayNextSvrPort() {
		return arrayNextSvrPort;
	}

	public void setArrayNextSvrPort(ArrayList<String> arrayNextSvrPort) {
		this.arrayNextSvrPort = arrayNextSvrPort;
	}

	public ArrayList<String> getArrayNodePort() {
		return arrayNodePort;
	}

	public void setArrayNodePort(ArrayList<String> arrayNodePort) {
		this.arrayNodePort = arrayNodePort;
	}

	public ArrayList<String> getArrayLastSvrIp() {
		return arrayLastSvrIp;
	}

	public void setArrayLastSvrIp(ArrayList<String> arrayLastSvrIp) {
		this.arrayLastSvrIp = arrayLastSvrIp;
	}

	public ArrayList<String> getArrayLastSvrPort() {
		return arrayLastSvrPort;
	}

	public void setArrayLastSvrPort(ArrayList<String> arrayLastSvrPort) {
		this.arrayLastSvrPort = arrayLastSvrPort;
	}

	public ConcurrentHashMap<String, ArrayList<HxSessionObject>> getAvailableConn() {
		return availableConn;
	}

	public void setAvailableConn(ConcurrentHashMap<String, ArrayList<HxSessionObject>> availableConn) {
		this.availableConn = availableConn;
	}

}
