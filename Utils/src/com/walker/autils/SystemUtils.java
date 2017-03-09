package com.walker.autils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import com.walker.constant.ConstantFlag;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class SystemUtils {
	static Context context;

	/**
	 * @Description: TODO 获取设备指定信息
	 * @param type
	 *            需要获取信息类别标识 DEVICE_ID： 设备唯一标识 SYSTEM_VERSION：设备操作系统版本号 android
	 *            4.2.1 DEVICE_MODEL：设备型号：小米4 三星 APP_VERION：应用程序版本号 version
	 * @return 反回当前设备对应的信息
	 * @throws NameNotFoundException
	 */
	public static String getPhoneInfo(int type) throws NameNotFoundException {
		if (context == null) {
			context = UIUtils.getContext();
		}
		TelephonyManager telephonyManager = (TelephonyManager) UIUtils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
		switch (type) {
		case ConstantFlag.DEVICE_IMEI_ID:// 设备唯一标识 IMEI
			return telephonyManager.getDeviceId();
		case ConstantFlag.SYSTEM_VERSION:// 系统版本号
			return "android" + android.os.Build.VERSION.RELEASE;
		case ConstantFlag.DEVICE_MODEL:// 设备型号
			return android.os.Build.MODEL;
		case ConstantFlag.APP_VERION:// 应用程序版本号
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		case ConstantFlag.SDK_MODEL:// android系统SDK版本号
			return android.os.Build.VERSION.SDK;
		case ConstantFlag.MAC_ADDRESS:// 获取Mac地址
			return getMacAddress(context);
		case ConstantFlag.IP_ADDRESS:// IP地址
			return getIpAddress();
		case ConstantFlag.CPU_INFO:// CPU信息
			return getCPUInfos();
		case ConstantFlag.ROM_VERSION:// ROM包版本
			return getRomversion();
		default:
			return "";
		}
	}

	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return 获取版本号
	 */
	public static int getVersionCode(Context context) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(),
					0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/** 获得android系统sdk版本号 */
	public static int getOSVersionSDKINT() {
		return android.os.Build.VERSION.SDK_INT;
	}

	// 获取android系统版本号
	private static String getOSVersion() {
		String release = android.os.Build.VERSION.RELEASE; // android系统版本号
		release = "android" + release;
		return release;
	}

	// android系统sdk版本号
	private static String getOSVersionSDK() {
		return android.os.Build.VERSION.SDK;
	}

	// 获取手机型号
	private static String getDeviceModel() {
		return android.os.Build.MODEL;
	}

	// 获取设备的IMEI
	private static String getIMEI() {
		if (context == null) {
			context = UIUtils.getContext();
		}

		String imei = null;
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imei = tm.getDeviceId();
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return null == context ? null : imei;
	}

	/**
	 * 判断电话卡状态
	 * 
	 * @param context
	 * @return
	 */
	public static String readSIMCard(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		switch (tm.getSimState()) { // getSimState()取得sim的状态 有下面6中状态
		case TelephonyManager.SIM_STATE_ABSENT:
			return "无卡";
		case TelephonyManager.SIM_STATE_UNKNOWN:
			return "未知状态";
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			return "需要NetworkPIN解锁";
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			return "需要PIN解锁";
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			return "需要PUK解锁";
		}
		return "正常";
	}

	/** 检测手机是否已插入SIM卡 */
	public static boolean isCheckSimCardAvailable() {
		Context context = UIUtils.getContext();
		if (null == context) {
			return false;
		}
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
	}

	/** sim卡是否可读 */
	public static boolean isCanUseSim() {
		Context context = UIUtils.getContext();
		if (null == context) {
			return false;
		}
		try {
			TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return false;
	}

	/** 取得当前sim手机卡的imsi */
	public static String getIMSI() {
		Context context = UIUtils.getContext();
		if (null == context) {
			return null;
		}
		String imsi = null;
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imsi = tm.getSubscriberId();
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return imsi;
	}

	/** 返回本地手机号码，这个号码不一定能获取到 */
	public static String getNativePhoneNumber() {
		Context context = UIUtils.getContext();
		if (null == context) {
			return null;
		}
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String NativePhoneNumber = null;
		NativePhoneNumber = telephonyManager.getLine1Number();
		return NativePhoneNumber;
	}

	/** 返回手机服务商名字 */
	public static String getProvidersName() {
		String ProvidersName = null;
		// 返回唯一的用户ID;就是这张卡的编号神马的
		String IMSI = getIMSI();
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
			ProvidersName = "中国移动";
		} else if (IMSI.startsWith("46001")) {
			ProvidersName = "中国联通";
		} else if (IMSI.startsWith("46003")) {
			ProvidersName = "中国电信";
		} else {
			ProvidersName = "其他服务商:" + IMSI;
		}
		return ProvidersName;
	}

	/** 获取当前SIM卡的串号SN */
	public static String getSimSN() {
		if (null == context) {
			context = UIUtils.getContext();
		}
		String simSN = null;
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			simSN = tm.getSimSerialNumber();
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return context == null ? null : simSN;
	}

	/** 获取当前设备的MAC地址 */
	private static String getMacAddress(Context context) {
		if (null == context) {
			return null;
		}
		String mac = null;
		try {
			WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wm.getConnectionInfo();
			mac = info.getMacAddress();
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return mac;
	}

	/** 获得设备ip地址 */
	private static String getIpAddress() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface intf = en.nextElement();
				Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
				while (enumIpAddr.hasMoreElements()) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			LogUtils.e(e);
		}
		return null;
	}

	/** 获取屏幕的分辨率 */
	@SuppressWarnings("deprecation")
	public static int[] getResolution() {
		Context context = UIUtils.getContext();
		if (null == context) {
			return null;
		}
		WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int[] res = new int[2];
		res[0] = windowMgr.getDefaultDisplay().getWidth();
		res[1] = windowMgr.getDefaultDisplay().getHeight();
		return res;
	}

	/** 获得设备的横向dpi */
	public static float getWidthDpi() {
		Context context = UIUtils.getContext();
		if (null == context) {
			return 0;
		}
		DisplayMetrics dm = null;
		try {
			if (context != null) {
				dm = new DisplayMetrics();
				dm = context.getApplicationContext().getResources().getDisplayMetrics();
			}

			return dm.densityDpi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/** 获得设备的纵向dpi */
	public static float getHeightDpi() {
		Context context = UIUtils.getContext();
		if (null == context) {
			return 0;
		}
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		return dm.ydpi;
	}

	/** 获取设备信息 */
	public static String[] getDivceInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" };
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
			LogUtils.e(e);
		}
		return cpuInfo;
	}

	/** 判断手机CPU是否支持NEON指令集 */
	public static boolean isNEON() {
		boolean isNEON = false;
		String cupinfo = getCPUInfos();
		if (cupinfo != null) {
			cupinfo = cupinfo.toLowerCase();
			isNEON = cupinfo != null && cupinfo.contains("neon");
		}
		return isNEON;
	}

	/** 读取CPU信息文件，获取CPU信息 */
	@SuppressWarnings("resource")
	private static String getCPUInfos() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		StringBuilder resusl = new StringBuilder();
		String resualStr = null;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((str2 = localBufferedReader.readLine()) != null) {
				resusl.append(str2);
				// String cup = str2;
			}
			if (resusl != null) {
				resualStr = resusl.toString();
				return resualStr;
			}
		} catch (IOException e) {
			LogUtils.e(e);
		}
		return resualStr;
	}

	/** 获取当前设备cpu的型号 */
	public static int getCPUModel() {
		return matchABI(getSystemProperty("ro.product.cpu.abi")) | matchABI(getSystemProperty("ro.product.cpu.abi2"));
	}

	/** 匹配当前设备的cpu型号 */
	private static int matchABI(String abiString) {
		if (TextUtils.isEmpty(abiString)) {
			return 0;
		}
		if ("armeabi".equals(abiString)) {
			return 1;
		} else if ("armeabi-v7a".equals(abiString)) {
			return 2;
		} else if ("x86".equals(abiString)) {
			return 4;
		} else if ("mips".equals(abiString)) {
			return 8;
		}
		return 0;
	}
	
	public static String getCpuName(){
		switch (getCPUModel()) {
		case 0:
			return "未匹配";
		case 1:
			return "armeabi";
		case 2:
			return "armeabi-v7a";
		case 4:
			return "x86";
		case 8:
			return "未匹配";

		}
		return "";
	}

	/** 获取CPU核心数 */
	public static int getCpuCount() {
		return Runtime.getRuntime().availableProcessors();
	}

	/** 获取Rom版本 */
	private static String getRomversion() {
		String rom = "";
		try {
			String modversion = getSystemProperty("ro.modversion");
			String displayId = getSystemProperty("ro.build.display.id");
			if (modversion != null && !modversion.equals("")) {
				rom = modversion;
			}
			if (displayId != null && !displayId.equals("")) {
				rom = displayId;
			}
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return rom;
	}

	/** 获取系统配置参数 */
	public static String getSystemProperty(String key) {
		String pValue = null;
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method m = c.getMethod("get", String.class);
			pValue = m.invoke(null, key).toString();
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return pValue;
	}

	/** 获取系统中的Library包 */
	public static List<String> getSystemLibs() {
		Context context = UIUtils.getContext();
		if (null == context) {
			return null;
		}
		PackageManager pm = context.getPackageManager();
		String[] libNames = pm.getSystemSharedLibraryNames();
		List<String> listLibNames = Arrays.asList(libNames);
		LogUtils.d("SystemLibs: " + listLibNames);
		return listLibNames;
	}

}
