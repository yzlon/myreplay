package cn.com.adtec.comm.proxy.handler;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import cn.com.adtec.comm.proxy.bean.NewMoniObject;

import cn.com.adtec.comm.proxy.client.HXClient;

import cn.com.adtec.comm.proxy.message.MessageManage;
import cn.com.adtec.comm.proxy.message.NodeStruct;

import cn.com.adtec.comm.proxy.server.AbstractSubServer;
import cn.com.adtec.comm.proxy.server.SubXML_HXServer;
import cn.com.adtec.comm.proxy.tools.HxThreadPool;
import cn.com.adtec.comm.proxy.tools.SystemTime;

import cn.com.adtec.comm.proxy.tools.XMLDeal;




/**
 * ESB-核心节点，子服务端业务处理总控
 * 
 * @author 汤继波
 *
 */
public class SubXML_HXServerBusinessHandler extends IoHandlerAdapter{
	private final static Logger log = LoggerFactory	.getLogger(SubXML_HXServerBusinessHandler.class);
	private SubXML_HXServer subServer;
	private static int count = 0;
	public SubXML_HXServerBusinessHandler(SubXML_HXServer sss)
	{
		subServer = sss;
	}


	
	/**
	 * 接收协议报文内容，进行处理，并返回相应协议报文
	 */
	
