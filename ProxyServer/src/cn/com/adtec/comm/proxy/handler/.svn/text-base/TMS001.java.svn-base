package cn.com.adtec.comm.proxy.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.xml.soap.SOAPException;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.adtec.comm.proxy.bean.SOAPCommObject;
import cn.com.adtec.comm.proxy.message.MessageManage;
import cn.com.adtec.comm.proxy.message.NodeStruct;
import cn.com.adtec.comm.proxy.server.SubService;
import cn.com.adtec.comm.proxy.tools.FileTool;

/**
 * 通讯节点服务启停
 * @author tangjb
 *
 */
public class TMS001 {
	
	private static final Logger logger = LoggerFactory.getLogger(TMS001.class);
	
	private ConcurrentHashMap<String, String> proxyNodeMap = new ConcurrentHashMap<String, String>();
	// 定义存放“已经启动通讯代理节点”的set,
	private ConcurrentSkipListSet<String> startedNodeSet = new ConcurrentSkipListSet<String>();
	
	private ConcurrentSkipListSet<String> needStopNodeSet = new ConcurrentSkipListSet<String>();
	public ConcurrentSkipListSet<String> getNeedStopNodeSet() {
		return needStopNodeSet;
	}

	public void setNeedStopNodeSet(ConcurrentSkipListSet<String> needStopNodeSet) {
		this.needStopNodeSet = needStopNodeSet;
	}
	
	

	// 定义存放"节点报文采集规则"的map,其中，“节点编号”为KEY 值
	// private HashMap<String,String[]> pkgFilterRuleMap = new
	// HashMap<String,String[]>();
	private IoSession session = null;

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public ConcurrentHashMap<String, String> getProxyNodeMap() {
		return proxyNodeMap;
	}

	public void setProxyNodeMap(ConcurrentHashMap<String, String> proxyNodeMap) {
		this.proxyNodeMap = proxyNodeMap;
	}

	public ConcurrentSkipListSet<String> getStartedNodeSet() {
		return startedNodeSet;
	}

	public void setStartedNodeSet(ConcurrentSkipListSet<String> startedNodeSet) {
		this.startedNodeSet = startedNodeSet;
	}

	/**
	 * 通讯节点服务启停业务处理主入口
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
		String[] fields = new String[] { "Tms_serialno", "Tms_proxy_node_no",
				"Tms_record_num", "Tms_info_content" };
		try {
			ArrayList<NodeStruct> list = MessageManage.parse("SOAP", receveMsg
					.toString().getBytes(charset), charset, fields);
			if (null != list) {
				/*
				 * for (NodeStruct ns:list) {
				 * log.info("name:【"+ns.getNodeName()+
				 * "】value:【"+ns.getNodeValue(
				 * )+"】___level:["+ns.getNodeLevel()+"]___parentName:["
				 * +ns.getParentNodeName()+"]"); }
				 */
				// 3.解析报文成功，进行数据校验
				String content = null;
				String strRecordNum = null;

				int numTmp = 0;
				for (int i = 0; i < list.size(); i++) {

					if (list.get(i).getNodeName().equalsIgnoreCase(
							"Tms_info_content")) {
						content = list.get(i).getNodeValue();
						numTmp++;
					} else if (list.get(i).getNodeName().equalsIgnoreCase(
							"Tms_record_num")) {
						strRecordNum = list.get(i).getNodeValue();
						numTmp++;
					}
					if (numTmp == 2) {
						break;
					}
				}// for (int i=0;i<list.size();i++)
				// 检查必需的字段
				if (null == content || "".equals(content)) {
					list = null;
					content = null;
					strRecordNum = null;
					receveMsg = null;
					fields = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_info_content字段内容必须上送!", charset);
				}
				if (null == strRecordNum || "".equals(strRecordNum)) {
					list = null;
					content = null;
					strRecordNum = null;
					receveMsg = null;
					fields = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_record_num记录条数字段必须上送!", charset);
				}
				int iRecordNum = Integer.parseInt(strRecordNum);

				// log.info("countent_____["+content+"]");
				String[] records = content.split("@@@");
				int count = 0; // 统计正常解析条数。

				// 4.业务处理
				count = bussiness(records);

				if (count != iRecordNum) {
					// log.info("count:["+count+"]iRecordNum["+iRecordNum+"]");
					list = null;
					content = null;
					strRecordNum = null;
					records = null;
					receveMsg = null;
					fields = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_record_num记录条数与实际上送内容的条数不一致!", charset);
				}

