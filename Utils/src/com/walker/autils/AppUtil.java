package com.walker.autils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.json.JSONException;
import org.json.JSONObject;

import com.walker.constant.ConstantFlag;
import com.walker.jutil.DataUtil;
import com.walker.jutil.StringUtil;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Environment;
import android.util.DisplayMetrics;


public class AppUtil {
	public static final String SDCARD_SOFT_STORE =Environment.getExternalStorageDirectory()+"/htjxsdk/";
	public static final File SDCARD =Environment.getExternalStorageDirectory();
	private static DisplayMetrics displayMetrics = new DisplayMetrics();
	
	/**
	 * Json字符串转化成JSONObject
	 * @param str
	 * @return JSONObject对象
	 * @throws Exception
	 */
	public static JSONObject stringToJSONObject(String str) throws Exception {
		try {
			return new JSONObject(str);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * 方法描述：将资源目录下文件写入本地 
	 * @param fileName :文件名 
	 * @param path :路径名 
	 * @return 是否成功
	 */
	public static boolean retrieveApkFromAssets(Context context,
			String fileName, String path) {
		boolean bRet = false;
		try {
			InputStream is = context.getAssets().open(fileName);

			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			bRet = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bRet;
	}

	
	/**
	 * imei和mac地址的md5值
	 * @param context
	 * @return imei和mac地址的md5值
	 */
	public static String getXid(Context context) {
		String xid = "";
		try {
			xid = DataUtil.md5(SystemUtils.getPhoneInfo(ConstantFlag.DEVICE_IMEI_ID) + SystemUtils.getPhoneInfo(ConstantFlag.MAC_ADDRESS));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return xid;
	}
	/**
	 * 获取包名
	 * @param context
	 * @return 获取包名
	 */
	public static String getPkName(Context context) {
		String xid = "";
		try {
			xid = DataUtil.md5(SystemUtils.getPhoneInfo(ConstantFlag.DEVICE_IMEI_ID) + SystemUtils.getPhoneInfo(ConstantFlag.MAC_ADDRESS));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return xid;

	}
	
	
	
	static class OnCancelListener implements DialogInterface.OnCancelListener {
		Activity mcontext;

		OnCancelListener(Activity context) {
			this.mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			this.mcontext.onKeyDown(4, null);
		}
	}

	/**
	 * 方法描述：检查SD卡是否存在
	 */
	public static boolean isExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 返回屏幕宽(px)
	 */
	public static int getScreenWidth(Activity activity) {
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}

	/**
	 * 返回屏幕高(px)
	 */
	public static int getScreenHeight(Activity activity) {
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		return displayMetrics.heightPixels;
	}
	
	private static int id;

	public void token(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(),
							PackageManager.GET_SIGNATURES);

			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			byte[] signbyteArray = sign.toByteArray();
			CertificateFactory instance = CertificateFactory
					.getInstance("X.509");
			X509Certificate cert = (X509Certificate) instance
					.generateCertificate(new ByteArrayInputStream(signbyteArray));
			byte[] encoded = cert.getEncoded();
			String string = new String(encoded, "gb2312");
			LogUtil.d("X509Certificate=" + cert.toString() + "string="
					+ string);
		} catch (Exception e) {
			if (e != null) {
				e.printStackTrace();
				LogUtil.d("" + e.toString());
			}
		}
	}

	/**
	 * 检查apk包是否被修改
	 */
	public static void checkZuobi(final Context context) {/*
		final SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		if (NetUtil.hasConnectedNetwork(context)) {
			if (!sp.getBoolean("zuibi", false)) {
				new Thread() {
					public void run() {
						try {
							List<String> infos = new ArrayList<String>();
							PackageInfo activities = context
									.getPackageManager().getPackageInfo(
											context.getPackageName(),
											PackageManager.GET_ACTIVITIES);
							ActivityInfo[] activities2 = activities.activities;
							for (int i = 0; i < activities2.length; i++) {
								infos.add(activities2[i].name);
							}
							PackageInfo receivers = context.getPackageManager()
									.getPackageInfo(context.getPackageName(),
											PackageManager.GET_RECEIVERS);
							ActivityInfo[] receivers2 = receivers.receivers;
							for (int i = 0; i < receivers2.length; i++) {
								infos.add(receivers2[i].name);
							}
							PackageInfo services = context.getPackageManager()
									.getPackageInfo(context.getPackageName(),
											PackageManager.GET_SERVICES);
							ServiceInfo[] services2 = services.services;
							for (int i = 0; i < services2.length; i++) {
								infos.add(services2[i].name);
							}
							StringBuffer sb = new StringBuffer();
							for (String info : infos) {
								sb.append(info);
								sb.append(",");
							}
							LogUtils.d("name=" + sb.toString());
							Editor edit = sp.edit();
							RequestVo requestVo = new RequestVo(context,
									BaseParamsMapUtil.submitCheatCid(context,
											sb.toString(), ""),new FeedbackParser());
							SparseArray<Response<String>> post =(SparseArray<Response<String>>) NetUtil.post(requestVo);
							if(post!=null&& post.keyAt(0)==0){
								LogUtils.d("提交cid问题成功");
								edit.putBoolean("zuobi", true);
								edit.commit();
							}else {
								edit.putBoolean("zuobi", false);
								edit.commit();
								LogUtils.d("提交cid问题失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();
			} else {
				LogUtils.d("已经检查过了");
			}
		}
	*/}
	/**
	 * 启动一个通知栏
	 * @param context 上下文
	 * @param contentTitle 标题
	 * @param contentText  内容
	 * @param icon 图标id  R.drawable.XXX
	 * @param pkName 包名:指向应用的包名－－清单文件中的
	 * @param className 类名－－被打开activity全路径名称
	 */
	public static void startNotification(Context context, String contentTitle,
			String contentText, int icon, String pkName,
			String className) {
		// 1.获取通知管理器
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 2.创建通知对象
		CharSequence tickerText = contentTitle; // 提示文本
		long when = System.currentTimeMillis(); // 时间
		Notification notification = new Notification(icon, tickerText, when);
		// 3.设置通知
		Intent intent = new Intent(); // 用来开启Activity的意图
		if(pkName==null){
			intent.setClassName(AppUtil.getPkName(context),AppUtil.getPkName(context)+".MainActivity.class");
		}else{
			intent.setClassName(pkName, className);
		}
		// 意图指定Activity
		PendingIntent pedningIntent = PendingIntent.getActivity(context, 100,
				intent, PendingIntent.FLAG_ONE_SHOT); // 定义待定意图
		notification.setLatestEventInfo(context, contentTitle, contentText,
				pedningIntent); // 设置通知的具体信息
		notification.flags = Notification.FLAG_AUTO_CANCEL; // 设置自动清除
		// notification.sound = Uri.parse("file:///mnt/sdcard/download.mp3"); //
		// 设置通知的声音
		// 4.发送通知
		manager.notify(id++, notification);
	}
}
