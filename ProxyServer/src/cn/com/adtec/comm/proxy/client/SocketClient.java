package cn.com.adtec.comm.proxy.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;


import java.io.UnsupportedEncodingException;
import java.net.Socket;


import org.apache.mina.core.buffer.IoBuffer;



public class SocketClient {
	//private static final Logger log = LoggerFactory.getLogger(SocketClient.class);
	private Socket socket;
	private String serverIP;		//服务器IP地址
	private int serverPort;		//服务器端口
	private int timeout;			//超时时限（单位秒)
	
	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	
	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * 构造函数
	 * @param IP		服务器IP
	 * @param PORT		服务器侦听端口	
	 * @param timeout	与服务器连接超时秒数
	 */
	public SocketClient(String IP,int PORT,int timeout)
	{
		this.setServerIP(IP);
		this.setServerPort(PORT);
		this.setTimeout(timeout);
		//log.info("ip:["+this.getServerIP()+"]port["+this.getServerPort()+"]timeout["+this.getTimeout()+"]");
	}
	
	/**
	 * 将流式报文发送出去，并获取响应报文内容
	 * @param reqHead			//原报文头
	 * @param reqBody			//原报文体
	 * @param codeOutCharset	//字符集编码
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] sendOut(String reqHead,String reqBody,String codeOutCharset) throws UnsupportedEncodingException
	{
		//StringBuilder sbstr = new StringBuilder();
		IoBuffer buffer = IoBuffer.allocate(1).setAutoExpand(true);
		//将报文头与报文体连接在一起
		//sbstr.append(reqHead);
		//sbstr.append(reqBody);
		byte[] retStr = null;
		int ttlLoc = 0;
		try
		{
			socket = new Socket(this.getServerIP(), this.getServerPort());
			socket.setSoTimeout(this.getTimeout()*1000);//将秒转换成毫秒
			//BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			
			//log.info("发往"+"ip:["+this.getServerIP()+"]port["+this.getServerPort()+"]timeout["+this.getTimeout()+"]"+"SOCKET发送报文["+sbstr.toString()+"]");
			//out.println(sbstr.toString());
			
			//20141227 修改 by 汤继波
			BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
			bos.write(reqHead.getBytes(codeOutCharset));
			bos.write(reqBody.getBytes(codeOutCharset));
			bos.flush();
			
			BufferedInputStream instr = new BufferedInputStream(socket.getInputStream());
			byte[] b = new byte[1024];
			int len = 0;
			//sbstr.delete(0, sbstr.length());
			buffer.position(len);
			
			while (((len = instr.read(b)) != -1))
			{
				//log.info("len:["+len+"]");
				//sbstr.append(new String(b, 0, len,codeOutCharset));
				//System.out.println("socket client _len:["+len+"]");
				buffer.position(ttlLoc);
				buffer.put(b);
				ttlLoc = ttlLoc + len;
				
			}
			b = null;
			//log.info("最后len:["+len+"]");
			//log.info("返回报文：【"+sbstr.toString()+"】");
			
			if (instr != null)
				instr.close();
			if (bos != null)
				bos.close();
			instr = null;
			bos = null;
			

		}
		catch (IOException ex)
		{
			//ex.printStackTrace();
			//log.error("与服务端["+this.getServerIP()+":"+this.getServerPort()+"]SOCKET通讯异常【"+ex.getLocalizedMessage()+"】");

		}
		finally
		{

			if (socket != null)
				try
				{
					socket.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
		
		retStr = new byte[ttlLoc];
		buffer.flip().get(retStr);
		//System.out.println("socket client收到报文长度:["+retStr.length+"]");
		buffer = null;
		
		return retStr;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
