package org.onetwo.android.app.callfirewall;

import java.util.Date;

import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptWay;
import org.onetwo.android.utils.AndroidUtils;
import org.onetwo.android.view.BaseListActivity;
import org.onetwo.android.view.GenericComponentImpl;
import org.onetwo.android.view.OneTwoContext;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.StringUtils;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class CallLogListActivity extends BaseListActivity {
    /** Called when the activity is first created. */
	
	private static ViewBinder viewBinder = new ViewBinder() {
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if(R.id.txt_calllog_date==view.getId()){
				long dateLong = cursor.getLong(columnIndex);
				Date date = new Date(dateLong);
				String dateStr = DateUtil.formatDateTime(date);
				((TextView)view).setText(dateStr);
				return true;
			}
			else if(R.id.txt_calllog_name==view.getId()){
				String name = cursor.getString(columnIndex);
				if(StringUtils.isBlank(name))
					name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
				((TextView)view).setText(name);
				return true;
			}
			/*else if(R.id.txt_blacklist_phone==view.getId()){
				String phone = cursor.getString(columnIndex);
				if(phone!=null && phone.length()>11)
					phone = phone.substring(phone.length()-11);
				((TextView)view).setText(phone);
				return true;
			}*/
			else if(R.id.btn_calllog_block==view.getId()){
				Button btn = (Button)view;
				if(InterceptWay.isWhiteList(CallFirewallUtils.getInterceptWay(OneTwoContext.get().activity())))
		        	btn.setText(R.string.btn_allow_this_phone);
				String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
				String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
				if(StringUtils.isBlank(name))
					name = number;
				btn.getInputExtras(true).putString(T_BLACK_PHONES.NAME, name);
				btn.getInputExtras(true).putString(T_BLACK_PHONES.PHONE, number);
				return true;
			}
			return false;
		}
	};
	
	private static final String tag = CallLogListActivity.class.getSimpleName();

	private BusinessFacade business = CallFirewallFactory.getBusinessFacade();
	
	public CallLogListActivity(){
		component = new GenericComponentImpl(this);
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
        Cursor cursor = business.findCallLog();
        startManagingCursor(cursor);
        
        String[] from = new String[]{CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls._ID};
        int[] to = new int[]{R.id.txt_calllog_name, R.id.txt_calllog_phone, R.id.txt_calllog_date, R.id.btn_calllog_block};
        SimpleCursorAdapter datalist = new SimpleCursorAdapter(this, R.layout.calllog_row, cursor, from, to);
        datalist.setViewBinder(viewBinder);
        setListAdapter(datalist);
    }
    
	public boolean toAddPhone(View view){
		Button btn = (Button)view;
		Log.i(tag, "info.id: " +view);
		String name = T_BLACK_PHONES.NAME;
		String number = T_BLACK_PHONES.PHONE;
		
		Intent intent = AndroidUtils.createFrom(this, AddPhoneActivity.class)
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