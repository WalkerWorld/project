/**   
* @Title: DbUtils.java
* @Package com.walker.java
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年2月4日 下午12:48:57
* @version V1.0   
*/
package com.walker.jutil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.walker.autils.LogUtils;
import com.walker.jutil.Column.columnType;

import android.content.SyncStatusObserver;

/**
 *  @ClassName: DbUtils
 *	数据库操作相关工具类
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年2月4日 下午12:48:57
 * 
 */
public class SqlUtil {
	static SqlUtil dbUtils;
	public SqlUtil() {}
	/**单例获取数据库工具类*/
	public static SqlUtil getInstance() {
		if (dbUtils == null) {
			dbUtils = new SqlUtil();
		}
		return dbUtils;
	}
	/**
	 * Add by walker Date 2017年2月4日
	 * @Description: TODO
	 *  获取插入数据库数据的sql语句 
	 *  @param map 插入数据的键值对
	 *  @param tableName 数据库表名称
	 *  @return 插入数据库的标准sql语句
	 */
	public String getInsertSql(Map<String, String> map, String tableName) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into " + tableName + "(");

		Set<Entry<String, String>> set = map.entrySet();
		Iterator<Entry<String, String>> it = set.iterator();
		ArrayList<String> valueList = new ArrayList<String>();
		while (it.hasNext()) {
			Map.Entry<String, String> me = it.next();
			sql.append(me.getKey() + ",");
			valueList.add(me.getValue());
		}
		sql.replace(sql.length() - 1, sql.length(), ")");
		sql.append("value(");
		for (int i = 0; i < valueList.size(); i++) {
			sql.append(valueList.get(i) + ",");
		}
		sql.replace(sql.length() - 1, sql.length(), ")");

		for (String value : valueList) {
			System.out.println(value);
		}
		
		return sql.toString();
	}
	/**
	 * Add by walker Date 2017年2月13日
	 * @Description: TODO
	 * 	根据Map获取更新sql 
	 *  @param map 
	 *  tableName－更新数据表名称（必填）
	 *  where－更新条件
	 *  @return
	 */
	public String getUpdateSql(Map<String, String> map){

		StringBuilder sql = new StringBuilder();
		sql.append("update " + map.get("tableName") + " set ");
		map.remove("tableName");
		map.remove("where");
		Set<Entry<String, String>> set = map.entrySet();
		Iterator<Entry<String, String>> it = set.iterator();
		Map.Entry<String, String> me;
		while (it.hasNext()) {
			 me = it.next();
			sql.append(me.getKey() + "='"+ me.getValue() + "', ");
		}
		sql.replace(sql.lastIndexOf(","), sql.length(), "");
		System.out.println("update: " + sql);
		return sql.toString();
	}
	
