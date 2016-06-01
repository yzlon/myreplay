package cn.com.adtec.comm.proxy.client;



import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;



import java.util.concurrent.ConcurrentHashMap;

import java.util.concurrent.TimeUnit;




import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cn.com.adtec.comm.proxy.bean.HxSessionObject;
import cn.com.adtec.comm.proxy.bean.NewMoniObject;
import cn.com.adtec.comm.proxy.codec.NewMoniClientCodecFactory;

import cn.com.adtec.comm.proxy.handler.COMM01;



import cn.com.adtec.comm.proxy.server.SubXML_HXServer;
import cn.com.adtec.comm.proxy.tools.SystemTime;
import cn.com.adtec.comm.proxy.tools.TMSThreadPool;



/**
 * 连接核心客户端
 * @author tangjb
 *
 */
public class HXClient{
	//public ClientHandler(String )
	private SubXML_HXServer hxserver;
	private final static Logger log = LoggerFactory	.getLogger(HXClient.class);
	private static HXClient hxclient=null;
	private static NioSocketConnector connector;
	private static int count = 0;

	
	//private ConnectFuture future;
	
	
	private HXClient(SubXML_HXServer serv)
	{
		this.hxserver = serv;
		
	}
	

	public static HXClient getInstance(SubXML_HXServer serv)
	{
		if (null==hxclient)
		{
			hxclient = new HXClient(serv);
			//SendHxTask sht = new SendHxTask(serv);
			//Calendar  cal = new GregorianCalendar();
			//hxclient.scheduleAtFixedRate(sht, cal.getTime(), 10 );
			//cal = null;
			//sht = null;
			if (null==connector)
			{
				
				count++;
				log.info("创建客户端连接器["+count+"]次");
				connector = new NioSocketConnector();
				connector.setConnectTimeoutMillis(Integer.parseInt(serv.getNextSystemTimeOut())*1000);
				connector.getFilterChain().addLast(
						"codec",
						new ProtocolCodecFilter(
								new NewMoniClientCodecFactory(Charset.forName(serv.getCharset()))));
				
				//connector.setHandler(this);
				connector.getSessionConfig().setUseReadOperation(true);
				connector.getSessionConfig().setKeepAlive(true);
				connector.getSessionConfig().setReuseAddress(true);
				connector.setConnectTimeoutCheckInterval(5000);
				connector.getSessionConfig().setTcpNoDelay(true);
				
			}
		}
		return hxclient;
	}
	
