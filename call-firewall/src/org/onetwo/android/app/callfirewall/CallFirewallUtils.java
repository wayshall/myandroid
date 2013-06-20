package org.onetwo.android.app.callfirewall;

import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptType;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptWay;
import org.onetwo.android.utils.AndroidUtils;

import android.app.Activity;
import android.content.Intent;

public abstract class CallFirewallUtils {
	
	public static Intent createIntentWithInterceptWay(Activity context, Class<?> clazz, int interceptWay){
		Intent intent = AndroidUtils.createFrom(context, clazz).putExtra(SettingData.INTERCEPT_WAY, interceptWay);
		return intent;
	}
	
	public static Intent createIntentWithInterceptType(Activity context, Class<?> clazz, int interceptType){
		return AndroidUtils.createFrom(context, clazz).putExtra(T_BLACK_PHONES.INTERCEPT_TYPE, interceptType);
	}
	
	public static int getInterceptWay(Activity activity){
		return activity.getIntent().getIntExtra(SettingData.INTERCEPT_WAY, InterceptWay.BLACK_LIST);
	}
	
	public static int getInterceptType(Activity activity){
		return activity.getIntent().getIntExtra(T_BLACK_PHONES.INTERCEPT_TYPE, InterceptType.PHONE);
	}
}
