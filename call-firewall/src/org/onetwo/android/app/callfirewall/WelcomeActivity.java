package org.onetwo.android.app.callfirewall;

import org.onetwo.android.view.BaseActivity;
import org.onetwo.common.exception.BaseException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public class WelcomeActivity extends BaseActivity implements Callback{
    /** Called when the activity is first created. */
	
	public static final int TO_BLACK_PHONES = 1;
	
	private Handler hander = null;
	
	public WelcomeActivity(){
		hander = new Handler(this);
	}
	
    @Override
	public boolean handleMessage(Message msg) {
    	if(msg.what==TO_BLACK_PHONES){
//			final Intent intent = new Intent(this, PhoneBlacklistActivity.class);
			final Intent intent = new Intent(this, MainActivity.class);
			this.startActivity(intent);
			this.finish();
    	}
		return true;
	}
 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        this.startMainActivity();
        
        //init
        CallFirewallFactory.initialize(this);
    }
    
    public void startMainActivity(){
    	new Thread(){
    		public void run(){
    	        try {
    				Thread.sleep(3000);
    			} catch (InterruptedException e) {
    				throw new BaseException("error!", e);
    			}
    			Message msg = hander.obtainMessage(TO_BLACK_PHONES);
    			hander.sendMessage(msg);
    		}
    	}.start();
    }
    
}