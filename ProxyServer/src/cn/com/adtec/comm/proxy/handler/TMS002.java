package cn.com.adtec.comm.proxy.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


import javax.xml.soap.SOAPException;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.adtec.comm.proxy.bean.NodeAttibute;
import cn.com.adtec.comm.proxy.bean.SOAPCommObject;
import cn.com.adtec.comm.proxy.message.MessageManage;
import cn.com.adtec.comm.proxy.message.NodeStruct;

import cn.com.adtec.comm.proxy.server.SubService;
import cn.com.adtec.comm.proxy.tools.FileTool;

/**
 * 接收报文采集规则
 * @author tangjb
 *
 */

public class TMS002 {
	private String[][] ruleInfo = null;
	private static final Logger log = LoggerFactory.getLogger(TMS002.class);

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



	public String[][] getRuleInfo() {
		return ruleInfo;
	}

	public void setRuleInfo(String[][] ruleInfo) {
		this.ruleInfo = ruleInfo;
	}

	/**
	 * 接收报文采集规则业务处理主入口
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
		String[] fields = new String[] { "Tms_serialno", "Tms_function",
				"Tms_record_num", "Tms_info_content" };
		try {
			ArrayList<NodeStruct> list = MessageManage.parse("SOAP", receveMsg
					.toString().getBytes(charset), charset, fields);
			if (null != list) {

				// 3.解析报文成功，进行数据校验
				String content = null;         	//信息内容
				String strRecordNum = null;		//记录条数
				String funcFlag=null;			//功能标识
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
					else if (list.get(i).getNodeName().equalsIgnoreCase(
					"Tms_function")) {
						funcFlag = list.get(i).getNodeValue();
						numTmp++;
					}
					if (numTmp == 3) {
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
				
				if (null == funcFlag || "".equals(funcFlag)) {
					list = null;
					content = null;
					strRecordNum = null;
					receveMsg = null;
					fields = null;
					funcFlag = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_function功能标识必须上送!", charset);
				}
				
				
				int iRecordNum = Integer.parseInt(strRecordNum);

				log.info("countent_____["+content+"]");
				String[] records = content.split("@@@");
				int count = 0; // 统计正常解析条数。

				// 4.业务处理
				count = bussiness(records,funcFlag,charset);

				if (count != iRecordNum) {
					//System.out.println("count:["+count+"]iRecordNum["+iRecordNum+"]");
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

	
	/*   backup by TJB 20140723
	public byte[] process(SOAPCommObject nco, IoSession session, String charset,String defNodeNo,AbstractSubServer subServer) {
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

				// 3.解析报文成功，进行数据校验
				String content = null;         	//信息内容
				String strRecordNum = null;		//记录条数
				String nodeNo=null;				//通讯节点编号
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
					else if (list.get(i).getNodeName().equalsIgnoreCase(
					"Tms_proxy_node_no")) {
						nodeNo = list.get(i).getNodeValue();
						numTmp++;
					}
					if (numTmp == 3) {
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
				if (null == nodeNo || "".equals(nodeNo)) {
					list = null;
					content = null;
					strRecordNum = null;
					receveMsg = null;
					fields = null;
					nodeNo = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：Tms_proxy_node_no通讯节点编号字段必须上送!", charset);
				}
				else if (defNodeNo.equalsIgnoreCase(nodeNo.trim()))
				{
					list = null;
					content = null;
					strRecordNum = null;
					receveMsg = null;
					fields = null;
					nodeNo = null;
					return MessageManage.assembleFailSOAPMessage(
							"CPS：通讯节点编号为["+nodeNo+"]的报文录制规则交易，不能发往节点编号为["+defNodeNo+"]的服务器!", charset);
				}
				
				
				
				int iRecordNum = Integer.parseInt(strRecordNum);

				// log.info("countent_____["+content+"]");
				String[] records = content.split("@@@");
				int count = 0; // 统计正常解析条数。

				// 4.业务处理
				count = bussiness(records,defNodeNo,subServer);

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
	*/
	
