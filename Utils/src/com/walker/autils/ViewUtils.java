package com.walker.autils;

import com.walker.app.BaseApplication;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

public class ViewUtils {
	/** 把自身从父View中移除 */
	public static void removeSelfFromParent(View view) {
		if (view != null) {
			ViewParent parent = view.getParent();
			if (parent != null && parent instanceof ViewGroup) {
				ViewGroup group = (ViewGroup) parent;
				group.removeView(view);
			}
		}
	}

	/** 请求View树重新布局，用于解决中层View有布局状态而导致上层View状态断裂 */
	public static void requestLayoutParent(View view, boolean isAll) {
		ViewParent parent = view.getParent();
		while (parent != null && parent instanceof View) {
			if (!parent.isLayoutRequested()) {
				parent.requestLayout();
				if (!isAll) {
					break;
				}
			}
			parent = parent.getParent();
		}
	}

	/** 判断触点是否落在该View上 */
	public static boolean isTouchInView(MotionEvent ev, View v) {
		int[] vLoc = new int[2];
		v.getLocationOnScreen(vLoc);
		float motionX = ev.getRawX();
		float motionY = ev.getRawY();
		return motionX >= vLoc[0] && motionX <= (vLoc[0] + v.getWidth()) && motionY >= vLoc[1] && motionY <= (vLoc[1] + v.getHeight());
	}

	/** FindViewById的泛型封装，减少强转代码 */
	public static <T extends View> T findViewById(View layout, int id) {
		return (T) layout.findViewById(id);
	}
	
	static View view;
	private static Toast toast;
	/**
	 * @Description: TODO 根据View ID获取对应的View布局对象
	 * @param id
	 *            ViewID
	 * @return View View对象
	 */
	public static View getViewById(int layoutId) {
		view = null;
		try {
			view = View.inflate(UIUtils.getContext(), layoutId, null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return view;
	}

	/**
	 * Add by walker Date 2017年3月25日
	 * @Description: TODO
	 *  弹出Toast消息
	 *  @param msg 具体提示内容
	 */
	public static void showToast(String msg){
		if (toast == null) {
			toast = Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_SHORT);
		}else{
			toast.cancel();
			toast.setText(msg);
		}
		toast.show();
	}

}

