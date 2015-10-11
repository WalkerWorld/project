package com.walker.module.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import android.R.integer;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.walker.utils.FileUtils;
import com.walker.utils.IOUtils;
import com.walker.utils.LogUtils;

/**
 * @类名: BaseProtocol
 * 
 * @描述: TODO
 * 
 */
public abstract class BaseProtocol<T> {

	private static final String DIR = "json";
	private static final long DURATION = 5 * 60 * 1000;
	private HttpUtils utils;

	/**
	 * 模块节点
	 * 
	 * @return
	 */
	protected abstract String getKey();

	/**
	 * 解析json
	 * 
	 * @param json
	 * @return
	 */
	protected abstract T parseJson(String json);

	/**
	 * 其他参数
	 * 
	 * @return
	 */
	protected Map<String, String> getParmaters() {
		return null;
	}

	public T loadData(int index) throws Exception {

		// 1. 到缓存中去去
		T data = getDataFromLocal(index);
		if (data != null) {
			LogUtils.d("从缓存中取数据");
			return data;
		}

		// 2.去网络中去取数据
		LogUtils.d("从网络中取数据");
		return getDataFromNet(index);
	}

	private String getExtraName() {
		Map<String, String> map = getParmaters();
		if (map != null) {

			StringBuffer sb = new StringBuffer();
			for (Map.Entry<String, String> me : map.entrySet()) {
				String key = me.getKey();
				String value = me.getValue();

				sb.append("_");
				sb.append(key);
				sb.append("_");
				sb.append(value);
			}
			return sb.toString();
		}

		return "";
	}

	private T getDataFromLocal(int index) throws Exception {
		// 存储缓存
		// 1.存储为文件 --->文件名的命名规范
		// ------> getKey() + "." + index + (paramters _key_value)
		// 2.文件内容的 --->文件内容中的规范
		// ------> 时间戳 + "\r\n" +json

		String name = getKey() + "." + index + getExtraName();
		File file = new File(FileUtils.getDir(DIR), name);

		// 如果文件不存在，说明本地没有缓存
		if (!file.exists()) {
			return null;
		}

		// 文件存在
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));

			String timeString = reader.readLine();// 时间戳
			long time = Long.valueOf(timeString);

			if (time + DURATION < System.currentTimeMillis()) {
				// 过期了
				return null;
			}

			String json = reader.readLine();// 读json
			return parseJson(json);
		} finally {
			IOUtils.close(reader);
		}
	}

	private T getDataFromNet(int index) throws Exception {
		if (utils == null) {
			utils = new HttpUtils();
		}
		String url = "http://10.0.2.2:8080/GooglePlayServer/" + getKey();

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("index", index + "");

		Map<String, String> parmaters = getParmaters();
		if (parmaters != null) {
			for (Map.Entry<String, String> me : parmaters.entrySet()) {
				params.addQueryStringParameter(me.getKey(), me.getValue());
			}
		}

		ResponseStream stream = utils.sendSync(HttpMethod.GET, url, params);

		int statusCode = stream.getStatusCode();
		if (200 == statusCode) {
			// 正确返回
			String json = stream.readString();
			LogUtils.d(json);

			// =存储到缓存中
			write2Local(index, json);

			// 解析json
			return parseJson(json);
		}
		throw new RuntimeException("服务器连接异常");
	}

	private void write2Local(int index, String json) throws Exception {
		// 存储缓存
		// 1.存储为文件 --->文件名的命名规范
		// ------> getKey() + "." + index + (paramters _key_value)
		// 2.文件内容的 --->文件内容中的规范
		// ------> 时间戳 + "\r\n" +json

		String name = getKey() + "." + index + getExtraName();
		File file = new File(FileUtils.getDir(DIR), name);

		// 往文件中写数据
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(System.currentTimeMillis() + "");// 写时间戳
			writer.write("\r\n");// 换行
			writer.write(json);
		} finally {
			IOUtils.close(writer);
		}
	}
}
