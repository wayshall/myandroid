package org.onetwo.android.app.callfirewall;


import java.util.HashMap;
import java.util.Map;

import org.onetwo.android.app.callfirewall.service.BlackPhoneService;
import org.onetwo.android.view.OneTwoContext;

import android.content.Context;

public class CallFirewallFactory {
	
	public static interface Destroyable {
		
		public void onDestroy();
		
	}

	private static final Map<String, Object> CONTAINER = new HashMap<String, Object>();
	private static boolean init = false;
	private static SettingData settingDate;
	
	
	
	public static void initialize(Context context) {
		build();
        PhoneListenerService.startPhoneListenerService(context);
        SmsListenerService.startSmsService(context);
        init = true;
	}
	
	protected synchronized static void checkInit(){
		if(!init){
			initialize(OneTwoContext.get().context());
		}
	}
	
	private static void build(){
		BlackPhoneService blackPhoneDao = new BlackPhoneService(OneTwoContext.get().context());
		CONTAINER.put(BlackPhoneService.class.getSimpleName(), blackPhoneDao);
		
		BusinessFacade businessFacade = new BusinessFacade(blackPhoneDao);
		CONTAINER.put(BusinessFacade.class.getSimpleName(), businessFacade);
		
		settingDate = new SettingData();
		if(!settingDate.isDefaultSettingCreated()){
			settingDate.setDefaultValue();
		}
	}


	public static BlackPhoneService getDatabase(){
		checkInit();
		return (BlackPhoneService) CONTAINER.get(BlackPhoneService.class.getSimpleName());
	}

	public static BusinessFacade getBusinessFacade(){
		checkInit();
		return (BusinessFacade)CONTAINER.get(BusinessFacade.class.getSimpleName());
	}
	
	public static SettingData getSettingData(){
		checkInit();
		return settingDate;
	}
	
	public static void clear(){
		Object value = null;
		for(Map.Entry<String, Object> entry : CONTAINER.entrySet()){
			value = entry.getValue();
			if(value instanceof Destroyable)
				((Destroyable) value).onDestroy();
			value = null;
		}
		CONTAINER.clear();
	}
}
