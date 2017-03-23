package com.walker.autils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

public class StringUtil {
	public final static String UTF_8 = "utf-8";

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
	public static CharSequence getHighLightText(String content, int color, int start, int end) {
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
	 * String转JsonArray数组
	 * 
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
