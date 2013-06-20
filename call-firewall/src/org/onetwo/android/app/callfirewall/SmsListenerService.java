package org.onetwo.android.app.callfirewall;

import org.onetwo.android.app.callfirewall.SmsContentObserver.SmsHandler;
import org.onetwo.android.app.callfirewall.service.SmsService;
import org.onetwo.android.view.BaseService;
import org.onetwo.android.view.OneTwoContext;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;

public class SmsListenerService extends BaseService{
	
	private ContentObserver observer ;
	
	public static void startSmsService(Context context){
    	Intent intent = new Intent(context, SmsListenerService.class);
    	context.startService(intent);
	}
	
	public static void stopSmsService(){
		OneTwoContext.stopService(SmsListenerService.class);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();

        ContentResolver resolver = getContentResolver();
        Handler handler = new SmsHandler(CallFirewallFactory.getBusinessFacade());
        observer = new SmsContentObserver(resolver, handler);
        resolver.registerContentObserver(SmsService.SMS_URI, true, observer);
	}
	
	@Override
	public void onDestroy() {
		getContentResolver().unregisterContentObserver(observer);
		super.onDestroy();
	}

}
