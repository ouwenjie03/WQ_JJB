package com.jjb.activity;

import com.jjb.util.Constant;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * 所有Activity继承自本Activity。
 * BaseActivity功能包括：
 * 		- 接收到广播后关闭activity
 * 
 * @author Robert Peng
 */
public abstract class BaseActivity extends Activity {
	
	// TODO 通过设定一个所有Activity都有的menu，放置一个关闭app的按钮，点击后发送关闭app的广播
	
	protected BroadcastReceiver appCloseReceiver = new BroadcastReceiver() {  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            finish();  
        }  
    };
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override  
    public void onResume() {  
        super.onResume();  
        // 注册关闭app的广播  
        IntentFilter filter = new IntentFilter();  
        filter.addAction(Constant.EXIT_APP_ACTION);
        this.registerReceiver(this.appCloseReceiver, filter);
    }  
      
    @Override  
    protected void onPause() {
        this.unregisterReceiver(this.appCloseReceiver);  
        super.onPause();
    }

}
