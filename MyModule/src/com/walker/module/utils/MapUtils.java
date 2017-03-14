/**   
* @Title: MapUtils.java
* @Package com.qixie.chebaotong.utils
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年9月10日 上午10:18:05
* @version V1.0   
*/
package com.walker.module.utils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.walker.autils.LogUtils;
import com.walker.module.BaseApplication;
import com.walker.module.callback.MapCallback;

import android.widget.Toast;

/**
 * @ClassName: MapUtils
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年9月10日 上午10:18:05
 * 
 */
public class MapUtils implements OnGetGeoCoderResultListener{
	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	MapCallback callback;
	
	protected MapView mapview;
	private BaiduMap mBaiduMap;
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private int time;
	private static MapUtils mapUtils;
	private LocationClientOption mOption;
	
	/**
	 * 
	 */
	public MapUtils(MapCallback callback) {
		
		this.callback = callback;
		
		initMapLocation();
	}
	
	public MapUtils getInstance(MapCallback callback){
		if(mapUtils != null){
			return mapUtils;
		}else{
			return new MapUtils(callback);
		}
	}
	public MapUtils getInstance(MapCallback callback, int time){
		if(mapUtils != null){
//			startLocation();
			initMapLocation();
			return mapUtils;
			
		}else{
			return new MapUtils(callback, time);
		}
	}
	
	/**
	 * 
	 */
	public MapUtils(MapCallback callback, int time) {
		
		this.callback = callback;
		this.time = time;
		initMapLocation();
	}

	public void initMapLocation() {
		
		if(mLocationClient == null)
			mLocationClient = new LocationClient(BaseApplication.getApplication());
		if(mLocationListener == null){
			mLocationListener = new MyLocationListener();// 获取位置监听
			mLocationClient.registerLocationListener(mLocationListener);// 注册位置监听
		}
		
		if(mOption == null){
			mOption = new LocationClientOption();
			mOption.setLocationMode(LocationMode.Battery_Saving);
			mOption.setCoorType("bd09ll");// 坐标类型
			mOption.setIsNeedAddress(true);
			mOption.setOpenGps(true);// 设置开启gps定位
			mOption.setScanSpan(time < 5000 ? 5000 : time);// 设置位置刷新时间
			mLocationClient.setLocOption(mOption);
		}
		SDKInitializer.initialize(BaseApplication.getApplication());
		
		if(mSearch == null){
		// 初始化搜索模块，注册事件监听
			mSearch = GeoCoder.newInstance();
			mSearch.setOnGetGeoCodeResultListener(this);
		}
		
		startLocation();
	}
	
	
	private void startLocation(){
		// 开启定位
		if (!mLocationClient.isStarted())
			mLocationClient.start();
		
	}
	
	/**
	 * 
	 * @ClassName: MyLocationListener
	 *
	 * @Description: TODO
	 *
	 * @author walker
	 *
	 * @date 2015年8月31日 下午11:29:44
	 *
	 */
	private class MyLocationListener implements BDLocationListener {
		
		
		/*
		 * (非 Javadoc) Description:
		 * 
		 * @param arg0
		 */
		@Override
		public void onReceiveLocation(BDLocation location){

			MyLocationData mData = new MyLocationData.Builder()// 创建初始参数
					.accuracy(location.getRadius())// 精度
					.latitude(location.getLatitude())// 纬度
					.longitude(location.getLongitude())// 经度
					.build();
			
				System.out.println("获取我当前位置：" + location.getLatitude() + "####" + location.getLongitude());
				LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
				callback.getMyLatLng(latLng);
				
				try {
					// 反Geo搜索
					mSearch.reverseGeoCode(new ReverseGeoCodeOption()
							.location(latLng));					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
		}

	}
	
	
	public void stopLoc(){
		LogUtils.e("停止定位服务！");
		mSearch.destroy();
		mLocationClient.stop();
	}

	/* (非 Javadoc)
	 * Description:
	 * @param 
	 */
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//			Toast.makeText(BaseApplication.getApplication(), "抱歉，定位失败！", Toast.LENGTH_LONG)
//			.show();
			return;
		}
		
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		
	}

	/* (非 Javadoc)
	 * Description:
	 * @param arg0
	 */
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(BaseApplication.getApplication(), "抱歉，定位失败", Toast.LENGTH_LONG)
					.show();
			return;
		}
		callback.getCity(result.getAddressDetail().city);
		LogUtils.d("MapUtils获取我的位置： " + result.getAddressDetail().city);
	}
}
