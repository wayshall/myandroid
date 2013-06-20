package org.onetwo.android.app.callfirewall;

import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptType;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptWay;
import org.onetwo.android.view.BaseActivity;
import org.onetwo.android.view.MyAlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(tag, "onCreate");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		
		this.getComponent().setExitDialog(
				new MyAlertDialog(this, R.string.app_alert_title, R.string.app_alert_message)
								.ok(R.string.app_alert_ok)
								.cancel(R.string.app_alert_cancel)
		);
	}
    
	//setExitDialog.ok
    public void onAlertOk(DialogInterface dialog, int whichButton){
    	CallFirewallFactory.clear();
//		android.os.Process.killProcess(android.os.Process.myPid());
    	this.finish();
	}
    
	public void showBlacklist(View view){
		/*Intent intent = new Intent(this, PhoneBlacklistActivity.class);
		intent.putExtra(SettingData.INTERCEPT_WAY, InterceptWay.BLACK_LIST);*/
		Intent intent = CallFirewallUtils.createIntentWithInterceptWay(this, PhoneBlacklistActivity.class, InterceptWay.BLACK_LIST);
		getComponent().startActivity(intent);
	}
	
	public void showWhitelist(View view){
		/*Intent intent = new Intent(this, PhoneBlacklistActivity.class);
		intent.putExtra(SettingData.INTERCEPT_WAY, InterceptWay.WHITE_LIST);*/
		Intent intent = CallFirewallUtils.createIntentWithInterceptWay(this, PhoneBlacklistActivity.class, InterceptWay.WHITE_LIST);
		getComponent().startActivity(intent);
	}
	
	public void showPhoneLogs(View view){
		Intent intent = CallFirewallUtils.createIntentWithInterceptType(this, InterceptorLogsList.class, InterceptType.PHONE);
		getComponent().startActivity(intent);
	}
	
	public void showSmsLogs(View view){
		Intent intent = CallFirewallUtils.createIntentWithInterceptType(this, InterceptorLogsList.class, InterceptType.SMS);
		getComponent().startActivity(intent);
	}
	
	public void showSetting(View view){
		getComponent().startActivity(SettingActivity.class);
	}
	
	public void callTo(View view){
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:552"));
		this.startActivity(intent);
	}
	
	protected void onDestroy(){
		super.onDestroy();
	}

}
