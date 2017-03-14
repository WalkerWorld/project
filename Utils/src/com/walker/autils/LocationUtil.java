/**   
* @Title: LocationUtil.java
* @Package com.example.gprs
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月4日 上午9:01:47
* @version V1.0   
*/
package com.walker.autils;

import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * @ClassName: LocationUtil
 *	获取当前位置信息工具类
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年1月4日 上午9:01:47
 * 
 */
public class LocationUtil {
	/** 位置管理器：获取定位信息，及发起跟新位置请求 */
	LocationManager locationManager;
	/** 定位工具类单例对象 */
	private static LocationUtil locationUtil;
	/** 程序所在Context 对象 */
	static Context context;
	/**位置回调方法*/
	static LocationCallback mLocationCallback;

	public static Location myLocation;
	String bestProvider;
	/**是否正在定位：true正在执行定位任务，false无定位任务*/
	public boolean isLocation = false;
	
	
	public LocationUtil() {
		if (locationManager == null)
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * @Description: TODO 获取单例实例方法
	 * @param appContext
	 *            当前系统Context对象
	 * @return 定位工具类单例对象
	 */
	public static LocationUtil getInstance(Context appContext) {
		context = appContext;
		
		if (locationUtil == null) {
			locationUtil = new LocationUtil();
		}
		return locationUtil;
	}
	/**定位方式标记：0-Gps与net双重定位 1-Gps定位 2-net定位 */
	int flag;
	/**位置信息更新周期，单位毫秒 */
	long times  = 0;
	/**位置变化最小距离：当位置距离变化超过此值时，将更新位置信息 */
	float distance = 0;
	
	
	/**
	 * @Description: TODO
	 **	开启定位服务
	 *  @param flag 0-Gps与net双重定位 1-Gps定位 2-net定位 
	 *  @param times 位置信息更新周期，单位毫秒 
	 *  @param distance 位置变化最小距离：当位置距离变化超过此值时，将更新位置信息 
	 *  参数times和distance，如果参数distance不为0，则以参数distance为准；参数distance为0，则通过时间来定时更新；两者为0，则随时刷新
	 *  @param locationCallback 地址信息回调
	 */
	public void startGetLocation(int flag, long times, float distance, LocationCallback locationCallback) {
		mLocationCallback = locationCallback;
		// 启用GPS定位时，如果定位GPS定位不可用，则跳转系统设置页面，提示开启GPS定位
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && flag == 1 || flag == 0) {
			Toast.makeText(context, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
			// 返回开启GPS导航设置界面
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			// startActivityForResult(intent, 0);
			context.startActivity(intent);
			return;
		}
		
		//如果定位参数相同与系统设置的相同且正在定位，那么不再重复请求定位，否则更新参数，移除定位监听，重新请求定位
		if(flag == this.flag && times == this.times && distance == this.distance && isLocation){
			showMsg("重复定位任务：flag:" + flag + "\ttimes: " + times + "\tdistance: " + distance + "\t isLocation: " + isLocation);
			return;
		}else{
			this.flag = flag;
			this.times = times;
			this.distance = distance;
			isLocation = true;
			locationManager.removeUpdates(locationListener);
			locationManager.removeGpsStatusListener(GpsStatuslistener);
		}
		
//		bestProvider = locationManager.getBestProvider(getCriteria(), true);

		// 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
		/*
		 * 参数1，设备： GPS_PROVIDER（精度高，耗电量大，不耗费流量 ） 权限：<uses-permission
		 * android:name="android.permission.ACCESS_FINE_LOCATION"/>
		 * NETWORK_PROVIDER（精度低，省电，需要网络访问） 权限：<uses-permission
		 * android:name="android.permission.ACCESS_FINE_LOCATION"/> 或
		 * <uses-permission
		 * android:name="android.permission.ACCESS_COARSE_LOCATION"/>
		 * PASSIVE_PROVIDER（并不自己实例化去获取地理位置，而是通过getProvider获取其他的服务或者activity更新位置，
		 * 被动的获取更新。） 
		 * 参数2，位置信息更新周期，单位毫秒 
		 * 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
		 *  参数4，监听
		 * 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新
		 * 1秒更新一次，或最小位移变化超过1米更新一次；
		 */
		if (flag == 1) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, times, distance, locationListener);
		} else if (flag == 2) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, times, distance, locationListener);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, times, distance, locationListener);
		} else if (flag == 0) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, times, distance, locationListener);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, times, distance, locationListener);
		}
		locationManager.addGpsStatusListener(GpsStatuslistener);
		location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location != null){
//			mLocationCallback.location(location);
			myLocation = location;
		}
		
