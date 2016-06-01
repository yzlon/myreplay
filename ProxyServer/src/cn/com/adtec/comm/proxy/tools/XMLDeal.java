package cn.com.adtec.comm.proxy.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;

import org.dom4j.Element;
//import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import cn.com.adtec.comm.proxy.message.NodeStruct;

public class XMLDeal {

	/**
	 * 获取节点值
	 * @param doc 文档
	 * @param nodeName 节点名称
	 * @return 直接获取节点信息
	 * 
	 */
	/*
	private static String getNodeText(Document doc, String nodeName) {
		Node node = doc.selectSingleNode("//" + nodeName);
		return (node == null || node.getText() == null) ? "" : node.getText();
	}
	*/
	/**
	 * 获取某节点下的所有子节点列表
	 * @param doc 文档名称
	 * @param paraElement 父节点
	 * @param subElement  子节点
	 * @return list列表集合
	 */
	@SuppressWarnings("unchecked")
	private static List<Object> getNodeList(Document doc, String paraElement,
			String subElement) throws Exception {
		List<Object> list = new ArrayList<Object>();
		Element root = doc.getRootElement();
		Element parElement = root.element(paraElement);
		if (parElement == null) {
			throw new Exception(
					"方法：getNodeList(),传入参数paraElement节点名称在xml文档格式中不存在");
		}
		list = parElement.elements(subElement);
		return list;
	}
	
	/**
	 * 解析定时任务类配置文件
	 * <?xml version=\"1.0\" encoding=\"UTF-8\"?>
	 *  <root>
	 *    <classes-list>
	 *      <task-class>
	 *        <class-name>org.log.ldo.Task1</class-name>    类名
	 *        <execute-time>20140808080000</execute-time>   第一次执行时间
	 *        <period-time>300000</period-time>   重复周期，单位：毫秒
	 *        <class-desc>purpose description </class-desc>
	 *      </task-class>
	 *      <task-class>
	 *        <class-name>org.log.ldo.Task2</class-name>    类名
	 *        <execute-time>20140808080000</execute-time>   第一次执行时间
	 *        <period-time>400000</period-time>   重复周期，单位：毫秒
	 *        <class-desc>purpose description </class-desc>
	 *      </task-class>
	 *     </classes-list>
	 *    </root>
	 *        
	 * @param absolutePathFileName		文件名绝对路径
	 * @param parelement				需要获取的重复节点父节点名
	 * @param subelement				每个重复的节点名
	 * @param nodeArr					重复节点下面取哪些子节点
	 * @return
	 * @throws Exception 
	 */
	public static String[][] parseXMLConfig(String absolutePathFileName,String parelement,String subelement,String[] nodeArr) throws Exception
	{
		if (null==subelement || null==absolutePathFileName ||null==nodeArr ||"".equals(absolutePathFileName) ||"".equals(subelement) || nodeArr.length==0)
			return null;
		SAXReader reader = new SAXReader();
		BufferedReader br = new BufferedReader(new FileReader(new File(absolutePathFileName)));
		Document doc = reader.read(br);
		List<Object> res = getNodeList(doc, parelement,subelement);
		
		Iterator<Object> totalEle = res.iterator();
		
		String[][] val = new String[res.size()][nodeArr.length];
		
		int count = 0;
		for (;totalEle.hasNext();)
		{
			Element ele = (Element)totalEle.next();
			for (int j=0;j<nodeArr.length;j++)
			{
				val[count][j]=ele.elementText(nodeArr[j]);
			}
			count++;
			ele = null;
		}
		
		totalEle = null;
		res = null;
		doc = null;
		br.close();
		br = null;
		reader = null;
		return  val;
	}
	