	private  HxSessionObject connect(String clientAddr) { 
		 //if (session != null && session.isConnected()) { 
			/* throw new IllegalStateException( 
			 "Already connected. Disconnect first."); */
			 //return session;
		 //} 
		
		//System.out.println("寻找长连接的客户端地址["+clientAddr+"]");
		 //1.寻找可用的连接，找到就返回
		 //StringBuilder key = new StringBuilder();
		 //全部都已经连接或者没有取到事先配置的服务器地址
		 ConcurrentHashMap<String,ArrayList<HxSessionObject>> servMap = hxserver.getAvailableConn();
		
		 boolean existFlag = false;
		 HxSessionObject hso = null;
		 
		 if (servMap!=null)
		 {
			 //synchronized(servMap){
				if (servMap.containsKey(clientAddr))
				{
					 //System.out.println("编号：["+tmp+"]状态【"+servMap.get(tmp).getStatusNum()+"】");
					existFlag = true;
					 ArrayList<HxSessionObject> tArr = servMap.get(clientAddr);
					 int len = tArr.size();
					 for (int k=0;k<len;k++)
					 {
						 if (tArr.get(k).getStatusNum()==1)
						 {
							 hso = tArr.get(k);
							 hso.setStatusNum(2);
							 
							 
							 //System.out.println("找到了可用链接【"+tmp+"】");
							 
							 break;
						 }
						 else if (tArr.get(k).getStatusNum()==0)
						 {
							 /*
							 if (tArr.get(k).getSession().isConnected())
							 {
								 tArr.get(k).getSession().close(false);
							 }
							 tArr.remove(tArr.get(k));
							 log.info("关闭了核心长连接：【"+clientAddr+"】第【"+(k+1)+"】个");
							 */
						 }
					 }//for (int k=0;k<tArr.size();k++)
					 tArr = null;
				 }//if (servMap.containsKey(clientAddr))
			 //}//synchronized(servMap){
			 
		 }//if (servMap!=null)
		 
		//如果没有可用的连接,则新建一个
		if (null==hso)
		{
			//session.setAttribute("createFlag", loc);	//设置建立连接标志
			/*
			if (servMap.size()>sessionLimit)
			{
				//key = null;
				System.out.println("建立连接失败，与新监控长连接会话个数已经超过"+sessionLimit+"，不能新建连接！");
				//return null;
			}
			else
			{
			*/
				IoSession session = null;
				try {
					/*
				 	log.info("hxserver==null?"+(hxserver==null));
				 	log.info("hxserver.getArrayNextSvrIp()==null?"+(hxserver.getArrayNextSvrIp()==null));
				 	log.info("hxserver.getArrayNextSvrIp().get(0)==null?"+(hxserver.getArrayNextSvrIp().get(0)==null));
				 	
				 	log.info("hxserver.getArrayNextSvrPort()==null?"+(hxserver.getArrayNextSvrPort()==null));
				 	log.info("hxserver.getArrayNextSvrPort().get(0)==null?"+(hxserver.getArrayNextSvrPort().get(0)==null));
				 	*/
				 	
					 ConnectFuture	futureNew = connector
								.connect(new InetSocketAddress(hxserver.getArrayNextSvrIp().get(0), Integer.parseInt(hxserver.getArrayNextSvrPort().get(0))));
					 futureNew.awaitUninterruptibly();
						
						if (!futureNew.isConnected()) {
							log.info("会话future不能建立连接!直接返回null!");
							return null;
						}
						session = futureNew.getSession();
						//sessionCnt ++;
						//System.out.println("会话个数:["+servMap.size()+"]");
						//session.write(map);
					} catch (Exception ex) {
						ex.printStackTrace();
						//throw new IllegalStateException("session is already closed");
					}
				hso = new HxSessionObject();
				//int intLoc = 0;
				hso.setStatusNum(2);
				
				hso.setSession(session);
				//sessionCnt ++;
				
				
				if (existFlag==true)
				{
					//已经存在key
					servMap.get(clientAddr).add(hso);
				}
				else
				{
					ArrayList<HxSessionObject> tArr = new ArrayList<HxSessionObject>();
					tArr.add(hso);
					servMap.put(clientAddr, tArr);
				}
				
				//
				//System.out.println("找不到可用连接，重新建立新的连接，直接用连接会话:["+clientAddr+"]");
			//}
					
		}//if (null==hxSession)
		else
		{
			if (!hso.getSession().isConnected())
			{
				//重建连接前，先关掉原会话
				log.info("原会话不能连接或者忙，重新连接会话["+clientAddr+"]");
				hso.getSession().close(false);
				IoSession session = null;
				ConnectFuture futureNew = connector
				.connect(new InetSocketAddress(hxserver.getArrayNextSvrIp().get(0), Integer.parseInt(hxserver.getArrayNextSvrPort().get(0))));
				futureNew.awaitUninterruptibly();
		
				if (!futureNew.isConnected()) {
					log.info("会话future没有连接!直接返回null!");
					return null;
				}
				session = futureNew.getSession();
				//sessionCnt ++;
				hso.setSession(session);
				hso.setStatusNum(2);
			}
			
		}
		servMap = null;
		return hso;

	}
	
	
	/*
	private HxLongConnObject connect(String pkgPK,String pkgType) { 
		 
		
		//System.out.println("connect,pkgPK["+pkgPK+"]pkgType["+pkgType+"]");
		 //1.寻找可用的连接，找到就返回
		 StringBuilder key = new StringBuilder();
		 //全部都已经连接或者没有取到事先配置的服务器地址
		 ConcurrentHashMap<String,HxLongConnObject> servMap = hxserver.getAvailableConn();
		 HxLongConnObject hlc=null;
		 
		 if (servMap!=null)
		 {
			 if (null==pkgPK || "".equals(pkgPK))
			 {
				 //如果为控制类报文
				 if ("00".equals(pkgType.substring(0, 2)))
				 {
					 //建链接
					 IoSession session = null;
					 try {
						 	NioSocketConnector connector = new NioSocketConnector();
							//connector.setConnectTimeoutMillis(Integer.parseInt(hxserver.getNextSystemTimeOut())*1000);
							connector.getFilterChain().addLast(
									"codec",
									new ProtocolCodecFilter(
											new NewMoniClientCodecFactory(Charset.forName(hxserver.getCharset()))));
							
							//connector.setHandler(this);
							connector.getSessionConfig().setUseReadOperation(true);
							future = connector
									.connect(new InetSocketAddress(hxserver.getArrayNextSvrIp().get(0), Integer.parseInt(hxserver.getArrayNextSvrPort().get(0))));
							future.awaitUninterruptibly();
							
							if (!future.isConnected()) {
								return null;
							}
							session = future.getSession();
							sessionCnt ++;
							System.out.println("会话个数:["+sessionCnt+"]");
							//session.write(map);
						} catch (Exception ex) {
							ex.printStackTrace();
							//throw new IllegalStateException("session is already closed");
						}
						//session.setAttribute("createFlag", loc);	//设置建立连接标志
						if (sessionCnt>sessionLimit)
						{
							key = null;
							System.out.println("建立连接失败，与新监控长连接会话个数已经超过"+sessionLimit+"，不能新建连接！");
							
						}
						else
						{
							hlc = new HxLongConnObject();
							int intLoc = 0;
							hlc.setIp(hxserver.getArrayNextSvrIp().get(intLoc));
							hlc.setPort(Integer.parseInt(hxserver.getArrayNextSvrPort().get(intLoc)));
							hlc.setSession(session);
							//hlc.setLinkGroupId(co.getCtrlPkgBody().substring(9));
							hlc.setStatusNum(2);
							key.delete(0, key.length());
							key.append(hlc.getIp());
							key.append("_");
							key.append(hlc.getPort());
							key.append("_");
							key.append(sessionCnt);
							//hlc.setKeyStr(key.toString());
							hxserver.getAvailableConn().put(key.toString(), hlc);
							//System.out.println("放进去key["+key.toString()+"]");
							System.out.println("直接用连接会话:["+key.toString()+"]");
						}
				 }//if ("00".equals(pkgType))
				 else
				 {
					 //找一个可用的链接就行
					 Set<String> keys = servMap.keySet();
					 for (String tmp:keys)
					 {
						 //状态为可使用
						 //System.out.println("编号：["+tmp+"]状态【"+servMap.get(tmp).getStatusNum()+"】");
						 if (servMap.get(tmp).getStatusNum()==1)
						 {
							 keys = null;
							 servMap.get(tmp).setStatusNum(2);
							 System.out.println("找到了可用链接【"+tmp+"】");
							 hlc = servMap.get(tmp);
							 break;
						 }
					 }
					 keys = null;
				 }//if ("00".equals(pkgType)) else
				 
			 }//if (null==pkgPK)
			 else
			 {
				 //数据报文,找到包标签相符的对应连接
				 
				 Set<String> keys = servMap.keySet();
				 for (String tmp:keys)
				 {
					 //状态为可使用
					 System.out.println("编号：["+tmp+"]keystr【"+servMap.get(tmp).getKeyStr()+"】数据包标签【"+pkgPK+"】");
					 if (pkgPK.equals(servMap.get(tmp).getKeyStr()))
					 {
						 keys = null;
						 servMap.get(tmp).setStatusNum(2);
						 System.out.println("找到了数据包文标签["+pkgPK+"]可用链接【"+tmp+"】");
						 hlc = servMap.get(tmp);
						 break;
					 }
				 }
				 keys = null;

			 }//if (null==pkgPK) else
		 }//if (servMap!=null)
		 

		if (null==hlc)
		{
			//先查询现有连接中是否有keyStr为空的连接，再建立新的连接
			boolean hasNullConn = false;
			 Set<String> keys = servMap.keySet();
			 for (String tmp:keys)
			 {
				 //状态为可使用
				 //System.out.println("编号：["+tmp+"]keystr【"+servMap.get(tmp).getKeyStr()+"】数据包标签【"+pkgPK+"】");
				 if (null==servMap.get(tmp).getKeyStr())
				 {
					 hasNullConn = true;
					 hlc = servMap.get(tmp);
					 if (null!=pkgPK && !"".equals(pkgPK.trim()))
					 {
						System.out.println("重建连接时，将原空keystr放进去封包标签["+pkgPK+"]");
						hlc.setKeyStr(pkgPK);
					 }
					 break;
				 }
			 }
			 keys = null;
			 if (!hasNullConn)
			 {
				//建链接
				 IoSession session = null;
				 try {
					 	NioSocketConnector connector = new NioSocketConnector();
						//connector.setConnectTimeoutMillis(Integer.parseInt(hxserver.getNextSystemTimeOut())*1000);
						connector.getFilterChain().addLast(
								"codec",
								new ProtocolCodecFilter(
										new NewMoniClientCodecFactory(Charset.forName(hxserver.getCharset()))));
						
						//connector.setHandler(this);
						connector.getSessionConfig().setUseReadOperation(true);
						future = connector
								.connect(new InetSocketAddress(hxserver.getArrayNextSvrIp().get(0), Integer.parseInt(hxserver.getArrayNextSvrPort().get(0))));
						future.awaitUninterruptibly();
						
						if (!future.isConnected()) {
							return null;
						}
						session = future.getSession();
						sessionCnt ++;
						System.out.println("会话个数:["+sessionCnt+"]");
						//session.write(map);
					} catch (Exception ex) {
						ex.printStackTrace();
						//throw new IllegalStateException("session is already closed");
					}
					//session.setAttribute("createFlag", loc);	//设置建立连接标志
					if (sessionCnt>sessionLimit)
					{
						key = null;
						System.out.println("建立连接失败，与新监控长连接会话个数已经超过"+sessionLimit+"，不能新建连接！");
						//return null;
					}
					else
					{
						hlc = new HxLongConnObject();
						int intLoc = 0;
						hlc.setIp(hxserver.getArrayNextSvrIp().get(intLoc));
						hlc.setPort(Integer.parseInt(hxserver.getArrayNextSvrPort().get(intLoc)));
						hlc.setSession(session);
						//hlc.setLinkGroupId(co.getCtrlPkgBody().substring(9));
						hlc.setStatusNum(2);
						key.delete(0, key.length());
						key.append(hlc.getIp());
						key.append("_");
						key.append(hlc.getPort());
						key.append("_");
						key.append(sessionCnt);
						if (null!=pkgPK && !"".equals(pkgPK.trim()))
						{
							System.out.println("重建连接时，放进去封包标签["+pkgPK+"]");
							hlc.setKeyStr(pkgPK);
						}
						
						hxserver.getAvailableConn().put(key.toString(), hlc);
						//
						System.out.println("找不到可用连接，重新建立新的连接，直接用连接会话:["+key.toString()+"]");
					}
			 }//if (!hasNullConn)
		}//if (null==hlc)
		servMap = null;
		 return hlc;

	}
	
	*/

