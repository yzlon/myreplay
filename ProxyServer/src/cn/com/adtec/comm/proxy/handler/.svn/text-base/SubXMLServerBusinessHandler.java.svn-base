package cn.com.adtec.comm.proxy.handler;


import java.util.ArrayList;
import java.util.HashMap;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;



import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cn.com.adtec.comm.proxy.bean.CommObject;

import cn.com.adtec.comm.proxy.client.SocketClient;
import cn.com.adtec.comm.proxy.message.MessageManage;
import cn.com.adtec.comm.proxy.message.NodeStruct;

import cn.com.adtec.comm.proxy.server.AbstractSubServer;
import cn.com.adtec.comm.proxy.tools.SystemTime;
import cn.com.adtec.comm.proxy.tools.TMSThreadPool;
import cn.com.adtec.comm.proxy.tools.XMLDeal;




/**
 * 子服务端业务处理总控
 * 
 * @author 汤继波
 *
 */
public class SubXMLServerBusinessHandler extends IoHandlerAdapter {
	private final static Logger log = LoggerFactory	.getLogger(SubXMLServerBusinessHandler.class);
	private AbstractSubServer subServer;
	public SubXMLServerBusinessHandler(AbstractSubServer sss)
	{
		subServer = sss;
	}


	
	/**
	 * 接收协议报文内容，进行处理，并返回相应协议报文
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// TODO Auto-generated method stub
		//super.messageReceived(session, message);
		//此处的message是与客户端事先约定的处理对象
		
		log.info("Sub XML Server ,Begin to process business (XML)......................");
		CommObject co = (CommObject)message;
		
		
		SystemTime beginST = new SystemTime();
		String beginTime = beginST.getDateStr()+beginST.getRigorTime();

		log.info("__XML请求,客户端【"+session.getRemoteAddress().toString()+"】报文头【"+co.getHead()+"】报文体【"+co.getBody()+"】");
		//根据不同的服务码进行处理
		byte[] respTmp=null;//存放返回信息
		
		//用于异步保存录制报文
		String recNodeNo = null;			//节点编号
		String recRuleNo = null;			//录制规则编号
		String endTime = null;				//交易结束时间
		boolean recFlag = false;			//录制标志
		String filesvrTestEnv = null;		//文件服务器所在环境

		//判断 模式是“直连”还是“仿真”   0-直联，1-仿真
		if (subServer.getNodeMode().equals("0"))
		{
			//直联模式，不需要判断规则，直接发往下游系统
			//log.error("do not need determine rule["+subServer.getNodeMode()+"]");
			respTmp = new SocketClient(subServer.getNextSystemIP(),Integer.parseInt(subServer.getNextSystemPort()),Integer.parseInt(subServer.getNextSystemTimeOut())).sendOut(co.getHead(), co.getBody(), subServer.getCharset());
				//Tools.sendOutToSOAP("http",subServer.getNextSystemIP(),subServer.getNextSystemPort(),"/"+appName,subServer.getNextSystemTimeOut(),null,nco.getBody(),subServer.getCharset());
		}
		else if (subServer.getNodeMode().equals("1"))
		{
			//仿真模式，需要判断规则，该交易是走仿真还是走直连
			//log.error("need determine rule["+subServer.getNodeMode()+"]");
			String[][] pcrTemp = subServer.getPkgCollectRules();
			String[][] simRules = subServer.getPkgSimRules();
			boolean findIt = false;
			boolean matched = false;
			boolean simMatched = false;
			boolean runNextSystem = true;//默认发往下游系统，如果中间有仿真，则置此值为false;
			//log.info("pcrTemp.length["+pcrTemp.length+"]");
			if (null!=pcrTemp)
			{
				for (int i =0;i<pcrTemp.length;i++)
				{
					//遍历每条规则，只要匹配上一条则跳出直接处理
					/*
					 * 是否发往仿真（直连仿真）(0-直联，1-仿真)开关标志|@|是否录制报文开关标志|@|节点编号|@|节点名称|@|原始交易报文种类|@|分隔符|@|录制规则批次号|@|关键字=关键值;关键字1=关键值1(或者“[0,5]=关键字2;[7]=关键字3”) |@|是否有返回文件标志|@|文件服务器地址|@|文件服务器端口|@|文件传输协议|@|文件名存放位置|@|文件传输用户名|@|文件传输密码
					 */
					HashMap<String,String> keyWordMap = null;
					findIt = false;
					matched = false;
					simMatched = false;
					
					
					if (subServer.getKeyWordVal().containsKey(pcrTemp[i][6]))
					{
						keyWordMap = subServer.getKeyWordVal().get(pcrTemp[i][6]);
						findIt = true;
						//业务处理,匹配报文
						
						matched=filterXML(co.getBody(),keyWordMap,subServer.getCharset());
							//filterFLOW(co.getHead(),co.getBody(),keyWordMap,subServer.getCharset(),"0");
							
						
						if (matched==true)
						{
							
							//log.info("规则找到,判断交易走向"+pcrTemp[i][0]);
							
							
							//判断交易走向，(0-直连,1-用例级仿真，2-交易级仿真
							if ("0".equals(pcrTemp[i][0]))
							{
								//走直连,检查是否需要录制报文
								//如果是录制，走直连
								//没找着合适的仿真规则，走直连
								
								//调用异步线程，将报文发送至TMS保存
								if (pcrTemp[i][1].equals("1"))
								{
									//需要保存报文
									//Tools.sendOutToSOAP("http",subServer.getTmsIP(),subServer.getTmsPort(),"/"+subServer.getTmsPkgSendBackApp(),"120",null,nco.getBody(),respTmp,subServer.getCharset());
									//String reqBody,String resBody,String oldApp,AbstractSubServer subsv,String nodeNo,String recBatch,String beginTime,String endTime
									recFlag = true;
									recNodeNo = pcrTemp[i][2];
									recRuleNo = pcrTemp[i][6];
									if (pcrTemp[i].length>=16)
									{
										filesvrTestEnv = pcrTemp[i][15];
									}
								}
								
								
								break;
							}
							else if ("2".equals(pcrTemp[i][0]))
							{
								//交易级仿真
								runNextSystem = false;
								if (pcrTemp[i].length>=19)
								{
									respTmp = new COMM11().process(co.getBody(), session, null, subServer, pcrTemp[i][2], pcrTemp[i][6], pcrTemp[i][17],"tran", pcrTemp[i][18]);
								}
								else
								{
									respTmp = new COMM11().process(co.getBody(), session, null, subServer, pcrTemp[i][2], pcrTemp[i][6], null,"tran",null);
								}
								break;
							}
							else
							{
								//走用例级仿真
	
								//匹配到录制规则，但根据标志，再匹配仿真规则，add by TJB 20141128
								//需要嵌套循环
								StringBuilder keyTmp = new StringBuilder();
								
								int simLoc = findMaxMatchedRule(pcrTemp[i][2],pcrTemp[i][6],simRules,co.getBody(),subServer.getCharset(),subServer.getKeyWordValSim(),keyTmp);
								if (simLoc<0)
								{
									//没找着合适的仿真规则，继续找下一条录制规则
									
								}
								else
								{
									//找着最全匹配的仿真规则了
									simMatched = true;
									if (simRules[simLoc][0].equals("1"))
									{
										//走用例级仿真
										runNextSystem = false;
										if (simRules[simLoc].length>=19)
										{
											respTmp = new COMM11().process(co.getBody(), session, null, subServer, simRules[simLoc][2], simRules[simLoc][6], simRules[simLoc][17],"case", simRules[simLoc][18]);
										}
										else
										{
											respTmp = new COMM11().process(co.getBody(), session, null, subServer, simRules[simLoc][2], simRules[simLoc][6], null,"case",null);
										}
										break;
									}
									else if (simRules[simLoc][0].equals("2"))
									{
										//走交易级仿真
										runNextSystem = false;
										if (simRules[simLoc].length>=19)
										{
											respTmp = new COMM11().process(co.getBody(), session, null, subServer, simRules[simLoc][2], simRules[simLoc][6], simRules[simLoc][17],"tran", simRules[simLoc][18]);
										}
										else
										{
											respTmp = new COMM11().process(co.getBody(), session, null, subServer, simRules[simLoc][2], simRules[simLoc][6], null,"tran",null);
										}
										break;
									}
									else if (simRules[simLoc][0].equals("0"))
									{
										//直接 发往下游系统
										
										//调用异步线程，将报文发送至TMS保存
										if (simRules[simLoc][1].equals("1"))
										{
											//需要保存报文
											//Tools.sendOutToSOAP("http",subServer.getTmsIP(),subServer.getTmsPort(),"/"+subServer.getTmsPkgSendBackApp(),"120",null,nco.getBody(),respTmp,subServer.getCharset());
											//String reqBody,String resBody,String oldApp,AbstractSubServer subsv,String nodeNo,String recBatch,String beginTime,String endTime
											//不录制，20141106
											/*
											recFlag = true;
											recNodeNo = simRules[simLoc][2];
											recRuleNo = simRules[simLoc][6];
											if (simRules[simLoc].length>=16)
											{
												filesvrTestEnv = simRules[simLoc][15];
											}
											*/
										}
										break;
									}
									else
									{
										//错误
										respTmp = MessageManage.assembleFailSOAPMessage("不支持的模式["+simRules[simLoc][0]+"]", subServer.getCharset());
									}
									keyWordMap = null;
									keyTmp = null;
									break;
								}
								
								keyWordMap = null;
								keyTmp = null;
							}
						}//if (matched==true)
						else
						{
							//未匹配到录制规则，再匹配仿真规则，add by TJB 20141030
							//需要嵌套循环
							StringBuilder keyTmp = new StringBuilder();
							
							int simLoc = findMaxMatchedRule(pcrTemp[i][2],pcrTemp[i][6],simRules,co.getBody(),subServer.getCharset(),subServer.getKeyWordValSim(),keyTmp);
							if (simLoc<0)
							{
								//没找着合适的仿真规则，继续找下一条录制规则
								
							}
							else
							{
								//找着最全匹配的仿真规则了
								simMatched = true;
								if (simRules[simLoc][0].equals("1"))
								{
									//走用例级仿真
									runNextSystem = false;
									if (simRules[simLoc].length>=19)
									{
										respTmp = new COMM11().process(co.getBody(), session, null, subServer, simRules[simLoc][2], simRules[simLoc][6], simRules[simLoc][17],"case", simRules[simLoc][18]);
									}
									else
									{
										respTmp = new COMM11().process(co.getBody(), session, null, subServer, simRules[simLoc][2], simRules[simLoc][6], null,"case",null);
									}
									break;
								}
								else if (simRules[simLoc][0].equals("2"))
								{
									//走交易级仿真
									runNextSystem = false;
									if (simRules[simLoc].length>=19)
									{
										respTmp = new COMM11().process(co.getBody(), session, null, subServer, simRules[simLoc][2], simRules[simLoc][6], simRules[simLoc][17],"tran", simRules[simLoc][18]);
									}
									else
									{
										respTmp = new COMM11().process(co.getBody(), session, null, subServer, simRules[simLoc][2], simRules[simLoc][6], null,"tran",null);
									}
									break;
								}
								else if (simRules[simLoc][0].equals("0"))
								{
									//直接 发往下游系统
									
									//调用异步线程，将报文发送至TMS保存
									if (simRules[simLoc][1].equals("1"))
									{
										//需要保存报文
										//Tools.sendOutToSOAP("http",subServer.getTmsIP(),subServer.getTmsPort(),"/"+subServer.getTmsPkgSendBackApp(),"120",null,nco.getBody(),respTmp,subServer.getCharset());
										//String reqBody,String resBody,String oldApp,AbstractSubServer subsv,String nodeNo,String recBatch,String beginTime,String endTime
										//不录制，20141106
										/*
										recFlag = true;
										recNodeNo = simRules[simLoc][2];
										recRuleNo = simRules[simLoc][6];
										if (simRules[simLoc].length>=16)
										{
											filesvrTestEnv = simRules[simLoc][15];
										}
										*/
									}
									break;
								}
								else
								{
									//错误
									respTmp = MessageManage.assembleFailSOAPMessage("不支持的模式["+simRules[simLoc][0]+"]", subServer.getCharset());
								}
								keyWordMap = null;
								keyTmp = null;
								break;
							}
							
							keyWordMap = null;
							keyTmp = null;
							//break;
						}//if (matched==true) else
						keyWordMap = null;
					}//if (subServer.getKeyWordVal().containsKey(pcrTemp[i][2])) findit!
				}//for (int i =0;i<pcrTemp.length;i++)
			}//if (null!=pcrTemp)
			
			//如果没找到，或者没有仿真规则默认还是发往下游系统
			if (runNextSystem ==true)
			{
				//log.info("没找到规则，默认发往下游系统！");
				respTmp = new SocketClient(subServer.getNextSystemIP(),Integer.parseInt(subServer.getNextSystemPort()),Integer.parseInt(subServer.getNextSystemTimeOut())).sendOut(co.getHead(), co.getBody(), subServer.getCharset());
				SystemTime endST = new SystemTime();
				endTime = endST.getDateStr()+endST.getRigorTime();
				endST = null;
			}
			
			pcrTemp = null;
			simRules = null;
		}
		else
		{
			log.error("not supported mode["+subServer.getNodeMode()+"]");
		}
			
			//respTmp = sendOut("http","esb.dev.cqrcb.com","10040","/"+appName,"120",null,nco.getBody(),subServer.getCharset());

		


		StringBuilder sb = new StringBuilder();
		
		//sb.append(new String(respTmp,subServer.getCharset()));
		
		//resp_msg = null;
		//sb.append();
		log.info("__XML响应报文【"+new String(respTmp,subServer.getCharset())+"】");
		session.write(new String(respTmp,subServer.getCharset()));
		if ("1".equals(subServer.getLinkMode()))
		{
			//短连接
			session.close(true);
		}
		if (recFlag==true)
		{
			sb.delete(0, sb.length());
			//sb.append(co.getHead());
			sb.append(co.getBody());
			
			//20150408,add by 汤,将长度放在响应头位置
			//log.info("respTmp.length["+respTmp.length+"]");
			byte[] respHead = new byte[subServer.getPkgHeadLen()];
			//log.info("respHead.length["+respHead.length+"]");
			byte[] respContent = new byte[respTmp.length-subServer.getPkgHeadLen()];
			//log.info("respContent.length["+respContent.length+"]");
			for (int i=0;i<respHead.length;i++)
			{
				respHead[i]=respTmp[i];
			}
			for (int i=subServer.getPkgHeadLen();i<respTmp.length;i++)
			{
				respContent[i-subServer.getPkgHeadLen()]=respTmp[i];
			}
			TMSThreadPool.addNewTask(
			new COMM01(sb.toString(),new String(respContent,subServer.getCharset()),"",subServer,recNodeNo,recRuleNo,beginTime,endTime,filesvrTestEnv,co.getHead(),new String(respHead,subServer.getCharset())));
			//.start();
			respHead = null;
			respContent = null;
		}
		recNodeNo = null;			//节点编号
		recRuleNo = null;			//录制规则编号
		endTime = null;				//交易结束时间
		respTmp=null;
		co = null;
		sb = null;
		beginST = null;
		beginTime = null;
		
		sb = null;
		
		
