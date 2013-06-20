package org.onetwo.android.app.callfirewall;

import org.onetwo.android.utils.AndroidUtils;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class AddBlackPhoneTabActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	TabHost tabHost = getTabHost();
    	
    	LayoutInflater.from(this).inflate(R.layout.add_phone_tab, tabHost.getTabContentView(), true);
    	
    	TabSpec tab1 = tabHost.newTabSpec("call_log_tab")
    							.setIndicator("来电记录", getResources().getDrawable(R.drawable.records))
    							.setContent(createIntent(CallLogListActivity.class));
    	TabSpec tab2 = tabHost.newTabSpec("call_log_tab")
    							.setIndicator("短信记录", getResources().getDrawable(R.drawable.message))
    							.setContent(createIntent(SmsLogListActivity.class));
    	TabSpec tab3 = tabHost.newTabSpec("add_phone_tab")
    							.setIndicator("添加黑名单", getResources().getDrawable(R.drawable.custom))
    							.setContent(createIntent(AddPhoneActivity.class));
    	tabHost.addTab(tab1);
    	tabHost.addTab(tab2);
    	tabHost.addTab(tab3);
    }
    
    protected Intent createIntent(Class<?> clazz){
    	return AndroidUtils.createFrom(this, clazz);
    }
}