//		new Thread() {
//			public void run() {
//				while (true) {
//					if (System.currentTimeMillis() % 1000 == 0) {
//						handler.sendEmptyMessage(0);
//					}
//				}
//			};
//		}.start();
	}

	/**
	 * GPRS状态监听：定位、状态改变、启动定位、停止定位
	 */
	GpsStatus.Listener GpsStatuslistener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			switch (event) {
			// 第一次定位
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				Log.i("GpsStatus", "第一次定位");
				mLocationCallback.notice(3, "首次定位！");
				break;
			// 卫星状态改变
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Log.i("GpsStatus", "卫星状态改变");
				// 获取当前状态
				GpsStatus gpsStatus = locationManager.getGpsStatus(null);
				// 获取卫星颗数的默认最大值
				int maxSatellites = gpsStatus.getMaxSatellites();
				// 创建一个迭代器保存所有卫星
				Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
				int count = 0;
				while (iters.hasNext() && count <= maxSatellites) {
					GpsSatellite s = iters.next();
					count++;
				}
				showMsg("搜索到：" + count + "颗卫星");
				mLocationCallback.notice(4, "搜索到：" + count + "颗卫星");
				break;
			// 定位启动
			case GpsStatus.GPS_EVENT_STARTED:
				Log.i("GpsStatus", "定位启动");
				mLocationCallback.notice(1, "开始定位");
				break;
			// 定位结束
			case GpsStatus.GPS_EVENT_STOPPED:
				Log.i("GpsStatus", "定位结束");
				mLocationCallback.notice(2, "结束定位");
				isLocation = false;
				break;
			}
		};
	};

	private Location location;
	/**
	 * 手动刷新地址信息：隔一段时间获取一次地址信息
	 */
//	Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if (location == null) {
//				location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//				mLocationCallback.location(location);
//				myLocation = location;
//			}
//
//			if (location == null) {
//				location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//			}
//
//			if (location == null) {
//				location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//			}
//		};
//	};

	/**
	 * 返回查询条件
	 * 
	 * @return
	 */
	private Criteria getCriteria() {
		Criteria criteria = new Criteria();
		// 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		// 设置是否要求速度
		criteria.setSpeedRequired(false);
		// 设置是否允许运营商收费
		criteria.setCostAllowed(false);
		// 设置是否需要方位信息
		criteria.setBearingRequired(false);
		// 设置是否需要海拔信息
		criteria.setAltitudeRequired(false);
		// 设置对电源的需求
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}

	/**
	 * @Description: TODO
	 *	停止定位，释放定位资源
	 */
	public void stop() {
		if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
			locationManager.removeGpsStatusListener(GpsStatuslistener);
		}
		isLocation = false;
		flag = 1;
		times= 0;
		distance = 0;
	}

	/**
	 * 定位回调监听： 在位置信息发生变化时进行回调，获取地址信息
	 */
	LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			showMsg("onStatusChanged:" + provider, "");
			switch (status) {
			// GPS状态为可见时
			case LocationProvider.AVAILABLE:
				showMsg( "当前GPS状态为可见状态");
				break;
				// GPS状态为服务区外时
			case LocationProvider.OUT_OF_SERVICE:
				showMsg("当前GPS状态为服务区外状态");
				break;
				// GPS状态为暂停服务时
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				showMsg("当前GPS状态为暂停服务状态");
				break;
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
			showMsg("onProviderEnabled:" + provider, "");
		}

		@Override
		public void onProviderDisabled(String provider) {
			showMsg("onProviderDisabled" + provider, "");
		}

		@Override
		public void onLocationChanged(Location location) {
			showMsg("@@@@\nprovider：" + location.getProvider());
			showMsg("时间：" + location.getTime());
			showMsg("经度：" + location.getLongitude());
			showMsg("纬度：" + location.getLatitude());
			showMsg("海拔：" + location.getAltitude());
			//定位信息不为空
			if(location != null){
				mLocationCallback.location(location);
				myLocation = location;
			}
		}
	};

	/**
	 * @Description: TODO 显示消息
	 * @param msg
	 *            显示的具体消息内容
	 * @param flag
	 *            被显示消息的标记
	 */
	private void showMsg(String msg, String flag) {
		if (flag.trim().equals("")) {
			showMsg(msg);
		} else {
			Log.d(flag, msg);
		}
	}

	/**
	 * @Description: TODO 显示消息
	 * @param msg
	 *            显示的具体消息内容
	 */
	private void showMsg(String msg) {
		Log.d("LocationUtil", msg);
	}

	public interface LocationCallback {
		public void location( Location location);
		/**
		 * @Description: TODO
		 *	提示回调：位置开启、卫星变化等
		 *  @param flag 提示标记
		 *  flag-1 卫星定位开始
		 *  flag-2 卫星定位结束
		 *  flag-3 第一次进行卫星定位
		 *  flag-4 卫星状态发生改变，提示搜索到多少卫星
		 *  @param msg 提示信息
		 */
		public void notice(int flag, String msg);
	}
}
