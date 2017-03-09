package com.walker.jutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {
	public final static String UTF_8 = "utf-8";

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };
	
	/**
	 * 是否是中文
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 是否是英文 数字组合
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isEnglish(String str) {
		return str.matches("^[0-9a-zA-Z]+$");
	}

	/**
	 * @Description: TODO 校验是否为中文
	 * @param str
	 *            被校验字符串
	 * @return
	 */
	public static boolean isChinese(String str) {
		String regEx = "[\\u4e00-\\u9fa5]+";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find())
			return true;
		else
			return false;
	}

	/**
	 * 校验是否仅包含中英文
	 */
	public static boolean isEnglishAndChinese(String str) {
		String strReg = "[A-Za-z0-9\\-\\u4e00-\\u9fa5]+";
		return str.matches(strReg);
	}

	/**
	 * @Description: TODO
	 * 	校验字符串是否包含特殊含义字符： &
	 *  @param str
	 *  @return 包含特殊字符 返回true 否则返回false
	 */
	public static boolean isContainSparator(String str){
		String strSparator[] = new String[]{"&","\t","=","|"};
		for (int i = 0; i < strSparator.length; i++) {
			//str 不为空，且包含指定特殊字符
			if(!isEmpty(str)&&str.contains(strSparator[i])){
				return true;
			}
		}
		return false;
	}
	
	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim())  && value.trim().length()!=0) {
			return false;
		} else {
			return true;
		}
	}
	/**判断所有字符是否为空, 只要一个字符串为空则返回true*/
	public static boolean isContainEmpty(String... values) {
		for (String value : values) {
			 if(isEmpty(value)){
				 return true;//只要一个值为空，则返回false
			 }
		}
		return false;
	}
	/**
	 * Add by walker Date 2017年2月4日
	 * @Description: TODO
	 * 判断字符串是否为空 或null 
	 *  @param value
	 *  @return 字符串为空或为字符串“null” 则返回true
	 */
	public static boolean isEmptyUnNull(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim()) && value.trim().length()!=0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 验证邮箱格式
	 */
	public static boolean isEmail(String email) {
		String str = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();

	}

	/**
	 * 验证手机号码格式
	 */
	public static boolean isMobileNO(String mobiles) {
		if (isEmpty(mobiles)) {
			return false;
		}
		Pattern p = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|8[025-9])\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/** 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true */
	public static boolean isEquals(String... agrs) {
		String last = null;
		for (int i = 0; i < agrs.length; i++) {
			String str = agrs[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}

	

	/**
	 * url编码为文件路径
	 * 
	 * @Description: TODO
	 *
	 * @param path
	 *            url路径
	 * @return String
	 *
	 */
	public static String getUrlEncodePath(String path) {
		try {
			String substring1 = path.substring(0, path.lastIndexOf("/") + 1);
			String substring = path.substring(path.lastIndexOf("/") + 1);
			path = substring1 + URLEncoder.encode(substring, "utf-8");
			return path;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据文件路径获取文件类型
	 * 
	 * @param path
	 *            文件路径
	 * @return 文件后缀名
	 */
	public static String getFileType(String path) {
		return path.substring(path.lastIndexOf("."));
	}

	/**
	 * 生成Md5
	 * 
	 * @param s
	 *            字符串
	 * @return 字符串的MD5值
	 */
	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * @Description: TODO
	 *	字符串转 哈希值
	 *  @param b
	 *  @return
	 */
	public static String toHexString(byte[] b) {
		// String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	

	/**
	 * 方法描述：将一个流转换成字符串
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null)
				sb.append(line);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				is.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 * 比较两个list数据是否相同，并将数据打印到log文件中
	 * @param <T>
	 */
	public static <T> void compareList(List<T> oldList, List<T> newList, String des){
		boolean isSame = false;
		if(oldList == null && newList == null){
			isSame = true;
		}else if(oldList != null && newList != null){
			
		}
		
		if (oldList != null && newList != null) {
			StringBuilder oldSb = new StringBuilder();
			StringBuilder newSb = new StringBuilder();

			for (int i = 0; i < oldList.size(); i++) {
				oldSb.append(oldList.get(i) + "");
			}
			for (int i = 0; i < newList.size(); i++) {
				newSb.append(newList.get(i) + "");
			}
			
			
			
			if ( (oldSb+"").replace("null", "").equals((newSb+"").replace("null", ""))) {
				isSame = true;
			}
			String res = des +"\n"+ oldSb + " \n改动后\n" + newSb + "\n =======比对结果=======" +">>>>>>>>>>> \n"+ isSame + "\n\n";
			System.out.println(res);
			
			
		}else{
			System.out.println( des +"\n" + oldList + " 改动后" + newList + "\n =======比对结果=======>>>>>>" + isSame);

		}
	}

	/**
	 * 将字符串转行成double类型
	 */
	public static Double parseDouble(String s){
		try {
			return Double.parseDouble(s);			
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	/**
	 * @Description: TODO
	 *	获取当前系统时间
	 *  @return  时间格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentDate(){
		try {
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			return format.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * Add by walker Date 2017年2月4日
	 * 
	 * @Description: TODO 测试数据加工类
	 */
	private static void testDataUtil() {
		String msg = "天行健，君子以自强不息！";
		String pwd = "jsdlkfja;dlsakd";
		assert msg != null : msg;
		byte[] encryptData = DataUtil.getInstance().encrypt(msg.getBytes(), pwd);
		System.out.println("加密密文： \n" + new String(encryptData));
		byte[] decryptData = DataUtil.getInstance().decrypt(encryptData, pwd);
		System.out.println("解密明文： \n" + new String(decryptData));
	}

	

	/** 判断所有字符是否为空 */
	public static boolean isAllNoEmpty(String... values) {
		boolean res = true;
		for (String value : values) {
			if (isEmpty(value)) {
				res = false;
			}
		}
		return res;
	}

	static String fomat(double d) {
		DecimalFormat decimalFormat = new DecimalFormat(".00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
		return decimalFormat.format(d);// format 返回的是字符串
	}
}
