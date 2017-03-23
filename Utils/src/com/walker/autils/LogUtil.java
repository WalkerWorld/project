package com.walker.autils;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.walker.app.AppConfig;
import com.walker.jutil.DateUtil;
import com.walker.jutil.ReflectUtil;
import com.walker.jutil.StringUtil;
import com.walker.jutil.TypeUtil;

import android.util.Log;

public class LogUtil {
	/** 日志输出时的TAG */
	private static String mTag = "MyLog";
	/**
	 * 是否允许输出log ,用于控制输出log的信息 0-所有日志信息 1－日志输出级别V 2－日志输出级别D 3－日志输出级别I 4－日志输出级别W
	 * 5－日志输出级别E
	 */
	private static int mDebuggable = 0;

	/**
	 * Add by walker Date 2017年3月22日
	 * 
	 * @Description: TODO 打印Log信息
	 * @param msg
	 *            log信息
	 * @param flag
	 *            打印模式 1－日志输出级别V 2－日志输出级别D 3－日志输出级别I 4－日志输出级别W 5－日志输出级别E 其它－输出语句
	 */
	public static void log(String msg, int flag) {
		System.out.println(flag + "\t" + mDebuggable + msg);
		if (flag < mDebuggable) {
			return;
		}
		System.out.println(flag + msg);
		switch (flag) {
		case 1:
			Log.v(mTag, msg);
			break;
		case 2:
			Log.d(mTag, msg);
			break;
		case 3:
			Log.i(mTag, msg);
			break;
		case 4:
			Log.w(mTag, msg);
			break;
		case 5:
			Log.e(mTag, msg);
			break;
		default:
			System.out.println("------------------\n" + msg);
			break;
		}
	}

	/**
	 * Add by walker Date 2017年3月22日
	 * 
	 * @Description: TODO 直接打印语句
	 * @param msg
	 *            日志信息
	 */
	public static void print(String msg) {
		log(msg, 6);
	}

	/** 以级别为 v 的形式输出LOG */
	public static void v(String msg) {
		log(msg, 1);
	}

	/** 以级别为 d 的形式输出LOG */
	public static void d(String msg) {
		log(msg, 2);
	}

	/** 以级别为 i 的形式输出LOG */
	public static void i(String msg) {
		log(msg, 3);
	}

	/** 以级别为 w 的形式输出LOG */
	public static void w(String msg) {
		log(msg, 4);
	}

	/** 以级别为 e 的形式输出LOG */
	public static void e(String msg) {
		log(msg, 5);
	}

	/** 以级别为 e 的形式输出Throwable */
	public static void e(Throwable tr) {
		if (mDebuggable <= 5) {
			Log.e(mTag, "", tr);
		}
	}

	/** 以级别为 e 的形式输出LOG信息和Throwable */
	public static void e(String msg, Throwable tr) {
		if (5 >= mDebuggable && null != msg) {
			Log.e(mTag, msg, tr);
		}
	}

	/** 写文件的锁对象 */
	private static final Object mLogLock = new Object();

	/**
	 * 把Log存储到文件中
	 * 
	 * @param log
	 *            需要存储的日志
	 * @param path
	 *            存储路径
	 */
	public static void log2File(String log, String path) {
		log2File(log, path, true);
	}

	/**
	 * Add by walker Date 2017年3月22日
	 * 
	 * @Description: TODO 写日志文件：指定是否添加
	 * @param log
	 * @param path
	 * @param append
	 */
	public static void log2File(String log, String path, boolean append) {
		synchronized (mLogLock) {
			FileUtils.getInstance().writeFile(log + "\r\n", path, append);
		}
	}

	/**
	 * Add by walker Date 2017年3月20日
	 * 
	 * @Description: TODO debug模式下，记录日志到日志缓存文件
	 * @param msg
	 *            日志信息
	 */
	public static void record2File(String msg) {
		writeLog(msg);
	}

