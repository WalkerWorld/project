/**   
* @Title: BaseApplication.java
* @Package com.walker.app
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年3月9日 下午9:00:08
* @version V1.0   
*/
package com.walker.app;

import java.io.File;

import android.app.Application;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * @ClassName: BaseApplication
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年3月9日 下午9:00:08
 * 
 */
public class BaseApplication extends Application {
	
	static BaseApplication baseApplication;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		baseApplication = this;
	}
	
	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 * 获取当前应用程序的application对象 
	 *  @return
	 */
	public static BaseApplication getInstance(){
		return baseApplication;
	}
	
	/**
	 * 获得数据库路径，如果不存在，则创建对象对象
	 * 
	 * @param name 数据库名称
	 */
	@Override
	public File getDatabasePath(String name) {
		/*
		 * // 判断是否存在sd卡 boolean sdExist = Environment.MEDIA_MOUNTED
		 * .equals(Environment.getExternalStorageState()); if (!sdExist) {//
		 * 如果不存在, Log.e("SD卡管理：", "SD卡不存在，请加载SD卡"); return null; } else {
		 */
		String dbDir = AppConfig.getInstance().getDb_path() + name;// 数据库路径
		File dbFile = new File(dbDir);
		return dbFile;

	}

	/**
	 * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
	 * 
	 * @param name 数据库名称
	 * @param mode 启动模式
	 * @param factory 数据库创建工厂
	 */
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
		return result;
	}

	/**
	 * Android 4.0会调用此方法获取数据库。
	 * 
	 * @param name
	 * @param mode
	 * @param factory
	 * @param errorHandler
	 */
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory,
			DatabaseErrorHandler errorHandler) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
		return result;
	}
}