	public void messageReceived_SIM(IoSession session, Object message)
			throws Exception {
		// TODO Auto-generated method stub
		//super.messageReceived(session, message);
		//此处的message是与客户端事先约定的处理对象
		//System.out.println(((InetSocketAddress)session.getRemoteAddress()).getHostName());
		//System.out.println(((InetSocketAddress)session.getRemoteAddress()).getPort());
		//log.info("Sub XML_HX Server ,Begin to process business (HX-XML)......................");
		NewMoniObject co = (NewMoniObject)message;	//请求报文对象
		NewMoniObject nmo = null;					//响应报文对象
		if (null==co)
		{
			log.error("........接收ESB请求报文失败！");
			return;
		}
		//log.info("业务报文体：["+co.getBody().getFmtData()+"]");
		
		SystemTime beginST = new SystemTime();
		String beginTime = beginST.getDateStr()+beginST.getTimeStrhhmmss();
		count++;
		String kkk = co.getInnerBuffer().flip().getString(Charset.forName(subServer.getCharset()).newDecoder());
		log.info("XML_HX请求报文【"+kkk+"】收到第【"+count+"】个");
		
		//根据不同的服务码进行处理
		byte[] respTmp=null;//存放返回信息
		StringBuilder sb = new StringBuilder();
		
		if ("0".equals(co.getTyp()))
		{
			//数据报文
			System.out.println("co.getHead().getPackFlg()["+co.getPackFlg()+"]");
			sb.append("10810a72d5711-aadb-4dfb-9ca6-dd9cd2cae572000045                                00188900002001990000631<?xml version=\"1.0\" encoding=\"GB18030\"?><Response><Fault><FaultCode>AAAAAAA</FaultCode><FaultString></FaultString><NHSvrTmStamp>2014-11-26-11.35.04.154000</NHSvrTmStamp></Fault><ResponseHeader><NHSvrTmStamp>2014-11-26-11.35.04.154000</NHSvrTmStamp><NHChanlNo>06A</NHChanlNo><NHReqSeqNo>06AAA2014112611334800313740001</NHReqSeqNo><CurrNum>0</CurrNum><SvrDate>20141126</SvrDate><SvrTime>113504</SvrTime><SvrSeqNo>               </SvrSeqNo></ResponseHeader><ResponseBody><FaultCode>       </FaultCode><CurrNum>0</CurrNum><SvrDate>20141126</SvrDate><SvrTime>113504</SvrTime><SvrSeqNo>               </SvrSeqNo><AcctNo>1601010125010319942</AcctNo><AcctBrch>160101</AcctBrch><CustNo>1862578646</CustNo><PrsnChNme>刘刚            </PrsnChNme><CcyNme>人民币  </CcyNme><SbjtNo>21111</SbjtNo><BalProp>1</BalProp><LastTxnDt>20141124</LastTxnDt><LastBalDrct>1</LastBalDrct><PrevBal>72928.00</PrevBal><LastInterDt>20141125</LastInterDt><ResvBal>0.00</ResvBal><FrzBal>0.00</FrzBal><CtrlBal>0.00</CtrlBal><AcctBal>72983.00</AcctBal><BalDrct>1</BalDrct><SettlIntMeth>4</SettlIntMeth><PasIntFlg>0</PasIntFlg><CshFlg>1</CshFlg><ChagTaxFlg> </ChagTaxFlg><IntTyp>100</IntTyp><SheetIntSum>0.00</SheetIntSum><Aggr>1303523.00</Aggr><IncOrDecInt>0.00</IncOrDecInt><IncOrDecAggr>0.00</IncOrDecAggr><UCSWFlg>1</UCSWFlg><AcctDt>20140915</AcctDt><AcctOper>160144</AcctOper><MntDt>20141125</MntDt><MntOper>050146</MntOper><CxlAcctDt>0</CxlAcctDt><CxlAcctOper>      </CxlAcctOper><LatestTxnDt>20141125</LatestTxnDt><RecdSt>0</RecdSt><NoRgstBkbookNum>86</NoRgstBkbookNum><AcctChiNme>人民币活期主帐户                                            </AcctChiNme><FloatPct>10.0000</FloatPct><CxlAcctBrch>      </CxlAcctBrch><AcctMngr>      </AcctMngr><ChgPengFlg>                                        </ChgPengFlg><AcptTyp>02</AcptTyp><IntCatgy>1</IntCatgy><IntRt>0.3850000</IntRt></ResponseBody></Response>");
			sb = sb.replace(5, 42, co.getPackFlg());
			respTmp=sb.toString().getBytes(subServer.getCharset());
		}
		else
		{
			//控制报文
			//System.out.println("co.getCtrlPkgBody()["+co.getCtrlPkgBody()+"]");
			
			String tmp = kkk.substring(5, 7);
			System.out.println("tmp["+tmp+"]");
			if ("00".equals(tmp))
			{
				respTmp="10461AAAAAAA007a662117-cfba-477c-bad9-f2f99517e96a".getBytes();
			}
			else if ("01".equals(tmp)) 
			{
				respTmp="10101AAAAAAA01".getBytes();
			}
		}

		
		

		//返回报文给请求方
		sb.delete(0, sb.length());
		sb.append(new String(respTmp,subServer.getCharset()).trim());
		session.write(sb.toString());
		if ("1".equals(subServer.getLinkMode()))
		{
			//短连接
			session.close(true);
		}
		
		System.out.println("__返回ESB["+session.getRemoteAddress().toString()+"]报文：["+sb.toString()+"]第【"+count+"】个");
		
		//session.write(sb.toString());
		 
		
						//交易结束时间
		respTmp=null;
		co = null;
		nmo = null;
		sb.delete(0, sb.length());
		sb = null;
		beginST = null;
		beginTime = null;
		
		//sb = null;
/*		ca = null;
		sdf = null;*/
	}
	

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {

		NewMoniObject co = (NewMoniObject) message; // 请求报文对象
		//NewMoniObject nmo = null; // 响应报文对象
		if (null == co) {
			log.error("........接收ESB请求报文失败！");
			return;
		}
		// log.info("业务报文体：["+co.getBody().getFmtData()+"]");
		//log.info("客户端地址：["+session.getRemoteAddress().toString()+"]已收到请求报文!");

		SystemTime beginST = new SystemTime();
		String beginTime = beginST.getDateStr() + beginST.getRigorTime();
		//log.info("beginTime:["+beginTime+"]");
		/*
		if (co.getPubhead().getTyp().equals("0")) {
			count++;
			String kkk = co.getInnerBuffer().flip().getString(
					Charset.forName(subServer.getCharset()).newDecoder());
			log.info("XML_HX请求报文【" + kkk + "】收到第【" + count + "】个");
			kkk = null;
		}
		*/
		// 根据不同的服务码进行处理
		byte[] respTmp = null;// 存放返回信息

		// 用于异步保存录制报文
		String recNodeNo = null; // 节点编号
		String recRuleNo = null; // 录制规则编号
		String endTime = null; // 交易结束时间
		boolean recFlag = false; // 录制标志
		String filesvrTestEnv = null; // 文件服务器所在环境
		String sessionId = String.valueOf(session.getId());// 客户端会话ID

		// 判断 模式是“直连”还是“仿真” 0-直联，1-仿真
		if (subServer.getNodeMode().equals("0")) {
			// 服务器总控,直联模式，不需要判断规则，直接发往下游系统
			// log.info("【直联模式】do not need determine rule["+subServer.getNodeMode()+"]");
			//sendMsg(NewMoniObject nmo,IoSession session,String clientAddr,String recNodeNo,
			///String recRuleNo,String beginTime,String filesvrTestEnv,boolean recFlag) 
			HxThreadPool.addNewTask((SubXML_HXServer)subServer, co, session, sessionId, null, null, beginTime, null, recFlag);
			/*
			HXClient.getInstance((SubXML_HXServer) subServer).sendMsg(co,
					session, sessionId,null,null,beginTime,null,recFlag);
			*/

			// respTmp="10461AAAAAAA007a662117-cfba-477c-bad9-f2f99517e96a".getBytes();
			// Tools.sendOutToSOAP("http",subServer.getNextSystemIP(),subServer.getNextSystemPort(),"/"+appName,subServer.getNextSystemTimeOut(),null,nco.getBody(),subServer.getCharset());
		} else if (subServer.getNodeMode().equals("1")) {
			// 服务器总控,仿真模式，需要判断规则，该交易是走仿真还是走直连
			// log.info("【节点总控仿真模式】need determine rule["+subServer.getNodeMode()+"]");
			// log.info("co==null["+(co==null)+"]co.getPubhead()["+co.getPubhead()+"]co.getPubhead().getTyp()["+co.getPubhead().getTyp()+"]");

			if (co.getTyp().equals("1")) {
				// 控制包不需要仿真，直接发往下游系统
				HxThreadPool.addNewTask((SubXML_HXServer)subServer, co, session, sessionId, null, null, beginTime, null, recFlag);
				
				/*
				HXClient.getInstance((SubXML_HXServer) subServer).sendMsg(co,
						session, sessionId,null,null,beginTime,null,recFlag);
						*/
				// IoBuffer buf = IoBuffer.allocate(nmo.getTotalLen()+1);
				// Tools.changeObj2Buf(buf, nmo, subServer.getCharset());
				//respTmp = nmo.getInnerBuffer().flip().array();
				// buf.clear();
				// buf = null;

			} else {
				// log.info("这是ESB发来的数据包......");
				String[][] pcrTemp = subServer.getPkgCollectRules();
				String[][] simRules = subServer.getPkgSimRules();
				boolean findIt = false;
				boolean matched = false;
				boolean simMatched = false;
				boolean runNextSystem = true;//默认发往下游系统，如果中间有仿真，则置此值为false;
				if (null == pcrTemp) {
					// 如果没有配置录制规则，仍然走直连模式
					log.info("没有匹配规则");
				} else {
					// log.info("有匹配规则");
					for (int i = 0; i < pcrTemp.length; i++) {
						// 遍历每条规则，只要匹配上一条则跳出直接处理
						/*
						 * 是否发往仿真（直连仿真）(0-直连,1-用例级仿真，2-交易级仿真)开关标志|@|是否录制报文开关标志|@|节点编号|@|节点名称
						 * |
						 * @|原始交易报文种类|@|分隔符|@|录制规则批次号|@|关键字=关键值;关键字1=关键值1(或者“[0,
						 * 5]=关键字2;[7]=关键字3”)
						 * |@|是否有返回文件标志|@|文件服务器地址|@|文件服务器端口|@|
						 * 文件传输协议|@|文件名存放位置|@|文件传输用户名|@|文件传输密码|@|文件服务器环境|@|规则类型|@|项目编号
						 */
						HashMap<String, String> keyWordMap = null;
						findIt = false;
						matched = false;
						simMatched = false;
						/*
						 * log.info("pcrTemp[i][2]...【"+pcrTemp[i][2]+"】");
						 * Set<String> keys =
						 * subServer.getKeyWordVal().keySet();
						 * log.info("keys.size["+keys.size()+"]"); for (String
						 * tm:keys) { log.info("key["+tm+"]"); }
						 */
						if (subServer.getKeyWordVal()
								.containsKey(pcrTemp[i][6])) {
							// log.info("关键字有这个["+pcrTemp[i][2]+"]");
							keyWordMap = subServer.getKeyWordVal().get(
									pcrTemp[i][6]);
							findIt = true;
							// 业务处理,匹配报文

							matched = filterXML(co.getFmtData(),
									keyWordMap, subServer.getCharset());
							// filterFLOW(co.getHead(),co.getBody(),keyWordMap,subServer.getCharset(),"0");

							if (matched == true) {
								// 20141106,根据行方要求，走直连。只做录制报文,不走仿真
								//20141128，修改为判断走向
								//log.info("规则找到,判断交易走向"+pcrTemp[i][0]);
								
								if ("0".equals(pcrTemp[i][0]))
								{
									//走直连,检查是否需要录制报文
										
									if (pcrTemp[i][1].equals("1")) {
										recFlag = true;
										recNodeNo = pcrTemp[i][2];
										recRuleNo = pcrTemp[i][6];
										if (pcrTemp[i].length >= 16) {
											filesvrTestEnv = pcrTemp[i][15];
										}
									}
									break;
								}else if ("2".equals(pcrTemp[i][0]))
								{
									//交易级仿真
									runNextSystem = false;
									if (pcrTemp[i].length>=19)
									{
										respTmp = new COMM11().process(new String(
											co
											.getInnerBuffer()
											.flip()
											.array(),
									subServer
											.getTmsCharSet()), session, null, subServer, pcrTemp[i][2], pcrTemp[i][6], pcrTemp[i][17],"tran", pcrTemp[i][18]);
									
										
									}
									else
									{
										respTmp = new COMM11().process(new String(
												co
												.getInnerBuffer()
												.flip()
												.array(),
										subServer
												.getTmsCharSet()), session, null, subServer, pcrTemp[i][2], pcrTemp[i][6], null,"tran",null);
									}
									HXClient.returnESB(respTmp, session, subServer);
									break;
								}
								else
								{
									//走用例级仿真

									// 匹配到录制规则，根据标志，走用例级仿真，所以需要再匹配仿真规则，add by TJB 20141128
									// 需要嵌套循环
									
									StringBuilder keyTmp = new StringBuilder();

									int simLoc = findMaxMatchedRule(pcrTemp[i][2],
											pcrTemp[i][6], simRules, co.getFmtData(), subServer
													.getCharset(), subServer
													.getKeyWordValSim(), keyTmp);
									if (simLoc < 0) {
										// 没找着合适的仿真规则，继续找，直到最后一笔没找着，走直连

									}// if (simLoc<0)
									else {
										simMatched = true;
										// 找着最全匹配的仿真规则了
										if (simRules[simLoc][0].equals("1")) {
											// 走用例级仿真
											log.info("匹配到了,走用例级TMS仿真");
											// log.info("co.getTotalLen()["+co.getTotalLen()+"]");
											runNextSystem = false;
											if (simRules[simLoc].length>=19)
											{
												respTmp = new COMM11()
														.process(
																new String(
																		co
																				.getInnerBuffer()
																				.flip()
																				.array(),
																		subServer
																				.getTmsCharSet()),
																session, null,
																subServer,
																simRules[simLoc][2],
																simRules[simLoc][6],
																simRules[simLoc][17],
																"case",simRules[simLoc][18]);
											}
											else
											{
												respTmp = new COMM11()
												.process(
														new String(
																co
																		.getInnerBuffer()
																		.flip()
																		.array(),
																subServer
																		.getTmsCharSet()),
														session, null,
														subServer,
														simRules[simLoc][2],
														simRules[simLoc][6],
														null,
														"case",null);
											}
											HXClient.returnESB(respTmp, session, subServer);
											break;
											// 将仿真里的封包标签换成请求报文里的内容

											// log.info("仿真返回字节数：【"+respTmp.length+"】报文【"+new
											// String(respTmp,subServer.getCharset()).trim()+"】");
										} else if (simRules[simLoc][0].equals("2")) {
											// 走交易仿真
											log.info("匹配到了,走交易级仿真");
											runNextSystem = false;
											// log.info("co.getTotalLen()["+co.getTotalLen()+"]");
											if (simRules[simLoc].length>=19)
											{
												respTmp = new COMM11()
														.process(
																new String(
																		co
																				.getInnerBuffer()
																				.flip()
																				.array(),
																		subServer
																				.getTmsCharSet()),
																session, null,
																subServer,
																simRules[simLoc][2],
																simRules[simLoc][6],
																simRules[simLoc][17],
																"tran",simRules[simLoc][18]);
											}
											else
											{
												respTmp = new COMM11()
												.process(
														new String(
																co
																		.getInnerBuffer()
																		.flip()
																		.array(),
																subServer
																		.getTmsCharSet()),
														session, null,
														subServer,
														simRules[simLoc][2],
														simRules[simLoc][6],
														null,
														"tran",null);
											}
											HXClient.returnESB(respTmp, session, subServer);
											break;
											// 将仿真里的封包标签换成请求报文里的内容

											// log.info("仿真返回字节数：【"+respTmp.length+"】报文【"+new
											// String(respTmp,subServer.getCharset()).trim()+"】");
										} else if (simRules[simLoc][0].equals("0")) {
											
											if (simRules[simLoc][1].equals("1")) {
												// 需要保存报文
												// Tools.sendOutToSOAP("http",subServer.getTmsIP(),subServer.getTmsPort(),"/"+subServer.getTmsPkgSendBackApp(),"120",null,nco.getBody(),respTmp,subServer.getCharset());
												// String reqBody,String
												// resBody,String
												// oldApp,AbstractSubServer
												// subsv,String nodeNo,String
												// recBatch,String beginTime,String
												// endTime
												// 数据类报文才需要保存
												// 20141106,根据行方要求，走直连。其余啥都不做
												/*
												 * if
												 * (co.getPubhead().getTyp().equals
												 * ("0")) { recFlag = true; }
												 * 
												 * recNodeNo = simRules[simLoc][2];
												 * recRuleNo = simRules[simLoc][6];
												 * if (simRules[simLoc].length>=16)
												 * { filesvrTestEnv =
												 * simRules[simLoc][15]; }
												 */
											}

											
											
										} else {
											// 错误
											respTmp = MessageManage
													.assembleFailSOAPMessage(
															"不支持的模式["
																	+ simRules[simLoc][0]
																	+ "]",
															subServer.getCharset());
											HXClient.returnESB(respTmp, session, subServer);
										}
										keyWordMap = null;
										keyTmp = null;
										break;
									}// if (simLoc<0) else

									keyWordMap = null;
									keyTmp = null;
									
								}
							}// if (matched==true)
							else {
								// 未匹配到录制规则，再匹配仿真规则，add by TJB 20141030
								// 需要嵌套循环
								StringBuilder keyTmp = new StringBuilder();

								int simLoc = findMaxMatchedRule(pcrTemp[i][2],
										pcrTemp[i][6], simRules, co.getFmtData(), subServer
												.getCharset(), subServer
												.getKeyWordValSim(), keyTmp);
								if (simLoc < 0) {
									// 没找着合适的仿真规则，继续找，直到最后一笔没找着，走直连

								}// if (simLoc<0)
								else {
									simMatched = true;
									// 找着最全匹配的仿真规则了
									if (simRules[simLoc][0].equals("1")) {
										// 走用例级仿真
										log.info("匹配到了,走用例级仿真");
										// log.info("co.getTotalLen()["+co.getTotalLen()+"]");
										runNextSystem = false;
										
										if (simRules[simLoc].length>=19)
										{
											respTmp = new COMM11()
													.process(
															new String(
																	co
																			.getInnerBuffer()
																			.flip()
																			.array(),
																	subServer
																			.getTmsCharSet()),
															session, null,
															subServer,
															simRules[simLoc][2],
															simRules[simLoc][6],
															simRules[simLoc][17],
															"case",simRules[simLoc][18]);
										}
										else
										{
											respTmp = new COMM11()
											.process(
													new String(
															co
																	.getInnerBuffer()
																	.flip()
																	.array(),
															subServer
																	.getTmsCharSet()),
													session, null,
													subServer,
													simRules[simLoc][2],
													simRules[simLoc][6],
													null,
													"case",null);
										}
										
										HXClient.returnESB(respTmp, session, subServer);
										// 将仿真里的封包标签换成请求报文里的内容
										break;
										// log.info("仿真返回字节数：【"+respTmp.length+"】报文【"+new
										// String(respTmp,subServer.getCharset()).trim()+"】");
									} else if (simRules[simLoc][0].equals("2")) {
										// 走交易仿真
										log.info("匹配到了,走交易级仿真");
										// log.info("co.getTotalLen()["+co.getTotalLen()+"]");
										runNextSystem = false;
										if (simRules[simLoc].length>=19)
										{
											respTmp = new COMM11()
													.process(
															new String(
																	co
																			.getInnerBuffer()
																			.flip()
																			.array(),
																	subServer
																			.getTmsCharSet()),
															session, null,
															subServer,
															simRules[simLoc][2],
															simRules[simLoc][6],
															simRules[simLoc][17],
															"tran",simRules[simLoc][18]);
										}
										else
										{
											respTmp = new COMM11()
											.process(
													new String(
															co
																	.getInnerBuffer()
																	.flip()
																	.array(),
															subServer
																	.getTmsCharSet()),
													session, null,
													subServer,
													simRules[simLoc][2],
													simRules[simLoc][6],
													null,
													"tran",null);
										}
										HXClient.returnESB(respTmp, session, subServer);
										// 将仿真里的封包标签换成请求报文里的内容
										break;
										// log.info("仿真返回字节数：【"+respTmp.length+"】报文【"+new
										// String(respTmp,subServer.getCharset()).trim()+"】");
									} else if (simRules[simLoc][0].equals("0")) {
										// 直接 发往下游系统
										// respTmp =
										
										if (simRules[simLoc][1].equals("1")) {
											// 需要保存报文
											// Tools.sendOutToSOAP("http",subServer.getTmsIP(),subServer.getTmsPort(),"/"+subServer.getTmsPkgSendBackApp(),"120",null,nco.getBody(),respTmp,subServer.getCharset());
											// String reqBody,String
											// resBody,String
											// oldApp,AbstractSubServer
											// subsv,String nodeNo,String
											// recBatch,String beginTime,String
											// endTime
											// 数据类报文才需要保存
											// 20141106,根据行方要求，走直连。其余啥都不做
											/*
											 * if
											 * (co.getPubhead().getTyp().equals
											 * ("0")) { recFlag = true; }
											 * 
											 * recNodeNo = simRules[simLoc][2];
											 * recRuleNo = simRules[simLoc][6];
											 * if (simRules[simLoc].length>=16)
											 * { filesvrTestEnv =
											 * simRules[simLoc][15]; }
											 */
										}

										break;
									} else {
										// 错误
										respTmp = MessageManage
												.assembleFailSOAPMessage(
														"不支持的模式["
																+ simRules[simLoc][0]
																+ "]",
														subServer.getCharset());
										
										HXClient.returnESB(respTmp, session, subServer);
									}
									keyWordMap = null;
									keyTmp = null;
									break;
								}// if (simLoc<0) else

								keyWordMap = null;
								keyTmp = null;
								// break;
							}

						}// if
							// (subServer.getKeyWordVal().containsKey(pcrTemp[i][2]))
					}// for (int i =0;i<pcrTemp.length;i++)
				}

				// 如果没找到，或者找着了，但仿真规则没匹配上，默认还是发往下游系统
				if (runNextSystem == true) {
					// System.out.println("之前没找着规则，默认发下游系统！");
					HxThreadPool.addNewTask((SubXML_HXServer)subServer, co, session, sessionId, recNodeNo, recRuleNo, beginTime, filesvrTestEnv, recFlag);
					
					/*
					HXClient.getInstance((SubXML_HXServer) subServer).sendMsg(co,
									session, sessionId,recNodeNo,recRuleNo,beginTime,filesvrTestEnv,recFlag) ;
					*/
					// IoBuffer buf = IoBuffer.allocate(nmo.getTotalLen()+1);
					// Tools.changeObj2Buf(buf, nmo, subServer.getCharset());
					

				}
				pcrTemp = null;
				simRules = null;
			}// if (co.getPubhead().getTyp().equals("1")) else

		} else {
			log.error("not supported mode[" + subServer.getNodeMode() + "]");
		}

		// respTmp =
		// sendOut("http","esb.dev.cqrcb.com","10040","/"+appName,"120",null,nco.getBody(),subServer.getCharset());

		// 返回报文给请求方
		
		/*
		if (co.getPubhead().getTyp().equals("0")) {
			log.info("XML_HX__返回ESB[" + sessionId + "]第【" + count + "】个报文：["
					+ sb.toString() + "]");
		}
		*/
		// session.write(sb.toString());


		recNodeNo = null; // 节点编号
		recRuleNo = null; // 录制规则编号
		endTime = null; // 交易结束时间
		respTmp = null;
		co = null;
		//nmo = null;
		
		beginST = null;
		beginTime = null;

		// sb = null;
		/* ca = null; sdf = null; */
	}

	
	/**
	 * 
	 * @param recNodeNo
	 * @param recRuleBatchNo
	 * @param simRules
	 * @param bodyPkg
	 * @param charset
	 * @param simKeyVal
	 * @param keyTmp
	 * @return
	 * @throws Exception 
	 */
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



