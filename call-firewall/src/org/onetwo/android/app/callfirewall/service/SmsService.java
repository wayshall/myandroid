package org.onetwo.android.app.callfirewall.service;

import org.onetwo.android.app.callfirewall.data.SmsCursor;
import org.onetwo.android.app.callfirewall.data.SmsCursor.SmsField;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class SmsService {
	
	public static final Uri SMS_URI = Uri.parse("content://sms");

	public static final int MESSAGE_TYPE_ALL = 0;
	public static final int MESSAGE_TYPE_INBOX = 1;
	public static final int MESSAGE_TYPE_SENT = 2;
	
	public static final int PROTOCOL_SMS = 0;
	public static final int PROTOCOL_MMS = 1;
	
	public static final String CHINA_MOBILE = "+86";
	
	
	public String tag = this.getClass().getSimpleName();
	
	private ContentResolver resolver;
//	private BlackPhoneService blackPhoneService;
	 
	public SmsService(ContentResolver resolver){
		this.resolver = resolver;
	}
	
	public SmsCursor findLastedSms(){
		/*Date date = new Date();
		date = DateUtil.addDay(date, -3);
		Cursor cursor = this.resolver.query(SMS_URI, SmsField.PROJECTION, "date>=? and type=?", new String[]{String.valueOf(date.getTime()), String.valueOf(MESSAGE_TYPE_INBOX)}, "date desc");
		*/
		Cursor cursor = this.resolver.query(SMS_URI, SmsField.PROJECTION, "type=?", new String[]{String.valueOf(MESSAGE_TYPE_INBOX)}, "date desc");
		if(cursor==null)
			return null;
		SmsCursor smsCursor = new SmsCursor(cursor);
		return smsCursor;
	}
	
}
