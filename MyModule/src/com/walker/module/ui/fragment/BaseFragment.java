package com.walker.module.ui.fragment;

import java.util.List;

import com.walker.module.BaseApplication;
import com.walker.module.manager.UiManager;
import com.walker.module.ui.activity.BaseActivity;
import com.walker.module.ui.pager.LoadingPager;
import com.walker.module.ui.pager.LoadingPager.LoadResult;
import com.walker.utils.LogUtils;
import com.walker.utils.UIUtils;
import com.walker.utils.ViewUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	private LoadingPager mContentView;
	protected static View view;
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {/*
		if (null == mContentView) {
			mContentView = new LoadingPager(BaseApplication.getContext()) {

				@Override
				protected LoadResult load() {
					return BaseFragment.this.Load();
				}

				@Override
				protected View createLoadedView() {
					return BaseFragment.this.createLoadedView();
				}
			};
		} else {
			ViewUtils.removeSelfFromParent(mContentView);
		}
		return mContentView;
	*/
		view = null;
		view = ViewUtils.getViewById(getViewId());
		initField();
		initView();
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
//		initView();
		super.onResume();
	}
	/**获取当前界面View对象的ID */
	protected abstract int getViewId();
	/**初始化View子控件：find ViewByid */
	 protected abstract void initField();
	/**  初始化当前View界面数据*/
	protected abstract void initView() ;
	
	
	/** FindViewById的泛型封装，减少强转代码 */
	public static <T extends View> T findViewById(int id) {
		if(view != null)
			return (T) view.findViewById(id);
		else
			Log.e("BaseFragment", "当前View对象为空，获取子控件View失败！");
		return null;
	}
	
//	protected abstract View createLoadedView();
//
//	protected abstract LoadResult Load();

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
