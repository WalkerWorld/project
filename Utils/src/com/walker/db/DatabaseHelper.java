/**   
* @Title: databaseHelper.java
* @Package com.walker.db
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年3月9日 下午8:55:43
* @version V1.0   
*/
package com.walker.db;

import com.walker.app.AppConfig;
import com.walker.app.BaseApplication;
import com.walker.autils.LogUtil;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** @ClassName: databaseHelper
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年3月9日 下午8:55:43
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	static DatabaseHelper helper ;
	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	private DatabaseHelper() {
		super(BaseApplication.getInstance(), AppConfig.getInstance().getDb_name(), null, AppConfig.getInstance().getDb_version());
	}


	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		LogUtil.eToFile("数据库打开");
	}


	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 * 获取数据库Helper 
	 *  @return 返回databaseHelper对象，复用已有helper
	 */
	public static DatabaseHelper getInstance(){
		if(helper == null){
			helper = new DatabaseHelper();
		}
		return helper;
	}
	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 *	获取新的Helper 
	 *  @return 返回一个新的databaseHelper对象
	 */
	public static DatabaseHelper getInstanceNew(){
		if(helper != null){
			helper.close();
			helper = new DatabaseHelper();			
		}
		helper = new DatabaseHelper();
		return helper;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		DataBaseManager.getInstance().onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		DataBaseManager.getInstance().upgrade(db, oldVersion, newVersion);
	}

}
