package com.walker.module.ui.activity;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/**
 * @类名: BaseActivity
 * 
 * @描述: TODO
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener{
	/**
	 * 存储全局的activity
	 */
	private static List<Activity>	mActivities	= new LinkedList<Activity>();

	/**
	 * 当前前台的activity
	 */
	private static Activity			mForegroundActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		synchronized (mActivities)
		{
			mActivities.add(this);
			
			initContentView();
//			setContentView(getViewId());
			initActionBar();
			initFiled();
			initData();
		}
	}


	
	
	
	@Override
	protected void onDestroy(){

		synchronized (mActivities)
		{
			mActivities.remove(this);
			super.onDestroy();
		}
	}

	@Override
	protected void onResume(){
		super.onResume();
		mForegroundActivity = this;
	}

	@Override
	protected void onPause(){
		super.onPause();
		mForegroundActivity = null;
	}

	/**
	 * 安全推出应用
	 * @Description: TODO
	 *
	 *  void 
	 *
	 */
	public void exitApp(){
//		 synchronized (mActivities)
//		 {
//		 for (Activity act : mActivities)
//		 {
//		 act.finish();
//		 }
//		 }

		// 线程安全的操作
		ListIterator<Activity> iterator = mActivities.listIterator();
		while (iterator.hasNext())
		{
			Activity next = iterator.next();
			next.finish();
		}

	}
	/**
	 * 初始化View的方法，子类如果有View的初始化，自己覆盖实现
	 */
	protected abstract  void initContentView();

	protected  int getViewId(){
		return 0;
		};


	protected abstract void initFiled();
	/**
	 * 加载数据的方法，自己覆盖实现
	 */
	protected abstract void initData();

	/**
	 * 初始化ActionBar的方法，子类如果有ActionBar的初始化，自己覆盖实现
	 */
	protected void initActionBar(){}

	@Override
	public abstract void onClick(View v) ;
	
	/**
	 * 获取前台activity
	 * 
	 * @return
	 */
	public static Activity getForegroundActivity(){
		return mForegroundActivity;
	}
	/** FindViewById的泛型封装，减少强转代码 */
	public  <T extends View> T findView_ById( int id) {
		return (T) findViewById(id);
	}
}
