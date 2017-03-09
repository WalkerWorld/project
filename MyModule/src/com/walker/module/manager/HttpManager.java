/**   
* @Title: HttpManager.java
* @Package com.qixie.chebaotong.manager
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年9月7日 上午9:42:36
* @version V1.0   
*/
package com.walker.module.manager;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.walker.module.callback.HttpCallback;
import com.walker.module.constant.Constants;

/**
 * @ClassName: HttpManager
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年9月7日 上午9:42:36
 * 
 */
public class HttpManager {
	public static String url = "";
	public static RequestParams params;
	public static HttpUtils httpUtils = new HttpUtils();
	// private static HttpCallback callback;
	public static Gson gson = new Gson();
	/**
	 * 用于添加参数列表
	 */
	public static ArrayList<String> paramList;

	public static void sendPost(final HttpCallback callback) {
		initHttp();
		callback.init();

		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtils.e("网络连接失败： " + url + "请求参数： " + params + arg1);
				callback.faile(Constants.ERROR_NET);
				url = null;
				params = null;
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
//				LogUtils.d("网络连接成功，返回结果为：\n" + arg0.result);
				try {
					JSONObject obj = new JSONObject(arg0.result);
//					System.out.println("返回状态： " + obj.getInt("status"));
					if (200 == obj.getInt("status")) {// 网络请求成功！
						String data = obj.getString("data");
//						System.out.println("#################网路请求成功获取网络数据数据为：#################\n" + data);
						// if(!StringUtils.isEmpty(data)){
						callback.success(obj.getString("data"), gson);
						// }else{
						// callback.faile(Constants.EMPTY_DATA);
						// }

					} else {
						LogUtils.e("网络请求失败！ 失败信息： " + obj.getString("info"));
						callback.faile(Constants.ERROR_REQUEST);
					}
					url = null;
					params = null;
				} catch (JSONException e) {
					e.printStackTrace();
					LogUtils.e("解析JSON数据出错");
					callback.faile(Constants.ERROR_PARSE_JSON);

					url = null;
					params = null;
				}
			}
		});
	}

	public static void sendPostGetRes(final HttpCallback callback) {
		initHttp();
		callback.init();

		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtils.e("网络连接失败： " + url + "请求参数： " + params + arg1);
				callback.faile(Constants.ERROR_NET);

				url = null;
				params = null;
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				LogUtils.d("网络连接成功，返回结果为：\n" + arg0.result);
				System.out.println("#################网路请求成功获取网络数据数据为：#################\n" + arg0.result);
				callback.success(arg0.result, gson);

				url = null;
				params = null;
			}
		});
	}

	private static void initHttp() {
		params = new RequestParams();
	}

	/**
	 * 
	 * @Description: TODO 添加网络参数
	 * @param key
	 * @param value
	 *            void
	 *
	 */
	public static void addParams(String key, String value) {
		System.out.println("请求参数： " + key + " :  " + value);
		params.addBodyParameter(key, value);
	}
}
