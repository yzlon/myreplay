package cn.com.adtec.comm.proxy.client;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;


public class HttpSoapClient {

	private String postUrl;
	private HttpConnectionManager connectionManager;
	private int iTimeOut=10000;			//单位:毫秒
	private int iSoTimeOut=60000;		//单位:毫秒
	private int iMaxConnections = 2000;	//总共最大连接数
	private int iConnPerHost=500;		//每主机连接最大会话数

	
	public  HttpSoapClient(String url){
		this.postUrl = url;
		//this.iTimeOut = timeout;
		//this.iSoTimeOut = sotimeout;
		//this.iMaxConnections = maxconn;
		//this.iConnPerHost = connperhost;
		
		
			//"http://10.136.2.26:7020/steaf/MainServlet" ;
		init();
	}
	
	public  HttpSoapClient(String url,int timeout){
		this.postUrl = url;
		this.iTimeOut = timeout;
		//this.iSoTimeOut = sotimeout;
		//this.iMaxConnections = maxconn;
		//this.iConnPerHost = connperhost;
		
		
			//"http://10.136.2.26:7020/steaf/MainServlet" ;
		init();
	}
	
	public  HttpSoapClient(String url,int timeout,int sotimeout,int maxconn,int connperhost){
		this.postUrl = url;
		this.iTimeOut = timeout;
		this.iSoTimeOut = sotimeout;
		this.iMaxConnections = maxconn;
		this.iConnPerHost = connperhost;
		
		
			//"http://10.136.2.26:7020/steaf/MainServlet" ;
		init();
	}
	
