/**   
* @Title: HttpRes.java
* @Package com.walker.bean
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年3月23日 下午11:43:54
* @version V1.0   
*/
package com.walker.bean;

import com.walker.jutil.HttpUtil.AsyncTaskListener;

/** @ClassName: HttpRes
 *	网络请求实体
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年3月23日 下午11:43:54
 * 
 */
public class HttpRequest {
	/**当前请求URL*/
	private String url;
	/**请求参数：字符串*/
	private String paramStr;
	/**请求参数字节数组*/
	private byte[] paramBytes;
	/**请求结果码：统一标准码如200ok*/
	private int resStatus;
	/**自定义返回结果码*/
	private int serverStatus;
	/**请求结果：（字符串）*/
	private String resStr;
	/**服务器返回结果：字节数组*/
	private byte[] resBytes;
	/**发起网络请求时间*/
	private String requestTime;
	/**请求结束时间*/
	private String compliteTime;
	/**扩展参数：用于临时存储任何类型数据*/
	private Object obj;
	AsyncTaskListener asyncTaskListener;
	
	public AsyncTaskListener getAsyncTaskListener() {
		return asyncTaskListener;
	}
	


	public void setAsyncTaskListener(AsyncTaskListener asyncTaskListener) {
		this.asyncTaskListener = asyncTaskListener;
	}
	


	public Object getObj() {
		return obj;
	}
	

	public void setObj(Object obj) {
		this.obj = obj;
	}
	

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getParamStr() {
		return paramStr;
	}
	
	public void setParamStr(String paramStr) {
		this.paramStr = paramStr;
	}
	
	public byte[] getParamBytes() {
		return paramBytes;
	}
	
	public void setParamBytes(byte[] paramBytes) {
		this.paramBytes = paramBytes;
	}
	
	public int getResStatus() {
		return resStatus;
	}
	
	public void setResStatus(int resStatus) {
		this.resStatus = resStatus;
	}
	
	public int getServerStatus() {
		return serverStatus;
	}
	
	public void setServerStatus(int serverStatus) {
		this.serverStatus = serverStatus;
	}
	
	public String getResStr() {
		return resStr;
	}
	
	public void setResStr(String resStr) {
		this.resStr = resStr;
	}
	
	public byte[] getResBytes() {
		return resBytes;
	}
	
	public void setResBytes(byte[] resBytes) {
		this.resBytes = resBytes;
	}
	
	public String getRequestTime() {
		return requestTime;
	}
	
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	
	public String getCompliteTime() {
		return compliteTime;
	}
	
	public void setCompliteTime(String compliteTime) {
		this.compliteTime = compliteTime;
	}
	
	
}
