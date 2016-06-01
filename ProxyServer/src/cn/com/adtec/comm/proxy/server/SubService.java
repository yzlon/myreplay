package cn.com.adtec.comm.proxy.server;

import java.io.IOException;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.adtec.comm.proxy.tools.FileTool;

/**
 * 用于管理其它代理节点的服务
 * 设计成单例模式
 * @author tangjb
 *
 */
public class SubService {
	private final static Logger log = LoggerFactory	.getLogger(SubService.class);
	
	private static boolean startedFlag = false;				//启动成功标志
	

	private static SubService subservice= null;    //全局唯一启动的实例
	
	//存放所有代理节点信息，key为节点编号，value为对该代理节点属性的描述串
	private ConcurrentHashMap<String,String> proxyNodeMap = new ConcurrentHashMap<String,String>();	
	
	//记录已启动的代理节点，key为节点编号，value为对应IoAcceptor类实例
	private ConcurrentHashMap<String,AbstractSubServer> startedNodeMap = new ConcurrentHashMap<String,AbstractSubServer>();
	
	//需要启动的代理节点集合,用于从文件或者别的地方传送需要启动的节点过来
	private ConcurrentSkipListSet<String> needToStart = new ConcurrentSkipListSet<String>();
	
	//已启动需要关闭的代理节点集合,用于从文件或者别的地方传送需要启动的节点过来
	private ConcurrentSkipListSet<String> needToShut = new ConcurrentSkipListSet<String>();
	
	public ConcurrentSkipListSet<String> getNeedToStart() {
		return needToStart;
	}

	public void setNeedToStart(ConcurrentSkipListSet<String> needToStart) {
		this.needToStart = needToStart;
	}

	public ConcurrentSkipListSet<String> getNeedToShut() {
		return needToShut;
	}

	public void setNeedToShut(ConcurrentSkipListSet<String> needToShut) {
		this.needToShut = needToShut;
	}

	private SubService()
	{
		//暂时啥也不做
		
	}
	