	/**
	 * 提取XML串所有节点
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<NodeStruct> parsePureXMLfromStr(String src) throws Exception
	{
		if (null==src)
			return null;
		SAXReader reader = new SAXReader();
		BufferedReader br = new BufferedReader(new StringReader(src));
		Document doc = reader.read(br);
		
		Element root = doc.getRootElement();
		ArrayList<NodeStruct> allNodes = new ArrayList<NodeStruct>();
		if (root.hasContent())
		{
			int parentId=0;
			getChild(root,parentId,allNodes,root);
		}
		
		root = null;
		doc = null;
		br.close();
		br = null;
		reader = null;
		
		return  allNodes;
	}
	
	/**
	 * 支持中间业务平台特殊XML报文格式处理和普通XML处理
	 * @param inputStr
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<NodeStruct> parseXMLfromStr(String inputStr) throws Exception
	{
		String k = inputStr;
		int firstLoc = k.indexOf("?xml",1);
		int secondLoc = k.indexOf("?xml",2);
		if (firstLoc!=-1 && secondLoc!=-1 && firstLoc!=secondLoc)
		{
			//有嵌套XML，将第二个做替换
			int lastLoc = k.lastIndexOf("?");
			StringBuilder sb = new StringBuilder();
			sb.append(k.substring(0, secondLoc));
			sb.append("____xml");
			sb.append(k.substring(secondLoc+4,lastLoc));
			sb.append("____=\"\"/");
			sb.append(k.substring(lastLoc+1));
			
			//System.out.println(sb.toString());
			return parsePureXMLfromStr(sb.toString());
			
		}
		else
		{
			return parsePureXMLfromStr(k);
		}
	}
	
	/**
	 * 递归获取XML格式各节点
	 * @param ele
	 * @param parentId
	 * @param allNodes
	 * @param parentEle
	 */
	@SuppressWarnings("unchecked")
	private static void getChild(Element ele,int parentId,ArrayList<NodeStruct> allNodes,Element parentEle)
	{
		List<Element> l = ele.elements();
		
		NodeStruct ns = new NodeStruct();
		ns.setNodeName(ele.getName());
		ns.setNodeLevel(parentId);
		ns.setNodeValue(ele.getText().trim());
		
		//add by 汤，20150213,增加对属性值的获取 begin
		List<Attribute> list = ele.attributes();
		for (Attribute att:list)
		{
			//System.out.println("name["+att.getName()+"]value["+att.getValue()+"]");
			ns.addAttribute(att.getName(), att.getValue());
		}
		
		//add by 汤，20150213,增加对属性值的获取 end
		
		ns.setParentNodeLevel(parentId-1);
		ns.setParentNodeName(parentEle.getName());
		//System.out.println("node....["+ns.getNodeName()+"]val["+ns.getNodeValue()+"]parentId["+ns.getParentNodeLevel()+"]thislevel["+ns.getNodeLevel()+"]parentName["+ns.getParentNodeName()+"]");
		allNodes.add(ns);
		if (null!=l && l.size()>0)
		{
			parentId++;
			for (Element e:l)
			{
				getChild(e,parentId,allNodes,ele);
			}
		}
		l = null;
		ns = null;
	}
	
	public static void main(String[] args) throws Exception
	{
/*		
		String[] fields = new String[]{
				"class-name",
				"execute-time",
				"period-time",
				"class-desc"
		};
		String[][] a = parseXMLConfig("c:\\多少.xml","classes-list","task-class",fields);
		for (int i=0;i<a.length;i++)
		{
			for (int j=0;j<a[i].length;j++)
			{
				System.out.print(" "+a[i][j]);
			}
			System.out.print("\n");
		}
		*/
		String k="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
	    +"<root>"
	 +"<classes-list>"
	 +"      <task-class>"
	 +"        <class-name>org.log.ldo.Task1</class-name>"
	 +"        <execute-time>20140808080000</execute-time>"
	 +"        <period-time>300000</period-time>"
	 +"        <class-desc>purpose description </class-desc>"
	 +"      </task-class>"
	 +"      <task-class>"
	 +"        <class-name>org.log.ldo.Task2</class-name>"
	 +"        <execute-time>20140808080000</execute-time>"
	 +"        <period-time>400000</period-time>"
	 +"        <class-desc>purpose description </class-desc>"
	 +"      </task-class>"
	 +"     </classes-list>"
	 +"    </root>";
		parseXMLfromStr(k);
	}
}
