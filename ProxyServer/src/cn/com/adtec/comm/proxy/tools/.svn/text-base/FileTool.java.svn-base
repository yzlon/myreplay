package cn.com.adtec.comm.proxy.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.FileWriter;

import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.mina.core.buffer.IoBuffer;

public class FileTool {
	private static final String splitStr = "\\|@\\|";
	private static final String proxyNodeFileName = "proxyNode.txt";
	private static final String startedNodeFileName = "startedNode.txt";
	
	
	public static String getSplitStr() {
		return splitStr;
	}
/*
	public static void setSplitStr(String splitStr) {
		FileTool.splitStr = splitStr;
	}
*/
	/**
	 * 返回运行JAR包时的当前路径（绝对路径）
	 * @return
	 */
	public static String getAddress() {
		File file = new File("");
		String str = file.getAbsolutePath();
		if (!str.endsWith(File.separator))
		{
			str = str + File.separator;
		}
		file = null;	
		return str;
	}

	/**
	 * 返回给出文件名所在的绝对路径
	 * @param filename
	 * @return
	 */
	public static String getAbsoluteFile(String filename)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getAddress());
		sb.append(filename);
		return sb.toString();
	}
	
	/**
	 * 读取通讯代理节点文件内容
	 * @return
	 * @throws IOException
	 */
	public static ConcurrentHashMap<String,String>  readProxyNodeFile() throws IOException
	{
		//1.获取本地文件
		StringBuilder fileName = new StringBuilder();
		fileName.append(FileTool.getAddress());  //绝对路径
		fileName.append(proxyNodeFileName);
		
		ConcurrentHashMap<String,String> proxyNodeMap = new ConcurrentHashMap<String,String>();
		
			
		//如果本地文件存在，则先读取文件内容
		File localFile = new File(fileName.toString());
		if (localFile.exists() && localFile.isFile())
		{
			//存在，且是文件，则读取内容
			BufferedReader in = new BufferedReader(new FileReader(localFile));
			String tmp = null;
			while((tmp=in.readLine())!=null)
			{
				proxyNodeMap.put(tmp.split(splitStr)[0],tmp);
			}
			in.close();
			in = null;
			tmp = null;
		}//if (localFile.exists() && localFile.isFile())
		localFile = null;
		fileName = null;
		return proxyNodeMap;
	}
	
	/**
	 * 写入代理节点信息到文本文件
	 * @param proxyNodeMap
	 * @return
	 */
	public static boolean writeProxyNodeFile(ConcurrentHashMap<String,String> proxyNodeMap) 
	{
		boolean sucFlag = true;
		//1.获取本地文件
		StringBuilder fileName = new StringBuilder();
		fileName.append(FileTool.getAddress());  //绝对路径
		fileName.append(proxyNodeFileName);
		
		//直接覆盖本地文件
		File localFile = new File(fileName.toString());
		BufferedWriter out=null;
		try {
			out = new BufferedWriter(new FileWriter(localFile));
			Collection<String> al = (Collection<String>)proxyNodeMap.values();
			for (Iterator<String> ite=al.iterator();ite.hasNext();)
			{
				out.write(ite.next()+"\n");
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sucFlag = false;
		}finally
		{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		out = null;
		localFile = null;
		fileName = null;
		return sucFlag;
	}
	
	
	/**
	 * 读取已启动的节点编号
	 * @return
	 * @throws IOException
	 */
	public static ConcurrentSkipListSet<String>  readStartedProxyNodeFile() throws IOException
	{
		//1.获取本地文件
		StringBuilder fileName = new StringBuilder();
		fileName.append(FileTool.getAddress());  //绝对路径
		fileName.append(startedNodeFileName);
		
		ConcurrentSkipListSet<String> proxyNodeMap = new ConcurrentSkipListSet<String>();
		
			
		//如果本地文件存在，则先读取文件内容
		System.out.println("本地文件:["+fileName.toString()+"]");
		File localFile = new File(fileName.toString());
		if (localFile.exists() && localFile.isFile())
		{
			//存在，且是文件，则读取内容
			BufferedReader in = new BufferedReader(new FileReader(localFile));
			String tmp = null;
			while((tmp=in.readLine())!=null)
			{
				proxyNodeMap.add(tmp);
			}
			in.close();
			in = null;
			tmp = null;
		}//if (localFile.exists() && localFile.isFile())
		localFile = null;
		fileName = null;
		return proxyNodeMap;
	}
	
	/**
	 * 将已启动的节点编号写入文件
	 * @param proxyNodeMap
	 * @return
	 */
	public static boolean writeStartedNodeFile(ConcurrentSkipListSet<String> proxyNodeMap) 
	{
		boolean sucFlag = true;
		//1.获取本地文件
		StringBuilder fileName = new StringBuilder();
		fileName.append(FileTool.getAddress());  //绝对路径
		fileName.append(startedNodeFileName);
		
		//直接覆盖本地文件
		File localFile = new File(fileName.toString());
		BufferedWriter out=null;
		try {
			out = new BufferedWriter(new FileWriter(localFile));
			
			for (Iterator<String> ite = proxyNodeMap.iterator();ite.hasNext();)
			{
				out.write(ite.next()+"\n");
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sucFlag = false;
		}finally
		{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		out = null;
		localFile = null;
		fileName = null;
		return sucFlag;
	}
	
	
	
	/**
	 * 读取指定节点编号的报文截取规则所有信息
	 * @return
	 * @throws IOException
	 */
	public static String[][]  readAllRuleFile(String nodeNo) throws IOException
	{
		//1.获取本地文件
		StringBuilder fileName = new StringBuilder();
		fileName.append(FileTool.getAddress());  //绝对路径
		fileName.append(nodeNo);
		fileName.append(".txt");

		String[][] contents = null;
		
			
		//如果本地文件存在，则先读取文件内容
		File localFile = new File(fileName.toString());
		if (localFile.exists() && localFile.isFile())
		{
			//存在，且是文件，则读取内容
			BufferedReader in = new BufferedReader(new FileReader(localFile));
			ArrayList<String> fileContent = new ArrayList<String>();
			String tmp = null;
			while((tmp=in.readLine())!=null)
			{
				if (!"".equals(tmp))
				{
					fileContent.add(tmp);
				}
				//proxyNodeMap.put(tmp.split(splitStr)[0],tmp);
			}
			
			contents = new String[fileContent.size()][];
			for (int i=0;i<contents.length;i++)
			{
				contents[i] = fileContent.get(i).split(splitStr);
			}
			
			in.close();
			in = null;
			tmp = null;
			fileContent = null;
		}//if (localFile.exists() && localFile.isFile())
		localFile = null;
		fileName = null;
		return contents;
	}

	/**
	 * 读取指定节点编号的报文截取规则所有信息,每条记录不拆分
	 * @return
	 * @throws IOException
	 */
	public static String[]  readAllRuleFileNotSplit(String nodeNo,String charset) throws IOException
	{
		//1.获取本地文件
		StringBuilder fileName = new StringBuilder();
		fileName.append(FileTool.getAddress());  //绝对路径
		fileName.append(nodeNo);
		fileName.append(".txt");

		String[] contents = null;
		
			
		//如果本地文件存在，则先读取文件内容
		File localFile = new File(fileName.toString());
		if (localFile.exists() && localFile.isFile())
		{
			//存在，且是文件，则读取内容
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(localFile));
			ArrayList<String> fileContent = new ArrayList<String>();
			String tmp = null;
			IoBuffer buf = IoBuffer.allocate(5).setAutoExpand(true);
			byte b = (byte)0;
			while((b=(byte)in.read())!=-1)
			{
				//System.out.print(Integer.toHexString(b));
				//System.out.print(" ");
				if (b==0x0A || b==0x0D)
				{
					buf.flip();
					tmp = new String(buf.array(),charset);
					fileContent.add(tmp.trim());
					//System.out.println("\n.....["+tmp+"]");
					buf.clear();
				}
				else
				{
					buf.put(b);
				}
				//proxyNodeMap.put(tmp.split(splitStr)[0],tmp);
			}
			//System.out.println("last:"+b);
			contents = new String[fileContent.size()];
			for (int i=0;i<contents.length;i++)
			{
				contents[i] = fileContent.get(i);
			}
			
			in.close();
			in = null;
			tmp = null;
			buf = null;
			fileContent = null;
		}//if (localFile.exists() && localFile.isFile())
		localFile = null;
		fileName = null;
		return contents;
	}
	
	/**
	 * 只获取每个录制规则批次号对应的关键字"KEY-VAL"对照表
	 * 是否发往仿真（直连仿真）开关标志|@|是否录制报文开关标志|@|节点编号|@|节点名称|@|
	 * 原始交易报文种类|@|分隔符|@|录制规则批次号|@|关键字=关键值;关键字1=关键值1(或者“[0,5]=关键字2;[7]=关键字3”) 
	 * |@|是否有返回文件标志|@|文件服务器地址|@|文件服务器端口|@|文件传输协议|@|文件名存放位置|@|
	 * 文件传输用户名|@|文件传输密码
	 * @param nodeNo
	 * @return
	 * @throws IOException
	 */
	
	public static ConcurrentHashMap<String, HashMap<String, String>>  readCollectRuleFile(String nodeNo) throws IOException
	{
		//1.获取本地文件
		StringBuilder fileName = new StringBuilder();
		fileName.append(FileTool.getAddress());  //绝对路径
		fileName.append(nodeNo);
		fileName.append(".txt");

		ConcurrentHashMap<String, HashMap<String, String>> contents = new ConcurrentHashMap<String, HashMap<String, String>>();
		
			
		//如果本地文件存在，则先读取文件内容
		File localFile = new File(fileName.toString());
		if (localFile.exists() && localFile.isFile())
		{
			//存在，且是文件，则读取内容
			BufferedReader in = new BufferedReader(new FileReader(localFile));
			String tmp = null;

			while((tmp=in.readLine())!=null)
			{
				String[] items = tmp.split(splitStr);
				if (items.length>7)
				{
					String[] keyval = items[7].split(";");
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
					contents.put(items[6].trim(), keyvalMap);
					keyval = null;
					keyvalMap = null;
				}
				items = null;
				
			}
			
			in.close();
			in = null;
			tmp = null;
		}//if (localFile.exists() && localFile.isFile())
		localFile = null;
		fileName = null;
		return contents;
	}

	/**
	 * 读取仿真规则文件
	 */
	public static ConcurrentHashMap<String, HashMap<String, String>>  readSimRuleFile(String nodeNo) throws IOException
	{
		//1.获取本地文件
		StringBuilder fileName = new StringBuilder();
		fileName.append(FileTool.getAddress());  //绝对路径
		fileName.append(nodeNo);
		fileName.append("_sim.txt");

		ConcurrentHashMap<String, HashMap<String, String>> contents = new ConcurrentHashMap<String, HashMap<String, String>>();
		
			
		//如果本地文件存在，则先读取文件内容
		File localFile = new File(fileName.toString());
		if (localFile.exists() && localFile.isFile())
		{
			//存在，且是文件，则读取内容
			BufferedReader in = new BufferedReader(new FileReader(localFile));
			String tmp = null;

			while((tmp=in.readLine())!=null)
			{
				String[] items = tmp.split(splitStr);
				if (items.length>17)
				{
					String[] keyval = items[7].split(";");
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
					fileName.delete(0, fileName.length());
					fileName.append(items[2]);
					fileName.append("_");
					fileName.append(items[6]);
					fileName.append("_");
					fileName.append(items[17]);

					contents.put(fileName.toString(), keyvalMap);
					keyval = null;
					keyvalMap = null;
				}
				items = null;
				
			}
			
			in.close();
			in = null;
			tmp = null;
		}//if (localFile.exists() && localFile.isFile())
		localFile = null;
		fileName = null;
		return contents;
	}
	
	/**
	 * 将报文截取规则追加写入文件
	 * @param nodeNo		节点编号
	 * @param rules			报文截取规则
	 * @param appendFlag	是否追加，true-追加，false-覆盖
	 * @return
	 */
	public static boolean writeAllRuleFile(String nodeNo,String[] rules,boolean appendFlag,String charset) 
	{
		boolean sucFlag = false;
		//1.获取本地文件
		StringBuilder fileName = new StringBuilder();
		fileName.append(FileTool.getAddress());  //绝对路径
		fileName.append(nodeNo);
		fileName.append(".txt");
		
		//直接追加
		File localFile = new File(fileName.toString());
				
		BufferedOutputStream out=null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(localFile,appendFlag));
			
			for (int i=0;i<rules.length;i++)
			{
				//System.out.println("原串：["+rules[i]+"]");
				out.write(rules[i].getBytes(charset));
				out.write("\n".getBytes());
				out.flush();
			}
			sucFlag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally
		{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		out = null;
		localFile = null;
		fileName = null;
		return sucFlag;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
