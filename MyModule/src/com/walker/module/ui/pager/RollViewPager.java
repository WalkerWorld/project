package com.walker.module.ui.pager;

import java.util.List;

import com.example.mymodule.R;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RollViewPager extends ViewPager {

	private List<View> viewList;
	//显示当前viewpager中文字集合
	private List<String> titleList;
	//需要去管理显示内容的TextView
	private TextView top_news_title;
	//显示当前viewpager中图片的链接地址
	private List<Bitmap> imageUrlList;
	private Context context;
	private BitmapUtils bitmapUtils;
	private MyRollPagerAdapter myRollPagerAdapter;
	private int currentPosition = 0;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//延续当前3秒一次的滚动状态
			RollViewPager.this.setCurrentItem(currentPosition);
			startRoll();
		};
	};


	public boolean dispatchTouchEvent(android.view.MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//放行(不允许拦截事件)
			getParent().requestDisallowInterceptTouchEvent(true);
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			if(Math.abs(moveX-downX)>Math.abs(moveY-downY)){
				getParent().requestDisallowInterceptTouchEvent(true);
			}else{
				//夫控件可以去拦截事件
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			//夫控件可以去拦截事件
			getParent().requestDisallowInterceptTouchEvent(false);
			break;
		case MotionEvent.ACTION_UP:
			break;
		
		}
		return super.dispatchTouchEvent(ev);
	};
	
	
	private RunnableTask runnableTask;
	private int downX;
	private int downY;
	private onPageClick pageClick;	
	public RollViewPager(Context context) {
		super(context);
		this.context = context;
	}
	
	public RollViewPager(Context context,final List<View> viewList,onPageClick pageClick) {
		super(context);
		this.context = context;
		this.pageClick = pageClick;
		this.viewList = viewList;
		
		bitmapUtils = new BitmapUtils(context);
		runnableTask = new RunnableTask();
		
		setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				
				if(top_news_title!=null && titleList!=null && titleList.size()>0){
					top_news_title.setText(titleList.get(arg0));
				}
				
				for(int i=0;i<viewList.size();i++){
					if(i == arg0){
						viewList.get(i).setBackgroundResource(R.drawable.dot_focus);
					}else{
						viewList.get(i).setBackgroundResource(R.drawable.dot_normal);
					}
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	//移除view调用的方法
	@Override
	protected void onDetachedFromWindow() {
		//清除当前handler维护的任务
		handler.removeCallbacksAndMessages(null);
		super.onDetachedFromWindow();
	}
	
	
	public void setTitleList(List<String> titleList, TextView top_news_title) {
		this.titleList = titleList;
		this.top_news_title = top_news_title;
		
		if(top_news_title!=null && titleList!=null && titleList.size()>0){
			top_news_title.setText(titleList.get(0));
		}
	}

	public void setImageUrlList(List<Bitmap> imageUrlList) {
		this.imageUrlList = imageUrlList;
	}

	public void startRoll() {
		//滚动viewpager的方法
		if(myRollPagerAdapter  == null){
			myRollPagerAdapter = new MyRollPagerAdapter();
			this.setAdapter(myRollPagerAdapter);
		}else{
			myRollPagerAdapter.notifyDataSetChanged();
		}
		//滚动，按时滚动
		handler.postDelayed(runnableTask, 3000);
	}
	
	class RunnableTask implements Runnable{
		@Override
		public void run() {
			currentPosition = (currentPosition+1)%(imageUrlList.size());
			//发送一条空消息
			handler.obtainMessage().sendToTarget();
		}
	}
	
	public interface onPageClick{
		public void onclick(int position);
	}
	
	class MyRollPagerAdapter extends PagerAdapter{

		protected static final String tag = "MyRollPagerAdapter";

		@Override
		public int getCount() {
			return imageUrlList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = View.inflate(context,R.layout.viewpager_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			//异步加载图片，并且将图片添加到指定控件上
//			bitmapUtils.display(imageView, imageUrlList.get(position));
			Bitmap bitmap = imageUrlList.get(position);
//			imageView.setB
			((RollViewPager)container).addView(view);
			
			view.setOnTouchListener(new OnTouchListener() {
				private int downX;
				private long downTime;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						handler.removeCallbacksAndMessages(null);
						downTime = System.currentTimeMillis();
						downX = (int) event.getX();
						break;
					case MotionEvent.ACTION_UP:
						startRoll();
						if(System.currentTimeMillis() - downTime<500 && downX == event.getX()){
							//触发点击事件(回调)
							Log.i(tag, "点击事件被触发");
							pageClick.onclick(position);
						}
						break;
					}
					return true;
				}
			});
			
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((RollViewPager)container).removeView((View)object);
		}
	}

	/*public void setImageUrlList(ArrayList<Bitmap> bps) {
		// TODO Auto-generated method stub
		
	}*/
}
