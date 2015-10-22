/**   
* @Title: HttpCallback.java
* @Package com.qixie.chebaotong.callback
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年9月7日 上午9:51:25
* @version V1.0   
*/
package com.walker.module.callback;

import com.google.gson.Gson;

/**
 * @ClassName: HttpCallback
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年9月7日 上午9:51:25
 * 
 */
public interface HttpCallback {
	/**
	 * @Description: TODO
	 *		初始化网络参数
	 *	HttpManager.params.addBodyParameter("token", token);
	 *	HttpManager.params.addBodyParameter("carId", carId+"");
	 *	HttpManager.url = UrlConstant.BASE_URL + UrlConstant.DELETE_CAR;
	 *
	 *  void 
	 *
	 */
	public void init();
	
	/**
	 * @Description: TODO
	 *	网络请求成功返回对应JSON字符串
	 *  @param data data数据对应的字符串
	 *  void 
	 *
	 */
	public void success(String data, Gson gson);
	
	/**
	 * @Description: TODO
	 *	网络请求失败回调方法，根据状态吗code的值判别不同错误状态
	 *  @param code 错误代码
	 *  void 
	 *
	 */
	public void faile(int code);
}
