/**   
* @Title: FileUtils.java
* @Package com.walker.Test
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2016年10月12日 下午5:10:52
* @version V1.0   
*/
package com.walker.jutil;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.List;

import android.os.Environment;

/** @ClassName: FileUtils
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2016年10月12日 下午5:10:52
 * 
 */
public class FileUtil {
	private static String logPath;
	/** 日志记录标记：为true则记录日志，为false不记录日志，程序启动默认为false */
	private static boolean isRecord = false;

	/**
	 * Add by walker Date 2017年2月23日
	 * 
	 * @Description: TODO 设置日志记录标记
	 * @param isRecords
	 *            设置当前日志记录标记
	 */
	public static void setLogFlag(boolean isRecords) {
		isRecord = isRecords;
	}

	/**
	 * Add by walker Date 2017年2月23日
	 * 
	 * @Description: TODO 获取记录标记
	 * @return 返回当前是否记录日志：返回true为记录日志状态；返回false非日志状态
	 */
	public static boolean getRecordFlag() {
		return isRecord;
	}

	public static void init() {
		logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.bohan.ems/Log/";
		File file = new File(logPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * Add by walker Date 2017年2月20日
	 * 
	 * @param <T>
	 * @Description: TODO
	 * 
	 * @param gekou_list
	 */
	public static <T> void writeLog(List<T> list) {
		if (list == null) {
			return;
		}
		for (Object obj : list) {
			writeLog(obj + "");
		}
	}

	/**
	 * Add by walker Date 2017年2月20日
	 * 
	 * @param <T>
	 * @Description: TODO
	 * 
	 * @param resultStrings
	 */
	public static <T> void writeLog(T[] res) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < res.length; i++) {
			sb.append("[" + i + "]:" + res[i] + "\t");
		}
		writeLog(sb.toString());
	}

	/**
	 * Add by walker Date 2017年2月23日
	 * 
	 * @Description: TODO 写文件日志信息
	 * @param msg
	 *            写入日志信息
	 */
	public static void writeLog(String msg) {
		init();
		// 当前为非日志状态，不记录日志信息
		if (!isRecord) {
			return;
		}
		try {
			String path = logPath + "log.txt";
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file, true);
			fw.write(msg + "\n");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Add by walker Date 2017年2月23日
	 * 
	 * @Description: TODO 写文件日志信息
	 * @param msg
	 *            写入日志信息
	 * @param flag 0-获取实体中所有的字段; 其他为有值字段
	 */
	public static void writeLog(Object obj, int flag) {
		init();
		// 当前为非日志状态，不记录日志信息
		if (!isRecord) {
			return;
		}
		try {
			String path = logPath + "log.txt";
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file, true);
			fw.write(getJsonByObj(obj, flag) + "\n");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add by walker Date 2017年2月7日
	 * @Description: TODO
	 * 	获取对象对应的JSON数据 
	 *  @param object
	 *  @param flag 0-获取实体中所有的字段; 其他为有值字段
	 *  @return 返回对象的json字符串
	 */
	public static String getJsonByObj(Object object, int flag){
		StringBuilder sb = new StringBuilder();
		Field[] fields = object.getClass().getDeclaredFields();
		sb.append("{");
		for(Field field : fields){
			field.setAccessible(true);
			try {
				if(flag == 0){
					sb.append("\"" + field.getName() + "\":\"" +field.get(object) + "\",");
				}else if(!StringUtil.isEmptyUnNull("" + field.get(object))){
					sb.append("\"" + field.getName() + "\":\"" +field.get(object) + "\",");
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		sb.replace(sb.length()-1, sb.length(), "");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Add by walker Date 2017年2月23日
	 * 
	 * @Description: TODO 写文件日志信息
	 * @param msg
	 *            写入日志信息
	 * @param fileName
	 *            日志文件名称
	 */
	public static void writeLog(String msg, String fileName) {
		// 当前为非日志状态，不记录日志信息
		if (!isRecord) {
			return;
		}
		try {
			String path = logPath + fileName;
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file, true);
			fw.write(msg + "\n");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
