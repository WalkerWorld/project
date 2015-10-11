package com.walker.module.ui.adapter;

import java.util.List;

import com.walker.manager.ThreadManager;
import com.walker.module.constant.Constants;
import com.walker.module.manager.UiManager;
import com.walker.module.ui.holder.BaseHolder;
import com.walker.module.ui.holder.LoadMoreHolder;
import com.walker.utils.UIUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

/**
 * @类名: SuperBaseAdapter
 * @创建时间 : 2015-3-20 下午4:45:53
 * 
 * @描述: listView对应的adapter基类
 * 
 */
public abstract class SuperBaseAdapter<T> extends BaseAdapter implements OnItemClickListener {
	private static final int VIEW_TYPE_LOAD_MORE = 0; // 加载更多的view的类型
	private static final int VIEW_TYPE_LIST_NORMAL = 1; // listView
														// 的item对应的类型

	protected List<T> mDatas;

	protected LoadMoreHolder mLoadMoreHolder;
	private LoadMoreTask mLoadMoreTask;
	private AbsListView mListView;

	public SuperBaseAdapter(AbsListView listView, List<T> datas) {
		this.mDatas = datas;
		this.mListView = listView;

		mListView.setOnItemClickListener(this);
	}

	@Override
	public int getCount() {
		if (mDatas != null) {
			return mDatas.size() + 1;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mDatas != null) {
			return mDatas.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 返回 listView 中item的类型个数
	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount() + 1;
	}

	// 返回 当前条目的item是什么类型的 View
	@Override
	public int getItemViewType(int position) {
		if (position == getCount() - 1) {
			// 加载更多的类型
			return VIEW_TYPE_LOAD_MORE;
		}
		return getInnerItemViewType(position);
	}

	/**
	 * 如果子类的listView 的item的类型有多种，就去复写这个方法
	 * 
	 * @param position
	 * @return
	 */
	protected int getInnerItemViewType(int position) {
		return VIEW_TYPE_LIST_NORMAL;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder holder = null;
		if (convertView == null) {
			// 没有复用

			if (getItemViewType(position) == VIEW_TYPE_LOAD_MORE) {
				// 应该给加载更多的holder:
				// holder = getLoadMoreHolder();
			} else {
				// 1. holder初始化
				holder = getHolder(position);
			}
			// 2. 加载View
			convertView = holder.getRootView();
		} else {
			// 有复用
			holder = (BaseHolder) convertView.getTag();
		}

		if (getItemViewType(position) == VIEW_TYPE_LOAD_MORE) {
			// 设置数据
			if (hasLoadMore()) {
				// 有加载更多的功能
				// 去加载更多数据
				loadMoreData();
			} else {
				// 没有加载更多的功能
				// getLoadMoreHolder().setData(LoadMoreHolder.STATE_EMPTY);
			}

		} else {
			// 设置数据,给View铺数据
			holder.setData(mDatas.get(position));
		}

		return convertView;
	}

	/**
	 * 去加载更多的数据
	 */
	private void loadMoreData() {
		if (mLoadMoreTask != null) {
			return;
		}

		mLoadMoreTask = new LoadMoreTask();
		ThreadManager.getLongRunPool().execute(mLoadMoreTask);
	}

	/**
	 * 是否有加载更多的功能，默认有，如果子类不希望有加载更多的功能，去复写这个方法
	 * 
	 * @return
	 */
	protected boolean hasLoadMore() {
		return true;
	}

	/**
	 * 如果子类有加载更多的功能，那么必须实现加载更多的方法
	 * 
	 * @return
	 */
	protected List<T> onLoadMoreData() throws Exception {
		return null;
	}

	/**
	 * 加载更多的 holder
	 * 
	 * @return
	 */
	private LoadMoreHolder getLoadMoreHolder() {
		if (mLoadMoreHolder == null) {
			mLoadMoreHolder = new LoadMoreHolder();
		}

		return mLoadMoreHolder;
	}

	protected abstract BaseHolder<T> getHolder(int position);

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mListView instanceof ListView) {
			int headerViewsCount = ((ListView) mListView).getHeaderViewsCount();
			position = position - headerViewsCount;
		}

		int type = getItemViewType(position);
		if (VIEW_TYPE_LOAD_MORE == type) {
			// 点击的是加载更多
			int currentState = getLoadMoreHolder().getCurrentState();
			if (currentState == LoadMoreHolder.STATE_ERROR) {
				// UI刷新
				getLoadMoreHolder().setData(LoadMoreHolder.STATE_LOADING);

				// 点击重试
				loadMoreData();
			}
		} else {
			// 点击普通的item
			onInnerItemClick(parent, view, position, id);
		}
	}

	// 点击listView的普通item的点击事件,子类需要处理这个事件，必须复写此方法
	protected void onInnerItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	class LoadMoreTask implements Runnable {

		@Override
		public void run() {
			try {
				// 去网络获取数据
				final List<T> moreDatas = onLoadMoreData();

				Thread.sleep(2000);

				UiManager.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// 对数据进行判断
						if (moreDatas == null || moreDatas.size() == 0) {
							// 加载更多的View隐藏
							getLoadMoreHolder().setData(LoadMoreHolder.STATE_EMPTY);
						} else {
							int size = moreDatas.size();

							if (size < Constants.PAGE_SIZE) {
								// 说明已经没有下一页
								getLoadMoreHolder().setData(LoadMoreHolder.STATE_EMPTY);
							} else {
								getLoadMoreHolder().setData(LoadMoreHolder.STATE_LOADING);
							}

							// 把数据加到 list数据中
							mDatas.addAll(moreDatas);

							// UI刷新
							notifyDataSetChanged();
						}

					}
				});
			} catch (Exception e) {
				UiManager.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						getLoadMoreHolder().setData(LoadMoreHolder.STATE_ERROR);
					}
				});
			} finally {
				mLoadMoreTask = null;
			}
		}
	}

}
