package org.onetwo.android.app.callfirewall;

import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_INTERCEPT_LOG;
import org.onetwo.android.view.BaseListActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class InterceptorLogsList extends BaseListActivity {
	
	private static ViewBinder viewBinder = new ViewBinder() {
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if(R.id.txt_intercept_create_at==view.getId()){
				Log.e("binder: ", cursor.getString(columnIndex));
				String dateStr = cursor.getString(columnIndex);
				((TextView)view).setText(dateStr);
				return true;
			}
			return false;
		}
	};
	
	private BusinessFacade business;
	
//	private Button clearButton;
	private int interceptType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intercept_logs_list);
		
//		this.clearButton = getComponent().findViewById(R.id.btn_clear_intercept_logs, Button.class);
		this.interceptType = CallFirewallUtils.getInterceptType(this);
		this.business = CallFirewallFactory.getBusinessFacade();
		
		fillDatas();
	}
	
	public void fillDatas(){
		Cursor cursor = this.business.findInterceptLogs(interceptType);
		startManagingCursor(cursor);
		
		String[] form = new String[]{T_INTERCEPT_LOG.NAME,
									T_INTERCEPT_LOG.PHONE,
									T_INTERCEPT_LOG.CONTENT,
									T_INTERCEPT_LOG.CREATED_AT};
		
		int[] to = new int[]{R.id.txt_intercept_name, 
							R.id.txt_intercept_phone,
							R.id.txt_intercept_content,
							R.id.txt_intercept_create_at};
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					this, R.layout.intercept_logs_list_row,
					cursor, form, to
				);
		adapter.setViewBinder(viewBinder);
		setListAdapter(adapter);
	}
	
	public void clearInterceptLogs(View view){
		this.business.deleteInterceptLogs(interceptType);
		this.fillDatas();
	}

	@Override
	protected void onStart() {
		this.fillDatas();
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
