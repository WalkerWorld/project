/**   
* @Title: DataBaseManager.java
* @Package com.walker.db
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年3月9日 下午9:41:53
* @version V1.0   
*/
package com.walker.db;

import java.util.ArrayList;
import java.util.HashMap;

import com.walker.autils.DbUtil;

import android.database.sqlite.SQLiteDatabase;

/** @ClassName: DataBaseManager
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年3月9日 下午9:41:53
 * 
 */
public class DataBaseManager {
	static DataBaseManager dbBaseManager;
	ArrayList<Object> tableList;
	SQLiteDatabase db;
	DatabaseHelper dbHelper;
	private DataBaseManager() {}
	
	public static DataBaseManager getInstance(){
		if(dbBaseManager == null){
			dbBaseManager = new DataBaseManager();
		}
		dbBaseManager.initDb();
		return dbBaseManager;
	}
	
	/**
	 * Add by walker Date 2017年3月13日
	 * @Description: TODO
	 *	表实体注册完成后调用，创建数据库
	 */
	public void initDb(){
		if(dbHelper == null){
			dbHelper = DatabaseHelper.getInstanceNew();
		}
	}
	
	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 * 注册数据库表实体 :在应用启动时调用，用于数据库表创建作用
	 *  @param obj 数据库表对应的实体对象
	 */
	public void registTable(Object obj){
		if(tableList == null){
			tableList = new ArrayList<Object>();
		}
		tableList.add(obj);
	}
	
	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 * 获取原生数据库对象
	 */
	public SQLiteDatabase getDataBase(){
		return db;
	}
	
	/**
	 * Add by walker Date 2017年3月13日
	 * @Description: TODO
	 * 数据库创建时调用 
	 *  @param db
	 */
	public void onCreate(SQLiteDatabase db){
		for(Object obj: tableList){
			DbUtil.getInstance().insertTable(obj);
		}
	}
	public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		
	}
}