	/**
	 * 加载信息文件到内存
	 * @return
	 */
	public boolean loadFileInfo()
	{
		//1.从文件里读取以前保存的节点信息
		try {
			this.setProxyNodeMap(FileTool.readProxyNodeFile());
			this.setNeedToStart(FileTool.readStartedProxyNodeFile());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	


	public ConcurrentHashMap<String, String> getProxyNodeMap() {
		return proxyNodeMap;
	}

	public void setProxyNodeMap(ConcurrentHashMap<String, String> proxyNodeMap) {
		this.proxyNodeMap = proxyNodeMap;
	}

	public ConcurrentHashMap<String, AbstractSubServer> getStartedNodeMap() {
		return startedNodeMap;
	}

	public void setStartedNodeMap(ConcurrentHashMap<String, AbstractSubServer> startedNodeMap) {
		this.startedNodeMap = startedNodeMap;
	}

	public static boolean isStartedFlag() {
		return startedFlag;
	}

	public static void setStartedFlag(boolean startedFlag) {
		SubService.startedFlag = startedFlag;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static SubService getInstance()
	{
		
			if (null==subservice)
			{
				subservice = new SubService();
				setStartedFlag(true);
			}
			return subservice;
		
	}

	/**
	 * 启动新增的任务，需要比对记录的已启动任务MAP
	 * @param needToStart
	 * @return
	 * @throws IOException 
	 */
	public boolean start() throws IOException
	{
		if (needToStart==null || needToStart.size()==0)
		{
			log.error("sub service_____需要启动的集合为空对象或者集合内容为空！_______The set need to start is NULL or size is 0!");
			return false;
		}
		//log.error("有需要启动的节点!");
		//遍历需要启动的集合
		boolean flag = false;
		int failCount=0;
		int succCount=0;
		for (Iterator<String> ite = needToStart.iterator();ite.hasNext();)
		{
			String tmp = ite.next();
			log.info("......需要启动...["+tmp+"]");
			if (this.getStartedNodeMap().containsKey(tmp))
			{
				//表明已经启动了，不用管它,继续
				//log.info("哦，原先已经启动...["+tmp+"]");
			}
			else
			{
				log.info("即将启动...["+tmp+"]");
				flag = startOneNode(tmp);
				if (flag)
				{
					succCount++;
				}
				else
				{
					failCount++;
				}
				
			}
			tmp = null;
		}
		log.info("sub service staring successed ["+succCount+"]__________failed["+failCount+"]");
		/*if (failCount==0)
		{
			setStartedFlag(false);
		}
		else
		{
			setStartedFlag(true);
		}*/
		return flag;
	}
	
	/**
	 * 启动一个节点
	 * @param tmp	节点编号
	 * @return
	 * @throws IOException 
	 */
	private boolean startOneNode(String tmp) throws IOException
	{
		if (null==tmp)
			return false;
		boolean flag = false;
		log.info("启动节点编号：["+tmp+"]");
		//Set<String> keys = this.getProxyNodeMap().keySet();
/*		for (String kk:keys)
		{
			log.info("有这些key["+kk+"]");
		}*/
		if (this.getProxyNodeMap().containsKey(tmp))
		{
			//找到节点信息
			/*
			 * 各节点信息顺序如下：
			 * 节点编号|@|节点名称|@|上游系统IP |@|下游系统IP|@|下游系统端口|@|下游系统通讯超时秒数|@|
			 * 代理节点启动端口|@|代理启动开关标志|@|代理节点“直连/仿真”模式|@|报文种类|@|
			 * 下游系统交易报文字符集|@|TMS仿真服务地址|@|TMS仿真服务端口|@|TMS应用服务名称|@|
			 * TMS交易报文字符集|@|收取原始（请求响应）报文规则				
			 */
			String rule = this.getProxyNodeMap().get(tmp);
			String[] items = rule.split(FileTool.getSplitStr());
			log.info("____"+items[1]+" begin to start sub server....................");
			log.info("["+items[9]+"]");
			AbstractSubServer sss = null;
			if (items[9].trim().equalsIgnoreCase("SOAP"))
			{
				sss = new SubSOAPServer();
			}
			else if (items[9].trim().equalsIgnoreCase("FIX") ||items[9].trim().equalsIgnoreCase("FIXSEP"))
			{
				sss = new SubFLOWServer();
			}
			else if (items[9].trim().equalsIgnoreCase("XML"))
			{
				sss = new SubXMLServer();
			}
			else if (items[9].trim().equalsIgnoreCase("XML_HX"))
			{
				sss = new SubXML_HXServer();
			}
			else
			{
				//防止空指针异常，默认设置为流式
				sss = new SubFLOWServer();
			}
			
			//log.info("["+rule+"]");
			sss.refreshSubServer(rule);
			sss.setPkgCollectRules(FileTool.readAllRuleFile(tmp));
			sss.setKeyWordVal(FileTool.readCollectRuleFile(tmp));
			
			sss.setPkgSimRules(FileTool.readAllRuleFile(tmp+"_sim"));
			sss.setKeyWordValSim(FileTool.readSimRuleFile(tmp));
			
			flag = sss.startSubServer();
			if (flag)
			{
				this.getStartedNodeMap().put(tmp, sss);
				log.info("____"+items[1]+" successed to start....................");
			}
		}
		else
		{
			log.info("_____starting Node ["+tmp+"] failure,because no node info existed!");
			return false;
		}
		
		
		return flag;
	}
	
	
	/**
	 * 关闭子服务
	 * @param needToShut
	 * @return
	 */
	public boolean shut()
	{
		if (needToShut==null || needToShut.size()==0)
		{
			//log.error("_____需要关闭的集合为空对象或者集合内容为空！_______The set need to shut is NULL or size is 0!");
			return false;
		}
		
		//遍历需要关闭的集合
		boolean flag = false;
		int failCount=0;
		int succCount=0;
		for (Iterator<String> ite = needToShut.iterator();ite.hasNext();)
		{
			String tmp = ite.next();
			//log.info("需要停止["+tmp+"]");
			if (this.getStartedNodeMap().containsKey(tmp))
			{
				//表明已经启动了，需要关闭
				//log.info("已经启动，即将停止["+tmp+"]");
				flag = this.getStartedNodeMap().get(tmp).shutSubServer();
				if (flag)
				{
					//关闭成功，则将已启动节点从“已启动节点映射表”中移除
					this.getStartedNodeMap().remove(tmp);
					succCount++;
					//log.info("shut server node no:["+tmp+"]__________succeeded!");
				}
				else
				{
					failCount++;
					log.info("shut server node no:["+tmp+"]__________failed!");
				}
			}
			else
			{
				//表明从未启动，不需要关闭
				//log.info("从未启动，不需要关闭["+tmp+"]");
			}
			tmp = null;
		}
		log.info("shuting successed ["+succCount+"]__________failed["+failCount+"]");
		
		return flag;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
