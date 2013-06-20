package org.onetwo.android.view;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BaseService extends Service{
	
	protected String tag = this.getClass().getSimpleName();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.i(tag, "onCreate");
		super.onCreate();
		OneTwoContext.get().context(this);
		OneTwoContext.get().resolver(this.getContentResolver());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(tag, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.i(tag, "onDestroy");
		super.onDestroy();
	}
}
