package com.yzl.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigureProperties {
	private final static Logger logger = LoggerFactory.getLogger(ConfigureProperties.class);
	private static Map<String, String> configures = null;

	static {
		try {
			loadConfigures();
			logger.info("参数初始化完毕!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized static void loadConfigures() throws Exception {
		if (null == configures) {
			configures = new HashMap<String, String>();
		} else {
			return;
		}
		Properties properties = new Properties();

		InputStream is = ConfigureProperties.class.getClassLoader()
				.getResourceAsStream("conf/configure-basic.properties");
		properties.load(is);
		Set<Object> keys = properties.keySet();
		for (Object key : keys) {
			// configures.put((String) key,
			// properties.getProperty((String)key));
			/* 可以用一下方式 obj.toString */
			configures.put(key.toString(), properties.getProperty(key.toString()));
		}
		logger.info(configures.toString());
	}

	public static String getConfigure(String key) {
		if (null == configures) {
			try {
				loadConfigures();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return configures.get(key);
	}
}