				// 5.返回成功报文
				HashMap<String, String> values = new HashMap<String, String>();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getNodeName().equalsIgnoreCase(
							"Tms_serialno")) {
						values.put("Tms_serialno", list.get(i).getNodeValue());
						break;
					}
				}

				values.put("Tms_err_code", "SUCCESS");
				values.put("Tms_err_msg", "成功");
				// values.put("Tms_info_content", "kd|@|1515,|@|看来");

				String[] rsp_fields = new String[] { "Tms_serialno",
						"Tms_err_code", "Tms_err_msg" };

				list = null;
				content = null;
				strRecordNum = null;
				records = null;
				receveMsg = null;
				fields = null;

				return MessageManage.assembleSUCCSoapMessage(values, charset, rsp_fields,
						"AAAAAAA", "成功", "SUCCESS");
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
		// 2.
		// 定义存放通讯代理节点的map,其中，“节点编号”为KEY 值

		receveMsg = null;

		return null;
	}

	private int bussiness(String[] records) throws IOException {
		
		//1.先把上送的代理节点信息写入文件
		this.setProxyNodeMap(FileTool.readProxyNodeFile());
		int count = 0;
		for (int i = 0; i < records.length; i++) {
			String[] items = records[i].split(FileTool.getSplitStr());
			if (items.length >= 13) {
				// 正常内容，计数加一
				count++;

				this.getProxyNodeMap().put(items[0], records[i]); // 全部以刚上送的内容为主

			}
			items = null;
		}
		if (count > 0) {
			FileTool.writeProxyNodeFile(this.getProxyNodeMap());
		}
		
		//2.检查有多少节点需要停止的,先读取已经启动的节点记录
		this.setStartedNodeSet(FileTool.readStartedProxyNodeFile());
		for (Iterator<String> ite=this.getStartedNodeSet().iterator();ite.hasNext();)
		{
			String tmp = ite.next();
			if (this.getProxyNodeMap().containsKey(tmp))
			{
				String[] items = this.getProxyNodeMap().get(tmp).split(FileTool.getSplitStr());
				if (items[7].trim().equals("1"))
				{
					//现在也要求启动，不管它
					//logger.info("现在也要求启动，不管它");
				}
				else
				{
					//登记需要停止的节点
					logger.info("登记需要停止的节点");
					this.getNeedStopNodeSet().add(tmp);
					if (this.getStartedNodeSet().contains(tmp))
					{
						this.getStartedNodeSet().remove(tmp);
					}
				}
				items = null;
			}
			tmp = null;
		}
		
		//3.重新检查需要启动的节点
		Set<String> keys = (Set<String>) this.getProxyNodeMap().keySet();
		for (Iterator<String> ite = keys.iterator();ite.hasNext();)
		{
			String tmp = ite.next();
			//logger.info("重新检查需要启动的节点：["+this.getProxyNodeMap().get(tmp).split(FileTool.getSplitStr())[7]+"]");
			if (this.getProxyNodeMap().get(tmp).split(FileTool.getSplitStr())[7].trim().equals("1"))
			{
				logger.info("需要启动["+tmp+"]");
				this.getStartedNodeSet().add(tmp);
			}
			tmp = null;
		}
		
		keys = null;
		
		//4.将需要启动的节点写入文件
		FileTool.writeStartedNodeFile(this.getStartedNodeSet());
		
		//5.同时启动服务或者停掉服务 ,留待子通讯服务类开发完毕后加上。20140716
		SubService subservice = SubService.getInstance();
		if (SubService.isStartedFlag())
		{
			//已经启动
			logger.info("子服务已经启动");
			subservice.setProxyNodeMap(this.getProxyNodeMap());
			subservice.setNeedToShut(this.getNeedStopNodeSet());
			subservice.setNeedToStart(this.getStartedNodeSet());
			//logger.info("需要停止集合元素个数："+this.getNeedStopNodeSet().size());
/*			if (!subservice.loadFileInfo())
			{
				logger.error("booting sub services,load file fail!....");
			}
			*/
			Set<String> keyTests = subservice.getProxyNodeMap().keySet();
			for (String kt:keyTests)
			{
				if (subservice.getStartedNodeMap().containsKey(kt))
				{
					//在线更新规则
					subservice.getStartedNodeMap().get(kt).refreshSubServer(subservice.getProxyNodeMap().get(kt));
				}
				//logger.info(".......["+kt+"]:["+subservice.getProxyNodeMap().get(kt)+"]");
			}
			
			if (this.getStartedNodeSet().size()>0)
			{
				if (!subservice.start())
				{
					logger.error("booting sub services,start sub server fail!....");
				}
			}
			if (this.getNeedStopNodeSet().size()>0)
			{
				if (!subservice.shut())
				{
					logger.error("shuting sub services,shut sub server fail!....");
				}
			}
		}
		else
		{
			logger.info("子服务未启动");
			if (!subservice.loadFileInfo())
			{
				logger.error("booting sub services,load file fail!....");
			}
			if (!subservice.start())
			{
				logger.error("booting sub services,start sub server fail!....");
			}
		}
		//this.getStartedNodeSet()
		//this.getNeedStopNodeSet()
		
		return count;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
