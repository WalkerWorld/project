package com.walker.module.ui.fragment;

import java.util.List;

import com.walker.autils.UIUtils;
import com.walker.autils.ViewUtils;
import com.walker.module.callback.LoadingPagerCallback;
import com.walker.module.ui.pager.LoadingPager;
import com.walker.module.ui.pager.LoadingPager.LoadResult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class SuperFragment extends Fragment {

	private LoadingPager mContentView;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		if (null == mContentView) {
			mContentView = new LoadingPager(UIUtils.getContext()) {

				// @Override
				// protected LoadResult load() {
				// return SuperFragment.this.Load();
				// }

				@Override
				protected View createLoadedView() {
					return SuperFragment.this.createLoadedView();
				}

				@Override
				protected LoadResult load(LoadingPagerCallback callback) {
					// TODO Auto-generated method stub
					return SuperFragment.this.Load(callback);
				}

			
			};
		} else {
			ViewUtils.removeSelfFromParent(mContentView);
		}
		return mContentView;
	}

	protected abstract View createLoadedView();

	protected abstract LoadResult Load(LoadingPagerCallback callback);

	// 展示具体的页面
	public void show() {
		if (null != mContentView) {
			mContentView.show();
		}
	}

	// 检查服务器返回的数据情况
	protected LoadResult check(Object obj) {
		if (null == obj) {
			return LoadResult.ERROR;
		}
		if (obj instanceof List) {
			List list = (List) obj;
			if (list.size() == 0) {
				return LoadResult.EMPTY;
			}
		}
		return LoadResult.SUCCESS;
	}
}
