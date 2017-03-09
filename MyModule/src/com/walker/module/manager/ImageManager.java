/**   
* @Title: imageManager.java
* @Package com.qixie.chebaotong.manager
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年9月16日 下午4:32:13
* @version V1.0   
*/
package com.walker.module.manager;

import com.walker.module.utils.ImageUtil;
import com.walker.utils.LogUtils;
import com.walker.utils.StringUtils;

import android.widget.ImageView;

/**
 * @ClassName: imageManager
 *
 * @Description: TODO
 *		ImageView 加载图片工具类
 * @author walker
 *
 * @date 2015年9月16日 下午4:32:13
 * 
 */
public class ImageManager {
	
	/**
	 * 
	 * @Description: TODO
	 *		根据网络uri加载图片到ImageView
	 *  @param imageView 显示图片的ImageView
	 *  @param uri 网络图uri路径
	 *  void 
	 *	返回
	 */
	public static void showImgByUri(ImageView imageView, String uri){
		if(!StringUtils.isEmpty(uri)){
//			ImageUtil.imageLoader.displayImage(uri, imageView);
			ImageUtil.showImage(imageView, uri);
		}else{
			LogUtils.e("加载图片路径为空！");
		}
	}
	
}
