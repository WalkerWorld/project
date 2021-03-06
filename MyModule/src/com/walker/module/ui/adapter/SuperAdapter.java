package com.walker.module.ui.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.walker.module.callback.HolderCallback;
import com.walker.module.ui.holder.SuperHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class SuperAdapter extends BaseAdapter {
	protected List mList;
	protected SuperHolder holder;
	public SuperAdapter() {
	}

	public SuperAdapter(List mList) {
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = getHolder();
			convertView = holder.getView(  position,   convertView,   parent);
			convertView.setTag(holder);
		} else {
			holder = (SuperHolder) convertView.getTag();
		}
		holder.initData(position, convertView, parent, mList);
		return convertView;
	}
	public abstract SuperHolder getHolder();

	/**
	 * @Title: getItemView
	 *
	 * @Description: TODO 具体业务逻辑实现方法
	 *
	 * @param position
	 *            数据显示item位置，与数据集合中数据的位置相对应
	 * @param convertView
	 * @param parent
	 * @return View 显示的View对象
	 *
	 */
	protected View getItemView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = holder.getView(  position,   convertView,   parent);
			convertView.setTag(holder);
		} else {
			holder = (SuperHolder) convertView.getTag();
		}
		holder.initData(position, convertView, parent, mList);
		return convertView;
	};


}
