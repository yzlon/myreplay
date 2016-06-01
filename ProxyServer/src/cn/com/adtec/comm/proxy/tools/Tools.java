package cn.com.adtec.comm.proxy.tools;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


import org.apache.commons.httpclient.HttpException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


import cn.com.adtec.comm.proxy.bean.NewMoniObject;

import cn.com.adtec.comm.proxy.client.HttpSoapClient;



public class Tools {
	//private static StringBuilder sb = new StringBuilder();
	//private static int count=0;
	//public static ConcurrentLinkedQueue<SOAPsendBean> soapQueue = new ConcurrentLinkedQueue<SOAPsendBean>();
	/**
	 * 对字串进行右补空格
	 * @param str
	 * @param len
	 * @return
	 */
	public static String addonSpace(String str, int len)
	{
		if (str == null)
		{
			for (int i = 0; i < len; i++)
			{
				str += " ";
			}

		}
		else
		{
			int length = str.length();
			if (length < len)
				for (int i = 0; i < len - length; i++)
				{
					str += " ";
				}
		}
		return str;
	}
	
	/**
	 * 发送到第三方Web Service 服务器，并接收返回报文
	 * @param pkgType	  	报文格式:HTML,SOAP,TCP
	 * @param serverIP		服务器IP地址
	 * @param serverPORT	服务器端口
	 * @param appName		应用名称（匹配路径）
	 * @param timeout		连接超时，单位：秒
	 * @param reqHead		请求报文头（可选）
	 * @param reqBody		请求报文体（必送）
	 * @param codeOutCharset	发送报文时的编码格式
	 * @return			响应报文（字节数组）
	 * @throws NumberFormatException
	 * @throws HttpException
	 * @throws IOException
	 */
	public static byte[] sendOutToSOAP(String pkgType,String serverIP,String serverPORT,String appName,String timeout,String reqHead,String reqBody,String codeOutCharset,IoSession session,String linkMode) throws NumberFormatException, HttpException, IOException
	{

	if (null==pkgType || null==serverIP || null==serverPORT  || null==appName || null==timeout)
	{
		System.out.println("有内容为空！");
			return null;   //处理失败
	}
		
		//根据不同的协议,调用不同的客户端处理包
		//System.out.println("pkgType:["+pkgType+"]");
		if (pkgType.trim().equalsIgnoreCase("http")||pkgType.trim().equalsIgnoreCase("soap"))
		{
			StringBuilder url =new StringBuilder();
			url.append("http://");
			url.append(serverIP.trim());
			url.append(":");
			url.append(serverPORT.trim());
			if (appName.trim().indexOf("/")!=-1)
			{
				url.append(appName.trim());
			}
			else
			{
				url.append("/");
				url.append(appName.trim());
			}
			
			//System.out.println("URL:["+url+"]");
			/*
			SOAPsendBean soab = new SOAPsendBean();
			soab.setReqBody(reqBody);
			soab.setReqHead(reqHead);
			soab.setSession(session);
			soab.setTimeOut(Integer.parseInt(timeout.trim())*1000);
			soab.setUrl(url.toString());
			soab.setCharset(codeOutCharset);
			soab.setLinkMode(linkMode);
			soapQueue.add(soab);
			
			SendSoaPTask.start();
			*/
			//return new HttpSoapClient(url.toString(),Integer.parseInt(timeout.trim())*1000).doPostRetArray(reqBody, codeOutCharset.trim());
			return new HttpSoapClient(url.toString(),Integer.parseInt(timeout.trim())*1000).doPostRetArrayNetbank(reqBody, codeOutCharset.trim(),reqHead,serverIP.trim(),serverPORT.trim());
		}
		else		
			return null;
	}

	
	public static byte[] sendOutToSOAP2(String pkgType,String serverIP,String serverPORT,String appName,String timeout,String reqHead,String reqBody,String codeOutCharset) throws NumberFormatException, HttpException, IOException
	{

	if (null==pkgType || null==serverIP || null==serverPORT  || null==appName || null==timeout)
	{
		System.out.println("有内容为空！");
			return null;   //处理失败
	}
		
		//根据不同的协议,调用不同的客户端处理包
		//System.out.println("pkgType:["+pkgType+"]");
		if (pkgType.trim().equalsIgnoreCase("http")||pkgType.trim().equalsIgnoreCase("soap"))
		{
			StringBuilder url =new StringBuilder();
			url.append("http://");
			url.append(serverIP.trim());
			url.append(":");
			url.append(serverPORT.trim());
			if (appName.trim().indexOf("/")!=-1)
			{
				url.append(appName.trim());
			}
			else
			{
				url.append("/");
				url.append(appName.trim());
			}
			
			System.out.println("URL:["+url+"]");
			return new HttpSoapClient(url.toString(),Integer.parseInt(timeout.trim())*1000).doPostRetArray(reqBody, codeOutCharset.trim());
		}
		else		
			return null;
	}
	

