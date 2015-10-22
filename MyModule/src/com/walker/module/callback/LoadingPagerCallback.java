/**   
* @Title: LoadingPagerCallback.java
* @Package com.walker.module.callback
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年10月16日 上午7:02:09
* @version V1.0   
*/
package com.walker.module.callback;

/**
 * @ClassName: LoadingPagerCallback
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年10月16日 上午7:02:09
 * 
 */
public interface LoadingPagerCallback {
	public static int SUCESS = 0;
	public static int ERROR = -1;
	public static int EMPTY = 1;
	
	public void loadDataResult(int flg);
}
