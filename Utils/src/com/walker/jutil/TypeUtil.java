/**   
* @Title: TypeUtil.java
* @Package com.walker.jutil
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年3月9日 下午6:55:05
* @version V1.0   
*/
package com.walker.jutil;

import java.text.DecimalFormat;

/** @ClassName: TypeUtil
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年3月9日 下午6:55:05
 * 
 */
public class TypeUtil {
	
	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 * 	String 类型转换成double数字 
	 *  @param str 被转换字符串
	 *  @return 如果是合法数字字符串时返回对应数字，否则返回0
	 */
	public static double strToDouble(String str){
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	public static String getNumStr(double num){
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		return decimalFormat.format(num);
		
//		return String.format("%.2f", num);
	}
	
	/**
	 * Add by walker Date 2017年3月12日
	 * @Description: TODO
	 *  
	 *  @param date
	 */
	public static int strToInt(String intStr) {
		try {
			return Integer.parseInt(intStr);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
