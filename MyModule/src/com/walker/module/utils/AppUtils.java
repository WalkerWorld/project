/**   
* @Title: AppUtils.java
* @Package com.qixie.chebaotong.utils
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年9月2日 下午2:32:53
* @version V1.0   
*/
package com.walker.module.utils;

import com.walker.autils.StringUtil;
import com.walker.module.BaseApplication;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @ClassName: AppUtils
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年9月2日 下午2:32:53
 * 
 */
public class AppUtils {
	
	private static Editor editor;
	private static SharedPreferences sp;
	
	/**
	 * 以项目名保存应用信息（登陆用户名）
	 */
	public static final String APP = "hangxinghuche";
	public static void logOutUser(){
		//清空当前用户信息
		init();
		sp = BaseApplication.getApplication().getSp(getLoginUsername());
		editor = sp.edit();
		editor.clear();
		init();
		//将当前登陆用户名置空
		savaUsername("");
		
	}
	
	/**
	 * 当前登陆用户登陆号码
	 */
	public static String user;

	public static boolean isLogin(){
		return !StringUtil.isEmpty(getLoginUsername());
	}
	
	
	
	/**
	 * @Description: TODO
	 *		获取当前登陆用户的用户名
	 *  @return
	 *  String  当前登陆用户用户名
	 *
	 */
	public static String getLoginUsername(){
		sp = BaseApplication.getApplication().getSp(APP);
		return sp.getString("loginUser", null);
	}
	
	/**
	 * 保存登陆用户名
	 * @Description: TODO
	 *
	 *  @param username
	 *  void 
	 *
	 */
	public static void savaUsername(String username){
		init();
		sp = BaseApplication.getApplication().getSp(APP);
		editor = sp.edit();
		editor.putString("loginUser", username);
		editor.commit();
	}
	
	/**
	 * @Description: TODO
	 *		以用户名命名的sp中以键值对的形式保存用户的信心
	 *  @param key 保存值键
	 *  @param value 保存值
	 *  void 
	 *
	 */
	public static void saveUserInfo(String key, String value){
		init();
		sp = BaseApplication.getApplication().getSp(getLoginUsername());
		editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * @Description: TODO
	 *		根据键获取用户对应的信息值
	 *  @param key
	 *  @return
	 *  String  返回对应的用户信息
	 *
	 */
	public static String getUserInfo(String key){
		init();
		sp = BaseApplication.getApplication().getSp(getLoginUsername());
		return sp.getString(key, null);
	}
	
	/**
	 * 
	 * @Description: TODO
	 *		判断是否为第一次安装启动该软件
	 *  @return
	 *  boolean 
	 *		如果是首次安装启动返回true
	 */
	public static boolean isFirst(){
		init();
		sp = BaseApplication.getApplication().getSp(APP);
		if(sp.getBoolean("isFirst", false)){
			return false;
		}else{
			editor = sp.edit();
			editor.putBoolean("isFirst", true);
			editor.commit();
			return true;
		}
	}

	/**
	 * @Title: init
	 *		初始化sp与editor为null
	 * @Description: TODO
	 *
	 *  void 
	 */
	private static void init() {
		sp = null;
		editor = null;
	}
}
