/**   
* @Title: PopCallback.java
* @Package com.walker.module.ui.activity
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年8月27日 下午10:41:33
* @version V1.0   
*/
package com.walker.module.ui.activity;

import android.view.View;
import android.widget.AdapterView;

/**
 * @ClassName: PopCallback
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年8月27日 下午10:41:33
 * 
 */
public interface PopCallback {
	public void onItemClick(AdapterView<?> parant, View view, int position, long id);
}
