package cn.com.adtec.comm.proxy.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


import javax.xml.soap.SOAPException;

import org.apache.mina.core.session.IoSession;


import cn.com.adtec.comm.proxy.bean.SOAPCommObject;
import cn.com.adtec.comm.proxy.message.MessageManage;
import cn.com.adtec.comm.proxy.message.NodeStruct;

import cn.com.adtec.comm.proxy.server.AbstractSubServer;
import cn.com.adtec.comm.proxy.server.SubService;
import cn.com.adtec.comm.proxy.tools.Tools;

/**
 * 回归测试
 * @author tangjb
 *
 */
public class TMS101 {


	private IoSession session = null;

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}




	/**
	 * 回归测试业务处理主入口
	 * 
	 * @param nco
	 * @return
	 */

	public byte[] process(SOAPCommObject nco, IoSession session, String charset) {
		// 1.获取请求报文
		StringBuilder receveMsg = new StringBuilder();
		// receveMsg.append(nco.getHead());
		receveMsg.append(nco.getBody());

		this.setSession(session);

		// 2.只需要解析报文中的某几个字段
		String[] fields = new String[] { "Tms_Trcd","Tms_serialno", "Tms_proxy_node_no",
				"Tms_regres_batch", "Tms_req_pkg","Tms_old_app" };
		try {
			ArrayList<NodeStruct> list = MessageManage.parse("SOAP", receveMsg
					.toString().getBytes(charset), charset, fields);
			if (null != list) {

				// 3.解析报文成功，进行数据校验
				String tmsReqPkg = null;    		//请求报文体
				String tmsRegresBatch = null;		//回归测试批次号
				String nodeNo=null;					//通讯节点编号
				String tmsOldApp=null;				//原交易应用名称或者交易码
				int numTmp = 0;
				for (int i = 0; i < list.size(); i++) {

					if (list.get(i).getNodeName().equalsIgnoreCase(
							"Tms_req_pkg")) {
						tmsReqPkg = list.get(i).getNodeValue();
						numTmp++;
					} else if (list.get(i).getNodeName().equalsIgnoreCase(
							"Tms_regres_batch")) {
						tmsRegresBatch = list.get(i).getNodeValue();
						numTmp++;
					}
					else if (list.get(i).getNodeName().equalsIgnoreCase(
					"Tms_proxy_node_no")) {
						nodeNo = list.get(i).getNodeValue();
						numTmp++;
					}
					else if (list.get(i).getNodeName().equalsIgnoreCase(
					"Tms_old_app")) {
						tmsOldApp = list.get(i).getNodeValue();
						numTmp++;
					}
					if (numTmp == 4) {
						break;
					}
				}// for (int i=0;i<list.size();i++)
				// 检查必需的字段
				if (null == tmsReqPkg || "".equals(tmsReqPkg)) {
					list = null;
					tmsReqPkg = null;
					tmsRegresBatch = null;
					receveMsg = null;
					fields = null;
					tmsOldApp = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_req_pkg字段内容必须上送!", charset);
				}
				if (null == tmsRegresBatch || "".equals(tmsRegresBatch)) {
					list = null;
					tmsReqPkg = null;
					tmsRegresBatch = null;
					receveMsg = null;
					fields = null;
					tmsOldApp = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_regres_batch记录条数字段必须上送!", charset);
				}
				if (null == nodeNo || "".equals(nodeNo)) {
					list = null;
					tmsReqPkg = null;
					tmsRegresBatch = null;
					receveMsg = null;
					fields = null;
					nodeNo = null;
					tmsOldApp = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_proxy_node_no通讯节点编号字段必须上送!", charset);
				}
				if (null == tmsOldApp || "".equals(tmsOldApp)) {
					list = null;
					tmsReqPkg = null;
					tmsRegresBatch = null;
					receveMsg = null;
					fields = null;
					nodeNo = null;
					tmsOldApp = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_old_app原服务码或者交易码字段必须上送!", charset);
				}
				// 4.业务处理,转发至下游系统,获取下游系统返回结果
				byte[] regresResponseBody = bussiness(tmsOldApp,tmsReqPkg,nodeNo,charset);

				if (null==regresResponseBody)
				{
					//5.返回失败报文
					MessageManage.assembleFailSOAPMessage("回归测试失败，代理服务器不支持此种报文格式或者下游系统没有响应！", charset);
				}
				else
				{
					// 5.返回成功报文
					HashMap<String, String> values = new HashMap<String, String>();
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getNodeName().equalsIgnoreCase(
								"Tms_serialno")) {
							values.put("Tms_serialno", list.get(i).getNodeValue());
							break;
						}
					}
					values.put("Tms_regres_batch", tmsRegresBatch);
					values.put("Tms_err_code", "SUCCESS");
					values.put("Tms_err_msg", "成功");
					values.put("Tms_rsp_pkg", new String(regresResponseBody,charset));
	
					String[] rsp_fields = new String[] { "Tms_serialno","Tms_regres_batch",
							"Tms_err_code", "Tms_err_msg","Tms_rsp_pkg" };
	
					list = null;
					regresResponseBody = null;
					tmsReqPkg = null;
					tmsRegresBatch = null;
					receveMsg = null;
					fields = null;
					nodeNo = null;
	
					return MessageManage.assembleSUCCSoapMessage(values, charset, rsp_fields,
							"AAAAAAA", "成功", "SUCCESS");
				}//if (null==regresResponseBody)
			}// if (null!=list),表明解析成功
			else {
				// log.error("message can not be parsed!报文解析失败！");
				// 返回失败报文
				//
				return MessageManage.assembleFailSOAPMessage("通讯代理服务器解析请求报文失败!",
						charset);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				return MessageManage.assembleFailSOAPMessage(e.getMessage(),charset);
			} catch (SOAPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				return MessageManage.assembleFailSOAPMessage(e.getMessage(),charset);
			} catch (SOAPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				return MessageManage.assembleFailSOAPMessage(e.getMessage(),charset);
			} catch (SOAPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		receveMsg = null;

		return null;
	}

	/*
	 * 20140724 注释by TJB
	public byte[] process(SOAPCommObject nco, IoSession session, String charset,String defNodeNo,AbstractSubServer subServer) {
		// 1.获取请求报文
		StringBuilder receveMsg = new StringBuilder();
		// receveMsg.append(nco.getHead());
		receveMsg.append(nco.getBody());

		this.setSession(session);

		// 2.只需要解析报文中的某几个字段
		String[] fields = new String[] { "Tms_Trcd","Tms_serialno", "Tms_proxy_node_no",
				"Tms_regres_batch", "Tms_req_pkg","Tms_old_app" };
		try {
			ArrayList<NodeStruct> list = MessageManage.parse("SOAP", receveMsg
					.toString().getBytes(charset), charset, fields);
			if (null != list) {

				// 3.解析报文成功，进行数据校验
				String tmsReqPkg = null;    		//请求报文体
				String tmsRegresBatch = null;		//回归测试批次号
				String nodeNo=null;					//通讯节点编号
				String tmsOldApp=null;				//原交易应用名称或者交易码
				int numTmp = 0;
				for (int i = 0; i < list.size(); i++) {

					if (list.get(i).getNodeName().equalsIgnoreCase(
							"Tms_req_pkg")) {
						tmsReqPkg = list.get(i).getNodeValue();
						numTmp++;
					} else if (list.get(i).getNodeName().equalsIgnoreCase(
							"Tms_regres_batch")) {
						tmsRegresBatch = list.get(i).getNodeValue();
						numTmp++;
					}
					else if (list.get(i).getNodeName().equalsIgnoreCase(
					"Tms_proxy_node_no")) {
						nodeNo = list.get(i).getNodeValue();
						numTmp++;
					}
					else if (list.get(i).getNodeName().equalsIgnoreCase(
					"Tms_old_app")) {
						tmsOldApp = list.get(i).getNodeValue();
						numTmp++;
					}
					if (numTmp == 4) {
						break;
					}
				}// for (int i=0;i<list.size();i++)
				// 检查必需的字段
				if (null == tmsReqPkg || "".equals(tmsReqPkg)) {
					list = null;
					tmsReqPkg = null;
					tmsRegresBatch = null;
					receveMsg = null;
					fields = null;
					tmsOldApp = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_req_pkg字段内容必须上送!", charset);
				}
				if (null == tmsRegresBatch || "".equals(tmsRegresBatch)) {
					list = null;
					tmsReqPkg = null;
					tmsRegresBatch = null;
					receveMsg = null;
					fields = null;
					tmsOldApp = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_regres_batch记录条数字段必须上送!", charset);
				}
				if (null == nodeNo || "".equals(nodeNo)) {
					list = null;
					tmsReqPkg = null;
					tmsRegresBatch = null;
					receveMsg = null;
					fields = null;
					nodeNo = null;
					tmsOldApp = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_proxy_node_no通讯节点编号字段必须上送!", charset);
				}
				else if (defNodeNo.equalsIgnoreCase(nodeNo.trim()))
				{
					list = null;
					tmsReqPkg = null;
					tmsRegresBatch = null;
					receveMsg = null;
					fields = null;
					nodeNo = null;
					tmsOldApp = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：通讯节点编号为["+nodeNo+"]的回归测试交易，不能发往节点编号为["+defNodeNo+"]的服务器!", charset);
				}
				if (null == tmsOldApp || "".equals(tmsOldApp)) {
					list = null;
					tmsReqPkg = null;
					tmsRegresBatch = null;
					receveMsg = null;
					fields = null;
					nodeNo = null;
					tmsOldApp = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_old_app原服务码或者交易码字段必须上送!", charset);
				}
				//
				
				
				

				// 4.业务处理,转发至下游系统,获取下游系统返回结果
				byte[] regresResponseBody = bussiness(tmsOldApp,tmsReqPkg,subServer);

				if (null==regresResponseBody)
				{
					//5.返回失败报文
					MessageManage.assembleFailSOAPMessage("回归测试失败，下游系统没有响应！", charset);
				}
				else
				{
					// 5.返回成功报文
					HashMap<String, String> values = new HashMap<String, String>();
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getNodeName().equalsIgnoreCase(
								"Tms_serialno")) {
							values.put("Tms_serialno", list.get(i).getNodeValue());
							break;
						}
					}
					values.put("Tms_regres_batch", tmsRegresBatch);
					values.put("Tms_err_code", "SUCCESS");
					values.put("Tms_err_msg", "成功");
					values.put("Tms_rsp_pkg", new String(regresResponseBody,charset));
	
					String[] rsp_fields = new String[] { "Tms_serialno","Tms_regres_batch",
							"Tms_err_code", "Tms_err_msg","Tms_rsp_pkg" };
	
					list = null;
					regresResponseBody = null;
					tmsReqPkg = null;
					tmsRegresBatch = null;
					receveMsg = null;
					fields = null;
					nodeNo = null;
	
					return MessageManage.assembleSUCCSoapMessage(values, charset, rsp_fields,
							"AAAAAAA", "成功", "SUCCESS");
				}//if (null==regresResponseBody)
			}// if (null!=list),表明解析成功
			else {
				// log.error("message can not be parsed!报文解析失败！");
				// 返回失败报文
				//
				return MessageManage.assembleFailSOAPMessage("通讯代理服务器解析请求报文失败!",
						charset);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				return MessageManage.assembleFailSOAPMessage(e.getMessage(),charset);
			} catch (SOAPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				return MessageManage.assembleFailSOAPMessage(e.getMessage(),charset);
			} catch (SOAPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				return MessageManage.assembleFailSOAPMessage(e.getMessage(),charset);
			} catch (SOAPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		receveMsg = null;

		return null;
	}
	*/
	

	private byte[] bussiness(String tmsOldApp,String pkgReqContent,String nodeNo,String charset) throws IOException {
		
		SubService subservice = SubService.getInstance();
		ConcurrentHashMap<String,AbstractSubServer> serverMap = subservice.getStartedNodeMap();
		if (null!=serverMap)
		{
			if (serverMap.containsKey(nodeNo))
			{
			
				if (serverMap.get(nodeNo).getMsgKind().equalsIgnoreCase("SOAP"))
				{
					return Tools.sendOutToSOAP2(serverMap.get(nodeNo).getCharset(), serverMap.get(nodeNo).getNextSystemIP(), serverMap.get(nodeNo).getNextSystemPort(), tmsOldApp, serverMap.get(nodeNo).getNextSystemTimeOut(), null, pkgReqContent, serverMap.get(nodeNo).getCharset());
				}
				else
				{
					return null;
/*					
					try {
						return MessageManage.assembleFailSOAPMessage("暂不支持该种报文格式！", charset);
					} catch (SOAPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
				}
			}
			else
			{
				return null;
/*				
				try {
					return MessageManage.assembleFailSOAPMessage("发送失败，对应代理服务器子服务器没有启动！", charset);
				} catch (SOAPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				
			}
		}
		else
		{
			return null;
		/*	
			try {
				return MessageManage.assembleFailSOAPMessage("取对应代理服务器子服务器信息时失败！", charset);
			} catch (SOAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}
		
		
	}
	
	/*   注释by TJB,20140724
	private byte[] bussiness(String tmsOldApp,String pkgReqContent,AbstractSubServer subServer) throws IOException {
		
		if (subServer.getMsgKind().equalsIgnoreCase("SOAP"))
		{
			return Tools.sendOutToSOAP(subServer.getCharset(), subServer.getNextSystemIP(), subServer.getNextSystemPort(), tmsOldApp, subServer.getNextSystemTimeOut(), null, pkgReqContent, subServer.getCharset());
		}
		else
		{
			return null;
		}
	}
	*/

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
