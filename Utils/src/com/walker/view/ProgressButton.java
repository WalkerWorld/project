package com.walker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

public class ProgressButton extends Button {
	private int mProgress;
	private Drawable mProgressDrawable;

	public ProgressButton(Context context) {
		super(context);

		// mProgressDrawable =
		// context.getResources().getDrawable(R.drawable.progress_normal);
	}

	public ProgressButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		// mProgressDrawable =
		// context.getResources().getDrawable(R.drawable.progress_normal);
	}

	// 0 - 100
	public void setProgress(int progress) {
		this.mProgress = progress;

		// UI刷新
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 背景
		// 进度条

		int right = (int) (getMeasuredWidth() * mProgress / 100f + 0.5f);

		// 1. 设置形状
		mProgressDrawable.setBounds(0, 0, right, getMeasuredHeight());

		// 2. 画到canvas上
		mProgressDrawable.draw(canvas);

		super.onDraw(canvas);// 文本
	}

}
