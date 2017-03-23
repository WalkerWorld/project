/**   
* @Title: MemoryUtil.java
* @Package com.walker.utils
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年11月18日 下午3:02:54
* @version V1.0   
*/
package com.walker.autils;

import java.util.List;

import com.walker.bean.PhoneMemory;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;

/**
 * @ClassName: MemoryUtil
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年11月18日 下午3:02:54
 * 
 */
public class MemoryUtil {
	static PhoneMemory phoneMemory;
	public static PhoneMemory getMemory(){
		if(phoneMemory == null){
			phoneMemory = PhoneMemory.getInstance();
		}
		getMemory(phoneMemory);
		return phoneMemory;
	}
	
	/**
	 * @Description: TODO
	 *		获取Memory实体类
	 *  @param phoneMemory2
	 */
	private static PhoneMemory getMemory(PhoneMemory phoneMemory) {
		phoneMemory.MemoryAvailable = getAvailableMemory();
		phoneMemory.MemoryTotal = getTotalMemory();
		phoneMemory.ROMAvailableSize = getAvailableROM();
		phoneMemory.ROMTotalSize = getTotalRom();
		phoneMemory.SDCarTotalSize = getExternalTotalSpace();
		phoneMemory.SDCarAvailableSize = getExternalSpace();
		return phoneMemory;
	}

	/** 判断SD卡是否挂载 */
	public static boolean isSDCardAvailable() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static MemoryUtil memoryUtil;
	public MemoryUtil getInstance(){
		if(memoryUtil == null)
			memoryUtil = new MemoryUtil();
		return memoryUtil;
	}
	
	
	/** 获取手机外部空间大小，单位为byte */
	@SuppressWarnings("deprecation")
	public static long getExternalTotalSpace() {
		long totalSpace = -1L;
		if (isSDCardAvailable()) {
			try {
				String path = Environment.getExternalStorageDirectory().getPath();// 获取外部存储目录即
																					// SDCard
				StatFs stat = new StatFs(path);
				long blockSize = stat.getBlockSize();
				long totalBlocks = stat.getBlockCount();
				totalSpace = totalBlocks * blockSize;
			} catch (Exception e) {
				LogUtil.e(e);
			}
		}
	
		return totalSpace;   
	} 

	/** 获取外部存储可用空间，单位为byte */
	@SuppressWarnings("deprecation")
	public static long getExternalSpace() {
		long availableSpace = -1L;
		if (isSDCardAvailable()) {
			try {
				String path = Environment.getExternalStorageDirectory().getPath();
				StatFs stat = new StatFs(path);
				availableSpace = stat.getAvailableBlocks() * (long) stat.getBlockSize();
			} catch (Exception e) {
				LogUtil.e(e);
			}
		}
		return availableSpace;
	}

	/** 获取手机内部空间大小，单位为byte */
	@SuppressWarnings("deprecation")
	public static long getTotalRom() {
		long totalSpace = -1L;
		try {
			String path = Environment.getDataDirectory().getPath();
			
			StatFs stat = new StatFs(path);
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();// 获取该区域可用的文件系统数
			totalSpace = totalBlocks * blockSize;
		} catch (Exception e) {
			LogUtil.e(e);
		}
		return totalSpace;
	}

	/** 获取手机内部可用空间大小，单位为byte */
	@SuppressWarnings("deprecation")
	public static long getAvailableROM() {
		long availableSpace = -1l;
		try {
			String path = Environment.getDataDirectory().getPath();// 获取 Android
																	// 数据目录
			StatFs stat = new StatFs(path);// 一个模拟linux的df命令的一个类,获得SD卡和手机内存的使用情况
			long blockSize = stat.getBlockSize();// 返回 Int ，大小，以字节为单位，一个文件系统
			long availableBlocks = stat.getAvailableBlocks();// 返回 Int
																// ，获取当前可用的存储空间
			availableSpace = availableBlocks * blockSize;
		} catch (Exception e) {
			LogUtil.e(e);
		}
		return availableSpace;
	}

	/** 获取单个应用最大分配内存，单位为byte */
	public static long getOneAppMaxMemory() {
		Context context = UIUtils.getContext();
		if (context == null) {
			return -1;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getMemoryClass() * 1024 * 1024;
	}

	/** 获取指定本应用占用的内存，单位为byte */
	public static long getUsedMemory() {
		return getUsedMemory(null);
	}

	/** 获取指定包名应用占用的内存，单位为byte */
	public static long getUsedMemory(String packageName) {
		Context context = UIUtils.getContext();
		if (context == null) {
			return -1;
		}
		if (com.walker.jutil.StringUtil.isEmpty(packageName)) {
			packageName = context.getPackageName();
		}
		long size = 0;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> runapps = activityManager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo runapp : runapps) { // 遍历运行中的程序
			if (packageName.equals(runapp.processName)) {// 得到程序进程名，进程名一般就是包名，但有些程序的进程名并不对应一个包名
				// 返回指定PID程序的内存信息，可以传递多个PID，返回的也是数组型的信息
				Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[] { runapp.pid });
				// 得到内存信息中已使用的内存，单位是K
				size = processMemoryInfo[0].getTotalPrivateDirty() * 1024;
			}
		}
		return size;
	}

	/** 获取手机剩余内存，单位为byte */
	public static long getAvailableMemory() {
		Context context = UIUtils.getContext();
		if (context == null) {
			return -1;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		return info.availMem;
	}

	/** 获取手机总内存，单位为byte */
	public static long getTotalMemory() {
		long size = 0;
		String path = "/proc/meminfo";// 系统内存信息文件
		try {
			String totalMemory = FileUtils.getInstance().readProperties(path, "MemTotal", null);// 读出来是带单位kb的，并且单位前有空格，所以去掉最后三位
			if (!com.walker.jutil.StringUtil.isEmpty(totalMemory) && totalMemory.length() > 3) {
				size = Long.valueOf(totalMemory.substring(0, totalMemory.length() - 3)) * 1024;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		}
		return size;
	}

	/** 手机低内存运行阀值，单位为byte */
	public static long getThresholdMemory() {
		Context context = UIUtils.getContext();
		if (context == null) {
			return -1;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		return info.threshold;
	}

	/** 手机是否处于低内存运行 */
	public static boolean isLowMemory() {
		Context context = UIUtils.getContext();
		if (context == null) {
			return false;
		}
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		return info.lowMemory;
	}
}
