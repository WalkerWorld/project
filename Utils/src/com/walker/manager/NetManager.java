package com.walker.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.walker.autils.AppUtil;
import com.walker.autils.LogUtil;
import com.walker.autils.StringUtil;
import com.walker.autils.net.NetUtils;
import com.walker.bean.RequestParameters;
import com.walker.jutil.DataUtil;

import android.content.Context;
import android.net.Proxy;
import android.net.Uri;
import android.os.Environment;

/**
 * 网络请求工具类
 */
public class NetManager {
	private static int mConTimeout = 8000;
	private static int maxSize = 1048576;
	public static int NetType = 0;//网络类型
	public static final int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
	public static final int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
	public static final int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络
	private static final String TAG = "NetUtil";
	public static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	/** 中国移动联通wap代理网关 */
	public static final String proxyMobile = "10.0.0.172";
	/** 中国电信wap代理网关 */
	public static final String proxyTel = "10.0.0.200";
	
	
	private static HttpClient getHttpClient(Context context) {
		int port = Proxy.getDefaultPort();
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, false);
		int status = NetUtils.checkNetworkType(context);
		switch (status) {
		case TYPE_CM_CU_WAP:// 移动联通wap10.0.0.172
			// LogUtils.v(TAG, "移动联通wap代理模式");
			NetType = TYPE_CM_CU_WAP;
			HttpHost httpHost = new HttpHost(proxyMobile, port);
			basicHttpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, httpHost);
			HttpConnectionParams.setConnectionTimeout(basicHttpParams, mConTimeout);
			HttpConnectionParams.setSoTimeout(basicHttpParams, mConTimeout);
			HttpConnectionParams.setSocketBufferSize(basicHttpParams, maxSize);
			return new DefaultHttpClient(basicHttpParams);
		case TYPE_OTHER_NET:// 电信,移动,联通,wifi 等net网络
			// LogUtils.v(TAG, "wifi 等net网络无代理");
			NetType = TYPE_OTHER_NET;
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, mConTimeout);
			HttpConnectionParams.setSoTimeout(httpParams, mConTimeout);
			HttpConnectionParams.setSocketBufferSize(httpParams, maxSize);
			return new DefaultHttpClient(httpParams);
		case TYPE_CT_WAP:// 电信wap 10.0.0.200
			// LogUtils.v(TAG, "电信wap代理模式");
			NetType = TYPE_CT_WAP;
			HttpHost host = new HttpHost(proxyTel, port);
			basicHttpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, host);
			HttpConnectionParams.setConnectionTimeout(basicHttpParams, mConTimeout);
			HttpConnectionParams.setSoTimeout(basicHttpParams, mConTimeout);
			HttpConnectionParams.setSocketBufferSize(basicHttpParams, maxSize);
			return new DefaultHttpClient(basicHttpParams);
		}
		return new DefaultHttpClient(basicHttpParams);
	}

	public static HttpParams setHttpParams(Context context, int connectionTimeout) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, mConTimeout);
		HttpConnectionParams.setSoTimeout(httpParams, connectionTimeout == 0 ? mConTimeout : connectionTimeout);
		HttpConnectionParams.setSocketBufferSize(httpParams, maxSize);
		return httpParams;
	}



	/**
	 * 下载文件工具类 要放在后台
	 * 
	 * @param context
	 * @param url
	 *            下载的url
	 * @param path
	 *            下载路径 为空默认为sd卡根目录
	 * @return 是否下载成功
	 */
	public static boolean urlDownloadToFile(Context context, String url, String path) {
		boolean bRet = false;
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			HttpClient httpClient = null;
			try {
				httpClient = new DefaultHttpClient(setHttpParams(context, mConTimeout));
			} catch (Exception e) {
				e.printStackTrace();
			}
			HttpGet get = new HttpGet(url);
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			File file = null;
			if (path != null) {
				file = new File(path);
				file.createNewFile();// 使用已经存在的文件路径存储文件
			} else {
				file = new File(Environment.getExternalStorageDirectory() + "/walker/",
						"" + System.currentTimeMillis());// 已当前系统时间为名
			}
			fos = new FileOutputStream(file);
			if (entity != null) {
				is = entity.getContent();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
			}
			bRet = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return bRet;
	}

	/**
	 * get方法
	 * 
	 * @param vo
	 *            请求工具类
	 * @return 返回对象
	 * @throws Exception
	 */
	public static Object get(RequestParameters vo) throws Exception {
		String response = null;
		String encodedParams = "";
		if (vo.requestDataMap != null && !vo.requestDataMap.isEmpty()) {
			// http://192.168.16.251:8080/RedBabyServer/topic
			encodedParams = encodeParameters(vo.requestDataMap);
			// page=1&pageNum=8
			// http://192.168.16.251:8080/RedBabyServer/topic?page=1&pageNum=8
			//
		}
		if (encodedParams.length() > 0) {
			if (-1 == vo.requestUrl.indexOf("?"))
				// http://192.168.16.251:8080/RedBabyServer/topic
				vo.requestUrl = vo.requestUrl + "?" + encodedParams;
			else {
				// http://192.168.16.251:8080/RedBabyServer/topic?page=1&page=1&pageNum=8
				vo.requestUrl = vo.requestUrl + "&" + encodedParams;
			}
		}
		LogUtil.d("NetUtil:get:" + "url==" + vo.requestUrl);
		// 判断本地是否有数据,有的话取本地数据
		String md5Url = DataUtil.md5(vo.requestUrl);
		String path = new File(vo.context.getCacheDir(), URLEncoder.encode(md5Url) + ".json").getAbsolutePath();
		if (vo.isSaveLocal) {
			File file = new File(path);
			if (file.exists()) {

				// 是否超时
				long savetime = System.currentTimeMillis() - file.lastModified();
				LogUtil.d("本地缓存时间:=" + savetime);
				if (savetime <= 300000L) {
					LogUtil.d("取本地缓存");
					BufferedReader reader = new BufferedReader(new FileReader(file));
					String s = "";
					StringBuffer sb = new StringBuffer();
					while ((s = reader.readLine()) != null) {
						sb.append(s);
					}
					return vo.jsonParser.parseJSON(sb.toString());
				} else {
					file.delete();// 超时删除本地数据
				}
			}
		}
		HttpGet method = new HttpGet(vo.requestUrl);
		response = httpRequest(vo.context, method, path, vo.isSaveLocal);
		if (response != null) {
			return vo.jsonParser.parseJSON(response);
		}
		return vo.jsonParser.parseJSON(null);
	}

	/**
	 * 简单请求工具类 可以放在主线程
	 * 
	 * @param urlPath
	 *            请求url
	 */
	public static void getSimple(final String urlPath) {
		new Thread() {
			public void run() {
				try {
					HttpGet httpRequest = new HttpGet(urlPath);
					HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						System.out.println("get提交成功=:" + urlPath);
					} else {
						System.out.println("get提交失败=:" + urlPath);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	/**
	 * 方法描述：post方式提交请求
	 */
	public static Object post(RequestParameters vo) throws Exception {

		HttpPost method = new HttpPost(vo.requestUrl);
		LogUtil.d("NetUtil:post:" + "url==" + vo.requestUrl);
		List<BasicNameValuePair> keyParams = new ArrayList<BasicNameValuePair>();
		if (vo.requestDataMap != null) {
			Set<String> set = vo.requestDataMap.keySet();
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = (String) vo.requestDataMap.get(key);
				if ((value != null) && (!"".equals(value))) {
					keyParams.add(new BasicNameValuePair(key, value));
				}
			}
		}
		// 判断本地是否有数据,有的话取本地数据
		String md5Url = DataUtil.md5(vo.requestUrl);
		String path = new File(vo.context.getCacheDir(), URLEncoder.encode(md5Url) + ".json").getAbsolutePath();
		if (vo.isSaveLocal) {
			File file = new File(path);
			if (vo.isSaveLocal && file.exists()) {
				// 是否超时
				long savetime = System.currentTimeMillis() - file.lastModified();
				LogUtil.d("本地缓存时间:=" + savetime);
				if (savetime <= 300000L) {
					LogUtil.d("取本地缓存");
					BufferedReader reader = new BufferedReader(new FileReader(file));
					String s = "";
					StringBuffer sb = new StringBuffer();
					while ((s = reader.readLine()) != null) {
						sb.append(s);
					}
					return vo.jsonParser.parseJSON(sb.toString());
				} else {
					file.delete();// 超时删除本地数据
				}
			}
		}

		try {
			method.setEntity(new UrlEncodedFormEntity(keyParams, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		String response = httpRequest(vo.context, method, path, true);

		if (response != null) {
			return vo.jsonParser.parseJSON(response);
		} else {
			return vo.jsonParser.parseJSON(null);
		}
	}

	/**
	 * 
	 * @param method
	 * @param path
	 *            本地缓存路径
	 * @param isSaveLocal
	 *            是否要本地缓存
	 * @return 返回的结果字符串
	 * @throws Exception
	 */
	private static String httpRequest(Context context, HttpRequestBase method, String path, boolean isSaveLocal)
			throws Exception {
		int code = -1;
		String result = null;
		try {

			HttpResponse httpResponse = getHttpClient(context).execute(method);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			LogUtil.d("NetUtil:httpRequest:statusCode=" + statusCode);
			if (200 == statusCode) {
				result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
				LogUtil.d("NetUtil:httpRequest:result=" + result);
				if (result == null || result.equals("")) {
					return null;
				}
				// 这个json字符串格式是否正确
				JSONObject resBody = AppUtil.stringToJSONObject(result);
				if (resBody == null) {
					return null;
				}
				code = resBody.optInt("code");
				if (code == 0) {
					// 如果需要本地缓存则写入本地
					if (isSaveLocal && path != null) {
						wirteJsonToLocal(path, result);
					}
				}

			}

			return result;
		} catch (Exception ioe) {
			ioe.printStackTrace();
			LogUtil.d("NetUtil:Response" + "htjx httpRequest exception:" + ioe.getMessage());
			return null;
		} finally {
			method.abort();
		}

	}


	/**
	 * 写入本地字符串进文件
	 * 
	 * @param path
	 *            路径
	 * @param content
	 *            内容
	 * @throws IOException
	 */
	private static void wirteJsonToLocal(String path, String content) throws IOException {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream out = null;
		try {
			LogUtil.d("path=" + path);
			out = new FileOutputStream(file);
			byte[] bytes = content.getBytes("UTF-8");
			out.write(bytes);
		} catch (Exception e) {
			LogUtil.d(e.toString());
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 方法描述：对请求参数进行编码(get方式)
	 */
	private static String encodeParameters(Map<String, String> map) {
		StringBuffer buf = new StringBuffer();
		Set<String> set = map.keySet();
		Iterator<String> iterator = set.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = (String) map.get(key);

			if ((key == null) || ("".equals(key)) || (value == null) || ("".equals(value))) {
				continue;
			}
			if (i != 0)
				buf.append("&");
			try {
				buf.append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			i++;
		}
		return buf.toString();
	}

}