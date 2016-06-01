package cn.com.adtec.comm.proxy.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.HashMap;


import javax.xml.soap.SOAPException;

import org.apache.commons.httpclient.HttpException;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import cn.com.adtec.comm.proxy.message.MessageManage;
import cn.com.adtec.comm.proxy.message.NodeStruct;

import cn.com.adtec.comm.proxy.server.AbstractSubServer;
import cn.com.adtec.comm.proxy.tools.Tools;

/**
 * 交易仿真
 * @author tangjb
 *
 */
public class COMM11 {

	private final static Logger log = LoggerFactory	.getLogger(COMM11.class);
	private IoSession session = null;

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}



	/**
	 * 交易仿真业务处理主入口
	 * @param reqBody		//上游系统发来的请求报文体
	 * @param session		//会话
	 * @param oldApp   		//原服务码，暂不用
	 * @param subsv			//所在节点的子服务器
	 * @param nodeNo		//通讯代理节点编号
	 * @param recBatch		//录制报文的批次号
	 * @param projID        //项目ID
	 * @param simLevel		//仿真级别 ,case---用例级 tran---交易级
	 * @return
	 * @throws IOException 
	 * @throws SOAPException 
	 */
	public byte[] process(String reqBody, IoSession session, String oldApp,AbstractSubServer subsv,String nodeNo,String recBatch, String projID,String simLevel,String caseId) throws SOAPException, IOException {
		// 0.获取请求报文
		StringBuilder receveMsg = new StringBuilder();
		// receveMsg.append(nco.getHead());
		receveMsg.append(reqBody);
		log.info("COMM11仿真时原请求报文【"+reqBody+"】长度【"+reqBody.length()+"】");
		this.setSession(session);

		// 1.组织报文的字段
		String[] fields = new String[] { "Tms_Trcd","Tms_rec_rule_batch", "Tms_proxy_node_no","Tms_proj_id","Tms_sim_level",
				"Tms_req_pkg","Tms_case_id"};
		//2.组织报文
		
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("Tms_Trcd", "COMM11");
		values.put("Tms_rec_rule_batch", recBatch);
		values.put("Tms_proxy_node_no", nodeNo);
		values.put("Tms_proj_id", projID==null?"":projID);
		values.put("Tms_sim_level", simLevel==null?"":simLevel);
		values.put("Tms_case_id", caseId==null?"":caseId);
		values.put("Tms_req_pkg", Tools.encodeString(receveMsg.toString().getBytes(subsv.getTmsCharSet())));

		
		byte[] completePkg = null;
		try {
			completePkg = MessageManage.assembleRequestSoapMessage(values, subsv.getTmsCharSet(), fields);
		} catch (SOAPException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//3.发往TMS
		byte[] simulateResponseBody=null;
		try {
			simulateResponseBody = Tools.sendOutToSOAP2("SOAP", subsv.getTmsIP(),subsv.getTmsPort(), subsv.getTmsSimulApp(), "120", null, new String(completePkg,subsv.getTmsCharSet()), subsv.getCharset());
			if (null==simulateResponseBody)
			{
				log.error("__simulation mode,TMS did not response__仿真交易，测试管理系统没有响应！");
				log.error(new String(completePkg,subsv.getCharset()));
			}
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

		//4.将实际字段返给请求端
		if (null!=simulateResponseBody)
		{
			simulateResponseBody = getRealResponsePkg(simulateResponseBody,subsv.getCharset());
			log.info(new String(simulateResponseBody,subsv.getCharset()));
		}
		
		fields = null;
		completePkg = null;
		receveMsg = null;
		values = null;
		return simulateResponseBody ;
	}


	/**
	 * 获取实际应该返给请求端的响应报文
	 * @param simulateResponseBody
	 * @param tmsCharSet
	 * @return
	 * @throws SOAPException
	 * @throws IOException
	 */
	private byte[] getRealResponsePkg(byte[] simulateResponseBody,
			String tmsCharSet) throws SOAPException, IOException {
		ArrayList<NodeStruct> list = MessageManage.parse("SOAP", simulateResponseBody, tmsCharSet, null);
		for (int i=0;i<list.size();i++)
		{
			if (list.get(i).getNodeName().equals("Tms_rsp_pkg"))
			{
				return Tools.decodeString(list.get(i).getNodeValue());
			}
		}
		list = null;
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
