package cn.com.adtec.comm.proxy.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;


import javax.xml.soap.SOAPException;

import org.apache.commons.httpclient.HttpException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import cn.com.adtec.comm.proxy.message.MessageManage;
import cn.com.adtec.comm.proxy.message.NodeStruct;

import cn.com.adtec.comm.proxy.server.AbstractSubServer;
import cn.com.adtec.comm.proxy.tools.Tools;

/**
 * 保存录制报文
 * @author tangjb
 *
 */
public class COMM01 implements Runnable {
	private final static Logger log = LoggerFactory	.getLogger(COMM01.class);
	private String requestBody;				//请求报文体
	private String responseBody;			//响应报文体
	private String oldAppName;				//原服务码或者交易名称
	private AbstractSubServer subserver;	//子服务器
	private String proxyNodeNo;				//通讯代理节点编号
	private String recBatchNo;				//报文规则的批次号
	private String tranBeginTime;			//交易开始时间
	private String tranEndTime;				//交易结束时间
	private String filesvrTestEnv;			//文件服务器所在测试环境
	private String requestHead;				//请求报文头
	private String responseHead;			//响应报文头

	public String getFilesvrTestEnv() {
		return filesvrTestEnv;
	}

	public void setFilesvrTestEnv(String fsvrTestEnv) {
		if (null==fsvrTestEnv)
		{
			this.filesvrTestEnv = "";
		}
		else
		{
			this.filesvrTestEnv = fsvrTestEnv;
		}
	}

	public String getTranBeginTime() {
		return tranBeginTime;
	}

	public void setTranBeginTime(String tranBeginTime) {
		this.tranBeginTime = tranBeginTime;
	}

	public String getTranEndTime() {
		return tranEndTime;
	}

	public void setTranEndTime(String tranEndTime) {
		this.tranEndTime = tranEndTime;
	}

	public String getProxyNodeNo() {
		return proxyNodeNo;
	}

	public void setProxyNodeNo(String proxyNodeNo) {
		this.proxyNodeNo = proxyNodeNo;
	}

	public String getRecBatchNo() {
		return recBatchNo;
	}