	/**
	 * 发送报文,组装信息发到核心新监控
	 * @param nmo
	 * @return
	 */
	public  void sendMsg(NewMoniObject nmo,IoSession session,String clientAddr,String recNodeNo,
			String recRuleNo,String beginTime,String filesvrTestEnv,boolean recFlag) {
		
		HxSessionObject hlc=connect(clientAddr);
		if (null==hlc)
		{
			log.info("无可用的到核心新监控的连接会话，返回null!");
			return ;
		}
		
		//2.发送到核心
		byte[] respTmp = null;
		respTmp = new byte[nmo.getInnerBuffer().limit()];
		
		nmo.getInnerBuffer().flip().get(respTmp);
		
		nmo.getInnerBuffer().clear();
		nmo.getInnerBuffer().put(respTmp);
		
		//3.打印日志
		try {
			log.info("发往核心报文【"+new String(respTmp,hxserver.getCharset())+"】");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		hlc.getSession().write(nmo.getInnerBuffer().flip()).awaitUninterruptibly();
		
		
		//4.同步取回响应报文
		ReadFuture rf = hlc.getSession().read();
		NewMoniObject nmo1 = null;
		
		String endTime = null;
		
		/*
		log.info("等待核心响应！到核心新监控超时秒数为【"+hxserver
				.getNextSystemTimeOut()+"】秒");
		*/
		
		if (rf.awaitUninterruptibly(Integer.parseInt(hxserver
				.getNextSystemTimeOut()), TimeUnit.SECONDS)) {
			nmo1 = (NewMoniObject) rf.getMessage();
			if (null == nmo1) {
				log.info("返回报文业务对象为空！到核心新监控超时秒数为【"+hxserver
						.getNextSystemTimeOut()+"】秒");
				hlc.setStatusNum(1);   //不要重连会话，重新使用，20141229
			} else {
				hlc.setStatusNum(1);
				//System.out.println("innerBuffer:"+nmo1.getInnerBuffer().toString());
				respTmp = new byte[nmo1.getInnerBuffer().limit()];
				nmo1.getInnerBuffer().flip().get(respTmp);
				/*
				for (int i=0;i<respTmp.length;i++)
				{
					if (i!=0 && i%15==0)
					{
						System.out.print("\n");
					}
					System.out.print(" "+Integer.toHexString(respTmp[i]));
					
				}
				*/
				
			}
		}
		else
		{
			log.info("等待核心新监控返回超时,clientAddr["+session.getId()+"]超时秒数:["+hxserver.getNextSystemTimeOut()+"]，系统等不到返回，将返null!");
			hlc.setStatusNum(0);   //需要重连会话
			//hlc.getSession().close(false);
			//hlc.setSession(createSession());
		}
		

		//3.返回ESB
		//log.info("返回ESB,null!=respTmp:["+(null!=respTmp)+"]");
		if (null!=respTmp)
		{
			try {
				returnESB(respTmp,session,hxserver);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//4.录制报文
			if (recFlag==true)
			{
				SystemTime endST = new SystemTime();
				endTime = endST.getDateStr()+endST.getRigorTime();
				endST = null;
				try {
					recPkg(recNodeNo,recRuleNo,
							nmo,nmo1,hxserver,beginTime,endTime,
							filesvrTestEnv
							);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		respTmp = null;
		hlc = null;
		
		rf = null;
		nmo1 = null;
		endTime = null;
		
		//hxserver = null;
		
	}


	//返回ESB
	public static void returnESB(byte[] respTmp,IoSession session,
			SubXML_HXServer subServer) throws UnsupportedEncodingException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(new String(respTmp, subServer.getCharset()));
		session.write(sb.toString());
		//log.info("连接模式【"+subServer.getLinkMode()+"】");
		if ("1".equals(subServer.getLinkMode())) {
			// 短连接
			session.close(false);
		}
		else
		{
			//log.info("subServer.getArrayLastSvrIp().get(0)==null["+(subServer.getArrayLastSvrIp().get(0)==null)+"]");
			//log.info("session.getRemoteAddress()==null["+(session.getRemoteAddress()==null)+"]");
			if (session.getRemoteAddress().toString().indexOf(subServer.getTmsIP())!=-1)
			{
				//log.info("session.getRemoteAddress().toString():___"+session.getRemoteAddress().toString());
				//log.info("subServer.getArrayLastSvrIp().get(0):___"+subServer.getArrayLastSvrIp().get(0));
				//log.info("不是指定上游系统");
				//此会话不是指定上游系统，返回后就关掉,用于回归测试
				session.close(false);
			}
		}
		//sb.delete(0, sb.length());
		sb = null;
	}

	/**
	 * 录制报文
	 * @param respTmp
	 * @param session
	 * @param recNodeNo
	 * @param recRuleNo
	 * @param co
	 * @param nmo
	 * @param subServer
	 * @param beginTime
	 * @param endTime
	 * @param filesvrTestEnv
	 * @throws UnsupportedEncodingException
	 */
	public static void recPkg(String recNodeNo,String recRuleNo,
			NewMoniObject co,NewMoniObject nmo,SubXML_HXServer subServer,String beginTime,String endTime,
			String filesvrTestEnv
			) throws UnsupportedEncodingException
	{
		
			// log.info("开始准备录制报文数据");
			// 1.请求报文头
			StringBuilder reqHead = new StringBuilder();
			reqHead.append(co.getVersionNo());
			reqHead.append(co.getPkgHeadLen());
			reqHead.append(co.getTyp());
			reqHead.append(co.getPackFlg());
			reqHead.append(co.getLeftTimeOut());
			reqHead.append(co.getMD5());
			reqHead.append(co.getDataLen());
			reqHead.append(co.getEncptTyp());
			reqHead.append(co.getTxCd());
	
			// 2.请求报文体
			StringBuilder reqBody = new StringBuilder();
			reqBody.append(co.getFmtData());
	
			// 3.响应报文头
			StringBuilder rspHead = new StringBuilder();
			rspHead.append(nmo.getVersionNo());
			rspHead.append(nmo.getPkgHeadLen());
			rspHead.append(nmo.getTyp());
			rspHead.append(nmo.getPackFlg());
			rspHead.append(nmo.getLeftTimeOut());
			rspHead.append(nmo.getMD5());
			rspHead.append(nmo.getDataLen());
			rspHead.append(nmo.getEncptTyp());
			rspHead.append(nmo.getTxCd());
	
			// 4.响应报文体
			StringBuilder rspBody = new StringBuilder();
			rspBody.append(nmo.getFmtData());
			// log.info("保存核心报文，请求体["+reqBody.toString()+"]");
			// log.info("保存核心报文，响应体["+rspBody.toString()+"]");
			// log.info("保存核心报文，请求头["+reqHead.toString()+"]");
			// log.info("保存核心报文，响应头["+rspHead.toString()+"]");
			TMSThreadPool.addNewTask(
			new COMM01(reqBody.toString(), rspBody.toString(), co
					.getTxCd(), subServer, recNodeNo, recRuleNo, beginTime,
					endTime, filesvrTestEnv, reqHead.toString(), rspHead
							.toString()));
			//.start();
		
			reqHead = null; // 请求头
			reqBody = null; // 请求体
			rspHead = null; // 响应头
			rspBody = null;	// 响应体
	
	}

}
