package org.onetwo.android.app.callfirewall;

import org.onetwo.android.app.callfirewall.data.BlackPhone;
import org.onetwo.android.app.callfirewall.data.CallLogData;
import org.onetwo.android.app.callfirewall.data.InterceptLog;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService;
import org.onetwo.android.app.callfirewall.service.SmsService;
import org.onetwo.android.utils.SimpleCursor;
import org.onetwo.android.view.OneTwoContext;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

public class BusinessFacade{
	
	public static final String TAG = BusinessFacade.class.getSimpleName();
	
	public static final Uri SMS_URI = SmsService.SMS_URI;
	
	private BlackPhoneService blackPhoneService;
	
	public BusinessFacade(BlackPhoneService blackPhoneDao){
		this.blackPhoneService = blackPhoneDao;
	}
	
	/*public void fillDatas(BaseListActivity activity){
		blackPhoneDao.fillDatas(activity);
	}*/
	
	public void deleteSms(int id){
		Uri uri = ContentUris.withAppendedId(SMS_URI, id);
		OneTwoContext.get().context().getContentResolver().delete(uri, null, null);
	}
	
	public void saveInterceptLog(InterceptLog log){
		this.blackPhoneService.saveInterceptLog(log);
	}
	public Cursor findInterceptLogs(int interceptType){
		return this.blackPhoneService.findInterceptLogs(interceptType);
	}
	
	public void deleteInterceptLogs(int interceptType){
		this.blackPhoneService.deleteInterceptLogs(interceptType);
	}
	
	public Cursor findPhonesByWay(int interceptWay){
		return blackPhoneService.findAll(interceptWay);
	}
	
	public void deleteBlackPhone(long id){
		this.blackPhoneService.delete(id);
	}
	
	public BlackPhone findBlackPhone(long id){
		return this.blackPhoneService.find(id); 
	}

	public void saveBlackPhone(ContentValues attrs){
		blackPhoneService.save(attrs);
	}
	
	public boolean containBlackPhone(String phone, Object excludeId){
		return blackPhoneService.contains(phone, excludeId);
	}
	
	public BlackPhone findBlackPhone(String phone){
		return blackPhoneService.findByPhone(phone);
	}
	
	public Cursor findCallLog(){
		String[] projection = new String[]{CallLog.Calls._ID, CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.NUMBER, CallLog.Calls.DATE};
		Cursor cursor = OneTwoContext.get().context().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
		return cursor;
	}
	
	public SimpleCursor findLastedNewCallLog(String phone){
		String[] projection = new String[]{CallLog.Calls._ID, CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.NUMBER, CallLog.Calls.DATE};
		Cursor cursor = OneTwoContext.get().context().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, CallLog.Calls.NUMBER+"=? and "+CallLog.Calls.NEW+"=?", new String[]{phone, "1"}, CallLog.Calls.DEFAULT_SORT_ORDER);
		SimpleCursor simple = new SimpleCursor(cursor, CallLogData.ADAPTER);
		return simple;
	}
	
}
