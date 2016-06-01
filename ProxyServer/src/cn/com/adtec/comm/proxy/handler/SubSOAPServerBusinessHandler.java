package cn.com.adtec.comm.proxy.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.soap.SOAPException;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cn.com.adtec.comm.proxy.bean.SOAPCommObject;
import cn.com.adtec.comm.proxy.message.MessageManage;
import cn.com.adtec.comm.proxy.message.NodeStruct;
import cn.com.adtec.comm.proxy.server.AbstractSubServer;
import cn.com.adtec.comm.proxy.tools.SystemTime;
import cn.com.adtec.comm.proxy.tools.TMSThreadPool;
import cn.com.adtec.comm.proxy.tools.Tools;



/**
 * 子服务端业务处理总控
 * 
 * @author 汤继波
 *
 */
public class SubSOAPServerBusinessHandler extends IoHandlerAdapter {
	private final static Logger log = LoggerFactory	.getLogger(SubSOAPServerBusinessHandler.class);
	//private static int count=0;
	private AbstractSubServer subServer;
	public SubSOAPServerBusinessHandler(AbstractSubServer sss)
	{
		subServer = sss;
	}

	
	public void messageReceived_SIMULATE(IoSession session, Object message)
	throws Exception {
// TODO Auto-generated method stub
//super.messageReceived(session, message);
//此处的message是与客户端事先约定的处理对象

log.info("Sub SOAP Server ,Begin to process business (SOAP)......................");
SOAPCommObject nco = (SOAPCommObject)message;
System.out.println("SOAP请求报文头：【"+nco.getHead()+"】");
System.out.println("SOAP请求报文体：【"+nco.getBody()+"】");



StringBuilder sb = new StringBuilder();
sb.append("HTTP/1.1 200 OK\r\n");
/*		Calendar ca = new GregorianCalendar();
SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss",Locale.US);

sb.append(sdf.format(ca.getTime()));
sb.append(" GMT");*/
//sb.append("\r\nServer: Websphere Application Server/6.1\r\nContent-Type: text/html;\r\nContent-Length: ");
sb.append("Content-Type: text/html;\r\nContent-Length: ");
sb.append(1767);
//sb.append("\r\nConnection: Keep-Alive\r\nAccept: image/jpeg, */*\r\nAccept-Ranges: bytes");
sb.append("\r\n\r\n");
sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">  <soapenv:Header/>  <soapenv:Body>    <ns:Response xmlns:ns=\"http://www.cqrcb.com/esb\">      <ResponseHeader>        <VersionNo>2011012100</VersionNo>        <ReqSysCode>004001</ReqSysCode>        <ReqSecCode/>        <TxType/>        <TxMode>0</TxMode>        <TxCode>002001990000601</TxCode>        <ReqDate>20141113</ReqDate>        <ReqTime/>        <ReqSeqNo>06AAA2014111303023000285392001</ReqSeqNo>        <SvrDate>20141113</SvrDate>        <SvrTime>150352</SvrTime>        <SvrSeqNo>050146000011479</SvrSeqNo>        <RecvFileName/>        <TotNum>0</TotNum>        <CurrNum>0</CurrNum>        <FileHMac/>        <HMac/>      </ResponseHeader>      <NHRspHeader>        <NHSvrTmStamp>2014-11-13-15.03.52.745000</NHSvrTmStamp>        <NHChanlNo>06A</NHChanlNo>        <NHReqSeqNo>06AAA2014111303023000285392001</NHReqSeqNo>      </NHRspHeader>      <NullHeader/>      <NullHeader/>      <NullHeader/>      <NullHeader/>      <PayHeader>        <HostSeqNo/>      </PayHeader>      <ResponseBody>        <OneAcctNo>402230080232020810</OneAcctNo>        <PrsnChNme>刘刚            </PrsnChNme>        <CustNo>1862578646</CustNo>        <TotAmt>3.00</TotAmt>        <Bal>26983.00</Bal>        <NextPrtRow>0</NextPrtRow>        <SbjtNo>21111</SbjtNo>        <UCSWFlg>1</UCSWFlg>        <InfoPro> </InfoPro>        <AcctBrch>160101</AcctBrch>        <CfrmFlg>1</CfrmFlg>      </ResponseBody>      <soapenv:Fault>        <FaultCode>002001AAAAAAA</FaultCode>        <FaultString>ESBC:交易成功</FaultString>        <Detail>          <TxStatus>SUCCESS</TxStatus>        </Detail>      </soapenv:Fault>    </ns:Response>  </soapenv:Body></soapenv:Envelope>");


session.write(sb.toString());
//System.out.println("返回SOAP报文:【"+sb.toString()+"】报文个数【"+(++count)+"】");

nco = null;
sb = null;


/*		ca = null;
sdf = null;*/
}
	
	
	/**
	 * 接收SOAP协议报文内容，进行处理，并返回SOAP协议报文
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// TODO Auto-generated method stub
		//super.messageReceived(session, message);
		//此处的message是与客户端事先约定的处理对象
		
		//log.info("Sub SOAP Server ,Begin to process business (SOAP)......................");
		SOAPCommObject nco = (SOAPCommObject)message;
		//System.out.println("客户端地址：["+session.getRemoteAddress().toString()+"]SOAP请求报文头：【"+nco.getHead()+"】");
		//System.out.println("SOAP请求报文体：【"+nco.getBody()+"】");
		
		String appName = nco.getHead().substring(nco.getHead().indexOf("POST")+4, nco.getHead().indexOf("HTTP/")).trim().replaceFirst("/", "");
		//String appName = nco.getHead().substring(nco.getHead().indexOf("POST")+4, nco.getHead().indexOf("HTTP/")).trim().replace("/", "");
		//count++;
		log.info("客户端地址：["+session.getRemoteAddress().toString()+"]["+appName+"]收到请求报文【"+nco.getBody()+"】!");

		SystemTime beginST = new SystemTime();
		String beginTime = beginST.getDateStr()+beginST.getRigorTime();
		
		//根据不同的服务码进行处理
		byte[] respTmp=null;//存放返回信息
		
		//用于异步保存录制报文
		String recNodeNo = null;			//节点编号
		String recRuleNo = null;			//录制规则编号
		String endTime = null;				//交易结束时间
		boolean recFlag = false;			//录制标志
		String filesvrTestEnv = null;		//文件服务器所在环境

		
		//判断 节点总控 ，模式是“直连”还是“仿真”   0-直联，1-仿真
		if (subServer.getNodeMode().equals("0"))
		{
			//直联模式，不需要判断规则，直接发往下游系统
			//log.info("do not need determine rule["+subServer.getNodeMode()+"]");
			//respTmp =Tools.sendOutToSOAP("http",subServer.getNextSystemIP(),subServer.getNextSystemPort(),"/"+appName,subServer.getNextSystemTimeOut(),null,nco.getBody(),subServer.getCharset(),session,subServer.getLinkMode());
			respTmp =Tools.sendOutToSOAP("http",subServer.getNextSystemIP(),subServer.getNextSystemPort(),"/"+appName,subServer.getNextSystemTimeOut(),nco.getHead(),nco.getBody(),subServer.getCharset(),session,subServer.getLinkMode());

		}
		else if (subServer.getNodeMode().equals("1"))
		{
			//仿真模式，需要判断交易级控制规则，是走仿真还是走直连
			//log.info("need determine rule["+subServer.getNodeMode()+"]");
			String[][] pcrTemp = subServer.getPkgCollectRules();
			boolean findIt = false;
			boolean matched = false;
			boolean simMatched = false;
			boolean runNextSystem = true;//默认发往下游系统，如果中间有仿真，则置此值为false;
			if (null!=pcrTemp)
			{
				//log.info("规则条数：["+pcrTemp.length+"]");
				String[][] simRules = subServer.getPkgSimRules();
				for (int i =0;i<pcrTemp.length;i++)
				{
					//遍历每条规则，只要匹配上一条则跳出直接处理
					/*
					 * 是否发往仿真（直连仿真）((0-直连,1-用例级仿真，2-交易级仿真)开关标志|@|是否录制报文开关标志|@|节点编号|@|节点名称|@|原始交易报文种类|@|分隔符|@|录制规则批次号|@|关键字=关键值;关键字1=关键值1(或者“[0,5]=关键字2;[7]=关键字3”) |@|是否有返回文件标志|@|文件服务器地址|@|文件服务器端口|@|文件传输协议|@|文件名存放位置|@|文件传输用户名|@|文件传输密码|@|文件服务器环境|@|规则类型|@|项目编号
					 */
					HashMap<String,String> keyWordMap = null;
					findIt = false;
					matched = false;
					simMatched = false;
					//log.info("规则key：["+pcrTemp[i][6]+"]");
					/*	
					Set<String>  kws = subServer.getKeyWordVal().keySet();
				log.info("kws.size()"+kws.size());
					for (String kw:kws)
					{
						log.info("关键字MAP有这个key["+kw+"]");
					}*/
					
					if (subServer.getKeyWordVal().containsKey(pcrTemp[i][6]))
					{
						findIt = true; 
						
							
						//log.info("有这个key["+pcrTemp[i][6]+"]");
						keyWordMap = subServer.getKeyWordVal().get(pcrTemp[i][6]);
						
						
						//业务处理,匹配报文
						
						matched=filterSOAP(nco.getBody(),keyWordMap,subServer.getCharset());
						//log.info("matched:"+matched);
						if (matched==true)
						{
							//20141106,根据行方要求改为，走直连。只把录制的规则放入此文件，如果匹配到，则是录制，不做仿真
							//log.info("规则找到,判断交易走向"+pcrTemp[i][0]);
							
							
							//判断交易走向，(0-直连,1-用例级仿真，2-交易级仿真
							if ("0".equals(pcrTemp[i][0]))
							{
								//走直连,检查是否需要录制报文
								
								
								if (pcrTemp[i][1].equals("1"))
								{
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
									
									respTmp = new COMM11().process(nco.getBody(), session, appName, subServer, pcrTemp[i][2], pcrTemp[i][6], pcrTemp[i][17],"tran",pcrTemp[i][18]);
								}
								else
								{
									respTmp = new COMM11().process(nco.getBody(), session, appName, subServer, pcrTemp[i][2], pcrTemp[i][6], null,"tran",null);
								}
								
								break;
							}
							else
							{
								//走用例级仿真

								//再匹配仿真规则，add by TJB 20141030  ,20141128
								//需要嵌套循环
								StringBuilder keyTmp = new StringBuilder();
								/*
								keyTmp.append(pcrTemp[i][2]);
								keyTmp.append("_");
								keyTmp.append(pcrTemp[i][6]);
								*/
								int simLoc = findMaxMatchedRule(pcrTemp[i][2],pcrTemp[i][6],simRules,nco.getBody(),subServer.getCharset(),subServer.getKeyWordValSim(),keyTmp);
								//log.info("找仿真规则结果【"+simLoc+"】");
								if (simLoc<0)
								{
									//后面统一走直连
									//继续找，直到最后一笔没找着，
								}//if (simLoc<0)
								else
								{
									simMatched = true;
									//找着最全匹配的仿真规则了
									if (simRules[simLoc][0].equals("1"))
									{
										//走用例级仿真
										log.info("根据标志走用例级仿真");
										runNextSystem = false;
										if (simRules[simLoc].length>=19)
										{
											respTmp = new COMM11().process(nco.getBody(), session, appName, subServer, simRules[simLoc][2], simRules[simLoc][6], simRules[simLoc][17],"case", simRules[simLoc][18]);
										}
										else
										{
											respTmp = new COMM11().process(nco.getBody(), session, appName, subServer, simRules[simLoc][2], simRules[simLoc][6], null,"case",null);
										}
											//Tools.sendOutToSOAP("http",subServer.getTmsIP(),subServer.getTmsPort(),"/"+subServer.getTmsSimulApp(),"120",null,nco.getBody(),subServer.getTmsCharSet());
										
										break;
									}
									
									else if (simRules[simLoc][0].equals("2"))
									{
										//走交易级仿真
										log.info("根据标志走交易级仿真");
										runNextSystem = false;
										if (simRules[simLoc].length>=19)
										{
											respTmp = new COMM11().process(nco.getBody(), session, appName, subServer, simRules[simLoc][2], simRules[simLoc][6], simRules[simLoc][17],"tran", simRules[simLoc][18]);
										}
										else
										{
											respTmp = new COMM11().process(nco.getBody(), session, appName, subServer, simRules[simLoc][2], simRules[simLoc][6], null,"tran",null);
										}
											//Tools.sendOutToSOAP("http",subServer.getTmsIP(),subServer.getTmsPort(),"/"+subServer.getTmsSimulApp(),"120",null,nco.getBody(),subServer.getTmsCharSet());
										
										break;
									}
									
									else if (simRules[simLoc][0].equals("0"))
									{
										//走直连？
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
								}//if (simLoc<0) else
								keyTmp = null;
							}
							
							
						}//if (matched==true)
						else
						{
							//未匹配到录制规则，再匹配仿真规则，add by TJB 20141030
							//需要嵌套循环
							StringBuilder keyTmp = new StringBuilder();
							/*
							keyTmp.append(pcrTemp[i][2]);
							keyTmp.append("_");
							keyTmp.append(pcrTemp[i][6]);
							*/
							int simLoc = findMaxMatchedRule(pcrTemp[i][2],pcrTemp[i][6],simRules,nco.getBody(),subServer.getCharset(),subServer.getKeyWordValSim(),keyTmp);
							//log.info("找仿真规则结果【"+simLoc+"】");
							if (simLoc<0)
							{
								//后面统一走直连
								//继续找，直到最后一笔没找着，
							}//if (simLoc<0)
							else
							{
								simMatched = true;
								//找着最全匹配的仿真规则了
								if (simRules[simLoc][0].equals("1"))
								{
									//走用例级仿真
									log.info("根据标志走用例级仿真");
									runNextSystem = false;
									if (simRules[simLoc].length>=19)
									{
										respTmp = new COMM11().process(nco.getBody(), session, appName, subServer, simRules[simLoc][2], simRules[simLoc][6], simRules[simLoc][17],"case", simRules[simLoc][18]);
									}
									else
									{
										respTmp = new COMM11().process(nco.getBody(), session, appName, subServer, simRules[simLoc][2], simRules[simLoc][6], null,"case",null);
									}
										//Tools.sendOutToSOAP("http",subServer.getTmsIP(),subServer.getTmsPort(),"/"+subServer.getTmsSimulApp(),"120",null,nco.getBody(),subServer.getTmsCharSet());
									
									break;
								}
								else if (simRules[simLoc][0].equals("2"))
								{
									//走交易级仿真
									log.info("根据标志走交易级仿真");
									runNextSystem = false;
									if (simRules[simLoc].length>=18)
									{
										respTmp = new COMM11().process(nco.getBody(), session, appName, subServer, simRules[simLoc][2], simRules[simLoc][6], simRules[simLoc][17],"tran", simRules[simLoc][18]);
									}
									else
									{
										respTmp = new COMM11().process(nco.getBody(), session, appName, subServer, simRules[simLoc][2], simRules[simLoc][6], null,"tran",null);
									}
										//Tools.sendOutToSOAP("http",subServer.getTmsIP(),subServer.getTmsPort(),"/"+subServer.getTmsSimulApp(),"120",null,nco.getBody(),subServer.getTmsCharSet());
									
									break;
								}
								else if (simRules[simLoc][0].equals("0"))
								{
									//走直连？
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
							}//if (simLoc<0) else
							keyTmp = null;
						}
						keyWordMap = null;
						
					}
					else
					{
						log.info("没有这个key["+pcrTemp[i][6]+"]");
					}
				}//for (int i =0;i<pcrTemp.length;i++)
				simRules = null;
			}//if (null!=pcrTemp)
			//如果没找到；或者找着了，但仿真规则没匹配上，默认还是发往下游系统
			if (runNextSystem==true)
			{
				//log.info("findIt["+findIt+"]simMatched["+simMatched+"]如果没找到；或者找着了，但仿真规则没匹配上，默认还是发往下游系统");
				respTmp =	Tools.sendOutToSOAP("http",subServer.getNextSystemIP(),subServer.getNextSystemPort(),"/"+appName,subServer.getNextSystemTimeOut(),nco.getHead(),nco.getBody(),subServer.getCharset(),session,subServer.getLinkMode());
				SystemTime endST = new SystemTime();
				endTime = endST.getDateStr()+endST.getRigorTime();
				endST = null;
			}
			
			pcrTemp = null;
		}//else if (subServer.getNodeMode().equals("1"))
		else
		{
			log.error("not supported mode["+subServer.getNodeMode()+"]");
		}

		//System.out.println("SOAP响应报文体：【"+new String(respTmp,subServer.getCharset())+"】");

		
		if (null!=respTmp)
		{
			Tools.returnSOAP(session, respTmp, subServer.getCharset(),subServer.getLinkMode());
		}
		else
		{
			StringBuilder keyTmp = new StringBuilder();
			keyTmp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns:Response xmlns:ns=\"http://www.cqrcb.com/esb\"><soapenv:Fault><FaultCode>TMS0002</FaultCode><FaultString>后端服务器【");
			keyTmp.append(subServer.getNextSystemIP());
			keyTmp.append("】交易码【");
			keyTmp.append("/");
			keyTmp.append(appName);
			keyTmp.append("】没有返回响应报文#</FaultString><Detail><TxStatus>FAIL</TxStatus></Detail></soapenv:Fault></ns:Response></soapenv:Body></soapenv:Envelope>");
			
			respTmp = keyTmp.toString().getBytes(subServer.getCharset());
			keyTmp=null;
				//MessageManage.assembleFailSOAPMessage("", subServer.getCharset());
			Tools.returnSOAP(session, respTmp, subServer.getCharset(),subServer.getLinkMode());
		}
		
		
		
		if (recFlag==true)
		{
			TMSThreadPool.addNewTask(new COMM01(nco.getBody(),new String(respTmp,subServer.getCharset()),appName,subServer,recNodeNo,recRuleNo,beginTime,endTime,filesvrTestEnv,null,""));
			
		}
		recNodeNo = null;			//节点编号
		recRuleNo = null;			//录制规则编号
		endTime = null;				//交易结束时间
		respTmp=null;
		appName = null;
		nco = null;
		//sb = null;
		beginST = null;
		
