/**   
* @Title: DbUtil.java
* @Package com.walker.autil
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年2月18日 上午9:53:54
* @version V1.0   
*/
package com.walker.autils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.walker.bean.ColumnInfo;
import com.walker.bean.TableInfo;
import com.walker.db.DataBaseManager;
import com.walker.jutil.Column.columnType;
import com.walker.jutil.SqlUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

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
	private ArrayList<Map<String, Object>> cursorList;
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
	 *  @param tableObj 数据库表实体
	 */
	public boolean insertObj(Object tableObj) {
		boolean res = true;
//		ContentValues contentValues = new ContentValues();
//		sqLiteDatabase.insert(SqlUtil.getInstance().getTableName(tableObj.getClass()), null, contentValues);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String insertSql = SqlUtil.getInstance().getInsertSql(tableObj, map,false);
		LogUtil.eToFile("插入数据sql：\n" + insertSql + "\n参数：\n" + map);
		SQLiteStatement ps = sqLiteDatabase.compileStatement(insertSql);
		
		Set<Entry<String, Object>> set = map.entrySet();
		Iterator<Entry<String, Object>> it = set.iterator();
		String type;
		Entry<String, Object> me;
		int i = 0;
		Object value;
		while (it.hasNext()) {
			i++;
			me = it.next();
			type = me.getKey();
			value = me.getValue();
			if (value == null) {
				continue;
			}
			if (type.startsWith(columnType.BLOB+"")) {
				ps.bindBlob(i, (byte[]) me.getValue());
			} else if (type.startsWith(columnType.DOUBLE+"")) {
				ps.bindDouble(i, (Double) value);
			} else if (type.startsWith(columnType.INTEGER+"")) {
				ps.bindLong(i, (Integer) value);
			} else if (type.startsWith(columnType.TEXT+"")) {
				ps.bindString(i, (String) value);
			}
		}
		if (ps.executeInsert() == -1) {
			res = false;
		}
		ps.releaseReference();
		return res;
	}
	
	/**
	 * Add by walker Date 2017年3月9日
	 * @param <T>
	 * @Description: TODO
	 * 创建实体对应的数据库表 
	 *  @param object 数据库表对应的实体
	 */
	public void insertTable(Class<Object> object){
		String insertSql = SqlUtil.getInstance().getCreateTableSql(object);
		LogUtil.eToFile("建表语句：" + insertSql);
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
		String tableName = SqlUtil.getInstance().getTableName(object.getClass());
		 ArrayList<Map<String, Object>> res = query(" select name  from sqlite_master where name='" + tableName + "'");
		 if(res != null && res.size() > 0){
			 return true;
		 }
		return false;
	}
	
	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 *  更新或创建数据库表字段  
	 *  @param object 数据库表对应实体
	 */
	public void updateTableOrCreateTable(Object object){
		
	}
	/**
	 * Add by walker Date 2017年3月21日
	 * @Description: TODO
	 * 获取所有的表信息 
	 *  @return
	 */
	public ArrayList<TableInfo> getTables(){
		String sql = "select type,name,tbl_name,rootpage,sql from sqlite_master where type = 'table'";
		ArrayList<Map<String, Object>>  list = query(sql);
		LogUtil.record2File("查询结果：\n" + list);
		ArrayList<TableInfo> tableList = new ArrayList<TableInfo>();
		TableInfo info;
		for(Map<String, Object> map :list){
			info = new TableInfo();
			info.setName(map.get("name")+"");
			info.setRootpage(map.get("rootpage")+"");
			info.setSql(map.get("sql")+"");
			info.setTbl_name(map.get("tbl_name")+"");
			info.setType(map.get("type") +"");
			tableList.add(info);
		}
		return  tableList;
	}
	
	/**
	 * Add by walker Date 2017年3月21日
	 * @Description: TODO
	 * 获取当前链接数据库所有表 
	 *  @return 返回所有表的集合，以表名为键，建表语句为key
	 */
	public HashMap<String, String> getTablesMap(){
		HashMap<String, String> tableMap = new HashMap<String, String>();
		ArrayList<Map<String, Object>>  list = query("select name,sql from sqlite_master where type = 'table'");
		for(Map<String, Object> map: list){
			tableMap.put(map.get("name")+"", map.get("sql")+"");
		}
		return tableMap;
	}
	
	/**
	 * Add by walker Date 2017年3月21日
	 * @Description: TODO
	 * 执行sql 
	 *  @param sql
	 */
	public boolean execSql(String sql){
		try {
			sqLiteDatabase.execSQL(sql);	
			return true;
		} catch (Exception e) {
			LogUtil.eToFile(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Add by walker Date 2017年3月21日
	 * @Description: TODO
	 * 	根据sql查询数据库 
	 *  @param sql 查询sql语句
	 *  @return 返回查询结果map集合，字段名为key
	 */
	public ArrayList<Map<String, Object>> query(String sql){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
		return CursorToList(cursor);
	}
	/**
	 * Add by walker Date 2017年3月21日
	 * @Description: TODO
	 * 	根据sql查询数据库 
	 *  @param sql 查询sql语句
	 *  @return 返回查询结果map集合，字段名为key
	 */
	public ArrayList<Map<String, Object>> query(String sql, String[] args){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Cursor cursor = sqLiteDatabase.rawQuery(sql, args);
		return CursorToList(cursor);
	}
	
	/**
	 * Add by walker Date 2017年3月21日
	 * @Description: TODO
	 * 数据库查询游标转化为健值对列表 ，并关闭游标
	 *  @param cursor 查询游标
	 *  @return 返回游标对应的健值对列表
	 */
	protected ArrayList<Map<String, Object>> CursorToList(Cursor cursor) {
		cursorList = new ArrayList<Map<String,Object>>();
		HashMap<String, Object> map;
		while (cursor.moveToNext()) {
			 map = new HashMap<String, Object>();
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				switch (cursor.getType(i)) {
				case Cursor.FIELD_TYPE_BLOB:
					map.put(cursor.getColumnName(i), cursor.getBlob(i));
					break;
				case Cursor.FIELD_TYPE_FLOAT:
					map.put(cursor.getColumnName(i), cursor.getFloat(i));
					break;
				case Cursor.FIELD_TYPE_INTEGER:
					map.put(cursor.getColumnName(i), cursor.getLong(i));
					break;
				case Cursor.FIELD_TYPE_NULL:
					map.put(cursor.getColumnName(i), null);
					break;
				case Cursor.FIELD_TYPE_STRING:
					map.put(cursor.getColumnName(i), cursor.getString(i));
					break;
				default:
					map.put(cursor.getColumnName(i), null);
					break;
				}
			}
			cursorList.add(map);
		}
		cursor.close();
		return cursorList;
	}
	/**
	 * Add by walker Date 2017年3月21日
	 * @Description: TODO
	 *  向表中增加字段
	 *  @param columnInfo 字段信息实体
	 *  @param name 数据库表名称
	 */
	public void addColumn(ColumnInfo columnInfo, String tableName) {
		String sql = SqlUtil.getInstance().getAddColumnSql(columnInfo, tableName);
		LogUtil.record2File("数据库升级语句：\n" + sql);
		execSql(sql);
	}
	/**
	 * Add by walker Date 2017年3月22日
	 * @Description: TODO
	 *  根据数据库表名获取数据表信息
	 *  @param tableName 数据库名称
	 *  @return 返回数据库信息
	 */
	public TableInfo getTableInfo(String tableName) {
		TableInfo tableInfo = new TableInfo();
		tableInfo.setName(tableName);
		cursorList = query("PRAGMA table_info(" + tableName + ")");
		ArrayList<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
		ColumnInfo columnInfo;
		for(Map<String, Object> map : cursorList){
			columnInfo = new ColumnInfo();
			columnInfo.setName(map.get("name")+"");
			columnInfo.setDflt_value(map.get("dflt_value")+"");
			columnInfo.setNotnull(map.get("notnull") + "");
			columnInfo.setPk(map.get("pk")+"");
			columnInfo.setType(map.get("type")+"");
			columnInfos.add(columnInfo);
		}
		tableInfo.setColumnInfos(columnInfos);
		return tableInfo;
	}
	/**
	 * Add by walker Date 2017年3月22日
	 * @Description: TODO
	 *  还原数据库
	 *  @param newTable
	 *  @param oldTable
	 */
	public void resetTable(TableInfo newTable, TableInfo oldTable) {
		if (newTable == null || oldTable == null || newTable.getName() == null
				|| !newTable.getName().equals(oldTable.getName()) || newTable.getColumnInfos() == null
				|| oldTable.getColumnInfos() == null) {
			return;
		}
		//逻辑待完善
		if(oldTable.getColumnInfos().size() == newTable.getColumnInfos().size()){
			for(ColumnInfo columnInfo: newTable.getColumnInfos()){
				for(ColumnInfo oldColumn: oldTable.getColumnInfos()){
					if(!columnInfo.getName().equals(oldColumn.getName())){
						break;
					}
				}
				return;
			}
		}
		try {
			sqLiteDatabase.beginTransaction();
			StringBuilder sbCopyTable = new StringBuilder();
			sbCopyTable.append("create table TMP_" + newTable.getName());
			sbCopyTable.append(" as select ");
			for(ColumnInfo columnInfo: newTable.getColumnInfos()){
				sbCopyTable.append(columnInfo.getName() + ", ");
			}
			sbCopyTable.replace(sbCopyTable.lastIndexOf(","), sbCopyTable.length(), " from " + newTable.getName());
			String sqlDeleteTable = "drop table if exists"+newTable.getName();
			String sqlReaName = "alter table TMP_" + newTable.getName() + " rename to " + newTable.getName();
			
			sqLiteDatabase.execSQL(sbCopyTable+"");
			sqLiteDatabase.execSQL(sqlDeleteTable);
			sqLiteDatabase.execSQL(sqlReaName);
			sqLiteDatabase.setTransactionSuccessful();
			if(sqLiteDatabase.inTransaction()){
				sqLiteDatabase.endTransaction();
			}
		} catch (Exception e) {
			sqLiteDatabase.endTransaction();
		}
		
	}
}
