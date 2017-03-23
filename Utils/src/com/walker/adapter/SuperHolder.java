/**   
* @Title: MyBaseHolder.java
* @Package com.walker.module.ui.holder
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年10月14日 下午4:08:35
* @version V1.0   
*/
package com.walker.adapter;

import java.util.List;

import com.walker.app.BaseApplication;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ClassName: MyBaseHolder
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年10月14日 下午4:08:35
 * 
 */
public abstract class SuperHolder {

	protected static View view;

	/**
	 * @Description: TODO 获取条目布局
	 * @return View
	 * 
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		view = null;
		view = View.inflate(BaseApplication.getInstance(), getViewId(  position,   convertView,   parent), null);
		initFiled();
		return view;
	}

	/** 获取View对应的ID*/
	protected abstract int getViewId(int position, View convertView, ViewGroup parent);

	/** 初始化View对应的filed */
	protected abstract void initFiled();

	/**@Description: TODO 初始化条目数据 */
	public abstract void initData(int position, View convertView, ViewGroup parent, List mList);

	/** FindViewById的泛型封装，减少强转代码 */
	public static <T extends View> T findViewById(int id) {
		if(view != null)
			return (T) view.findViewById(id);
		else
			Log.e("BaseFragment", "当前View对象为空，获取子控件View失败！");
		return null;
	}
	/** FindViewById的泛型封装，减少强转代码 */
	protected  <T extends View> T findViewByID(View view, int id) {
		return (T) view.findViewById(id);
	}
}
