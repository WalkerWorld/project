package com.walker.view;

import com.walker.utils.UIUtils;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @类名: BaseListView
 * @描述: TODO
 */
public class BaseListView extends ListView {

	public BaseListView(Context context) {
		super(context);
		init();
	}

	public BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		this.setCacheColorHint(Color.TRANSPARENT);
		this.setSelector(android.R.color.transparent);
		this.setFadingEdgeLength(0);
		this.setDividerHeight(0);
		// this.setBackgroundColor(UIUtils.getColor(R.color.bg));
	}
}
