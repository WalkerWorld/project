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
public class DbUtil {
	static DbUtil dbUtils;
	public DbUtil() {}
	/**单例获取数据库工具类*/
	public static DbUtil getInstance() {
		if (dbUtils == null) {
			dbUtils = new DbUtil();
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
			Table tableAnnotation = field.getAnnotation(Table.class);
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
			Table tableAnnotation = field.getAnnotation(Table.class);
			System.out.println(field.getName() + "-name-: " + tableAnnotation.name() + "-type- :" + tableAnnotation.type() + "-value: -" );
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
}

