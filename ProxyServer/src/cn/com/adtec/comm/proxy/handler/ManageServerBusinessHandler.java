package cn.com.adtec.comm.proxy.handler;


import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cn.com.adtec.comm.proxy.bean.SOAPCommObject;
import cn.com.adtec.comm.proxy.server.ManageServer;




/**
 * 管理服务端业务处理总控
 * 
 * @author 平脚
 *
 */
public class ManageServerBusinessHandler extends IoHandlerAdapter {
	private final static Logger log = LoggerFactory	.getLogger(ManageServerBusinessHandler.class);
	private ManageServer ms = null;
	
	public ManageServerBusinessHandler(ManageServer manageServ)
	{
		this.ms = manageServ;
	}
	
	public ManageServer getMs() {
		return ms;
	}

	public void setMs(ManageServer ms) {
		this.ms = ms;
	}

	public String getCharset() {
		return this.getMs().getCharSet();
	}

	public void setCharset(String charset) {
		this.getMs().setCharSet(charset);
	}


	
	/**
	 * 接收SOAP协议报文内容，进行处理，并返回SOAP协议报文
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// TODO Auto-generated method stub
		//super.messageReceived(session, message);
		//此处的message是与客户端事先约定的处理对象
		
		log.info("Manage Server,Begin to process business (SOAP)......................");
		SOAPCommObject nco = (SOAPCommObject)message;
	
		String appName = nco.getHead().substring(nco.getHead().indexOf("POST")+4, nco.getHead().indexOf("HTTP/")).trim().replace("/", "");
		log.info("Manage Server,Application Name (Service code)------------["+appName+"]------------------");

		
		//根据不同的服务码进行处理
		byte[] respTmp=null;//存放返回信息
		//
		//007001010000275
		if ("TMS001".equals(appName))
		{
			//通讯服务节点启停
			respTmp = new TMS001().process(nco, session, getCharset());
		}
		else if ("TMS002".equals(appName))
		{
			//接收报文采集规则
			respTmp = new TMS002().process(nco, session, this.getCharset());
		}
		else if ("TMS101".equals(appName))
		{
			//回归测试
			
			respTmp = new TMS101().process(nco, session, this.getCharset());
		}
		else
		{
			log.info("不支持的交易！");
			//respTmp = "Not Supported Service Code!".getBytes();
			//respTmp = Tools.sendOutToSOAP("http","esb.dev.cqrcb.com","10040","/"+appName,"120",null,nco.getBody(),getCharset());
		}

		appName = null;
		nco = null;


		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 200 OK\r\n");
/*		Calendar ca = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss",Locale.US);
		
		sb.append(sdf.format(ca.getTime()));
		sb.append(" GMT");*/
		//sb.append("\r\nServer: Websphere Application Server/6.1\r\nContent-Type: text/html;\r\nContent-Length: ");
		sb.append("Content-Type: text/html;\r\nContent-Length: ");
		sb.append(respTmp.length);
		//sb.append("\r\nConnection: Keep-Alive\r\nAccept: image/jpeg, */*\r\nAccept-Ranges: bytes");
		sb.append("\r\n\r\n");
		sb.append(new String(respTmp,getCharset()));
		
		//resp_msg = null;
		//sb.append();

		session.write(sb.toString());
		System.out.println("server side completed:["+sb.toString()+"]");
		session.close(true);
		
		sb = null;
/*		ca = null;
		sdf = null;*/
	}

}