	private int bussiness(String[] records,String funcFlag,String charset) throws IOException {
		int count = 0; //统计正常的记录数
		if ("A".equalsIgnoreCase(funcFlag))
		{
			//A表示增加或者修改
			//1.拆分规则信息更新内存数据
			Map<String,NodeAttibute> allData = new HashMap<String,NodeAttibute>();		//用于实时更新内存中录制规则
			
			Map<String,NodeAttibute> allSimData = new HashMap<String,NodeAttibute>();	//用于实时更新内存中仿真规则
			
			for (int i=0;i<records.length;i++)
			{
				//System.out.println("___拆分信息前：["+records[i]+"]");
				String[] tt = records[i].split(FileTool.getSplitStr());
				//System.out.println("___拆分信息后数组元素个数：["+tt.length+"]");
				//2.先把上送的代理节点信息追加写入文件
				
				
				if (tt.length>7)
				{
					//判断是录制规则还是仿真规则
					if ("0".equals(tt[16])||"2".equals(tt[16]))
					{
						//录制规则的处理
						//2.1 先查找文件里是否存在该批次号
						//System.out.println("节点【"+tt[2]+"】");
						String[] oldRules = FileTool.readAllRuleFileNotSplit(tt[2].trim(),charset);
						//boolean findIt = false;
						ArrayList<String> restRule = new ArrayList<String>();
						
						//System.out.println("oldRules.length:["+oldRules.length+"]");
						if (null!=oldRules && oldRules.length>0)
						{
							boolean matchFlag = false;
							for (int m=0;m<oldRules.length;m++)
							{
								//System.out.println("文件已有串：["+oldRules[m]+"]");
								String[] tempArray = oldRules[m].split(FileTool.getSplitStr());
								
								if (tempArray[2].trim().equals(tt[2].trim()) && tempArray[6].trim().equals(tt[6].trim()))
								{
									//匹配上了，将替换掉
									restRule.add(records[i]);
									matchFlag = true;
									//System.out.println("匹配上了，将替换掉:["+tt[2].trim()+"]oldRules[m][6].trim()["+tempArray[6].trim()+"]");
								}
								else
								{
									restRule.add(oldRules[m]);
								}
								tempArray = null;
							}//for (int m=0;m<oldRules.length;m++)
							if (matchFlag==false)
							{
								//没匹配上，则把新串加上
								restRule.add(records[i]);
							}
						}//if (null!=oldRules)
						else
						{
							restRule.add(records[i]);
						}
						String[] oldInfo = new String[restRule.size()];
						for (int m=0;m<oldInfo.length;m++)
						{
							oldInfo[m] = restRule.get(m);
							//System.out.println("__"+m+":["+oldInfo[m]+"]");
						}
						
						//System.out.println("写进节点__"+tt[2].trim()+":["+oldInfo.length+"]条");
						FileTool.writeAllRuleFile(tt[2].trim(), oldInfo,false,charset);
						//System.out.println("写文件：["+isSuc+"]");
						oldRules = null;
						oldInfo = null;
						restRule = null;
						
						count++;
						NodeAttibute nodeAtt = null;
						if (allData.containsKey(tt[2].trim()))
						{
							nodeAtt = allData.get(tt[2].trim());
						}
						else
						{
							nodeAtt = new NodeAttibute();
							nodeAtt.setNodeNo(tt[2].trim());
							
							allData.put(tt[2].trim(), nodeAtt);
						}
						
						
						nodeAtt.getRuleInfo().add(tt);
		
						
						String[] keyval = tt[7].split(";");
						HashMap<String, String> keyvalMap= null;
						if (keyval.length>0)
						{
							keyvalMap = new HashMap<String, String> ();
							for (int j=0;j<keyval.length;j++)
							{
								String[] tempkeyval = keyval[j].split("=");
								if (tempkeyval.length>=2)
								{
									keyvalMap.put(tempkeyval[0].trim(), tempkeyval[1].trim());
								}
								tempkeyval = null;
							}
						}
						nodeAtt.getContents().put(tt[6].trim(), keyvalMap);
						
						keyval = null;
						keyvalMap = null;
					}//if ("0".equals(tt[16]))
					else if ("1".equals(tt[16]))
					{
						//仿真规则处理,以“规则批次号+项目编号”唯一
						String[] oldRules = FileTool.readAllRuleFileNotSplit(tt[2].trim()+"_sim",charset);
						//boolean findIt = false;
						ArrayList<String> restRule = new ArrayList<String>();
						
						//System.out.println("oldRules.length:["+oldRules.length+"]");
						if (null!=oldRules && oldRules.length>0)
						{
							boolean matchFlag = false;
							for (int m=0;m<oldRules.length;m++)
							{
								//System.out.println("文件已有串：["+oldRules[m]+"]");
								String[] tempArray = oldRules[m].split(FileTool.getSplitStr());
								
								if (tempArray[2].trim().equals(tt[2].trim()) && tempArray[6].trim().equals(tt[6].trim()) && (!"".equals(tt[16].trim())) && tempArray[16].trim().equals(tt[16].trim()))
								{
									//匹配上了，将替换掉
									restRule.add(records[i]);
									matchFlag = true;
									//System.out.println("匹配上了，将替换掉:["+tt[2].trim()+"]oldRules[m][6].trim()["+tempArray[6].trim()+"]");
								}
								else
								{
									restRule.add(oldRules[m]);
								}
								tempArray = null;
							}//for (int m=0;m<oldRules.length;m++)
							if (matchFlag==false)
							{
								//没匹配上，则把新串加上
								restRule.add(records[i]);
							}
						}//if (null!=oldRules)
						else
						{
							restRule.add(records[i]);
						}
						String[] oldInfo = new String[restRule.size()];
						for (int m=0;m<oldInfo.length;m++)
						{
							oldInfo[m] = restRule.get(m);
							//System.out.println("__"+m+":["+oldInfo[m]+"]");
						}
						
						//System.out.println("写进节点__"+tt[2].trim()+":["+oldInfo.length+"]条");
						FileTool.writeAllRuleFile(tt[2].trim()+"_sim", oldInfo,false,charset);
						//System.out.println("写文件：["+isSuc+"]");
						oldRules = null;
						oldInfo = null;
						restRule = null;
						
						count++;
						NodeAttibute nodeAtt = null;
						StringBuilder keyTmp = new StringBuilder();
						keyTmp.append(tt[2].trim());
						keyTmp.append("_");
						keyTmp.append(tt[6].trim());
					
						if (allSimData.containsKey(keyTmp.toString()))
						{
							nodeAtt = allSimData.get(keyTmp.toString());
						}
						else
						{
							nodeAtt = new NodeAttibute();
							nodeAtt.setNodeNo(keyTmp.toString());
							
							allSimData.put(keyTmp.toString(), nodeAtt);
						}
						
						
						nodeAtt.getRuleInfo().add(tt);
		
						
						String[] keyval = tt[7].split(";");
						HashMap<String, String> keyvalMap= null;
						if (keyval.length>0)
						{
							keyvalMap = new HashMap<String, String> ();
							for (int j=0;j<keyval.length;j++)
							{
								String[] tempkeyval = keyval[j].split("=");
								if (tempkeyval.length>=2)
								{
									keyvalMap.put(tempkeyval[0].trim(), tempkeyval[1].trim());
								}
								tempkeyval = null;
							}
						}
						keyTmp.delete(0, keyTmp.length());
						keyTmp.append(tt[2]);
						keyTmp.append("_");
						keyTmp.append(tt[6]);
						keyTmp.append("_");
						keyTmp.append(tt[16]);
						nodeAtt.getContents().put(keyTmp.toString(), keyvalMap);
						
						keyval = null;
						keyvalMap = null;
						keyTmp = null;
					}
					else
					{
						log.error("TMS发送的规则【"+records[i]+"】不符合接口要求！规则类型["+tt[16]+"]不符");
					}
					
					
					
				}//if (tt.length>7)
				else
				{
					log.error("TMS发送的规则【"+records[i]+"】不符合接口要求！");
				}
				tt = null;
			}//for (int i=0;i<records.length;i++)
			//System.out.println("allData.size()["+allData.size()+"]");
			//3.实时更新录制规则到内存
			
			if (allData.size()>0)
			{
				SubService subservice = SubService.getInstance();
				//HashMap<String,AbstractSubServer> serverMap = subservice.getStartedNodeMap();
				if (null!=subservice.getStartedNodeMap())
				{
					Set<String> nodes = allData.keySet();
					for (String nodeNo:nodes)
					{
						//System.out.println("节点["+nodeNo+"]");
						if (subservice.getStartedNodeMap().containsKey(nodeNo))
						{
							//先把原有内容复制出来
							//System.out.println("存在此节点,先把原有内容复制出来,节点["+nodeNo+"]");
							String[][] oldInfo = subservice.getStartedNodeMap().get(nodeNo).getPkgCollectRules();
							if (null!=oldInfo)
							{
								for (int m=0;m<oldInfo.length;m++)
								{
									allData.get(nodeNo).getRuleInfo().add(oldInfo[m]);
								}
							}
							ConcurrentHashMap<String, HashMap<String, String>> oldContent = subservice.getStartedNodeMap().get(nodeNo).getKeyWordVal();
							
							Set<String> oldkeys = oldContent.keySet();
							for (String tmp:oldkeys)
							{
								allData.get(nodeNo).getContents().put(tmp, oldContent.get(tmp));
							}
							String[][] infos = new String[allData.get(nodeNo).getRuleInfo().size()][];
							for (int j=0;j<infos.length;j++)
							{
								infos[j] = allData.get(nodeNo).getRuleInfo().get(j);
							}
							
							//再一起把迭加的内容赋回去
							//System.out.println("再一起把迭加的内容赋回去,规则条数["+infos.length+"]");
							subservice.getStartedNodeMap().get(nodeNo).setPkgCollectRules(infos);
							subservice.getStartedNodeMap().get(nodeNo).setKeyWordVal(allData.get(nodeNo).getContents());
							
							oldInfo = null;
							oldContent = null;
							oldkeys = null;
							infos = null;
						}//if (serverMap.containsKey(nodeNo))
					}//for (String nodeNo:nodes)
				}//if (null!=serverMap)
				//serverMap = null;
				subservice = null;
			}//if (allData.size()>0)
			//4.实时更新仿真规则到内存
			if (allSimData.size()>0)
			{
				SubService subservice = SubService.getInstance();
				//HashMap<String,AbstractSubServer> serverMap = subservice.getStartedNodeMap();
				if (null!=subservice.getStartedNodeMap())
				{
					Set<String> nodes = allSimData.keySet();
					for (String key:nodes)
					{
						//key为"节点编号_规则批次号";
						String nodeNo = key.split("_")[0];   //节点编号
						if (subservice.getStartedNodeMap().containsKey(nodeNo))
						{
							//先把原有内容复制出来
							//System.out.println("存在此节点,先把原有内容复制出来,节点["+nodeNo+"]");
							String[][] oldInfo = subservice.getStartedNodeMap().get(nodeNo).getPkgSimRules();
							if (null!=oldInfo)
							{
								for (int m=0;m<oldInfo.length;m++)
								{
									allSimData.get(key).getRuleInfo().add(oldInfo[m]);
								}
							}
							ConcurrentHashMap<String, HashMap<String, String>> oldContent = subservice.getStartedNodeMap().get(nodeNo).getKeyWordValSim();
							
							Set<String> oldkeys = oldContent.keySet();
							for (String tmp:oldkeys)
							{
								allSimData.get(key).getContents().put(tmp, oldContent.get(tmp));
							}
							String[][] infos = new String[allSimData.get(key).getRuleInfo().size()][];
							for (int j=0;j<infos.length;j++)
							{
								infos[j] = allSimData.get(key).getRuleInfo().get(j);
							}
							
							//再一起把迭加的内容赋回去
							//System.out.println("再一起把迭加的内容赋回去,规则条数["+infos.length+"]");
							subservice.getStartedNodeMap().get(nodeNo).setPkgSimRules(infos);
							subservice.getStartedNodeMap().get(nodeNo).setKeyWordValSim(allSimData.get(key).getContents());
							
							oldInfo = null;
							oldContent = null;
							oldkeys = null;
							infos = null;
						}//if (serverMap.containsKey(nodeNo))
						nodeNo = null;
					}//for (String key:nodes)
				}//if (null!=serverMap)
				//serverMap = null;
				subservice = null;
			}//if (allData.size()>0)
		}//if ("A".equalsIgnoreCase(funcFlag))
		else
		{
			//删除
			SubService subservice = SubService.getInstance();
			//HashMap<String,AbstractSubServer> serverMap = subservice.getStartedNodeMap();
			if (null!=subservice.getStartedNodeMap())
			{
				//System.out.println("records.length:["+records.length+"]");
				for (int i=0;i<records.length;i++)
				{
					String[] tt = records[i].split(FileTool.getSplitStr());
					count++;
					if (subservice.getStartedNodeMap().containsKey(tt[2].trim()))
					{
						if ("0".equals(tt[16].trim()))
						{
							//移除录制规则
							String[][] infos = subservice.getStartedNodeMap().get(tt[2].trim()).getPkgCollectRules();
							ArrayList<String[]> restInfor = new ArrayList<String[]>();
							for (int k=0;k<infos.length;k++)
							{
								if (infos[k][6].equals(tt[6].trim()))
								{
									//匹配上要删除的
								}
								else
								{
									restInfor.add(infos[k]);
								}
							}
							infos = new String[restInfor.size()][];
							for (int k=0;k<infos.length;k++)
							{
								infos[k] = restInfor.get(k);
							}
							subservice.getStartedNodeMap().get(tt[2].trim()).setPkgCollectRules(infos);
							
							infos = null;
							restInfor = null;
							
							//移除关键字数组
							subservice.getStartedNodeMap().get(tt[2].trim()).getKeyWordVal().remove(tt[6].trim());
						}//if ("0".equals(tt[16].trim()))
						else if ("1".equals(tt[16].trim()))
						{
							//移除仿真规则
							String[][] infos = subservice.getStartedNodeMap().get(tt[2].trim()).getPkgSimRules();
							ArrayList<String[]> restInfor = new ArrayList<String[]>();
							for (int k=0;k<infos.length;k++)
							{
								if (infos[k][6].equals(tt[6].trim()) && infos[k][16].equals(tt[16].trim()))
								{
									//匹配上要删除的
								}
								else
								{
									restInfor.add(infos[k]);
								}
							}
							infos = new String[restInfor.size()][];
							for (int k=0;k<infos.length;k++)
							{
								infos[k] = restInfor.get(k);
							}
							subservice.getStartedNodeMap().get(tt[2].trim()).setPkgSimRules(infos);
							
							infos = null;
							restInfor = null;
							
							//移除仿真关键字数组
							StringBuilder keyTmp = new StringBuilder();
							keyTmp.append(tt[2].trim());
							keyTmp.append("_");
							keyTmp.append(tt[6].trim());
							keyTmp.append("_");
							keyTmp.append(tt[16].trim());
							subservice.getStartedNodeMap().get(tt[2].trim()).getKeyWordValSim().remove(keyTmp.toString());
							keyTmp = null;
						}
							
						
						
					}//if (serverMap.containsKey(tt[2].trim()))
					
					//删除文件里的内容：
					if ("0".equals(tt[16].trim()))
					{
						//如果是录制规则
						String[] oldRules = FileTool.readAllRuleFileNotSplit(tt[2].trim(),charset);
						ArrayList<String> restRule = new ArrayList<String>();
						
						//System.out.println("oldRules.length:["+oldRules.length+"]");
						for (int m=0;m<oldRules.length;m++)
						{
							
							String[] tempArray = oldRules[m].split(FileTool.getSplitStr());
							
							if (tempArray[2].trim().equals(tt[2].trim()) && tempArray[6].trim().equals(tt[6].trim()))
							{
								//匹配上了，将删除掉
								//System.out.println("匹配上了:["+tt[2].trim()+"]oldRules[m][6].trim()["+oldRules[m][6].trim()+"]");
							}
							else
							{
								restRule.add(oldRules[m]);
							}
							tempArray = null;
						}
						String[] oldInfo = new String[restRule.size()];
						for (int m=0;m<oldInfo.length;m++)
						{
							oldInfo[m] = restRule.get(m);
						}
						FileTool.writeAllRuleFile(tt[2].trim(), oldInfo,false,charset);
						oldRules = null;
						restRule = null;
						
						oldInfo = null;
						
						tt = null;
					}
					else if ("1".equals(tt[16].trim()))
					{
						//如果是仿真规则
						StringBuilder fileName = new StringBuilder();
						fileName.append(tt[2].trim());
						fileName.append("_sim");

						String[] oldRules = FileTool.readAllRuleFileNotSplit(fileName.toString(),charset);
						ArrayList<String> restRule = new ArrayList<String>();
						
						//System.out.println("oldRules.length:["+oldRules.length+"]");
						for (int m=0;m<oldRules.length;m++)
						{
							
							String[] tempArray = oldRules[m].split(FileTool.getSplitStr());
							
							if (tempArray[2].trim().equals(tt[2].trim()) && tempArray[6].trim().equals(tt[6].trim()) && tempArray[16].trim().equals(tt[16].trim()))
							{
								//匹配上了，将删除掉
								//System.out.println("匹配上了:["+tt[2].trim()+"]oldRules[m][6].trim()["+oldRules[m][6].trim()+"]");
							}
							else
							{
								restRule.add(oldRules[m]);
							}
							tempArray = null;
						}
						String[] oldInfo = new String[restRule.size()];
						for (int m=0;m<oldInfo.length;m++)
						{
							oldInfo[m] = restRule.get(m);
						}
						FileTool.writeAllRuleFile(fileName.toString(), oldInfo,false,charset);
						oldRules = null;
						restRule = null;
						fileName = null;
						oldInfo = null;
						
						tt = null;
					}
					
				}//for (int i=0;i<records.length;i++)
			}//if (null!=serverMap)
			//serverMap = null;
			subservice = null;
		}
		
		return count;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
