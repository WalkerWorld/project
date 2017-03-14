package com.walker.module.ui.pager;

import com.example.mymodule.R;
import com.walker.autils.UIUtils;
import com.walker.manager.ThreadPoolManager;
import com.walker.module.callback.LoadingPagerCallback;
import com.walker.module.manager.UiManager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


public abstract class LoadingPager extends FrameLayout {

	// 加载默认的状态
	private static final int START_UNLOADING = -2;
	// 加载的状态
	private static final int START_LOADING = -3;
	// 失败的状态
	public static final int START_ERROR = -1;
	// 加载空的状态
	public static final int START_EMPTY = 1;
	// 加载成功的状态
	public static final int START_SUCCESS = 0;

	private int mState;
	private View loadingView;
	private View emptyView;
	private View errorView;
	private View successView;

	public LoadingPager(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public LoadingPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LoadingPager(Context context) {
		super(context);
		init();
	}

	private void init() {
		// 初始化 默认的状态
		mState = START_UNLOADING;

		loadingView = createLoadingView();

		if (null != loadingView) {
			addView(loadingView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		}
		errorView = createErrorView();

		if (null != errorView) {
			addView(errorView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		}

		emptyView = createEmptyView();
		if (null != emptyView) {
			addView(emptyView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		}

		// 显示出界面
		showSafePagerView();
	}

	private void showSafePagerView() {
		UiManager.runInMainThread(new Runnable() {

			@Override
			public void run() {
				System.out.println();
				showPageView();
			}

		});
	}

	protected void showPageView() {
		if (null != loadingView) {
			loadingView.setVisibility(mState == START_LOADING
					|| mState == START_UNLOADING ? View.VISIBLE
					: View.INVISIBLE);
		}
		if (null != errorView) {
			errorView.setVisibility(mState == START_ERROR ? View.VISIBLE
					: View.INVISIBLE);
		}
		if (null != emptyView) {
			emptyView.setVisibility(mState == START_EMPTY ? View.VISIBLE
					: View.INVISIBLE);
		}

		// 成功的界面
		if (null == successView && mState == START_SUCCESS) {
			successView = createLoadedView();
			addView(successView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		}

		if (null != successView) {
			successView.setVisibility(mState == START_SUCCESS ? View.VISIBLE
					: View.INVISIBLE);
		}
	}

	public void show() {
		if (mState == START_ERROR || mState == START_EMPTY) {
			mState = START_UNLOADING;
		}
		if (mState == START_UNLOADING) {
			mState = START_LOADING;

			ThreadPoolManager.getInstance().addTask(new TaskRunable());
		}
		/**
		 * 这个地方代码中没有写需要注意
		 */
		showSafePagerView();
	}

	/**
	 * 获取数据的接口:更改为回调方式执行，回调加载数据
	 * 
	 * @author xml_tech
	 * 
	 */
	private class TaskRunable implements Runnable {

		@Override
		public void run() {
			final LoadResult result = load(new LoadingPagerCallback() {
				
				@Override
				public void loadDataResult(int flg) {
					mState = flg;
					UiManager.runInMainThread(new Runnable() {
						
						@Override
						public void run() {
//							mState = result.getValue();
							showPageView();
						}
					});
				}
			});
			
		}

	}

	public enum LoadResult {
		ERROR(-1), EMPTY(1), SUCCESS(0);

		int value;

		LoadResult(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

	protected abstract View createLoadedView();

	protected abstract LoadResult load(LoadingPagerCallback callback);

	// 空的界面
	private View createEmptyView() {
		return UIUtils.inflate(R.layout.pager_empty);
//		return null;
	}

	// 错误界面
	private View createErrorView() {
		return UIUtils.inflate(R.layout.pager_error);
//		return null;
	}

	// 加载界面
	private View createLoadingView() {
		return UIUtils.inflate(R.layout.pager_loading);
//		return null;
	}

}
