/**   
* @Title: BaiduMapActivity.java
* @Package com.qixie.chebaotong.ui.activity
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年8月28日 下午4:35:33
* @version V1.0   
*/
package com.walker.module.ui.activity;


import com.baidu.mapapi.SDKInitializer;
import com.example.mymodule.R;
import com.walker.module.BaseApplication;

import android.widget.Toast;

/**
 * @ClassName: BaiduMapActivity
 *			展示百度地图Activity
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年8月28日 下午4:35:33
 * 
 */
public class BaiduMapActivity extends BaseActivity {
	/* (非 Javadoc)
	 * Description:
	 */
	@Override
	protected void initView() {
		super.initView();
		SDKInitializer.initialize(BaseApplication.getApplication());
		setContentView(R.layout.activity_baidu_map);
	}
}
