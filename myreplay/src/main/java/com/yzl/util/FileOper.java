package com.yzl.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileOper {
	private final static Logger logger = LoggerFactory.getLogger(FileOper.class);

	public static String createFilePathByUuid(final String fileType, final String uuid, Date fileDate) {
		if (null == fileType || 0 == fileType.trim().length()) {
			System.out.println("传入的报文请求类型是空的");
			return null;
		}
		if (null == uuid || 0 == uuid.trim().length()) {
			System.out.println("传入的UUID是空的");
			return null;
		}
		if (null == fileDate) {
			fileDate = new Date();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(ConfigureProperties.getConfigure(Constants.FILE_PATH_ROOT));
		sb.append(File.separator);
		sb.append(new SimpleDateFormat("yyyyMMdd").format(fileDate));
		sb.append(File.separator);
		sb.append(fileType);
		sb.append(File.separator);
		sb.append(uuid.substring(0, 2));
		sb.append(File.separator);
		sb.append(uuid.substring(2, 4));
//		System.out.println("uuid:" + uuid + " path:" + sb.toString());

		return sb.toString();
	}

	public static void saveFile(String fileType, String uuid, String fileText) throws IOException {
		String rootPath = FileOper.createFilePathByUuid(fileType, uuid, null);
		File rootDir = new File(rootPath);
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		File file = new File(rootPath + File.separator + uuid);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(fileText.getBytes());

		fos.close();
	}

	public static String readFile(String fileType, String uuid, Date fileDate, String path) throws IOException {
		String rootPath = null;
		if (null == uuid || 0 == uuid.trim().length()) {
			System.out.println("传入的UUID是空的");
			return null;
		}
		if (null == path || 0 == path.trim().length()) {
			rootPath = createFilePathByUuid(fileType, uuid, fileDate);
		} else {
			rootPath = path;
		}
		String filePath;
		if (rootPath.endsWith(File.separator)) {
			filePath = rootPath + uuid;
		} else {
			filePath = rootPath + File.separator + uuid;
		}
		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		while (true) {
			String readLine = br.readLine();
			if (readLine == null) {
				break;
			}
			sb.append(readLine);
		}
		br.close();
		isr.close();
		fis.close();
//		System.out.println("读到的数据为：" + sb.toString());
		return sb.toString();
	}
}
