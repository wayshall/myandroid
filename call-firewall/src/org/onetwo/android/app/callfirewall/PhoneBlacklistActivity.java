package org.onetwo.android.app.callfirewall;

import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptWay;
import org.onetwo.android.utils.AndroidUtils;
import org.onetwo.android.view.BaseListActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class PhoneBlacklistActivity extends BaseListActivity {
    /** Called when the activity is first created. */

	private BusinessFacade business ;
	
	private Button btnAddPhone;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
/*
		this.getComponent().setExitDialog(
				new MyAlertDialog(this, R.string.app_alert_title, R.string.app_alert_message)
								.ok(R.string.app_alert_ok)
								.cancel(R.string.app_alert_cancel)
		);*/
		
        this.setTheme(android.R.style.Theme_Black);
        setContentView(R.layout.phone_blacklist);
        
        this.btnAddPhone = getComponent().findViewById(R.id.btn_add_phone, Button.class);

        this.registerForContextMenu(this.getListView());
        
//        startPhoneListenerService();
        getMenuCompt().setMenuGroup(MenuGroupBuilder.MENU_BLACK_PHONES);
        getMenuCompt().setContextMenuGroup(MenuGroupBuilder.CONTEXT_MENU_BLACK_PHONES);
        
        this.business = CallFirewallFactory.getBusinessFacade();
        if(InterceptWay.isWhiteList(CallFirewallUtils.getInterceptWay(this)))
        	this.btnAddPhone.setText("添加白名单");
        this.fillDatas();
        
    }
    
    public void onAlertOk(DialogInterface dialog, int whichButton){
    	CallFirewallFactory.clear();
		android.os.Process.killProcess(android.os.Process.myPid());
    	this.finish();
	}
    
    protected void fillDatas(){
        Cursor cursor = business.findPhonesByWay(CallFirewallUtils.getInterceptWay(this));
        startManagingCursor(cursor);
        
        String[] from = new String[]{T_BLACK_PHONES.NAME, T_BLACK_PHONES.PHONE};
        int[] to = new int[]{R.id.txt_blacklist_name, R.id.txt_blacklist_phone};
        SimpleCursorAdapter datalist = new SimpleCursorAdapter(this, R.layout.phone_blacklist_row, cursor, from, to);
        setListAdapter(datalist);
    }
 
	public boolean toAddPhone(MenuItem item){
		getComponent().startActivity(createIntent(AddPhoneActivity.class));
		return false;
	}
	
	protected Intent createIntent(Class<?> clazz){
		return AndroidUtils.createFrom(this, clazz);
	}

	public void toAddPhoneTab(View view){
		getComponent().startActivity(createIntent(AddBlackPhoneTabActivity.class));
	}
    
    public void delPhone(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//		String phone = (String) this.getListAdapter().getItem((int)info.id);
		Log.i(tag, "delete phone: " + info.id);
		this.business.deleteBlackPhone(info.id);
    	Toast.makeText(this, R.string.delete_success, Toast.LENGTH_SHORT).show();
		this.fillDatas();
    }
    
    public void editPhone(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//		String phone = (String) this.getListAdapter().getItem((int)info.id);
		Intent intent = createIntent(AddPhoneActivity.class);
		intent.putExtra(Constant.BLACK_PHONE_ID_KEY, info.id);
		Log.i(tag, "edit phone: " + info.id);
		this.startActivity(intent);
    }
    
	@Override
	protected void onStart() {
		this.fillDatas();
		super.onStart();
	}

	@Override
	protected void onDestroy() {
//		this.stopPhoneListenerService();
		super.onDestroy();
	}
    
}