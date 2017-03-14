/**   
* @Title: DbUtil.java
* @Package com.walker.autil
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年2月18日 上午9:53:54
* @version V1.0   
*/
package com.walker.autils;

import com.walker.db.DataBaseManager;
import com.walker.jutil.SqlUtil;

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
	public static DbUtil getInstance(){
		if(dbUtil == null){
			dbUtil = new DbUtil();
		}
		dbUtil.init();
		return dbUtil;
	}
	
	public void init(){
		if(sqLiteDatabase == null || !sqLiteDatabase.isOpen() || sqLiteDatabase.isReadOnly()){
			sqLiteDatabase = DataBaseManager.getInstance().getDataBase();
		}
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
	
	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 * 创建实体对应的数据库表 
	 *  @param object 数据库表对应的实体
	 */
	public void insertTable(Object object){
		String insertSql = SqlUtil.getInstance().getCreateTableSql(object);
		sqLiteDatabase.execSQL(insertSql);
	}

	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 * 判断实体对应的表是否存在 
	 *  @param object 
	 *  @return 如果表已存在返回true，否则返回false
	 */
	public boolean isExistTable(Object object){
		String queryTableSql = SqlUtil.getInstance().getQueryTableExistSql(object);
		return false;
	}
	
	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 *  更新数据库表字段  
	 *  @param object 数据库表对应实体
	 *  
	 */
	public void updateTable(Object object){
	}
	
	

}
