package org.onetwo.android.app.callfirewall;

import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptWay;
import org.onetwo.android.view.OneTwoContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingData {
	public static final String SETTING_DATA_FILE = "SETTING_DATA_FILE";
	public static final String INTERCEPT_WAY = T_BLACK_PHONES.INTERCEPT_WAY;
	public static final String SETTING_DEFAULT = "setting_default";
	
	private int interceptWay = -1;
	private SharedPreferences setting;
	
	public SettingData(){
		setting = OneTwoContext.get().context().getSharedPreferences(SettingData.SETTING_DATA_FILE, Context.MODE_PRIVATE);
	}
	
	public void setDefaultValue(){
		Editor editor = setting.edit();
		editor.putInt(INTERCEPT_WAY, InterceptWay.BLACK_LIST)
			.putInt(SETTING_DEFAULT, 1)
			.commit();
	}
	
	public Editor edit(){
		return setting.edit().putInt(SETTING_DEFAULT, 0);
	}
	
	public void setNotDefault(Editor editor){
		editor.putInt(SETTING_DEFAULT, 0);
	}
	
	public boolean isDefaultSettingCreated(){
		return contains(SETTING_DEFAULT);
	}
	
	public boolean contains(String key){
		return setting.contains(key);
	}

	public int getInterceptWay() {
		if(this.interceptWay==-1){
			this.interceptWay = this.setting.getInt(INTERCEPT_WAY, InterceptWay.BLACK_LIST);
		}
		return interceptWay;
	}

	public void setInterceptWay(int interceptWay) {
		this.interceptWay = interceptWay;
		edit().putInt(INTERCEPT_WAY, interceptWay).commit();
	}
	
	public boolean isBlacklistWay(){
		return getInterceptWay()==InterceptWay.BLACK_LIST;
	}
	
	public boolean isWhitelistWay(){
		return getInterceptWay()==InterceptWay.WHITE_LIST;
	}

}
