/**   
* @Title: ReflectUtils.java
* @Package com.walker.java
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年2月7日 下午8:14:19
* @version V1.0   
*/
package com.walker.jutil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: ReflectUtils
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年2月7日 下午8:14:19
 * 
 */
public class ReflectUtil {

	/**
	 * Add by walker Date 2017年2月7日
	 * 
	 * @Description: TODO 比较两个对象的值
	 * @param object1
	 *            对象1（源数据）
	 * @param object2
	 *            对象2（比较数据）
	 */
	public static void copareObj(Object object1, Object object2) {
		Field[] fields = object1.getClass().getDeclaredFields();
		Field[] fields2 = object2.getClass().getDeclaredFields();
		String fieldName1;
		String fieldName2;
		StringBuilder sb = new StringBuilder();
		sb.append("原数据：" + object1.getClass().getName() + "1, --比较数据：" + object2.getClass().getName());
		String fieldValue1;
		String fieldValue2;
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		sb1.append("{");
		sb2.append("{");
		for (Field field : fields) {
			field.setAccessible(true);
			fieldName1 = field.getName();

			for (Field field2 : fields2) {
				field2.setAccessible(true);
				fieldName2 = field2.getName();
				if (fieldName2.equals(fieldName1)) {
					
					try {
						fieldValue2 = (String) field2.get(object2);
						fieldValue1 = (String) field.get(object1);
						sb1.append("\"" + fieldName1 + "\":\"" + (/**StringUtils.isEmpty(fieldValue1) ? "\t":*/fieldValue1) + "\",");
						sb2.append("\"" + fieldName2 + "\":\"" + (/**StringUtils.isEmpty(fieldValue2) ? "\t":*/fieldValue2) + "\",");
						if (!StringUtil.isEmpty(fieldValue1) || !StringUtil.isEmpty(fieldValue2)) {
							if (!(fieldValue1 + "").equals(fieldValue2 +"")) {// 值不同
								sb.append("[" + fieldName1 + ",原：" + fieldValue1 + ",较：" + fieldValue2 + "]");
							}
						}

					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		}
		sb1.replace(sb1.length()-1, sb1.length(), "");
		sb2.replace(sb2.length()-1, sb2.length(), "");
		sb1.append("}");
		sb2.append("}");
		System.out.println("比较结果： \n" + sb);
		System.out.println(getJsonByObj(object1,0));
		System.out.println(sb2);
	}
	
	/**
	 * Add by walker Date 2017年2月7日
	 * @Description: TODO
	 * 	获取对象对应的JSON数据 
	 *  @param object
	 *  @param flag 0-获取实体所有字段；1-获取实体中有值的字段。
	 *  @return 返回对象的json字符串
	 */
	public static String getJsonByObj(Object object, int flag){
		StringBuilder sb = new StringBuilder();
		Field[] fields = object.getClass().getDeclaredFields();
		sb.append("{");
		for(Field field : fields){
			field.setAccessible(true);
			try {
				if(flag == 0){
					sb.append("\"" + field.getName() + "\":\"" +field.get(object) + "\",");
				}else if(!StringUtil.isEmpty("" + field.get(object))){
					sb.append("\"" + field.getName() + "\":\"" +field.get(object) + "\",");
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		sb.replace(sb.length()-1, sb.length(), "");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Add by walker Date 2017年2月8日
	 * @Description: TODO
	 * 	将实体的List集合转化成对应的JSON字符串 
	 *  @param obLists 实体List集合
	 *  @param flag 0-获取实体所有字段；1-获取实体中有值的字段。
	 *  @return 返回集合对应的JSON字符串
	 */
	public static <T> String getJsonByListObj(List<T> obLists, int flag) {
		StringBuilder sb = new StringBuilder();
		// Field[] fields = obLists.get(0).getClass().getDeclaredFields();
		Object object1 = obLists.get(0);
		sb.append("{\"");
		sb.append(object1.getClass().getSimpleName() + "\":[");
		for (Object object : obLists) {
			sb.append(getJsonByObj(object, flag)+",");
		}
		 sb.replace(sb.length()-1, sb.length(), "");
		sb.append("]}");
		System.out.println("组合：  \n" +sb );
		return sb.toString();
	}
	
	/**
	 * Add by walker Date 2017年2月18日
	 * @Description: TODO
	 *  将实体转化为Map集合：key－表列名； value－实际值
	 *  @param object 转化实体对象
	 *  @param isNull 是否转化为null的值
	 *  @return 返回对象对应的Map集合
	 */
	public static HashMap<String, String> getMapByObj(Object object, boolean isNull) {
		HashMap<String, String> map = new HashMap<String, String>();
		// 指定类的字段集合
		Field[] files = object.getClass().getDeclaredFields();
		//表名为列名
		map.put("tableName", object.getClass().getSimpleName());
		// 遍历字段集合，获取字段信息：注解信息值、字段名称、字段值
		for (Field field : files) {
			String fieldName = "";
			String filedValue = "";
			field.setAccessible(true);

			try {
				filedValue = field.get(object) + "";
				System.out.println("变量值： " + filedValue);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// 获取字段注解：表名、类型
			Column tableAnnotation = field.getAnnotation(Column.class);
			// System.out.println(field.getName() + "-name-: " +
			// tableAnnotation.name() + "-type- :" + tableAnnotation.type() +
			// "-value: -" );
			fieldName = field.getName();
			if (!isNull) {// 标记为1:仅添加有值的字段
				if (!StringUtil.isEmptyUnNull(filedValue)) {
					map.put(fieldName, filedValue);
				}
			} else {// 所有字段更新
				map.put(fieldName, filedValue);
			}
		}
		return map;
	}
}
