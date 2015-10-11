package com.walker.module.ui.activity;

import java.util.ArrayList;

import com.example.mymodule.MediaActivity;
import com.example.mymodule.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.walker.bean.RequestParameters;
import com.walker.manager.NetManager;
import com.walker.module.BaseApplication;
import com.walker.utils.LogUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @ClassName: MainActivity
 *
 * @Description: TODO
 *		测试代码管理Activity
 * @author walker
 *
 * @date 2015年9月13日 上午8:09:39
 *
 */
public class MainActivity extends BaseActivity implements OnClickListener {

	private TextView tvProvince;
	private TextView tvCity;
	private TextView tvCounty;
	private RelativeLayout rlFrag;


	@SuppressLint("NewApi")
	@Override
	protected void initView() {
		super.initView();
		
		setContentView(R.layout.activity_main);
		
		ImageView ivTest = (ImageView) findViewById(R.id.iv_test);
		
		
		ImageView ivTest2 = (ImageView) findViewById(R.id.iv_test2);
		
		rlFrag = (RelativeLayout) findViewById(R.id.rl_fragment);
//
//FragmentManager fragmentManager = getFragmentManager();
//fragmentManager.beginTransaction().replace(R.id.rl_fragment, new Fragment1()).commit();
		testPopWindow();
		
//		testNet();
		testNet2();
	}
	
	private void testNet2() {
		System.out.println("############测试网络请求＃＃＃＃＃＃＃＃＃＃＃＃＃");
		String url = "http://101.200.199.79/app/index/advList.json?position=1";
		NetManager net = new NetManager();
		RequestParameters vo = new RequestParameters();
		vo.requestUrl = url;
		vo.context = BaseApplication.getApplication();
		try {
			NetManager.get(vo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		net.getSimple(url);
	}

	private void testNet() {
		System.out.println("测试网络请求＃＃＃＃＃＃＃＃＃＃＃＃＃");
		String url = "http://101.200.199.79/app/index/advList.json?position=1";
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				System.out.println(responseInfo.result);
			} 

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("网络访问失败！！！");
			}
		});
	}

	
	
	/**
	 * @Title: testPopWindow
	 *
	 * @Description: TODO
	 *
	 *  void 
	 * 
	 */
	
	private void testPopWindow() {
		tvProvince = (TextView) findViewById(R.id.tv1);
		tvCity = (TextView) findViewById(R.id.tv2);
		tvCounty = (TextView) findViewById(R.id.tv3);
		
		tvProvince.setOnClickListener(this);
		tvCity.setOnClickListener(this);
		tvCounty.setOnClickListener(this);
	}


	@Override
	protected void initData() {
		super.initData();
//		AppInfoUtil.stratNotification(BaseApplication.getContext(), "Title", "推送消息，收到发财", R.drawable.ic_launcher, "com.example.mymodule", "com.walker.module.ui.activity.MainActivity");
	}


	/* (非 Javadoc)
	 * Description:
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv1:
			LogUtils.e("显示列表");
			new ThreeSelectorPop().showPopWidow(tvProvince, getList(), new PopCallback() {
				
				@Override
				public void onItemClick(AdapterView<?> parant, View view, int position, long id) {
					System.out.println("HOmeActivity" + position + "=======id" + id);
					switch (position) {
					case 1://展示数据库操作
//						DbUtils dbUtils = DbUtils.create(getApplicationContext(), "user_info");
////						dbUtils.save(user);
//						
//						UserDbOpenHelper helper = new UserDbOpenHelper(getApplicationContext(), "user", null, 1);
//						SQLiteDatabase database = helper.getWritableDatabase();
//						s
						Intent intent = new Intent();
						intent.setClass(BaseApplication.getApplication(), MediaActivity.class);
						startActivity(intent);
						break;
						
					case 2:
						break;

					default:
						break;
					}
				}
			});
			break;
			
		case R.id.tv2:
			Intent  intent = new Intent(this, BaiduMapActivity.class);
			startActivity(intent);
			break;
			
		case R.id.tv3:
			
			break;
		default:
			break;
		}
	}


	/**
	 * @Title: getList
	 *
	 * @Description: TODO
	 *
	 *  @return
	 *  View 
	 * 
	 */
	
	private ArrayList<String> getList() {
		ArrayList<String> list  = new ArrayList<>();
		
		for(int i = 0; i < 100; i++){
		list.add("popWi送哈哈哈" + 1000*i + i);
		}
		return list;
	}
	
	
}
