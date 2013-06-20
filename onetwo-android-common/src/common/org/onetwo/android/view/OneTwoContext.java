package org.onetwo.android.view;

import org.onetwo.common.exception.BaseException;

import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

public class OneTwoContext {
	
	public static OneTwoContext instance = new OneTwoContext();

	protected Activity currentActivity = null;
	protected Context currentContext = null;
	protected ContentResolver currentResolver;
//	protected Intent currentIntent;
	
	private OneTwoContext(){
	}

	public static OneTwoContext get() {
		return instance;
	}

	public Activity activity() {
		return currentActivity;
	}

	public void activity(Activity currentActivity) {
		this.currentActivity = currentActivity;
		context(currentActivity);
	}

	public Context context() {
		return currentContext;
	}

	public Context context(boolean throwIfnull) {
		if(currentContext==null && throwIfnull)
			throw new BaseException("context can not be null");
		return currentContext;
	}

	public void context(Context currentContext) {
		get().currentContext = currentContext;
	}

	public void resolver(ContentResolver resolver) {
		this.currentResolver = resolver;
	}

	public ContentResolver resolver() {
		return currentResolver;
	}
	
	/*public Intent intent(){
		return currentIntent;
	}
	
	public void intent(Intent intent){
		this.currentIntent = intent;
	}*/
	
	public static void startService(Class<? extends Service> clazz){
		Context ctx = get().context();
		Intent intent = new Intent(ctx, clazz);
		ctx.startService(intent);
	}
	
	public static void stopService(Class<? extends Service> clazz){
		Context ctx = get().context();
		Intent intent = new Intent(ctx, clazz);
		ctx.stopService(intent);
	}
	
	public static void startActivity(Class<Activity> clazz){
		Context ctx = get().activity();
		Intent intent = new Intent(ctx, clazz);
		ctx.startActivity(intent);
	}
	

}
