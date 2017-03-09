package com.example.mymodule;

import java.util.ArrayList;
import java.util.List;

import com.walker.module.BaseApplication;
import com.walker.module.ui.adapter.SuperAdapter;
import com.walker.module.ui.holder.SuperHolder;
import com.walker.module.ui.widget.MovieRecorderView;
import com.walker.module.ui.widget.MovieRecorderView.OnRecordFinishListener;
import com.walker.utils.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class MediaActivity extends Activity {
    private MovieRecorderView mRecorderView;
    private Button mShootBtn;
    private boolean isFinish = true;
     
 
    @Override  
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);StringUtils.isEmail("");
        List list = new ArrayList<String>();
        
        adapter = new SuperAdapter(list) {
			
			@Override
			public SuperHolder getHolder() {
				return null;
			}
		};
        
		
        System.out.println("########################################");
//        testNet();
        mRecorderView = (MovieRecorderView) findViewById(R.id.movieRecorderView);
        mShootBtn = (Button) findViewById(R.id.shoot_button);
         
        mShootBtn.setOnTouchListener(new OnTouchListener() {
             
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mRecorderView.record(new OnRecordFinishListener() {
 
                        @Override
                        public void onRecordFinish() {
                            handler.sendEmptyMessage(1);
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mRecorderView.getTimeCount() > 1)
                        handler.sendEmptyMessage(1);
                    else {
                        if (mRecorderView.getmVecordFile() != null)
                            mRecorderView.getmVecordFile().delete();
                        mRecorderView.stop();
                        Toast.makeText(BaseApplication.getApplication(), "视频录制时间太短", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }
 
 
 
	


	@Override
    public void onResume() {
        super.onResume();
        isFinish = true;
    }
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);       
        isFinish = false;
        mRecorderView.stop();
    }
 
    @Override
    public void onPause() {
        super.onPause();
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
 
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finishActivity();
        }
    };
	private SuperAdapter adapter;
 
    private void finishActivity() {
        if (isFinish) {
            mRecorderView.stop();
//            VideoPlayerActivity.startActivity(this, mRecorderView.getmVecordFile().toString());
        }
    }
 
    /**
     * 录制完成回调
     *
     * @author liuyinjun
     * 
     * @date 2015-2-9
     */
    public interface OnShootCompletionListener {
        public void OnShootSuccess(String path, int second);
        public void OnShootFailure();
    }}
