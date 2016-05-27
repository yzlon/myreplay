package com.yzl.util;

import java.io.File;

public class Constants {
	public final static int DEFAULT_HANDLER_POOL_NUMBERS = 50;
	public final static String H_FMT_CODE_STATUS_DEFAULT = "0";/* 处理状态 */
	public final static String H_FMT_CODE_STATUS_SUCC = "1";
	public final static String H_FMT_CODE_STATUS_FAIL = "2";
	public final static String H_FMT_CODE_DIFF_CODE_SAME = "0";/* 比对结果 */
	public final static String H_FMT_CODE_DIFF_CODE_DIFFERENT = "1";
	public final static String H_FMT_CODE_FLAG_DEFAULT = "0";

	// 接收报文的存放路径
	public final static String FILE_PATH_ROOT = "FMT_CODE_SAVE_PATH";
	// 请求报文
	public final static String FILE_TYPE_REQUEST = "REQUEST";
	// 应答报文
	public final static String FILE_TYPE_RESPONSE = "RESPONSE";
	public final static String FILE_TYPE_NEW_RESPONSE = "NEW_RESPONSE";
}
