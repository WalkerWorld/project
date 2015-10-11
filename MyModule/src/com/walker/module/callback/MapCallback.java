/**   
* @Title: MapCallback.java
* @Package com.qixie.chebaotong.callback
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年9月10日 上午11:44:36
* @version V1.0   
*/
package com.walker.module.callback;

import com.baidu.mapapi.model.LatLng;

/**
 * @ClassName: MapCallback
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年9月10日 上午11:44:36
 * 
 */
public interface MapCallback {
	public void getCity(String city);
	public void getMyLatLng(LatLng latLng);
} 
