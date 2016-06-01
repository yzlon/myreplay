package cn.com.adtec.comm.proxy.tools;

import java.util.concurrent.ArrayBlockingQueue;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TMSThreadPool {

	private static ThreadPoolExecutor pool;

	//private final static Logger log = LoggerFactory	.getLogger(HxThreadPool.class);
	private static int corePoolSize=4;
	private static int maximumPoolSize=1000;
	private static int keepAliveTime = 30;
	private static final Logger log = LoggerFactory.getLogger(TMSThreadPool.class);
	
	private TMSThreadPool()
	{
		//this.hxserver = serv;
	}
	
	public static boolean addNewTask(Runnable runable)
	{
		//初始化线程池
		if (null==pool)
		{
			log.info("第一次建立线程池：");
			pool = new  ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES,new ArrayBlockingQueue<Runnable>(100));
			pool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
			pool.allowCoreThreadTimeOut(true);
		}
		pool.execute(runable);
		//log.info("池中线程最多时容量【"+pool.getLargestPoolSize()+"】已完成任务计数【"+pool.getCompletedTaskCount()+"】活动线程计数【"+pool.getActiveCount()+"】总容量【"+pool.getMaximumPoolSize()+"】允许核心线程超时【"+pool.allowsCoreThreadTimeOut()+"】");
		if (pool.isTerminating()||pool.isTerminated()||pool.isShutdown())
		{
			pool.purge();
			pool = null;
		}
		return true;
	}
	
	
	
}
