package com.walker.utils;

import android.text.*;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;

public class StringUtils {
	public final static String UTF_8 = "utf-8";
	public final static int MOBILE = 0;
	public final static int EMAIL = 1;
	public final static int CAR_NUMBER = 2;
	
	
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final String REGULAR_MOBILE = "13\\d{9}|14[57]\\d{8}|15[012356789]\\d{8}|18[01256789]\\d{8}|17[0678]\\d{8}";
	private static final String REGULAR_EMAIL = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
//	private static final String REGULAR_CAR_NUMBER = "^[\u4E00-\u9FA5]{1}[A-Z0-9]{6}$)|(^[A-Z]{2}[A-Z0-9]{2}[A-Z0-9\u4E00-\u9FA5]{1}[A-Z0-9]{4}$)|(^[\u4E00-\u9FA5]{1}[A-Z0-9]{5}[挂学警军港澳]{1}$)|(^[A-Z]{2}[0-9]{5}$)|(^(08|38){1}[A-Z0-9]{4}[A-Z0-9挂学警军港澳]{1}$";
	//车牌号校验有误
	private static final String REGULAR_CAR_NUMBER = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";
	
	
	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim())
				&& !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}
	private static boolean res;
	public static boolean matches(String matchStr, int matchType){
		res = false;
		switch (matchType) {
		case MOBILE:
			res = matchStr.matches(REGULAR_MOBILE);
			break;

		case EMAIL:
			res = matchStr.matches(REGULAR_EMAIL);
			
			break;

		case CAR_NUMBER:
			res = matchStr.matches(REGULAR_CAR_NUMBER);
			break;
		default:
			break;
		}
		
		return res;
	}
	
	/**
	 * 验证邮箱格式
	 * */
	public static boolean isEmail(String email) {
		String str = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();

	}

	/**
	 * 验证手机号码格式
	 * */
	public static boolean isMobileNO(String mobiles) {
		if(isEmpty(mobiles)){
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
	 * 返回一个高亮spannable
	 * 
	 * @param content
	 *            文本内容
	 * @param color
	 *            高亮颜色
	 * @param start
	 *            起始位置
	 * @param end
	 *            结束位置
	 * @return 高亮spannable
	 */
	public static CharSequence getHighLightText(String content, int color,
			int start, int end) {
		if (TextUtils.isEmpty(content)) {
			return "";
		}
		start = start >= 0 ? start : 0;
		end = end <= content.length() ? end : content.length();
		SpannableString spannable = new SpannableString(content);
		CharacterStyle span = new ForegroundColorSpan(color);
		spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 获取链接样式的字符串，即字符串下面有下划线
	 * 
	 * @param resId
	 *            文字资源
	 * @return 返回链接样式的字符串
	 */
	public static Spanned getHtmlStyleString(int resId) {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"\"><u><b>").append(UIUtils.getString(resId))
				.append(" </b></u></a>");
		return Html.fromHtml(sb.toString());
	}

	/**
	 * url编码为文件路径
	 * @Description: TODO
	 *
	 *  @param path url路径
	 *  @return
	 *  String 
	 *
	 */
	public static String getUrlEncodePath(String path){
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
	 * @param path 文件路径
	 * @return 文件后缀名
	 */
	public static String getFileType(String path){
		try {
			String substring = path.substring(path.lastIndexOf("."));
			return substring;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getDislodgeSuffix(String name){
		try {
			if(name.lastIndexOf(".")!=-1){
				String substring = name.substring(0,name.lastIndexOf("."));
				return substring;
			}else{
				return name;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}




	public static String[] getSchoolYear(int year, int month) {
		if (month < 7) {
			year -= 1;
		}
		String[] schoolYear = new String[] { (year - 1) + "-" + year, year + "-" + (year + 1), (year + 1) + "-" + (year + 2) };
		return schoolYear;
	}
	
	/**
	 * 生成Md5
	 * 
	 * @param s 字符串
	 * @return 字符串的MD5值
	 */
	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}
	
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
	 * String转JsonArray数组
	 * @param str
	 * @return JsonArray数组
	 * @throws Exception
	 */
	public static JSONArray stringToJSONArray(String str) throws Exception {
		try {
			return new JSONArray(str);
		} catch (JSONException e) {
			throw new RuntimeException(e.getMessage() + ":" + str, e);
		}

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
}
