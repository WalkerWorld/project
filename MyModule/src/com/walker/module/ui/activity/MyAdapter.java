/**   
* @Title: MyAdapter.java
* @Package com.walker.module.ui.activity
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年8月27日 下午10:06:34
* @version V1.0   
*/
package com.walker.module.ui.activity;

import java.util.ArrayList;

import com.example.mymodule.R;
import com.walker.module.BaseApplication;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @ClassName: MyAdapter
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2015年8月27日 下午10:06:34
 * 
 */
public class MyAdapter extends BaseAdapter {

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
//			convertView.getLayoutParams().width = 30;
			mHolder = new NumberHolder(); // 缓存类
			mHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_listview_item_number);
//			mHolder.ibDelete = (ImageButton) convertView.findViewById(R.id.ib_listview_item_delete);
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
//	public ImageButton ibDelete;
}
