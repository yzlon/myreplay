package com.yzl.replay;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yzl.util.Constants;
import com.yzl.util.FileOper;

public class HostClient {
	private final static Logger logger = LoggerFactory.getLogger(HostClient.class);

	public static String client(String requestMsg) {
//		logger.info("发送报文[ " + requestMsg + " ]到核心");
		String responseMsg = null;
		try {
			responseMsg = FileOper.readFile(Constants.FILE_TYPE_RESPONSE, requestMsg, null, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseMsg;
	}
}