	public String callBack(byte[] returMsg,String charset)
	{
		try {
			return new String(returMsg,charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 匹配XML节点
	 * @param content
	 * @param keyWordMap
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	private boolean filterXML(String content, HashMap<String, String> keyWordMap,String charset) throws Exception {
		//log.info("原报文【"+content+"】");
		ArrayList<NodeStruct> list = XMLDeal.parseXMLfromStr(content);
		if (null != list) {
			//log.info("到核心的XML报文过滤规则有内容");
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
		else
		{
			log.info("到核心，没有XML报文过滤规则");
		}
		
		list = null;
		//content = null;
		return false;

	}




	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		AttributeKey CONTEXT = new AttributeKey(getClass(), "context");
		session.removeAttribute(CONTEXT);
		session.close(false);
		log.info(cause.getMessage());
		super.exceptionCaught(session, cause);
	}



	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		session.close(false);
		log.info("会话被关闭");
		super.sessionClosed(session);
	}



	@Override
	public void sessionCreated(IoSession session) throws Exception {
		if ("0".equals(subServer.getLinkMode()))
		{
			//长连接
			((SocketSessionConfig)session.getConfig()).setKeepAlive(true);
			//((SocketSessionConfig)session.getConfig()).setReuseAddress(true);
			//log.info("ESB请求连接接入...[长连接]");
		}
		else
		{
			((SocketSessionConfig)session.getConfig()).setKeepAlive(false);
			//((SocketSessionConfig)session.getConfig()).setReuseAddress(true);
		}
		super.sessionCreated(session);
	}

	


	
	

	

}