	/**
	 * Add by walker Date 2017年3月20日
	 * 
	 * @Description: TODO debug模式下，记录日志到日志缓存文件
	 * @param obj
	 *            日志信息实体
	 */
	public static void record2File(Object obj) {
		writeLog(ReflectUtil.getJsonByObj(obj, 0));
	}

	/**
	 * Add by walker Date 2017年3月20日
	 * 
	 * @Description: TODO debug模式下，记录日志到日志缓存文件
	 * @param objList
	 *            日志信息列表
	 */
	public static void record2File(List<Object> objList) {
		writeLog(ReflectUtil.getJsonByListObj(objList, 0));
	}

	/**
	 * Add by walker Date 2017年3月20日
	 * 
	 * @Description: TODO debug模式下，记录日志到日志缓存文件
	 * @param objMap
	 *            日志信息集合
	 */
	public static void record2File(Map<Object, Object> objMap) {
		writeLog(ReflectUtil.getJonsByMap(objMap));
	}

	/**
	 * Add by walker Date 2017年3月20日
	 * 
	 * @Description: TODO debug模式下，记录日志到日志缓存文件
	 * @param objList
	 *            日志信息列表
	 */
	public static void record2File(Object[] objArr) {
		writeLog(ReflectUtil.getJonsByArr(objArr));
	}

	/**
	 * Add by walker Date 2017年3月20日
	 * 
	 * @Description: TODO debug模式下，记录日志到日志缓存文件
	 * @param throwable
	 *            异常信息
	 */
	public static void record2File(Throwable throwable) {
		writeLog(throwable != null ? throwable.getMessage() : "未知异常");
	}

	/**
	 * Add by walker Date 2017年3月22日
	 * 
	 * @Description: TODO 记录错误信息到日志文件，
	 * @param msg
	 *            异常信息
	 */
	public static void eToFile(String msg) {
		writeLog(msg);
	}

	/**
	 * Add by walker Date 2017年3月20日
	 * 
	 * @Description: TODO debug模式下，记录日志到日志缓存文件
	 * @param obj
	 *            日志信息实体
	 */
	public static void record2FileError(Object obj) {
		writeLog(ReflectUtil.getJsonByObj(obj, 0));
	}

	/**
	 * Add by walker Date 2017年3月20日
	 * 
	 * @Description: TODO debug模式下，记录日志到日志缓存文件
	 * @param objList
	 *            日志信息列表
	 */
	public static void record2FileError(List<Object> objList) {
		writeLog(ReflectUtil.getJsonByListObj(objList, 0));
	}

	/**
	 * Add by walker Date 2017年3月20日
	 * 
	 * @Description: TODO debug模式下，记录日志到日志缓存文件
	 * @param objMap
	 *            日志信息集合
	 */
	public static void record2FileError(Map<Object, Object> objMap) {
		writeLog(ReflectUtil.getJonsByMap(objMap));
	}

	/**
	 * Add by walker Date 2017年3月20日
	 * 
	 * @Description: TODO debug模式下，记录日志到日志缓存文件
	 * @param objList
	 *            日志信息列表
	 */
	public static void record2FileError(Object[] objArr) {
		writeLog(ReflectUtil.getJonsByArr(objArr));
	}

	/**
	 * Add by walker Date 2017年3月20日
	 * 
	 * @Description: TODO debug模式下，记录日志到日志缓存文件
	 * @param throwable
	 *            异常信息
	 */
	public static void record2FileError(Throwable throwable) {
		writeLog(throwable != null ? throwable.getMessage() : "未知异常");
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
		if (!AppConfig.getInstance().isDebug()) {
			return;
		}
		FileUtils.getInstance().writeStr(DateUtil.formateTime(System.currentTimeMillis()) + "\n" +msg, fileName);
	}

	/**
	 * Add by walker Date 2017年2月23日
	 * 
	 * @Description: TODO 写文件日志信息
	 * @param msg
	 *            写入日志信息
	 */
	private static void writeLog(String msg) {
		FileUtils.getInstance().writeStr(DateUtil.formateTime(System.currentTimeMillis()) + "\n" +msg, "LogError");
	}
	
}
