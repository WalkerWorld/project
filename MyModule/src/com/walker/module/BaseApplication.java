package com.walker.module;

import java.io.IOException;
import java.io.InputStream;

import com.walker.utils.UIUtils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

/**
 * @类名: BaseApplication
 * 
 * @描述: 应用程序的入口
 */
public class BaseApplication extends Application
{
	 static Context	mContext;
	 static Thread	mMainThread;
	 static long		mMainThreadId;
	 static Handler	mMainThreadHandler;
	 static Looper	mMainThreadLooper;
	 static BaseApplication mApplication;
	@Override
	public void onCreate()
	{
		super.onCreate();

		// 在应用程序入口提供全局的工具
		UIUtils.setApplication(this);
		// 上下文
		mContext = this;

		mApplication = this;
		// 主线程和子线程
		mMainThread = Thread.currentThread();

		// 主线程id
		// mMainThreadId = mMainThread.getId();
		// android.os.Process.myPid();// 进程id
		mMainThreadId = android.os.Process.myTid();// 当前线程id
		// android.os.Process.myUid();//用户id

		// 主线程handler
		mMainThreadHandler = new Handler();

		//
		mMainThreadLooper = getMainLooper();
	}

	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		BaseApplication.mContext = mContext;
	}

	public static Thread getmMainThread() {
		return mMainThread;
	}

	public static void setmMainThread(Thread mMainThread) {
		BaseApplication.mMainThread = mMainThread;
	}

	public static long getmMainThreadId() {
		return mMainThreadId;
	}

	public static void setmMainThreadId(long mMainThreadId) {
		BaseApplication.mMainThreadId = mMainThreadId;
	}

	public static Handler getmMainThreadHandler() {
		return mMainThreadHandler;
	}

	public static void setmMainThreadHandler(Handler mMainThreadHandler) {
		BaseApplication.mMainThreadHandler = mMainThreadHandler;
	}

	public static Looper getmMainThreadLooper() {
		return mMainThreadLooper;
	}

	public static void setmMainThreadLooper(Looper mMainThreadLooper) {
		BaseApplication.mMainThreadLooper = mMainThreadLooper;
	}

	public static Context getContext()
	{
		return mContext;
	}

	public static Thread getMainThread()
	{
		return mMainThread;
	}

	public static long getMainThreadId()
	{
		return mMainThreadId;
	}

	public static Handler getMainThreadHandler()
	{
		return mMainThreadHandler;
	}

	public static Looper getMainThreadLooper()
	{
		return mMainThreadLooper;
	}

	public SharedPreferences getSp(String fileName) {
		return getSharedPreferences(fileName, MODE_PRIVATE);
	}
	
	/**
	 * @Title: getApplication
	 *
	 * @Description: TODO
	 *
	 *  @return
	 *  Context 
	 * 
	 */
	
	public static BaseApplication getApplication() {
		// TODO Auto-generated method stub
		return mApplication;
	}

	/**
	 * @Title: getColor
	 *
	 * @Description: TODO
	 *
	 *  @param bgGray
	 *  @return
	 *  int 
	 * 
	 */
	
	public  int getColor(int bgGray) {
		// TODO Auto-generated method stub
		return getResources().getColor(bgGray);
	}
	
	/*  
	   * 从Assets中读取图片  
	   */  
	  public Bitmap getImageFromAssetsFile(String fileName)  
	  {  
	      Bitmap image = null;  
	      AssetManager am = getResources().getAssets();  
	      try  
	      {  
	          InputStream is = am.open(fileName);  
	          image = BitmapFactory.decodeStream(is);  
	          is.close();  
	      }  
	      catch (IOException e)  
	      {  
	          e.printStackTrace();  
	      }  
	  
	      return image;  
	  
	  }
}
