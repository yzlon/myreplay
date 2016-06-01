package cn.com.adtec.comm.proxy.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.adtec.comm.proxy.codec.ManageServerCodecFactory;
import cn.com.adtec.comm.proxy.handler.ManageServerBusinessHandler;


public class ManageServer {
	private  int PORT = 9999;			//本地侦听端口
	private  int TIMEOUT = 10;			//通讯超时时间,单位:秒
	private static final Logger logger = LoggerFactory.getLogger(ManageServer.class);
	private IoAcceptor acceptor;
	private static String scharSet=null;    //字符集

	
	public int getPORT() {
		return PORT;
	}
	public void setPORT(int port) {
		PORT = port;
	}
	public int getTIMEOUT() {
		return TIMEOUT;
	}
	public void setTIMEOUT(int timeout) {
		TIMEOUT = timeout;
	}
	public IoAcceptor getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(IoAcceptor acceptor) {
		this.acceptor = acceptor;
	}
	public String getCharSet() {
		return scharSet;
	}
	public void setCharSet(String charSet) {
		scharSet = charSet;
	}
	
	//启动服务
	public void start() throws IOException {
		// TODO Auto-generated method stub

		if (acceptor==null)
		{
			acceptor=new NioSocketAcceptor();
		
			acceptor.getSessionConfig().setReadBufferSize(2048);
			((NioSocketAcceptor)acceptor).setReuseAddress(true);
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,TIMEOUT);
			try {
				
				//绑定之前将组装过滤器和业务逻辑处理器
				logger.info("Set manager Server business handler...............");
				acceptor.setHandler(new ManageServerBusinessHandler(this));
				//((SocketSessionConfig)acceptor.getSessionConfig()).setReuseAddress(true);
				logger.info("binding manager Server protocol codec.............");
				acceptor.getFilterChain().addLast("codec",
						new ProtocolCodecFilter(
						new ManageServerCodecFactory(
						Charset.forName(this.getCharSet()))
						)
						);
				
				acceptor.bind(new InetSocketAddress(PORT));
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("管理服务器启动失败,具体原因....\n"+e.toString());
				e.printStackTrace();
			}
		}//if (acceptor==null)
		
		//启动子服务
		SubService subs = SubService.getInstance();
		if (!subs.loadFileInfo())
		{
			logger.error("booting sub services,load file fail!....");
		}
		if (!subs.start())
		{
			logger.error("booting sub services,start sub server fail!....");
		}
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//只支持从配置文件启动
		if (args.length<1)
		{
			System.out.println("Usage: java -jar ProxyServer.jar [port] [timeout] [charset]");
			System.out.println("Default configuration:[port=9999] [timeout=10(seconds)] [charset=UTF-8] ,\nwhile you do not add the parameters.");
						
		}
		else
		{
			ManageServer ss = new ManageServer();
			if (args.length >=1 && args[0]!=null && !args[0].equals(""))
			{
				ss.setPORT(Integer.parseInt(args[0]));
				
				//StandaloneServer ss = StandaloneServer.getInstance();
			}
			if (args.length >=2 && args[1]!=null && !args[1].equals(""))
			{
				ss.setTIMEOUT(Integer.parseInt(args[1]));
				//StandaloneServer ss = StandaloneServer.getInstance();
				//ss.setBStop(true);
			}
			if (args.length >=3 && args[2]!=null && !args[2].equals(""))
			{
				ss.setCharSet(args[2]);
				//StandaloneServer ss = StandaloneServer.getInstance();
				//ss.setBStop(true);
			}
			else
			{
				ss.setCharSet("UTF-8");
			}
			ss.start();
			logger.info("Service Port:"+ss.getPORT());
			logger.info("Communication Proxy Server(JAVA) is running.........");
		}
	}

	   
}
