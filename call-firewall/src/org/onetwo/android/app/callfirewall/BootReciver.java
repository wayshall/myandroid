package org.onetwo.android.app.callfirewall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReciver extends BroadcastReceiver {
	
	public static final String ACTION = "android.intent.action.MAIN";
	public static final String CATEGORY = "android.intent.category.LAUNCHER";

	@Override
	public void onReceive(Context context, Intent intent) {
		/*Intent myIntent = new Intent(context, PhoneBlacklistActivity.class);
		myIntent.setAction(ACTION);
		myIntent.addCategory(CATEGORY);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(myIntent);*/

		Intent service = new Intent(context, PhoneListenerService.class);
//		service.setAction(PhoneListenerService.ACTION);
		context.startService(service);
	}

}
