package com.walker.module.ui.holder;

import android.view.View;

/**
 * @类名: BaseHolder

 * @描述: MVC中C，用来控制视图和数据，对视图和数据进行合理的显示
 * 
 */
public abstract class BaseHolder<T>
{
	protected View	mRootView;
	protected T		mData;

	public BaseHolder() {
		mRootView = initView();

		// 打标记
		mRootView.setTag(this);
	}

	protected abstract View initView();

	protected abstract void refreshUI(T data);

	public void setData(T data)
	{
		this.mData = data;

		// UI刷新
		refreshUI(mData);
	}

	public View getRootView()
	{
		return mRootView;
	}
}
