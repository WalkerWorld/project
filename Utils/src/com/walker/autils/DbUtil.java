/**   
* @Title: DbUtil.java
* @Package com.walker.autil
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年2月18日 上午9:53:54
* @version V1.0   
*/
package com.walker.autils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/** @ClassName: DbUtil
 *	数据库操作工具类封装
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年2月18日 上午9:53:54
 * 
 */
public class DbUtil {
	private static DbUtil dbUtil;
	private static SQLiteDatabase sqLiteDatabase;
	public DbUtil() {}
	/**数据库工具类单例实现*/
	public static DbUtil getInstance(SQLiteDatabase sqLiteDb){
		if(dbUtil == null){
			dbUtil = new DbUtil();
		}
		sqLiteDatabase = sqLiteDb;
		return dbUtil;
	}
	
	/**
	 * Add by walker Date 2017年2月18日
	 * @Description: TODO
	 *  插入实体到数据库:
	 *  数据库表名为类名，数据库字段名与实体字段名相同
	 *  @param tableObj
	 */
	public void insertObj(Object tableObj){
		ContentValues contentValues = new ContentValues();
		
		sqLiteDatabase.insert("", null, contentValues);
		
	}
}