	public void setRecBatchNo(String recBatchNo) {
		this.recBatchNo = recBatchNo;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getOldAppName() {
		return oldAppName;
	}

	public void setOldAppName(String oldAppName) {
		this.oldAppName = oldAppName;
	}

	public AbstractSubServer getSubserver() {
		return subserver;
	}

	public void setSubserver(AbstractSubServer subserver) {
		this.subserver = subserver;
	}

	/**
	 * 构造方法 
	 * @param reqBody		//请求报文体
	 * @param resBody		//响应报文体
	 * @param oldApp		//原服务码或者交易名称 ,用于保存，下次做回归测试
	 * @param subsv			//对应的子服务器对象，可获取相关参数
	 * @param nodeNo		//对应的代理节点编号
	 * @param recBatch		//匹配到的报文过滤规则的批次编号
	 * @param beginTime		//交易开始时间
	 * @param endTime		//交易结束时间
	 * @param fsvrenv		//文件服务器所在测试环境
	 * @param reqHead		//请求报文头
	 * @param rspHead		//响应报文头
	 * @throws UnsupportedEncodingException 
	 */
	public COMM01(String reqBody,String resBody,String oldApp,AbstractSubServer subsv,String nodeNo,String recBatch,String beginTime,String endTime,String fsvrenv,String reqHead,String rspHead) throws UnsupportedEncodingException
	{
		//将原报文全部压缩
		//log.info("发送之前：原响应报文["+resBody+"]");
		//.getBytes(subsv.getTmsCharSet())
		this.setRequestBody(Tools.encodeString(Tools.compress(reqBody,subsv.getTmsCharSet()).getBytes(subsv.getTmsCharSet())));
		
		this.setResponseBody(Tools.encodeString(Tools.compress(resBody,subsv.getTmsCharSet()).getBytes(subsv.getTmsCharSet())));
				//Tools.encodeString(resBody.getBytes(subsv.getTmsCharSet())));
		
		//log.info("解密响应报文["+new String(Tools.decodeString(this.getResponseBody()),subsv.getTmsCharSet())+"]");
		
		this.setOldAppName(oldApp);
		this.setSubserver(subsv);
		this.setProxyNodeNo(nodeNo);
		this.setRecBatchNo(recBatch);
		this.setTranBeginTime(beginTime);
		this.setTranEndTime(endTime);
		this.setFilesvrTestEnv(fsvrenv);
		if (null==reqHead)
		{
			this.setRequestHead("");
		}
		else
		{
			this.setRequestHead(Tools.encodeString(reqHead.getBytes(subsv.getTmsCharSet())));
		}
		if (null==rspHead)
		{
			this.setResponseHead("");
		}
		else
		{
			this.setResponseHead(Tools.encodeString(rspHead.getBytes(subsv.getTmsCharSet())));
		}
	}
	
	public void run() {
		// 1.组织报文的字段
		String[] fields = new String[] { "Tms_Trcd","Tms_rec_rule_batch", "Tms_proxy_node_no",
				"Tms_tran_req_time", "Tms_tran_rsp_time","Tms_old_app" ,"Tms_req_pkg","Tms_rsp_pkg","Tms_test_env","Tms_req_head","Tms_rsp_head"};
		//2.组织报文
		
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("Tms_Trcd", "COMM01");
		values.put("Tms_rec_rule_batch", this.getRecBatchNo());
		values.put("Tms_proxy_node_no", this.getProxyNodeNo());
		values.put("Tms_tran_req_time", this.getTranBeginTime());
		values.put("Tms_tran_rsp_time", this.getTranEndTime());
		values.put("Tms_old_app", this.getOldAppName());
		values.put("Tms_req_pkg", this.getRequestBody());
		values.put("Tms_rsp_pkg", this.getResponseBody());
		values.put("Tms_test_env", this.getFilesvrTestEnv());
		values.put("Tms_req_head", this.getRequestHead());
		values.put("Tms_rsp_head", this.getResponseHead());
		
		byte[] completePkg = null;
		try {
			completePkg = MessageManage.assembleRequestSoapMessage(values, this.getSubserver().getTmsCharSet(), fields);
			//System.out.println("汤completePkg.length:["+completePkg.length+"]");
			/*for (int i=0;i<completePkg.length;i++)
			{
				System.out.print(Integer.toHexString(completePkg[i])+" ");
				if (i%15==0)
				{
					System.out.print("\n");
				}
			}*/
			
		} catch (SOAPException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
/*		try {
			log.info("发送TMS报文：["+new String(completePkg,"UTF-8")+"]");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}*/
		//3.发往TMS
		byte[] regresResponseBody=null;
		try {
			//log.info("TMS IP:["+this.getSubserver().getTmsIP()+"]port["+this.getSubserver().getTmsPort()+"]");
			
			regresResponseBody = Tools.sendOutToSOAP2("SOAP", this.getSubserver().getTmsIP(),this.getSubserver().getTmsPort(), this.getSubserver().getTmsPkgSendBackApp(), "120", null, new String(completePkg,this.getSubserver().getTmsCharSet()), this.getSubserver().getTmsCharSet());
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (HttpException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
				// 4.判断返回结果
				
				if (null==regresResponseBody)
				{
					log.info("COMM01:TMS has no response[测试管理系统没有响应]!");
				}
				else
				{
					log.info("TMS响应报文【"+new String(regresResponseBody,this.getSubserver().getTmsCharSet())+"】!");
					// 5.分析响应报文
					ArrayList<NodeStruct> list = MessageManage.parse("SOAP", regresResponseBody, this.getSubserver().getTmsCharSet(), null);
					String errorCode = null;
					String errorMsg = null;
					int count = 0;
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getNodeName().equalsIgnoreCase(
								"Tms_err_code")) {
							errorCode = list.get(i).getNodeValue();
							count++;
						}
						else if (list.get(i).getNodeName().equalsIgnoreCase(
								"Tms_err_msg")) {
							errorMsg = list.get(i).getNodeValue();
							count++;
						}
						if (count>=2)
						{
							break;
						}
					}
					
					
					if ("SUCCESS".equals(errorCode))
					{
						log.info("___规则批次号["+this.getRecBatchNo()+"],将收集报文发送给TMS成功！");
						
					}
					else
					{
						log.info("___规则批次号["+this.getRecBatchNo()+"],将收集报文发送给TMS失败！原因：["+errorMsg+"]");
					}
					list = null;
					errorCode = null;
					errorMsg = null;
				}//if (null==regresResponseBody)
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		fields = null;
		completePkg = null;
		values = null;
		regresResponseBody = null;
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getRequestHead() {
		return requestHead;
	}

	public void setRequestHead(String requestHead) {
		this.requestHead = requestHead;
	}

	public String getResponseHead() {
		return responseHead;
	}

	public void setResponseHead(String responseHead) {
		this.responseHead = responseHead;
	}

}
