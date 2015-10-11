package com.walker.module.manager;

import com.lidroid.xutils.BitmapUtils;
import com.walker.utils.UIUtils;

import android.view.View;

/**
 * @类名: BitmapHelper
 * 
 * @描述: 图片加载的工具类
 * 
 */
public class BitmapHelper
{

	private static BitmapUtils	utils;

	public static void display(View view, String uri)
	{
		if (utils == null)
		{
			utils = new BitmapUtils(UIUtils.getContext());
		}
		utils.display(view, uri);
	}

}
