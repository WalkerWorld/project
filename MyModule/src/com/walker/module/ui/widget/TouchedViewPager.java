package com.walker.module.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @类名: TouchedViewPager

 * @描述: TODO
 * 
 */
public class TouchedViewPager extends ViewPager
{

	private float	mDownX;
	private float	mDownY;

	public TouchedViewPager(Context context) {
		super(context);
	}

	public TouchedViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
				mDownX = ev.getX();
				mDownY = ev.getY();
				getParent().requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_MOVE:
				float moveX = ev.getX();
				float moveY = ev.getY();

				float diffX = moveX - mDownX;
				float diffY = moveY - mDownY;

				if (Math.abs(diffX) > Math.abs(diffY))
				{
					// 水平滑动
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				else
				{
					// 垂直滑动
					getParent().requestDisallowInterceptTouchEvent(false);
				}

				break;
			case MotionEvent.ACTION_UP:

				break;

			default:
				break;
		}

		return super.dispatchTouchEvent(ev);
	}

}
