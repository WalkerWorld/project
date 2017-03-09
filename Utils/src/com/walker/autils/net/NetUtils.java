/**   
* @Title: NetUtils.java
* @Package com.walker.utils
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年11月18日 下午5:40:46
* @version V1.0   
*/
package com.walker.autils.net;

import com.walker.autils.UIUtils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/** @ClassName: NetUtils
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年11月18日 下午5:40:46
 * 
 */
public class NetUtils {
	
	public static final int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
	public static final int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
	public static final int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络
//	private static final String TAG = "NetUtil";
	public static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	/** 网络类型 0表示其他网络 1表示移动联通 2代表电信 */
	public static int NetType = 0;
	/** 中国移动联通wap代理网关 */
	public static final String proxyMobile = "10.0.0.172";
	/** 中国电信wap代理网关 */
	public static final String proxyTel = "10.0.0.200";
	public static final String CTWAP = "ctwap";
	public static final String CMWAP = "cmwap";
	public static final String WAP_3G = "3gwap";
	public static final String UNIWAP = "uniwap";
	
	/**
	 * 是否能上网
	 * 
	 * @param context
	 * @return 是否能上网
	 */
	public static boolean hasConnectedNetwork(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		UIUtils.showToast(context, "请连接网络");
		return false;
	}

	/**
	 * wifi是否连接
	 * 
	 * @param context
	 * @return wifi是否连接
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		UIUtils.showToast(context, "当前WiFi不可用");
		return false;
	}
	
	/**
	 * 获取网络类型
	 * 
	 * @param mContext
	 * @return 网络类型
	 */
	public static int checkNetworkType(Context mContext) {
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager.getActiveNetworkInfo();
			if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {

				// 注意一：
				// NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
				// 但是有些电信机器，仍可以正常联网，
				// 所以当成net网络处理依然尝试连接网络。
				// （然后在socket中捕捉异常，进行二次判断与用户提示）。
				Log.i("", "=====================>无网络");
				return TYPE_OTHER_NET;
			} else {
				// NetworkInfo不为null开始判断是网络类型
				int netType = mobNetInfoActivity.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					// wifi net处理
					Log.i("", "=====================>wifi网络");
					return TYPE_OTHER_NET;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {

					// 注意二：
					// 判断是否电信wap:
					// 不要通过getExtraInfo获取接入点名称来判断类型，
					// 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
					// 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
					// 所以可以通过这个进行判断！

					final Cursor c = mContext.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
					if (c != null) {
						c.moveToFirst();
						final String user = c.getString(c.getColumnIndex("user"));
						if (!TextUtils.isEmpty(user)) {
							Log.i("", "=====================>代理：" + c.getString(c.getColumnIndex("proxy")));
							if (user.startsWith(CTWAP)) {
								Log.i("", "=====================>电信wap网络");
								return TYPE_CT_WAP;
							}
						}
					}
					c.close();

					// 注意三：
					// 判断是移动联通wap:
					// 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
					// 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
					// 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
					// 所以采用getExtraInfo获取接入点名字进行判断

					String netMode = mobNetInfoActivity.getExtraInfo();
					Log.i("", "netMode ================== " + netMode);
					if (netMode != null) {
						// 通过apn名称判断是否是联通和移动wap
						netMode = netMode.toLowerCase();
						if (netMode.equals(CMWAP) || netMode.equals(WAP_3G) || netMode.equals(UNIWAP)) {
							Log.i("", "=====================>移动联通wap网络");
							return TYPE_CM_CU_WAP;
						}

					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return TYPE_OTHER_NET;
		}
		return TYPE_OTHER_NET;
	}

}
