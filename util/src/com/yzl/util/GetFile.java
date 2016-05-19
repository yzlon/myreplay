package com.yzl.util;

import java.io.File;

public class GetFile {

	public static void main(String[] args) {
		File file = new File("F:\\repository");
		System.out.println("main:" + file.isDirectory());
		getFile(file);
		System.out.println("delete:" + file.delete());
	}

	public static void getFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			System.out.println("files.length:" + files.length);
			if (null == files || files.length == 0) {
				System.out.println("file delete:" + file.delete());
			} else {
				for (File f : files) {
					getFile(f);
				}
			}
		} else {
			String fileName = file.getName();
			if (fileName.endsWith("jar")) {
				System.out.println(file.getPath());
				String dir = "F:" + File.separator + "jar-all";
				File fileDir = new File(dir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				File dest = new File(dir + File.separator + fileName);
				System.out.println(dest + "  " + file.renameTo(dest));
			} else {
				System.out.println("不是目标文件:" + fileName + " " + file.delete());

			}
		}
	}
}
