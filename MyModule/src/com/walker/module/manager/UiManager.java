package com.walker.module.manager;

import com.walker.module.BaseApplication;
import com.walker.utils.UIUtils;

import android.os.Handler;
import android.view.View;
import android.widget.Toast;

public class UiManager {
	static View view;
	
	public static Thread getMainThread() {
		return BaseApplication.getMainThread();
	}

	/** 获取主线程的handler */
	public static Handler getHandler() {
		return BaseApplication.getMainThreadHandler();
	}

	public static long getMainThreadId()
	{
		return BaseApplication.getMainThreadId();
	}

	public static Handler getMainThreadHandler()
	{
		return BaseApplication.getMainThreadHandler();
	}
	
	/**
	 * 执行延时操作
	 * 
	 * @param task
	 * @param delay
	 */
	public static void postDelayed(Runnable task, long delay)
	{
		getMainThreadHandler().postDelayed(task, delay);
	}

	/**
	 * 移除任务
	 * 
	 * @param task
	 */
	public static void removeCallbacks(Runnable task)
	{
		getMainThreadHandler().removeCallbacks(task);
	}
	
	/** 在主线程执行runnable */
	public static boolean post(Runnable runnable) {
		return getHandler().post(runnable);
	}

	/**
	 * 主线程中执行 任务
	 * 
	 * @param task
	 */
	public static void runOnUiThread(Runnable task)
	{
		long currentThreadId = android.os.Process.myTid();
		long mainThreadId = getMainThreadId();

		if (currentThreadId == mainThreadId)
		{
			// 如果在主线程中执行
			task.run();
		}
		else
		{
			// 需要转的主线程执行
			getMainThreadHandler().post(task);
		}
	}
	

	// 判断当前的线程是不是在主线程
	public static boolean isRunInMainThread() {
		return android.os.Process.myTid() == getMainThreadId();
	}

	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}
	/**
	 * @Description: TODO
	 * 		显示Toast
	 *
	 *  @param string 显示内容
	 *  void 
	 * 
	 */
	
	public static void showToast(final String string) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(UIUtils.getContext(), string, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public static int getColorFromRes(int id){
		return UIUtils.getContext().getResources().getColor(id);
	}
	

}