	/**
	 * 加密
	 * @param txt
	 * @return
	 */
	public static String encodeString(byte[] txt)
	{
		return (new BASE64Encoder()).encodeBuffer(txt);
	}

	/**
	 * 解密
	 * @param str
	 * @return
	 */
	public static byte[] decodeString(String str)
	{
		try {
			return (new BASE64Decoder()).decodeBuffer(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 用指定编码格式压缩字串，然后转换成ISO-8859-1的字串
	 * @param oldStr		原未压缩串
	 * @param charset		指定编码
	 * @return
	 */
	public static String compress(String oldStr,String charset) 
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip=null;
		String compressedStr = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(oldStr.getBytes(charset));
			gzip.close();
			compressedStr = new String(out.toByteArray(),"ISO-8859-1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			gzip = null;
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out = null;
		}
		
		return compressedStr;
	}

	/**
	 * 先用ISO-8859-1编码解压缩字串，再用指定编码转换解压缩后的字串
	 * @param str			原压缩串
	 * @param charset		指定编码
	 * @return
	 */
	public static String unCompress(String str,String charset)
	{
		if (null==str ||str.length()<=0)
		{
			return str;
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String temp = null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
			GZIPInputStream gzip = new GZIPInputStream(in);
			byte[] buffer = new byte[2048];
			int n=0;
			while((n=gzip.read(buffer))>=0)
			{
				out.write(buffer, 0, n);
			}
			temp = out.toString(charset);
			out.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		out = null;
		return temp;
	}
	
	public static void changeObj2Buf(IoBuffer buf, NewMoniObject nmo,String charset) throws UnsupportedEncodingException {
		buf.put(nmo.getVersionNo().getBytes(charset));
		buf.put(nmo.getPkgHeadLen().getBytes(charset));
		buf.put(nmo.getTyp().getBytes(charset));
		if (nmo.getTyp().equals("1"))
		{
			//控制报文
			if (nmo.getCtrlPkgBody()!=null)
			{
				buf.put(nmo.getCtrlPkgBody().getBytes(charset));
			}
		}
		else if (nmo.getTyp().equals("0"))
		{
			//数据报文,报文头
			buf.put(nmo.getPackFlg().getBytes(charset));
			buf.put(nmo.getLeftTimeOut().getBytes(charset));
			buf.put(nmo.getMD5().getBytes(charset));
			
			//报文体
			buf.put(nmo.getDataLen().getBytes(charset));
			buf.put(nmo.getEncptTyp().getBytes(charset));
			buf.put(nmo.getTxCd().getBytes(charset));
			buf.put(nmo.getFmtData().getBytes(charset));
		}

		
	}
	
	public static void returnSOAP(IoSession session,byte[] respTmp,String charset,String linkMode) throws UnsupportedEncodingException
	{
		System.out.println("respTmp.length【"+(respTmp.length)+"】内容【"+new String(respTmp,charset)+"】");
		StringBuilder sb = new StringBuilder();
		
		sb.delete(0, sb.length());
		sb.append("HTTP/1.1 200 OK\r\n");

		//sb.append("\r\nServer: Websphere Application Server/6.1\r\nContent-Type: text/html;\r\nContent-Length: ");
		sb.append("Content-Type: text/html;\r\nContent-Length: ");
		sb.append(respTmp.length);
		//sb.append("\r\nConnection: Keep-Alive\r\nAccept: image/jpeg, */*\r\nAccept-Ranges: bytes");
		sb.append("\r\n\r\n");
		sb.append(new String(respTmp,charset));

		if (sb.length()>10000)
		{
			/*
			int loc = 0;
			int times = 1;
			while (loc<=sb.length())
			{
				
				session.write(sb.substring(loc, times*10000<sb.length()? times*10000:sb.length()));
				loc += times*10000;
				times++;
			}
			*/
			System.out.println(sb.toString());
			session.write(sb.toString());
		}
		else
		{
			session.write(sb.toString());
		}
		
		//
		if ("1".equals(linkMode))
		{
			//短连接
			System.out.println("即将关掉连接");
			session.close(false);
			session = null;
		}
		
		//sb = null;
	}
}
