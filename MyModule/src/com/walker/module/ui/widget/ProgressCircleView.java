package com.walker.module.ui.widget;



import com.walker.utils.UIUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @类名: ProgressCircleView
 * @创建时间 : 2015-3-26 上午11:20:49
 * 
 * @描述: 环形进度条的view
 * 
 */
public class ProgressCircleView extends LinearLayout
{
	private TextView	mTvText;
	private ImageView	mIvIcon;
	private int			mProgress;

	private Paint		mPaint	= new Paint();	// 画笔
	private RectF		mOval;
	private boolean		mProgressEnable;
	public float test;

	public ProgressCircleView(Context context) {
		super(context);

		init();
	}

	public ProgressCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	private void init()
	{
		// view挂载xml
//		View.inflate(getContext(), R.layout.download_view, this);
//
//		this.mIvIcon = (ImageView) findViewById(R.id.progress_circle_iv_icon);
//		this.mTvText = (TextView) findViewById(R.id.progress_circle_iv_text);
	}

	/**
	 * 设置提示的文本
	 * 
	 * @param text
	 */
	public void setTipText(String text)
	{
		mTvText.setText(text);
	}

	/**
	 * 设置提示的图片
	 * 
	 * @param resId
	 */
	public void setTipIcon(int resId)
	{
		mIvIcon.setImageResource(resId);
	}

	/**
	 * 设置当前的进度
	 * 
	 * @param progress
	 *            1-100
	 */
	public void setProgress(int progress)
	{
		this.mProgress = progress;
		// UI刷新
		invalidate();
	}

	public void setProgressEnable(boolean enable)
	{
		this.mProgressEnable = enable;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		if (!mProgressEnable) { return; }

		// 画圆形进度条
		// 画扇形
		// canvas ,paint
		if (mOval == null)
		{
			float left = mIvIcon.getLeft() + UIUtils.dip2px(1);
			float top = mIvIcon.getTop() + UIUtils.dip2px(1);
			float right = mIvIcon.getRight() - UIUtils.dip2px(1);
			float bottom = mIvIcon.getBottom() - UIUtils.dip2px(1);
			mOval = new RectF(left, top, right, bottom);
		}

		float startAngle = -90;// 开始的角度
		float sweepAngle = mProgress * 360f / 100;// 扫过的角度
		boolean useCenter = false;// 不画中间

		// 设置画笔
		mPaint.reset();
		mPaint.setColor(Color.RED);// 设置颜色
		mPaint.setAntiAlias(true);// 设置抗锯齿
		mPaint.setStyle(Style.STROKE);// 空心
//		mPaint.setStrokeWidth(UIUtils.dip2px(3));

		canvas.drawArc(mOval, startAngle, sweepAngle, useCenter, mPaint);

	}
}
