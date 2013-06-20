package org.onetwo.android.app.callfirewall;

import java.util.Date;

import org.onetwo.android.app.callfirewall.data.SmsCursor;
import org.onetwo.android.app.callfirewall.data.SmsCursor.MessageItem;
import org.onetwo.android.app.callfirewall.data.SmsCursor.SmsField;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptWay;
import org.onetwo.android.app.callfirewall.service.SmsService;
import org.onetwo.android.utils.AndroidUtils;
import org.onetwo.android.view.BaseListActivity;
import org.onetwo.android.view.GenericComponentImpl;
import org.onetwo.android.view.OneTwoContext;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.StringUtils;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class SmsLogListActivity extends BaseListActivity {
    /** Called when the activity is first created. */
	
	private static ViewBinder viewBinder = new ViewBinder() {
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			SmsCursor smsCursor = (SmsCursor) cursor;
			MessageItem item = smsCursor.getMessageItem();
			if(R.id.txt_smslog_date==view.getId()){
				long dateLong = item.date;
				Date date = new Date(dateLong);
				String dateStr = DateUtil.formatDateTime(date);
				((TextView)view).setText(dateStr);
				return true;
			}
			/*else if(R.id.txt_smslog_name==view.getId()){
				String name = "";//item.person;
				if(StringUtils.isBlank(name))
					name = item.address;
				((TextView)view).setText(name);
				return true;
			}*/
			else if(R.id.btn_smslog_block==view.getId()){
				Button btn = (Button)view;
				if(InterceptWay.isWhiteList(CallFirewallUtils.getInterceptWay(OneTwoContext.get().activity())))
		        	btn.setText(R.string.btn_allow_this_phone);
				String name = "";//item.person;
				String number = item.address;
				if(StringUtils.isBlank(name))
					name = number;
//		    	Log.e(tag, "list smsId; " + item.id);
				btn.getInputExtras(true).putInt(T_BLACK_PHONES.ID, item.id);
				btn.getInputExtras(true).putString(T_BLACK_PHONES.NAME, name);
				btn.getInputExtras(true).putString(T_BLACK_PHONES.PHONE, number);
				return true;
			}
			return false;
		}
	};
	
	private static final String tag = SmsLogListActivity.class.getSimpleName();

	private SmsService smsService;
	
	public SmsLogListActivity(){
		component = new GenericComponentImpl(this);
		this.smsService = new SmsService(OneTwoContext.get().resolver());
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setTheme(android.R.style.Theme_Black_NoTitleBar);
        setContentView(R.layout.calllog_list);

        this.registerForContextMenu(this.getListView());

        this.fillDatas();
        
    }
    
    protected void fillDatas(){
		
    	SmsCursor cursor = smsService.findLastedSms();
        startManagingCursor(cursor);
        
        String[] from = new String[]{SmsField.ADDRESS, SmsField.BODY, SmsField.DATE, SmsField.ID};
        int[] to = new int[]{R.id.txt_smslog_phone, R.id.txt_sms_body, R.id.txt_smslog_date, R.id.btn_smslog_block};
        SimpleCursorAdapter datalist = new SimpleCursorAdapter(this, R.layout.smslog_row, cursor, from, to);
        datalist.setViewBinder(viewBinder);
        setListAdapter(datalist);
    }
    
	public boolean toAddPhone(View view){
		Button btn = (Button)view;
		Log.i(tag, "info.id: " +view);
		String name = T_BLACK_PHONES.NAME;
		String number = T_BLACK_PHONES.PHONE;
		

		Intent intent = AndroidUtils.createFrom(this, AddPhoneActivity.class)
						.putExtra(T_BLACK_PHONES.ID, btn.getInputExtras(true).getInt(T_BLACK_PHONES.ID))
						.putExtra(name, btn.getInputExtras(true).getString(name))
						.putExtra(number, btn.getInputExtras(true).getString(number));
		
		getComponent().startActivity(intent);
		return false;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		this.fillDatas();
	}

	@Override
	protected void onDestroy() {
//		this.stopPhoneListenerService();
		super.onDestroy();
	}
    
}