/*		ca = null;
		sdf = null;*/
	}

	
	
	//pcrTemp[i][2],pcrTemp[i][6],simRules,nco.getBody());
	private int findMaxMatchedRule(String recNodeNo, String recRuleBatchNo,
			String[][] simRules, String bodyPkg ,String charset, ConcurrentHashMap<String, HashMap<String, String>> simKeyVal, StringBuilder keyTmp) throws UnsupportedEncodingException, SOAPException, IOException {
		int loc = -1;				//用来记录关键字完全匹配的simRules数组的一维下标
		int maxNodeCount = 0;		//用来记录关键字完全匹配的最大的关键字个数
		ArrayList<NodeStruct> list = MessageManage.parse("SOAP", bodyPkg
				.toString().getBytes(charset), charset, null);
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
			
			
			
		}//if (null != list)
		
		list = null;
		return loc;
	}



	private boolean filterSOAP(String body, HashMap<String, String> keyWordMap,String charset) throws UnsupportedEncodingException, SOAPException, IOException {
		ArrayList<NodeStruct> list = MessageManage.parse("SOAP", body
				.toString().getBytes(charset), charset, null);
		if (null != list) {
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
		// TODO Auto-generated method stub
		/*
		if ("1".equals(subServer.getLinkMode()))
		{
			//短连接，则立即关闭
			session.close(false);
		}
		*/
		super.sessionIdle(session, status);
	}

	


	
	

	

}
