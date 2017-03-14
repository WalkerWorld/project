/**   
* @Title: ThreeSelectorPop.java
* @Package com.qixie.wooddoor.manager
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年8月27日 下午1:25:47
* @version V1.0   
*/
package com.walker.module.ui.activity;

import java.util.ArrayList;

import com.example.mymodule.R;
import com.walker.module.BaseApplication;
import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @ClassName: ThreeSelectorPop
 *  PopWindow实现三级联动
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年8月27日 下午1:25:47
 * 
 */
public class ThreeSelectorPop {

	
	
	private PopupWindow pw;
	private PopCallback callback;
	
/*	
	public void showPopWidow(View view, View listView){
		System.out.println("显示POP window。。。。。。。。。。。。。。。。。。");
		// pw = new PopupWindow(listView, view.getWidth()-4 ,
		// LayoutParams.WRAP_CONTENT);
		pw = new PopupWindow(listView, view.getWidth() + 80,
				LayoutParams.WRAP_CONTENT);

		// 设置可以使用焦点
		pw.setFocusable(true);

		// 设置popupwindow点击外部可以被关闭
		pw.setOutsideTouchable(true);
		// 设置一个popupWindow的背景
		pw.setBackgroundDrawable(new BitmapDrawable());

		// 把popupwindow显示出来, 显示的位置是: 在输入框的下面, 和输入框是连着的.
		pw.showAsDropDown(view, 2, -5);
	}
	*/
	public void showPopWidow(View view, ArrayList<String> items, PopCallback callback){
		this.items = items;
		this.callback = callback;
		System.out.println("显示POP window。。。。。。。。。。。。。。。。。。");
//		 pw = new PopupWindow(initListView(1), LayoutParams.WRAP_CONTENT ,
//		 LayoutParams.WRAP_CONTENT);
		pw = new PopupWindow(initListView(1), view.getWidth() + 80,
				LayoutParams.WRAP_CONTENT);
		// ListView lv = (ListView) listView.findViewById(R.id.lv_list);
		// 设置可以使用焦点
		pw.setFocusable(true);

		// 设置popupwindow点击外部可以被关闭
		pw.setOutsideTouchable(true);
		// 设置一个popupWindow的背景
		pw.setBackgroundDrawable(new BitmapDrawable());

		// 把popupwindow显示出来, 显示的位置是: 在输入框的下面, 和输入框是连着的.
		pw.showAsDropDown(view, 2, -5);
	}
	
	
	MyAdapter mAdapter;
	ArrayList<String> items;
	/**
	 * 初始化一个Listview,用于显示下拉列表
	 * @param flag 
	 * @return
	 */
	@SuppressLint("NewApi")
	public ListView initListView(int flag) {
		
		ListView mListView = new ListView(BaseApplication.getApplication());
		mListView.setBackgroundColor(BaseApplication.getApplication().getColorById(R.color.bg_gray));
		mListView.setDividerHeight(1);
		android.view.ViewGroup.LayoutParams layoutParams = mListView.getLayoutParams();
//		mListView.setBackgroundResource(R.drawable.bg_btn);
		// 去掉右侧垂直滑动条
		mListView.setVerticalScrollBarEnabled(false);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

				String content = items.get(position);
				System.out.println("点击内容： " + content);
				pw.dismiss();
				callback.onItemClick(arg0, arg1, position, id);
			}
		});
		
		// 设置适配器展示数据
		mAdapter = new MyAdapter(items);
		mListView.setAdapter(mAdapter);
		return mListView;
	}
    
	class MyAdapter extends BaseAdapter {

		ArrayList<String> items;
		public MyAdapter(ArrayList<String> items) {
			this.items = items;
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			NumberHolder mHolder = null;
			if(convertView == null) {
				convertView = View.inflate(BaseApplication.getApplication(), R.layout.listview_item, null);
//				convertView.getLayoutParams().width = 30;
				mHolder = new NumberHolder(); // 缓存类
				mHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_listview_item_number);
//				mHolder.ibDelete = (ImageButton) convertView.findViewById(R.id.ib_listview_item_delete);
				// 把Holder类设置给convertView对象.
				convertView.setTag(mHolder);
			} else {
				mHolder = (NumberHolder) convertView.getTag();
			}
			
			mHolder.tvNumber.setText(items.get(position));

			return convertView;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
	class NumberHolder {
		public TextView tvNumber;
//		public ImageButton ibDelete;
	}

}