	private void init(){
		this.connectionManager = new MultiThreadedHttpConnectionManager();
		//this.connectionManager = new SimpleHttpConnectionManager(true);
	    HttpConnectionManagerParams params = new HttpConnectionManagerParams();
	    params.setConnectionTimeout(this.iTimeOut);
	    params.setSoTimeout(this.iSoTimeOut);
	    params.setDefaultMaxConnectionsPerHost(this.iConnPerHost);
	    params.setMaxTotalConnections(this.iMaxConnections);
	    //add by TJb ,20141124
	    params.setSendBufferSize(4096);
	    params.setReceiveBufferSize(4096);
	    params.setTcpNoDelay(true);
	   
	    
	    this.connectionManager.setParams(params);
	    //System.out.println("超时["+this.iTimeOut+"]秒SoTimeout["+this.iSoTimeOut+"]最大连接数["+this.iMaxConnections+"]");
	}
/*	
	public String doPost(String xml_msg,String charset) 
	  {
	    String result = null;
	    //log.info("postUrl :" + this.postUrl);
	    HttpClient client = new HttpClient(this.connectionManager);

	    PostMethod PostMsg = new PostMethod(this.postUrl);
	    //System.out.println("postUrl :" + this.postUrl);

	    
	    //PostMsg.addRequestHeader("SOAPAction","mainservlet");
	    PostMsg.addRequestHeader("Content-Type", "text/xml; charset="+charset);
	    //PostMsg.addRequestHeader("Host", "10.136.2.26:7020");
	    //PostMsg.addRequestHeader("Connection", "Keep-Alive");
	    //PostMsg.addRequestHeader("Cache-Control", "no-cache");
	    
	    //System.out.println("PostMsg.toString() :" + PostMsg.toString());
	    try {
	      
	    	PostMsg.setRequestEntity(new StringRequestEntity(xml_msg, null,charset));
	        client.executeMethod(PostMsg);
			if (PostMsg.getStatusCode() == 200) {
				result = new String(PostMsg.getResponseBodyAsString().getBytes(charset));
			
			    //System.out.println("发送通讯后处理结果:"+result);
			}else
			{
				result = "F";
			}
	    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
	      PostMsg.releaseConnection();
	      client.getHttpConnectionManager().closeIdleConnections(0);
	      PostMsg = null;
	      client = null;
	    }
	    return result;
	  }
*/	
	public byte[] doPostRetArray(String xml_msg,String charset) 
	  {
		byte[] result = null;
	    //log.info("postUrl :" + this.postUrl);
		
	    HttpClient client = new HttpClient(this.connectionManager);

	    PostMethod PostMsg = new PostMethod(this.postUrl);
	    //System.out.println("postUrl :" + this.postUrl);

	    try{
	    //PostMsg.addRequestHeader("SOAPAction","mainservlet");
	    	PostMsg.addRequestHeader("Content-Type", "text/xml; charset="+charset);
	    //PostMsg.addRequestHeader("Host", "10.136.2.26:7020");
	    //PostMsg.addRequestHeader("Connection", "Keep-Alive");
	    //PostMsg.addRequestHeader("Cache-Control", "no-cache");
	    
	    //System.out.println("PostMsg.toString() :" + PostMsg.toString());

	    	PostMsg.setRequestEntity(new StringRequestEntity(xml_msg, null,charset));
	    	  
	        client.executeMethod(PostMsg);
	        


	      if (PostMsg.getStatusCode() == 200) {
				result = PostMsg.getResponseBody();
			//	System.out.println("发送通讯后处理结果:"+new String(result,charset));
			
	      }else
	      {
	    	  result = PostMsg.getResponseBody();
	    	  //System.out.println("StatusCode______________________["+PostMsg.getStatusCode()+"]_________________");
	    	  
	    	  if (null!=result)
	    	  {
	    		  
	    	  }
	    	  else
	    	  {
	    		  StringBuilder tmpSB = new StringBuilder();
		    	  if (500==PostMsg.getStatusCode())
		    	  {
		    		  tmpSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns:Response xmlns:ns=\"http://www.cqrcb.com/esb\"><soapenv:Fault><FaultCode>TMS0001</FaultCode><FaultString>通讯代理等待【");
		    		  tmpSB.append(this.postUrl);
		    		  tmpSB.append("】返回响应报文超时！#通讯代理返回</FaultString><Detail><TxStatus>FAIL</TxStatus></Detail></soapenv:Fault></ns:Response></soapenv:Body></soapenv:Envelope>");
		    		  result = tmpSB.toString().getBytes(charset);
		    	  }
		    	  else if (404==PostMsg.getStatusCode())
		    	  {
		    		  tmpSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns:Response xmlns:ns=\"http://www.cqrcb.com/esb\"><soapenv:Fault><FaultCode>TMS0003</FaultCode><FaultString>服务端未配置该交易【");
		    		  tmpSB.append(this.postUrl);
		    		  tmpSB.append("！#通讯代理返回</FaultString><Detail><TxStatus>FAIL</TxStatus></Detail></soapenv:Fault></ns:Response></soapenv:Body></soapenv:Envelope>");
		    		  result = tmpSB.toString().getBytes(charset);
		    	  }
		    	  else
		    	  {
		    		  tmpSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns:Response xmlns:ns=\"http://www.cqrcb.com/esb\"><soapenv:Fault><FaultCode>TMS0004</FaultCode><FaultString>服务端【");
		    		  tmpSB.append(this.postUrl);
		    		  tmpSB.append("返回状态码：");
		    		  tmpSB.append(PostMsg.getStatusCode());
		    		  tmpSB.append("#通讯代理返回</FaultString><Detail><TxStatus>FAIL</TxStatus></Detail></soapenv:Fault></ns:Response></soapenv:Body></soapenv:Envelope>");
		    		  result = tmpSB.toString().getBytes(charset);
		    	  }
		    	  tmpSB = null;
	    	  }
	      }
	    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
	      PostMsg.releaseConnection();
	      client.getHttpConnectionManager().closeIdleConnections(0);
	      PostMsg = null;
	      client = null;
	    }
	    return result;
	  }
	
	
	/**
	 * 网银专用，需要对报文头进行解析
	 * @param xml_msg
	 * @param charset
	 * @param reqHead
	 * @param port 
	 * @param host 
	 * @return
	 */
	public byte[] doPostRetArrayNetbank(String xml_msg,String charset,String reqHead, String host, String port) 
	  {
		byte[] result = null;
	    System.out.println("postUrl :" + this.postUrl);
		
	    HttpClient client = new HttpClient(this.connectionManager);
	    
	    String methodKind = reqHead.substring(0, 4);
	    if (methodKind.indexOf("GET")!=-1)
	    {
	    	//get方法 
	    	GetMethod PostMsg = new GetMethod(this.postUrl);
	    	try{
		    	String[][] headers = parseHeader(reqHead);
		    	if (null!=headers)
		    	{
		    		for (int i=0;i<headers.length;i++)
		    		{
		    			if ("Host".equals(headers[i][0]))
		    			{
		    				headers[i][1] = host+":"+port;
		    			}
		    			PostMsg.addRequestHeader(headers[i][0],headers[i][1].trim());
		    			System.out.println(headers[i][0]+"___"+headers[i][1]);
		    		}
		    	}
		    //PostMsg.addRequestHeader("SOAPAction","mainservlet");
		    //PostMsg.addRequestHeader("Content-Type", "text/xml; charset="+charset);
		    //PostMsg.addRequestHeader("Host", "10.136.2.26:7020");
		    //PostMsg.addRequestHeader("Connection", "Keep-Alive");
		    //PostMsg.addRequestHeader("Cache-Control", "no-cache");
		    
		    
		    System.out.println("PostMsg.toString() :" + PostMsg.toString());
		    	
		    	  
		        client.executeMethod(PostMsg);
		        System.out.println("PostMsg.getStatusCode() :" + PostMsg.getStatusCode()); 
		        Header[] tmp = PostMsg.getResponseHeaders();
		    	  System.out.println("________________");
		    	  for (Header a:tmp)
		    	  {
		    		  
		    		  System.out.println(a.getName()+":"+a.getValue());
		    	  }
		    	  System.out.println("________________");

		      if (PostMsg.getStatusCode() == 200) {
					result = PostMsg.getResponseBody();
					System.out.println("发送通讯后处理结果:"+new String(result,charset));
				
		      }else
		      {
		    	 
		    	  result = PostMsg.getResponseBody();
		    	  System.out.println("StatusCode______________________["+PostMsg.getStatusCode()+"]_________________");
		    	  System.out.println("result["+result+"]_________________");
		    	  
		    	  if (null!=result)
		    	  {
		    		  
		    	  }
		    	  else
		    	  {
		    		  StringBuilder tmpSB = new StringBuilder();
			    	  if (500==PostMsg.getStatusCode())
			    	  {
			    		  tmpSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns:Response xmlns:ns=\"http://www.cqrcb.com/esb\"><soapenv:Fault><FaultCode>TMS0001</FaultCode><FaultString>通讯代理等待【");
			    		  tmpSB.append(this.postUrl);
			    		  tmpSB.append("】返回响应报文超时！#通讯代理返回</FaultString><Detail><TxStatus>FAIL</TxStatus></Detail></soapenv:Fault></ns:Response></soapenv:Body></soapenv:Envelope>");
			    		  result = tmpSB.toString().getBytes(charset);
			    	  }
			    	  else if (404==PostMsg.getStatusCode())
			    	  {
			    		  tmpSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns:Response xmlns:ns=\"http://www.cqrcb.com/esb\"><soapenv:Fault><FaultCode>TMS0003</FaultCode><FaultString>服务端未配置该交易【");
			    		  tmpSB.append(this.postUrl);
			    		  tmpSB.append("！#通讯代理返回</FaultString><Detail><TxStatus>FAIL</TxStatus></Detail></soapenv:Fault></ns:Response></soapenv:Body></soapenv:Envelope>");
			    		  result = tmpSB.toString().getBytes(charset);
			    	  }
			    	  else
			    	  {
			    		  tmpSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns:Response xmlns:ns=\"http://www.cqrcb.com/esb\"><soapenv:Fault><FaultCode>TMS0004</FaultCode><FaultString>服务端【");
			    		  tmpSB.append(this.postUrl);
			    		  tmpSB.append("返回状态码：");
			    		  tmpSB.append(PostMsg.getStatusCode());
			    		  tmpSB.append("#通讯代理返回</FaultString><Detail><TxStatus>FAIL</TxStatus></Detail></soapenv:Fault></ns:Response></soapenv:Body></soapenv:Envelope>");
			    		  result = tmpSB.toString().getBytes(charset);
			    	  }
			    	  System.out.println("tmpSB["+tmpSB.toString()+"]");
			    	  tmpSB = null;
		    	  }
			      }
			    } catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
			      PostMsg.releaseConnection();
			      client.getHttpConnectionManager().closeIdleConnections(0);
			      PostMsg = null;
			      client = null;
			    }
	    }//if (methodKind.indexOf("GET")!=-1)
	    else
	    {
	    	PostMethod PostMsg = new PostMethod(this.postUrl);
	    //System.out.println("postUrl :" + this.postUrl);

	    try{
	    	String[][] headers = parseHeader(reqHead);
	    	if (null!=headers)
	    	{
	    		for (int i=0;i<headers.length;i++)
	    		{
	    			if ("Host".equals(headers[i][0]))
	    			{
	    				headers[i][1] = host+":"+port;
	    			}
	    			PostMsg.addRequestHeader(headers[i][0],headers[i][1].trim());
	    			System.out.println(headers[i][0]+"___"+headers[i][1]);
	    		}
	    	}
	    //PostMsg.addRequestHeader("SOAPAction","mainservlet");
	    //PostMsg.addRequestHeader("Content-Type", "text/xml; charset="+charset);
	    //PostMsg.addRequestHeader("Host", "10.136.2.26:7020");
	    //PostMsg.addRequestHeader("Connection", "Keep-Alive");
	    //PostMsg.addRequestHeader("Cache-Control", "no-cache");
	    
	    
	    System.out.println("PostMsg.toString() :" + PostMsg.toString());
	    	if (null!=xml_msg && xml_msg.length()>0)
	    	{
	    		PostMsg.setRequestEntity(new StringRequestEntity(xml_msg, null,charset));
	    		System.out.println("报文体不为空！");
	    	}
	    	  
	        client.executeMethod(PostMsg);
	        System.out.println("PostMsg.getStatusCode() :" + PostMsg.getStatusCode()); 


	      if (PostMsg.getStatusCode() == 200) {
				result = PostMsg.getResponseBody();
			//	System.out.println("发送通讯后处理结果:"+new String(result,charset));
			
	      }else
	      {
	    	  Header[] tmp = PostMsg.getResponseHeaders();
	    	  System.out.println("________________");
	    	  for (Header a:tmp)
	    	  {
	    		  
	    		  System.out.println(a.getName()+":"+a.getValue());
	    	  }
	    	  System.out.println("________________");
	    	  result = PostMsg.getResponseBody();
	    	  System.out.println("StatusCode______________________["+PostMsg.getStatusCode()+"]_________________");
	    	  System.out.println("result["+result+"]_________________");
	    	  
	    	  if (null!=result)
	    	  {
	    		  
	    	  }
	    	  else
	    	  {
	    		  StringBuilder tmpSB = new StringBuilder();
		    	  if (500==PostMsg.getStatusCode())
		    	  {
		    		  tmpSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns:Response xmlns:ns=\"http://www.cqrcb.com/esb\"><soapenv:Fault><FaultCode>TMS0001</FaultCode><FaultString>通讯代理等待【");
		    		  tmpSB.append(this.postUrl);
		    		  tmpSB.append("】返回响应报文超时！#通讯代理返回</FaultString><Detail><TxStatus>FAIL</TxStatus></Detail></soapenv:Fault></ns:Response></soapenv:Body></soapenv:Envelope>");
		    		  result = tmpSB.toString().getBytes(charset);
		    	  }
		    	  else if (404==PostMsg.getStatusCode())
		    	  {
		    		  tmpSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns:Response xmlns:ns=\"http://www.cqrcb.com/esb\"><soapenv:Fault><FaultCode>TMS0003</FaultCode><FaultString>服务端未配置该交易【");
		    		  tmpSB.append(this.postUrl);
		    		  tmpSB.append("！#通讯代理返回</FaultString><Detail><TxStatus>FAIL</TxStatus></Detail></soapenv:Fault></ns:Response></soapenv:Body></soapenv:Envelope>");
		    		  result = tmpSB.toString().getBytes(charset);
		    	  }
		    	  else
		    	  {
		    		  tmpSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns:Response xmlns:ns=\"http://www.cqrcb.com/esb\"><soapenv:Fault><FaultCode>TMS0004</FaultCode><FaultString>服务端【");
		    		  tmpSB.append(this.postUrl);
		    		  tmpSB.append("返回状态码：");
		    		  tmpSB.append(PostMsg.getStatusCode());
		    		  tmpSB.append("#通讯代理返回</FaultString><Detail><TxStatus>FAIL</TxStatus></Detail></soapenv:Fault></ns:Response></soapenv:Body></soapenv:Envelope>");
		    		  result = tmpSB.toString().getBytes(charset);
		    	  }
		    	  tmpSB = null;
	    	  }
		      }
		    } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
		      PostMsg.releaseConnection();
		      client.getHttpConnectionManager().closeIdleConnections(0);
		      PostMsg = null;
		      client = null;
		    }
	    }
	    return result;
	  }
	private String[][] parseHeader(String reqHead) {
		if (null==reqHead)
			return null;
		String[][] headers = null;
		String[] lines = reqHead.split("\\n");
		System.out.println("开始拆解报文头lines_length["+lines.length+"]");
		/*
		int c=0;
		for (String k:lines)
		{
			c++;
			System.out.print(c+"___");
			System.out.println(k);
		}
		*/
		headers = new String[lines.length-2][2];
		for (int i=0;i<headers.length;i++)
		{
			String[] tmp = lines[i+1].split(":");
			headers[i][0]=tmp[0];
			headers[i][1]=tmp[1];
		}
		
		return headers;
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//String xmlStr = "<?xml version=\"1.0\" encoding=\"GBK\"?><soap:Envelope><shbx xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"></shbx><soap:Header><in:system xmlns:in=\"http://www.molss.gov.cn/\"><para usr=\"hncsyh\"></para><para pwd=\"262626\"></para><para funid=\"I00.02.02\"></para></in:system></soap:Header><soap:Body><in:business xmlns:in=\"http://www.molss.gov.cn/\"><para code=\"CS\"></para><para type=\"003\"></para><para time=\"2010-09-29 12:47:44\"></para><para cae154=\"6223687310836851392\"></para><para aae140=\"150\"></para></in:business></soap:Body></soap:Envelope>";
		//new HttpSoapTest().doPost(xmlStr);
	}

}
