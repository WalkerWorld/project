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
import java.util.HashMap;
import java.util.List;

import com.walker.app.BaseApplication;

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
	private static int effectTime = 1;
	
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
		logPath = Environment.getExternalStorageDirectory() + "/"+BaseApplication.getInstance().getPackageName() + "/Log/";
		File file = new File(logPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		if (fileMap == null) {
			fileMap = new HashMap<String, File>();
		}
	}

	/**
	 * Add by walker Date 2017年3月13日
	 * @Description: TODO
	 * 	删除文件夹或文件 
	 *  @param dir 文件指定路径
	 */
	public static void deleteDirectory(File dir){
		if(dir == null || !dir.exists()){
			return;
		}
		if(!dir.isDirectory()){
			dir.delete();
			return;
		}
		File[] listFile = dir.listFiles();
		for(File file: listFile){
			if (file.exists()) {
				if (!file.delete()) {
					//删除失败
					if (file.isDirectory()) {
						deleteDirectory(file);
					}
				}
			}
		}
	}
	
	/**
	 * Add by walker Date 2017年2月20日
	 * 
	 * @param <T>
	 * @Description: TODO
	 * 写入日志信息
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
	 * 	写入日志信息
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
		writeLog(StringUtil.getCurrentDate() + "\n" + msg, "log");
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
		writeLog(getJsonByObj(obj, flag), "log");
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
		writeMsg(msg, fileName);
	}
	static HashMap<String, File> fileMap;
	/**
	 * Add by walker Date 2017年3月12日
	 * @Description: TODO
	 * 	向指定文件中输出信息 
	 *  @param msg 写入内容信息
	 *  @param fileName 写入文件名称
	 */
	public static void writeMsg(String msg, String fileName){
		try {
			init();
			String path = logPath+ fileName +"!"+StringUtil.getCurrentDay()+".txt";
			FileWriter fw;
			File file = fileMap.get(path);
			if(file == null){
				file = new File(path);
				fileMap.put(path, file);
			}
			if (!file.exists()) {
				file.createNewFile();
			} 
			fw = new FileWriter(file, true);
			fw.write(StringUtil.getCurrentDate() + "\n" + msg + "\n");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add by walker Date 2017年3月12日
	 * @Description: TODO
	 *	清空无效日志信息
	 */
	public static void deleteHistory() {
		try {
			init();
			File file = new File(logPath);
			if(file.isDirectory() && !file.exists()){
				file.mkdirs();
				writeError("创建LOG日志路径");
				return;
			}
			for (File fileChild : file.listFiles()) {
				int difData = TypeUtil.strToInt(StringUtil.getDay(System.currentTimeMillis()-fileChild.lastModified()));
				if (difData > effectTime) {
					writeError("删除日志：" +fileChild.getName() + "\t占用时间：" + StringUtil.getDay(System.currentTimeMillis()-fileChild.lastModified())+ "\t生效时间：" + effectTime);
					fileChild.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add by walker Date 2017年3月8日
	 * @Description: TODO
	 * 写error日志，无标记限制 
	 *  @param msg 写入文本内容
	 */
	public static void writeError(String msg) {
		writeMsg(msg, "error");
	}

	
}
