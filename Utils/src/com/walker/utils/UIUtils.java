package com.walker.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.walker.utils.AppUtil.OnCancelListener;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @类名: UIUtils
 * 
 * @描述: 和UI操作相关的类
 * 
 */
public class UIUtils
{
	private static ProgressDialog mProgress = null;//进度条对象
	

	private static Application application;
	
	public static void setApplication(Application applications) {
		application = applications;
	}
	
	/**
	 * 上下文的获取
	 * 
	 * @return
	 */
	public static Context getContext()
	{
		return application;
	}

	/**
	 * 获取资源
	 * 
	 * @return
	 */
	public static Resources getResources()
	{
		return getContext().getResources();
	}





	/**
	 * 
	 * @param dip
	 * @return
	 */
	public static int dip2px(int dip)
	{
		// 公式 1: px = dp * (dpi / 160)
		// 公式 2: dp = px / denistity;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;
		// metrics.densityDpi
		return (int) (dip * density + 0.5f);
	}

	public static int px2dip(int px)
	{
		// 公式 1: px = dp * (dpi / 160)
		// 公式 2: dp = px / denistity;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;
		// metrics.densityDpi
		return (int) (px / density + 0.5f);
	}


	public static String getString(int resId, Object... formatArgs)
	{
		return getResources().getString(resId, formatArgs);
	}

	public static String getPackageName()
	{
		return getContext().getPackageName();
	}
	
	/**
	 * 开启activity
	 * 
	 * @param intent
	 */
	public static void startActivity(Intent intent)
	{
		getContext().startActivity(intent);
	}

	/**
	 * Toast提示
	 * 
	 * @param message
	 */
	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * TextView是否为空
	 * 
	 * @param tv
	 * @return 是空返回true,否则返回false
	 */
	public static boolean isEmptyTextView(TextView tv) {
		if (tv == null) {
			return true;
		}
		if ("".equals(tv.getText().toString())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 创建日期及时间选择对话框
	 */
	public static Dialog dateDialog(Context context, final TextView et) {
		Dialog dialog = null;
		Calendar c = Calendar.getInstance();
		String time = et.getTag().toString();
		if (time != null && !"".equals(time)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date d = format.parse(time);
				c.setTime(d);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
				et.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
				Calendar cs = Calendar.getInstance();
				cs.set(Calendar.YEAR, year);
				cs.set(Calendar.MONTH, month);
				cs.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				et.setTag(format.format(cs.getTime()));
			}
		}, c.get(Calendar.YEAR), // 传入年份
				c.get(Calendar.MONTH), // 传入月份
				c.get(Calendar.DAY_OF_MONTH) // 传入天数
		);
		return dialog;
	}
	
	
	public static View inflate(int resId) {
		return LayoutInflater.from(getContext()).inflate(resId, null);
	}


	/** 获取文字 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/** 获取文字数组 */
	public static String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	/** 获取dimen */
	public static int getDimens(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}

	/** 获取drawable */
	public static Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	/** 获取颜色 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/** 获取颜色选择器 */
	public static ColorStateList getColorStateList(int resId) {
		return getResources().getColorStateList(resId);
	}

/*
	*//** 对toast的简易封装。线程安全，可以在非UI线程调用。 *//*
	public static void showToastSafe(final int resId) {
		showToastSafe(getString(resId));
	}

	*//** 对toast的简易封装。线程安全，可以在非UI线程调用。 *//*
	public static void showToastSafe(final String str) {
		if (isRunInMainThread()) {
			showToast(str);
		} else {
			post(new Runnable() {
				@Override
				public void run() {
					showToast(str);
				}
			});
		}
	}*/

	/**
	 * 在当前Activity中打印Toast
	 * @param str 被打印的数据
	 */
/* */
	/**
	 * 弹出进度条
	 * @param context
	 * @param title
	 * @param message
	 * @param indeterminate
	 * @param cancelable
	 * @return 进度对话框
	 */
	public static ProgressDialog showProgress(Context context,
			CharSequence title, CharSequence message, boolean indeterminate,
			boolean cancelable) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(indeterminate);
		dialog.setCancelable(false);
		dialog.setOnCancelListener(new OnCancelListener((Activity) context));
		dialog.show();
		mProgress = dialog;
		return dialog;
	}
	/**
	 * 关闭进度条对话框
	 */
	public static void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 弹出对话框
	 * @param context
	 * @param strTitle
	 * @param strText
	 * @param icon
	 * @param text
	 */
	public static void showDialog(Activity context, String strTitle,
			String strText, int icon, String text) {
		Builder tDialog = new Builder(context);
		tDialog.setIcon(icon);
		tDialog.setTitle(strTitle);
		tDialog.setMessage(strText);
		tDialog.setPositiveButton(text, null);
		tDialog.show();
	}

	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