	/**
	 * Add by walker Date 2017年2月13日
	 * @Description: TODO
	 *  
	 *  @param obj
	 *  @param flag
	 *  @return
	 */
	public String getupdateSqlByObj(Object obj, int flag, String tableName){
		HashMap<String, String> map = new HashMap<String, String>();
		//指定类的字段集合
		Field[] files = obj.getClass().getDeclaredFields();
		if(StringUtil.isEmptyUnNull(tableName)){
			map.put("tableName", obj.getClass().getSimpleName());
		}else{
			map.put("tableName", tableName);
			
		}
		//遍历字段集合，获取字段信息：注解信息值、字段名称、字段值
		for(Field field: files){
			String fieldName = "";
			String filedValue = "";
			field.setAccessible(true);
			
			try {
				filedValue = field.get(obj) + "";
				System.out.println("变量值： "+ filedValue);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//获取字段注解：表名、类型
			Column tableAnnotation = field.getAnnotation(Column.class);
//			System.out.println(field.getName() + "-name-: " + tableAnnotation.name() + "-type- :" + tableAnnotation.type() + "-value: -" );
			fieldName = field.getName();
			if(flag == 1){//标记为1:仅添加有值的字段
				if(!StringUtil.isEmptyUnNull(filedValue)){
					map.put(fieldName, filedValue);
				}
			}else{//所有字段更新
				map.put(fieldName, filedValue);
			}
		}
		map.remove("serialVersionUID");
		return getUpdateSql(map); 
	}
	
	
	/**
	 * Add by walker Date 2017年1月25日
	 * @param <T>
	 * 
	 * @Description: TODO
	 * 
	 */
	public <T> void getTableByAnnotate(Object obj) {
		HashMap<String, String> map = new HashMap<String, String>();
		//指定类的字段集合
		Field[] files = obj.getClass().getDeclaredFields();
		//遍历字段集合，获取字段信息：注解信息值、字段名称、字段值
		for(Field field: files){
			String fieldName = "";
			String filedValue = "";
			String tableName = "";
			field.setAccessible(true);
			
			try {
				filedValue = field.get(obj) + "";
				System.out.println("变量值： "+ filedValue);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//获取字段注解：表名、类型
//			Annotation[] annotations = field.getAnnotations();
			Column tableAnnotation = field.getAnnotation(Column.class);
			System.out.println(field.getName() + "-name-: " + tableAnnotation.columnName() + "-type- :" + tableAnnotation.columnType() + "-value: -" );
//			for(Annotation annotation: annotations){
//				Table table = (Table) annotation;
//				try {
//					System.out.println(field.getName() + "-name-: " + table.name() + "-type- :" + table.type() + "-value: -" );
//					tableName = table.name();
//				} catch (Exception e) {
//					e.printStackTrace();
//				} 
//			}
			fieldName = field.getName();
			map.put(fieldName, filedValue);
		}
		System.out.println("map : " + getInsertSql(map, "test"));
		
	}
	
	/**
	 * Add by walker Date 2017年2月13日
	 * @Description: TODO
	 * 	将网络请求参数转化为增加数据sql 
	 *  @param tableName
	 *  @param urlStr
	 *  @return
	 */
	public String getInsertSqlByUrlParam(String tableName, String urlStr){
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO " + tableName + " (");
		StringBuilder sbValue = new StringBuilder();
		sbValue.append(" ) VALUES (");
		String[] arr = urlStr.split("&");
		String[] arrValue;
		try {
			for(int i = 0; i < arr.length; i++){
				arrValue = arr[i].split("=");
				if(i < arr.length-1){
					if(arrValue.length > 1){
					sb.append(arrValue[0].trim() + ", ");					
					sbValue.append("'"+arrValue[1].trim() + "', ");
					}else{
						for(String str: arrValue){
							System.out.println("异常：" + str);
						}
					}
				}else{
					if(arrValue.length > 1){
					sb.append(arrValue[0].trim() + " ");					
					sbValue.append("'"+ arrValue[1].trim() + "' ");
					}else{
						for(String str: arrValue){
							System.out.println("异常：" + str);
						}
					}
				}
			}
			
			sb.delete(sb.length()-1, sb.length());
			sbValue.delete(sbValue.length()-1, sbValue.length());
			sbValue.append(" ) ");
			sb.append(sbValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 *  根据实体获取建表语句
	 *  @param object
	 *  @return
	 */
	public String getCreateTableSql(Object tableObj) {
		StringBuilder sb = new StringBuilder();
		Class<Object> tableClass = (Class<Object>) tableObj.getClass();
		HashMap<String, String> map = new HashMap<String, String>();
		Table tableAnnotation = tableClass.getAnnotation(Table.class);
		sb.append("CREATE TABLE ");
		//获取数据库表名
		if(tableAnnotation == null || StringUtil.isEmptyUnNull(tableAnnotation.tableName())){
			sb.append(tableObj.getClass().getSimpleName().toUpperCase() + "(");
		}else{
			sb.append(tableAnnotation.tableName().toUpperCase()+"(");
		}
		Column columnAnnotation;
		//指定类的字段集合
		Field[] files = tableObj.getClass().getDeclaredFields();
		//遍历字段集合，获取字段信息：注解信息值、字段名称、字段值
		for(Field field: files){
			//获取字段注解：表名、类型
			columnAnnotation = field.getAnnotation(Column.class);
			//字段被注解：数据库字段
			if(columnAnnotation != null){
				if(!StringUtil.isEmpty(columnAnnotation.columnType()+"")){
					//字段名称
					if (StringUtil.isEmpty(columnAnnotation.columnName())) {
//						System.out.println(field.getName() + "-name-: " + tableAnnotation.columnName() + "-type- :" + tableAnnotation.columnType() + "\n-type: -" + field.getType() );
//						System.out.println(" " + Double.class.getName());
						sb.append(field.getName().toUpperCase() + " ");
					}else{
						sb.append(columnAnnotation.columnName().toUpperCase() + " ");
					}
					System.out.println("-------------" + columnAnnotation.columnType());
					//字段类型
					if(StringUtil.isEmpty(columnAnnotation.columnType()+"")){
						String columnName= getColumnTypeByField(field);
						System.out.println("字段名称："+field.getName() + "\t类型：" +getColumnTypeByField(field) );
						if(!StringUtil.isEmpty(columnName)){
							sb.append(columnName + " , ");
						}else{
//							sb.append("TEXT")
						}
					}else{
						sb.append( columnAnnotation.columnType() + ",");
					}
				}
			}

		}
		sb.replace(sb.lastIndexOf(","), sb.length(), ")");
		System.out.println("" + sb);
		
	
		return sb+"";
	}
	
	/**
	 * Add by walker Date 2017年3月14日
	 * @Description: TODO
	 * 根据字段类型获取对应的表字段名称 
	 *  @param field 当前处理字段对象
	 *  @return 返回对应数据库名称
	 */
	public String getColumnTypeByField(Field field) {
		String type = field.getType() + "";
		if (StringUtil.isEmpty(type)) {
			return "";
		}
		type = type.replaceFirst("class", "").trim();
		if ("boolean".equals(type.trim()) || Boolean.class.getName().equals(type.trim())) {
			return "boolean";
		} else if ("double".equals(type.trim()) || "float".equals(type.trim())
				|| Double.class.getName().equals(type.trim()) || Float.class.getName().equals(type.trim())) {
			return "double";
		} else if ("char".equals(type.trim()) || String.class.getName().equals(type.trim())) {
			return "Text";
		} else if ("byte".equals(type.trim()) || Byte.class.getName().equals(type.trim())) {
			return "BLOB";
		} else if ("int".equals(type.trim()) || Integer.class.getName().equals(type.trim())) {
			return "INTEGER";
		}
		return "";
	}
	
	/**Add by walker Date 2017年3月9日
	 * @Description: TODO
	 *  
	 *  @param object
	 *  @return
	 */
	public String getQueryTableExistSql(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
}

