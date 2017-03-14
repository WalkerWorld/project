package com.walker.module.ui.holder;


import com.example.mymodule.R;
import com.lidroid.xutils.ViewUtils;
import com.walker.autils.UIUtils;

import android.view.View;

/**
 * @类名: LoadMoreHolder
 * 
 * @描述: 加载更多的holder
 * 
 */
public class LoadMoreHolder extends BaseHolder<Integer>
{
	public static final int	STATE_LOADING	= 0;	// 加载中的状态
	public static final int	STATE_ERROR		= 1;	// 加载失败的状态
	public static final int	STATE_EMPTY		= 2;	// 没有更多数据的状态

//	@ViewInject(R.id.item_loadmore_container_retry)
	private View			mErrorView;

//	@ViewInject(R.id.item_loadmore_container_loading)
	private View			mLoadingView;
	private int				mCurrentSate;

	@Override
	protected View initView()
	{
		View view = View.inflate(UIUtils.getContext(), R.layout.item_load_more, null);

		// 注入
		ViewUtils.inject(this, view);
		mErrorView = view.findViewById(R.id.item_loadmore_container_retry);
		mLoadingView = view.findViewById(R.id.item_loadmore_container_loading);
		refreshUI(STATE_LOADING);

		return view;
	}

	@Override
	protected void refreshUI(Integer data){
		this.mCurrentSate = data;

		switch (data)
		{
			case STATE_EMPTY:
				mErrorView.setVisibility(View.GONE);
				mLoadingView.setVisibility(View.GONE);
				break;
			case STATE_ERROR:
				mErrorView.setVisibility(View.VISIBLE);
				mLoadingView.setVisibility(View.GONE);
				break;
			case STATE_LOADING:
				mErrorView.setVisibility(View.GONE);
				mLoadingView.setVisibility(View.VISIBLE);
				break;
			default:
				break;
		}
	}

	public int getCurrentState()
	{
		return mCurrentSate;
	}

}
