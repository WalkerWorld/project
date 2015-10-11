package com.walker.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
/**
 * 请求参数工具类
 */
public class BaseParamsMapUtil {
	private static String imei;
	private static String imsi;
	private static String mac;
	private static String xid;//imei和mac的md5值
	private static String cid;//渠道号
	private static String pkName;//包名
	/**
	 * 通用标准参数
	 * @return 返回请求键值对Map
	 */
	public static Map<String, String> getParamsMap(Context context) {
		Map<String, String> paramsMap = new HashMap<String, String>();

		return paramsMap;
	}
	public static Map<String, String> submitCheatCid(Context context,
			String string, String string2) {
		// TODO Auto-generated method stub
		return  new HashMap<String, String>();
	}
}
