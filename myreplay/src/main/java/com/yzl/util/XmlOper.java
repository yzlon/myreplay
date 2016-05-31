package com.yzl.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yzl.db.entity.HDiffRule;
import com.yzl.db.entity.HFmtCode;
import com.yzl.vo.CompareResult;

public class XmlOper {
	private final static Logger logger = LoggerFactory.getLogger(XmlOper.class);

	public static CompareResult compare(HFmtCode fmtCode, HDiffRule rule) {
		logger.info("开始比对报文，交易码[ " + fmtCode.getTranCode() + " ],uuid[ " + fmtCode.getUuid() + " ],比对类型[ "
				+ rule.getCmpType() + " ],比对要素[ " + rule.getEleCode() + " ],是否反选[ " + rule.isDeSelectFlag() + " ]");
		String oxml = null;
		String txml = null;

		CompareResult compareResult = new CompareResult();
		List<String> compareInfo = new ArrayList<String>();
		compareResult.setCompareInfo(compareInfo);

		// 读取报文
		try {
			oxml = FileOper.readFile(Constants.FILE_TYPE_RESPONSE, fmtCode.getUuid(), new Date(), null);
			txml = FileOper.readFile(Constants.FILE_TYPE_NEW_RESPONSE, fmtCode.getUuid(), new Date(), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (oxml == null || oxml.trim().length() == 0 || txml == null || txml.trim().length() == 0) {
			String errMsg = null;
			if (oxml == null || oxml.trim().length() == 0) {
				errMsg = "源报文为空";
			} else if (txml == null || txml.trim().length() == 0) {
				errMsg = "目标报文为空";
			}
			logger.error(errMsg);
			compareInfo.add(errMsg);
			compareResult.setDiff(true);
			return compareResult;
		}

		// logger.info("oxml:" + oxml);
		// logger.info("txml:" + txml);
		// 获取xpath和值的映射集合
		Document oDoc = null;
		Document tDoc = null;
		try {
			oDoc = DocumentHelper.parseText(oxml);
			tDoc = DocumentHelper.parseText(txml);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Map<String, List<String>> oValues = getXPathAndValues(oDoc, rule.getEleCode(), rule.isDeSelectFlag());
		Map<String, List<String>> tValues = getXPathAndValues(tDoc, rule.getEleCode(), rule.isDeSelectFlag());
		Set<String> oKeys = oValues.keySet();
		for (String oKey : oKeys) {
			/*
			 * 比较规则：1-标签比对2-标签及值比对3-标签及值的合理范围比对
			 */
			if (!tValues.containsKey(oKey)) {
				String errMsg = "目标报文中缺少标签[" + oValues.get(oKey).get(0) + ">" + oValues.get(oKey).get(1) + " ]";
				logger.error(errMsg);
				compareInfo.add(errMsg);
				if (!compareResult.isDiff()) {
					compareResult.setDiff(true);
				}
				continue;
			}
			if (0 == "1".compareTo(rule.getCmpType())) {// 1-标签比对
				// 删除目标中有的
			} else if (0 == "2".compareTo(rule.getCmpType())) {// 2-标签及值比对
				List<String> oValueList = oValues.get(oKey);
				List<String> tValueList = tValues.get(oKey);
				String oValue = oValueList.get(1);
				String tValue = tValueList.get(1);
				if (0 != oValue.compareTo(tValue)) {
					String errMsg = "源报文中标签[< " + oValues.get(oKey).get(0) + ">的值为[ " + oValue + " ],目标报文标签的值为[ "
							+ tValue + " ]";
					logger.error(errMsg);
					compareInfo.add(errMsg);
					if (!compareResult.isDiff()) {
						compareResult.setDiff(true);
					}
				}
			} else if (0 == "3".compareTo(rule.getCmpType())) {// 3-标签及值的合理范围比对
			} else {
				String errMsg = "不支持的比对类型";
				logger.error(errMsg);
				compareInfo.add(errMsg);
				if (!compareResult.isDiff()) {
					compareResult.setDiff(true);
				}
				break;
			}
			tValues.remove(oKey);
		} // for
		return compareResult;
	}

	/**
	 * 
	 * @Title: getXPathAndValues @Description: 获得xml文本的叶子元素的集合，
	 *         key存放唯一路径，list存放xpat和叶子节点的值 @param document @param xpath
	 *         :xpath路径 @param deSelect 是否获取xpath指定的路径除外的元素 true-是 @return
	 *         Map<String,List<String>> @throws
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, List<String>> getXPathAndValues(Document document, String xpath, boolean deSelect) {
		Map<String, List<String>> values = new HashMap<String, List<String>>();
		List<Element> elements = document.selectNodes(xpath);
		for (Element element : elements) {
			get(element, values);
		}
		if (deSelect) {
			Map<String, List<String>> allValues = new HashMap<String, List<String>>();
			List<Element> allElements = document.selectNodes("//*");
			for (Element element : allElements) {
				get(element, allValues);
			}
			Set<String> keys = values.keySet();
			for (String key : keys) {
				allValues.remove(key);
			}
			values = allValues;
		}
		return values;

	}

	@SuppressWarnings("unchecked")
	private static void get(Element element, Map<String, List<String>> values) {
		List<Element> elements = element.elements();
		if (elements.isEmpty()) {

			List<String> value = new ArrayList<String>();
			value.add(element.getPath());
			value.add(element.getText());
			values.put(element.getUniquePath(), value);
		} else {
			for (Element ele : elements) {
				get(ele, values);
			}
		}
	}
}
