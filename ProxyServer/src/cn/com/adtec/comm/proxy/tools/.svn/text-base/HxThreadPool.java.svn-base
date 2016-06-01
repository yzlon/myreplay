package cn.com.adtec.comm.proxy.tools;

import java.util.concurrent.ArrayBlockingQueue;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cn.com.adtec.comm.proxy.bean.NewMoniObject;
import cn.com.adtec.comm.proxy.client.HXClient;
import cn.com.adtec.comm.proxy.server.SubXML_HXServer;

public class HxThreadPool {

	private static ThreadPoolExecutor pool;

	//private final static Logger log = LoggerFactory	.getLogger(HxThreadPool.class);
	private static int corePoolSize=30;
	private static int maximumPoolSize=1000;
	private static int keepAliveTime = 1;
	private static final Logger log = LoggerFactory.getLogger(HxThreadPool.class);
	private static int count=0;
	
	private HxThreadPool()
	{
		//this.hxserver = serv;
	}
	
	public static boolean addNewTask(final SubXML_HXServer serv,final NewMoniObject nmo,final IoSession session,final String clientAddr,final String recNodeNo,
			final String recRuleNo,final String beginTime,final String filesvrTestEnv,final boolean recFlag)
	{
		//初始化线程池
		if (null==pool)
		{
			log.info("第"+(++count)+"次建立线程池：");
			pool = new  ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.HOURS,new ArrayBlockingQueue<Runnable>(200));
			pool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
			pool.allowCoreThreadTimeOut(true);
		}
		pool.execute(new Runnable() {
            @Override
            public void run() {
                HXClient.getInstance(serv).sendMsg(nmo, session, clientAddr, recNodeNo, recRuleNo, beginTime, filesvrTestEnv, recFlag);
            }
        });
		//log.info("池中线程最多时容量【"+pool.getLargestPoolSize()+"】已完成任务计数【"+pool.getCompletedTaskCount()+"】活动线程计数【"+pool.getActiveCount()+"】总容量【"+pool.getMaximumPoolSize()+"】允许核心线程超时【"+pool.allowsCoreThreadTimeOut()+"】");
		if (pool.isTerminating()||pool.isTerminated()||pool.isShutdown())
		{
			pool.purge();
			pool = null;
		}
		return true;
	}
	
	
	
}