/*		ca = null;
		sdf = null;*/
	}

	private int findMaxMatchedRule(String recNodeNo, String recRuleBatchNo,
			String[][] simRules, String bodyPkg ,String charset, ConcurrentHashMap<String, HashMap<String, String>> simKeyVal, StringBuilder keyTmp) throws Exception {
		int loc = -1;				//用来记录关键字完全匹配的simRules数组的一维下标
		int maxNodeCount = 0;		//用来记录关键字完全匹配的最大的关键字个数
		ArrayList<NodeStruct> list = XMLDeal.parseXMLfromStr(bodyPkg);
		if (null != list && null != simRules && null!= simKeyVal ) {
			
			for (int m=0;m<simRules.length;m++)
			{
				if (simRules[m].length>=18 &&simRules[m][2].equals(recNodeNo) && simRules[m][6].equals(recRuleBatchNo))
				{
					//组关键字KEY
					keyTmp.delete(0, keyTmp.length());
					keyTmp.append(simRules[m][2]);
					keyTmp.append("_");
					keyTmp.append(simRules[m][6]);
					keyTmp.append("_");
					keyTmp.append(simRules[m][17]);
					if (simKeyVal.containsKey(keyTmp.toString()))
					{
						HashMap<String, String> keyWordMap = simKeyVal.get(keyTmp.toString());
						Set<String> keys = keyWordMap.keySet();
						int countMatchNum = 0;
						for (String key :keys)
						{
							for (int i = 0; i < list.size(); i++) {
								//System.out.println("关键字:["+key+"],NodeName["+list.get(i).getNodeName()+"]");
								if (key.equalsIgnoreCase(list.get(i).getNodeName())) {
									//System.out.println("________关键字匹配上了,"+"关键字:["+key+"],NodeName["+list.get(i).getNodeName()+"]");
									//System.out.println("list.get(i).getNodeValue()["+list.get(i).getNodeValue()+"]keyWordMap.get(key)["+keyWordMap.get(key)+"]");
									if (null!=list.get(i).getNodeValue())
									{
										if (list.get(i).getNodeValue().equalsIgnoreCase(keyWordMap.get(key)))
										{
											countMatchNum++;
										}
									}
								}
								//key = null;
							}//for (int i = 0; i < list.size(); i++)
						}//for (String key :keys)
						if (countMatchNum==keys.size())
						{
							//匹配上了
							if (maxNodeCount<countMatchNum)
							{
								maxNodeCount = countMatchNum;
								loc = m;
							}
						}
						keys = null;
						keyWordMap = null;
						
					}//if (simKeyVal.containsKey(keyTmp.toString()))
				}//if (simRules[m][2].equals(recNodeNo) && simRules[m][6].equals(recRuleBatchNo))
			}//for (int m=0;m<simRules.length;m++)
			
			
			
		}//if (null != list && null != simRules && null!= simKeyVal )
		list = null;
		return loc;
	}



	private boolean filterXML(String content, HashMap<String, String> keyWordMap,String charset) throws Exception {

		ArrayList<NodeStruct> list = XMLDeal.parseXMLfromStr(content);
		//System.out.println("null != list["+(null != list)+"]");
		if (null != list) {
			Set<String> keys = keyWordMap.keySet();
			//System.out.println("keys.size()["+keys.size()+"]");
			int countMatchNum = 0;
			for (String key :keys)
			{
				for (int i = 0; i < list.size(); i++) {
					//System.out.println("关键字:["+key+"],NodeName["+list.get(i).getNodeName()+"]");
					if (key.equalsIgnoreCase(list.get(i).getNodeName())) {
						//System.out.println("________关键字匹配上了,"+"关键字:["+key+"],NodeName["+list.get(i).getNodeName()+"]");
						//System.out.println("list.get(i).getNodeValue()["+list.get(i).getNodeValue()+"]keyWordMap.get(key)["+keyWordMap.get(key)+"]");
						if (null!=list.get(i).getNodeValue())
						{
							if (list.get(i).getNodeValue().equalsIgnoreCase(keyWordMap.get(key)))
							{
								countMatchNum++;
							}
						}
					}
					//key = null;
				}//for (int i = 0; i < list.size(); i++)
			}//for (String key :keys)
			if (countMatchNum==keys.size())
			{
				list = null;
				keys = null;
				return true;//完全匹配
			}
			else
			{
				list = null;
				keys = null;
				return false;
			}
		}
		
		list = null;
		content = null;
		return false;

	}





	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		AttributeKey CONTEXT = new AttributeKey(getClass(), "context");
		session.removeAttribute(CONTEXT);
		session.close(false);
		super.exceptionCaught(session, cause);
	}



	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		session.close(false);
		super.sessionClosed(session);
	}



	@Override
	public void sessionCreated(IoSession session) throws Exception {
		if ("0".equals(subServer.getLinkMode()))
		{
			//长连接
			((SocketSessionConfig)session.getConfig()).setKeepAlive(true);
			//((SocketSessionConfig)session.getConfig()).setReuseAddress(true);
			//log.info("柜面ITP请求连接接入...[长连接]");
		}
		else
		{
			((SocketSessionConfig)session.getConfig()).setKeepAlive(false);
			//((SocketSessionConfig)session.getConfig()).setReuseAddress(true);
		}
		super.sessionCreated(session);
	}



	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		if ("1".equals(subServer.getLinkMode()))
		{
			//短连接，则立即关闭
			session.close(false);
		}
		super.sessionIdle(session, status);
	}
	


	
	

	

}
