package com.walker.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * @类名: DrawableUtils
 * 
 * @描述: drawable xml 对应的类的工具类
 */
public class DrawableUtils
{

	public static GradientDrawable getGradientDrawable(int shape, float radius, int argb)
	{
		GradientDrawable bg = new GradientDrawable();
		bg.setShape(shape);// 设置形状
		bg.setCornerRadius(radius);// 设置圆角
		bg.setColor(argb);
		return bg;
	}

	public static StateListDrawable getStateListDrawable(Drawable normalBg, Drawable pressBg)
	{
		StateListDrawable selectorBg = new StateListDrawable();
		selectorBg.addState(new int[] { android.R.attr.state_pressed }, pressBg);
		selectorBg.addState(new int[] {}, normalBg);
		return selectorBg;
	}
}
