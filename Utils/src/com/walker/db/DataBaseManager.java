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
import java.util.Map;

import com.walker.autils.DbUtil;
import com.walker.autils.LogUtil;
import com.walker.bean.ColumnInfo;
import com.walker.bean.TableInfo;
import com.walker.jutil.SqlUtil;
import com.walker.jutil.StringUtil;

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
	static ArrayList<Class> tableList;
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
			db = dbHelper.getWritableDatabase();
		}
	}
	
	/**
	 * Add by walker Date 2017年3月9日
	 * @param <T>
	 * @Description: TODO
	 * 注册数据库表实体 :在应用启动时调用，用于数据库表创建作用
	 *  @param obj 数据库表对应的实体对象
	 */
	public static <T> void registTable(Class<T> obj){
		if(tableList == null){
			tableList = new ArrayList<Class>();
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
		LogUtil.eToFile("创建数据库  onCreate");
		this.db = db;
		for(Class<Object> objClass: tableList){
			DbUtil.getInstance().insertTable(objClass);
		}
	}
	/**
	 * Add by walker Date 2017年3月21日
	 * @Description: TODO
	 * 数据库升级时调用 
	 *  @param db 数据库对象
	 *  @param oldVersion 升级前数据库版本
	 *  @param newVersion 升级后数据库版本
	 */
	public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		this.db = db;
//		ArrayList<TableInfo> tableInfos = DbUtil.getInstance().getTables();
		Map<String, String> tables = DbUtil.getInstance().getTablesMap();
		LogUtil.eToFile("升级表" + tables);
		for(Class<Object> obj: tableList){
			TableInfo tableInfo = SqlUtil.getInstance().getTableInfo(obj.getClass());
			String createSql = tables.get(tableInfo.getName());
			if(StringUtil.isEmptyUnNull(createSql)){
				//升级建表
				DbUtil.getInstance().insertTable(obj);
			}else {
				for(ColumnInfo columnInfo: tableInfo.getColumnInfos()){
					if(!createSql.contains("("+columnInfo.getName()) && !createSql.contains(columnInfo.getName()+" ")){
						DbUtil.getInstance().addColumn(columnInfo, tableInfo.getName());
					}
				}
			}
		}
	}
	
	/**
	 * Add by walker Date 2017年3月22日
	 * @Description: TODO
	 *	重置数据库：使数据库表字段信息与实体指定保持一致
	 */
	public void resetDb() {
		Map<String, String> tables = DbUtil.getInstance().getTablesMap();
		LogUtil.eToFile("升级表" + tables);
		ArrayList<ColumnInfo> delteColumns = new ArrayList<ColumnInfo>();
//		for (Class<Object> obj : tableList) {
//			TableInfo tableInfo = SqlUtil.getInstance().getTableInfo(obj.getClass());
//			String createSql = tables.get(tableInfo.getName());
//			if (StringUtil.isEmptyUnNull(createSql)) {
//				// 升级建表
//				DbUtil.getInstance().insertTable(obj);
//			} else {
//				for (ColumnInfo columnInfo : tableInfo.getColumnInfos()) {
//					if (!createSql.contains("(" + columnInfo.getName())
//							&& !createSql.contains(columnInfo.getName() + " ")) {
//						DbUtil.getInstance().addColumn(columnInfo, tableInfo.getName());
//					}
//				}
//			}
//		}
		for (Class<Object> obj : tableList) {
			TableInfo tableInfoObj = SqlUtil.getInstance().getTableInfo(obj);
			TableInfo tableInfo = DbUtil.getInstance().getTableInfo(SqlUtil.getInstance().getTableName(obj));
			DbUtil.getInstance().resetTable(tableInfoObj, tableInfo);
			ArrayList<ColumnInfo> columnInfosObj = tableInfoObj.getColumnInfos();
			ArrayList<ColumnInfo> columnInfos = tableInfo.getColumnInfos();
			if(columnInfos != null && columnInfosObj != null && columnInfos.size() != columnInfosObj.size()){
				
			}
			String createSql = tables.get(tableInfoObj.getName());
			//根据实体，构建恢复表语句
//			for (ColumnInfo columnInfo : tableInfoObj.getColumnInfos()) {
//				if (!createSql.contains("(" + columnInfo.getName())&& !createSql.contains(columnInfo.getName() + " ")) {
//					DbUtil.getInstance().addColumn(columnInfo, tableInfoObj.getName());
//				}
//			}
		}

	}
}
