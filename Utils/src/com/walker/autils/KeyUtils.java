/**   
* @Title: KeyUtils.java
* @Package com.walker.utils
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年10月22日 下午10:04:23
* @version V1.0   
*/
package com.walker.autils;

import java.io.IOException;

import android.view.KeyEvent;

/**
 * @ClassName: KeyUtils
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年10月22日 下午10:04:23
 * 
 */
public class KeyUtils {

	/** 调用键盘返回键 */
	public static void keyBack() {
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
