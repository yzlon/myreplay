package cn.com.adtec.comm.proxy.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.NodeList;

/**
 * 报文解析、组装类
 * 
 * @author tangjb
 *
 */
public class MessageManage {
	// private final static Logger log = LoggerFactory
	// .getLogger(MessageManage.class);
	/**
	 * 解析报文
	 * 
	 * @param msgType
	 *            报文类型，SOAP、XML（纯XML）、FIX（定长）、FIXSEP（带分隔符的流式报文）
	 * @param msgStr
	 *            报文被打散的字节数组
	 * @param encode
	 *            编码格式
	 * @param fieldList
	 *            字段列表(可选)
	 * @return 由上而下，由外及内，由左至右依序拆成的节点有序列表
	 * @throws IOException
	 * @throws SOAPException
	 * @throws DocumentException
	 * @throws UnsupportedEncodingException
	 */
	public static ArrayList<NodeStruct> parse(String msgType, byte[] msgStr, String encode, String[] fieldList)
			throws SOAPException, IOException {
		if (msgType.equals("SOAP")) {
			return parsedBySoap(msgStr, encode, fieldList);
		}

		return null;
	}

	/**
	 * 解析SOAP报文
	 * 
	 * @param msgStr
	 *            报文原内容
	 * @param encode
	 *            编码格式
	 * @param fieldList
	 *            字段列表(可选)
	 * @return 节点有序列表
	 * @throws SOAPException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws DocumentException
	 */
	private static ArrayList<NodeStruct> parsedBySoap(byte[] msgStr, String encode, String[] fieldList)
			throws SOAPException, IOException {
		SOAPMessage msg = null;
		MessageFactory mf = MessageFactory.newInstance();
		msg = mf.createMessage(null, new ByteArrayInputStream(msgStr));
		// log.info(new String(msgStr,encode) );
		/*
		 * SOAPHeader header = msg.getSOAPHeader();
		 * 
		 * if (null==header) { log.info("header is null"); } else { for
		 * (Iterator<Node> ite = header.getChildElements();ite.hasNext();) {
		 * Node node = (Node) ite.next(); if (null!=node) { log.info(
		 * "Header Node["+node.getNodeName()+"]value["+node.getValue()+"]"); }
		 * else { log.info("Header Node is null,value[null]"); } node = null; }
		 * } header = null;
		 */
		SOAPBody body = null;

		body = msg.getSOAPBody();
		mf = null;
		msg = null;

		return getBody(body, fieldList);
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<NodeStruct> getBody(SOAPBody body, String[] fieldList) {
		if (null != body) {
			int parentLevel = 0;
			ArrayList<NodeStruct> nodeArrList = new ArrayList<NodeStruct>();
			for (Iterator<Node> ite = body.getChildElements(); ite.hasNext();) {
				Node node = ite.next();
				getNode(node, parentLevel, null, nodeArrList, fieldList);
			}
			return nodeArrList;
		}
		return null;
	}

	private static void getNode(Node node, int parentLevel, Node parentNode, ArrayList<NodeStruct> nodeArrList,
			String[] fieldList) {
		// System.out.println("node["+node+"]node.getNodeName:["+node.getNodeName()+"]");
		if (null != node && node.hasChildNodes()) {
			parentLevel++;
			// log.info("Node name["+node.getLocalName()+"]
			// parentLevel:["+parentLevel+"]____URI["+node.getNamespaceURI()+"]");
			NodeStruct ns = new NodeStruct();
			ns.setNodeName(node.getLocalName());
			ns.setNodeLevel(parentLevel);
			if (null != parentNode) {
				ns.setParentNodeLevel(parentLevel - 1);
				ns.setParentNodeName(parentNode.getLocalName());
			}
			if (null != fieldList) {
				for (int i = 0; i < fieldList.length; i++) {
					if (fieldList[i].equalsIgnoreCase(node.getLocalName())) {
						nodeArrList.add(ns);
						break;
					}
				}
			} else {
				nodeArrList.add(ns);
			}
			ns = null;

			NodeList ite = node.getChildNodes();
			int len = ite.getLength();
			for (int i = 0; i < len; i++) {
				getNode((Node) ite.item(i), parentLevel, node, nodeArrList, fieldList);
			}
			ite = null;
		} else if (null != node) {
			// parentLevel--;
			if (null == node.getNodeName() || node.getNodeName().equals("#text")) {
				// log.info("Node
				// Node["+parentNode.getNodeName()+"]value["+node.getValue()+"]thisLevel:["+(parentLevel)+"]");
				if (nodeArrList.size() >= 1) {
					for (int i = 0; i < nodeArrList.size(); i++) {
						// if
						// (nodeArrList.get(i).getNodeName().equals(parentNode.getLocalName()))
						if (parentNode!=null && nodeArrList.get(i).getNodeName().equals(parentNode.getLocalName())) {
							nodeArrList.get(i).setNodeValue(node.getValue());
							break;
						}
					}
					// nodeArrList.get(nodeArrList.size()-1).setNodeValue(node.getValue());
				}

			} else {
				NodeStruct ns = new NodeStruct();
				ns.setNodeName(node.getLocalName());
				ns.setNodeLevel(parentLevel);
				if (null != parentNode) {
					ns.setParentNodeLevel(parentLevel - 1);
					ns.setParentNodeName(parentNode.getLocalName());
				}
				if (null != fieldList) {
					for (int i = 0; i < fieldList.length; i++) {
						if (fieldList[i].equalsIgnoreCase(node.getLocalName())) {
							nodeArrList.add(ns);
							break;
						}
					}
				} else {
					nodeArrList.add(ns);
				}
				ns = null;
			}
		}
	}

	/**
	 * 组装回应给TMS的SOAP报文
	 * 
	 * @param values
	 *            各字段及对应的值
	 * @param encode
	 *            字符集编码 (暂时未用)
	 * @param fieldList
	 *            实际需要组装的字段数组
	 * @param responseCode
	 *            响应码
	 * @param responseMsg
	 *            响应信息
	 * @param txStatus
	 *            操作状态（SUCCESS,FAIL)
	 * @return SOAP格式的报文字节数组
	 * @throws SOAPException
	 * @throws IOException
	 */

	public static byte[] assembleSUCCSoapMessage(HashMap<String, String> values, String encode, String[] fieldList,
			String responseCode, String responseMsg, String txStatus) throws SOAPException, IOException {
		if (null == fieldList || null == values) {
			return null;
		}

		SOAPMessage msg = null;
		MessageFactory mf = MessageFactory.newInstance();
		msg = mf.createMessage();

		// 1.处理Envelop和公司现有报文规范一致
		msg.getSOAPPart().getEnvelope().addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
		msg.getSOAPPart().getEnvelope().addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		msg.getSOAPPart().getEnvelope().setPrefix("soapenv");
		msg.getSOAPPart().getEnvelope().removeNamespaceDeclaration("SOAP-ENV");
		SOAPHeader header = msg.getSOAPHeader();
		header.removeNamespaceDeclaration("SOAP-ENV");
		header.setPrefix("soapenv");

		// 2.处理Body和公司现有报文规范一致
		SOAPBody body = msg.getSOAPBody();
		body.setPrefix("soapenv");
		body.removeNamespaceDeclaration("SOAP-ENV");
		SOAPElement response = body.addChildElement(msg.getSOAPPart().getEnvelope().createName("Response"));
		response.setPrefix("ns");
		response.addNamespaceDeclaration("ns", "http://www.cqrcb.com/cps");

		SOAPElement responseBody = response.addChildElement(msg.getSOAPPart().getEnvelope().createName("ResponseBody"));

		for (int i = 0; i < fieldList.length; i++) {
			Name name = msg.getSOAPPart().getEnvelope().createName(fieldList[i]);
			// body.createQName(fieldList[i], values.get(fieldList[i]));
			responseBody.addChildElement(name)
					.addTextNode(values.get(fieldList[i]) == null ? "" : values.get(fieldList[i]));
			name = null;
		}

		// 添加响应信息
		SOAPElement fault = response.addChildElement(msg.getSOAPPart().getEnvelope().createName("Fault"));
		// fault.setPrefix("soapenv");
		// msg.getSOAPPart().getEnvelope().createName(
		fault.addChildElement(msg.getSOAPPart().getEnvelope().createName("FaultCode")).addTextNode(responseCode);
		fault.addChildElement(msg.getSOAPPart().getEnvelope().createName("FaultString")).addTextNode(responseMsg);

		SOAPElement detail = fault.addChildElement(msg.getSOAPPart().getEnvelope().createName("Detail"));

		detail.addChildElement(msg.getSOAPPart().getEnvelope().createName("TxStatus")).addTextNode(txStatus);

		msg.saveChanges();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		msg.writeTo(out);
		msg = null;
		body = null;
		detail = null;
		fault = null;
		responseBody = null;
		response = null;

		return out.toByteArray();// .getBytes(encode);
	}

	public static byte[] assembleFailSOAPMessage(String err_msg, String charset) throws SOAPException, IOException {
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("Tms_serialno", "");
		values.put("Tms_err_code", "FFFF");
		values.put("Tms_err_msg", err_msg);
		String[] rsp_fields = new String[] { "Tms_serialno", "Tms_err_code", "Tms_err_msg" };
		byte[] retVal = assembleSUCCSoapMessage(values, charset, rsp_fields, "FFFFFFF", err_msg, "FAIL");
		values = null;
		rsp_fields = null;
		return retVal;
	}

	public static byte[] assembleRequestSoapMessage(HashMap<String, String> values, String encode, String[] fieldList)
			throws SOAPException, IOException {
		if (null == fieldList || null == values) {
			return null;
		}

		SOAPMessage msg = null;
		MessageFactory mf = MessageFactory.newInstance();
		msg = mf.createMessage();

		// 1.处理Envelop和公司现有报文规范一致
		msg.getSOAPPart().getEnvelope().addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
		msg.getSOAPPart().getEnvelope().addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		msg.getSOAPPart().getEnvelope().setPrefix("soapenv");
		msg.getSOAPPart().getEnvelope().removeNamespaceDeclaration("SOAP-ENV");
		SOAPHeader header = msg.getSOAPHeader();
		header.removeNamespaceDeclaration("SOAP-ENV");
		header.setPrefix("soapenv");

		// 2.处理Body和公司现有报文规范一致
		SOAPBody body = msg.getSOAPBody();
		body.setPrefix("soapenv");
		body.removeNamespaceDeclaration("SOAP-ENV");
		SOAPElement request = body.addBodyElement(msg.getSOAPPart().getEnvelope().createName("Request"));
		request.setAttribute("xmlns", " ");

		SOAPElement requestBody = request.addChildElement(msg.getSOAPPart().getEnvelope().createName("RequestBody"));
		requestBody.setAttribute("xmlns", " ");

		for (int i = 0; i < fieldList.length; i++) {
			Name name = msg.getSOAPPart().getEnvelope().createName(fieldList[i]);
			requestBody.addChildElement(name)
					.addTextNode(values.get(fieldList[i]) == null ? "" : values.get(fieldList[i]));
			name = null;
		}
		/*
		 * //添加响应信息 SOAPElement fault =
		 * response.addChildElement(msg.getSOAPPart().getEnvelope().createName(
		 * "Fault")); //fault.setPrefix("soapenv");
		 * //msg.getSOAPPart().getEnvelope().createName(
		 * fault.addChildElement(msg.getSOAPPart().getEnvelope().createName(
		 * "FaultCode")).addTextNode(responseCode);
		 * fault.addChildElement(msg.getSOAPPart().getEnvelope().createName(
		 * "FaultString")).addTextNode(responseMsg);
		 * 
		 * SOAPElement detail =
		 * fault.addChildElement(msg.getSOAPPart().getEnvelope().createName(
		 * "Detail"));
		 * 
		 * detail.addChildElement(msg.getSOAPPart().getEnvelope().createName(
		 * "TxStatus")).addTextNode(txStatus);
		 * 
		 */

		msg.saveChanges();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		msg.writeTo(out);
		msg = null;
		body = null;
		// detail = null;
		// fault = null;
		requestBody = null;
		request = null;

		return out.toByteArray();// .getBytes(encode);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws SOAPException
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException, SOAPException, IOException {

		HashMap<String, String> values = new HashMap<String, String>();
		values.put("Tms_Trcd", "COMM01");
		values.put("Tms_rec_rule_batch", "12415");
		values.put("Tms_proxy_node_no", "01");
		values.put("Tms_tran_req_time", "20140722101134");
		values.put("Tms_tran_rsp_time", "20140722101139");
		values.put("Tms_old_app", "001350136");
		values.put("Tms_rec_pkg", "<dkadf>kkdaf</dkadf><info>多少</info>");
		values.put("Tms_rsp_pkg", "<resp>success</resp>");

		String[] fields = new String[] { "Tms_Trcd", "Tms_rec_rule_batch", "Tms_proxy_node_no", "Tms_tran_req_time",
				"Tms_tran_rsp_time", "Tms_old_app", "Tms_rec_pkg", "Tms_rsp_pkg" };

		System.out.println(new String(assembleRequestSoapMessage(values, "UTF-8", fields), "UTF-8"));
	}

}
