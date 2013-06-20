package org.onetwo.android.app.callfirewall;

import org.onetwo.android.app.callfirewall.data.BlackPhone;
import org.onetwo.android.app.callfirewall.data.InterceptLog;
import org.onetwo.android.app.callfirewall.data.SmsCursor;
import org.onetwo.android.app.callfirewall.data.SmsCursor.MessageItem;
import org.onetwo.android.app.callfirewall.data.SmsCursor.SmsField;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptType;
import org.onetwo.android.app.callfirewall.service.SmsService;
import org.onetwo.android.view.MyAlertDialog;
import org.onetwo.android.view.OneTwoContext;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.util.Log;

public class SmsContentObserver extends ContentObserver implements BaseColumns {
	public String tag = this.getClass().getSimpleName();
	public static final Uri SMS_URI = SmsService.SMS_URI;
	
	
	public static final String[] PROJECTION = SmsField.PROJECTION;
	
	public static class SmsHandler extends Handler {
		public String tag = this.getClass().getSimpleName();
		private BusinessFacade business;
		
		public SmsHandler(BusinessFacade business){
			this.business = business;
		}
		
		@Override
		public void handleMessage(Message msg) {
			Log.i(tag, "handle message");
			MessageItem mi = (MessageItem) msg.obj;
			/*ContentValues values = new ContentValues();
			values.put("read", -1);*/
//			OneTwoContext.get().context().getContentResolver().update(uri, values, null, null);
			/*Uri uri = ContentUris.withAppendedId(SMS_URI, mi.id);
			OneTwoContext.get().context().getContentResolver().delete(uri, null, null);*/
			
			this.business.deleteSms(mi.id);
			
			InterceptLog log = InterceptLog.create(mi);
			log.setInterceptType(InterceptType.SMS);
			this.business.saveInterceptLog(log);
		}
		
	}
	
	
	private ContentResolver resolver;
	private Handler handler;
	private BusinessFacade business = CallFirewallFactory.getBusinessFacade();

	private int maxId = 0;
	
	public SmsContentObserver(ContentResolver resolver, Handler handler){
		super(handler);
		this.handler = handler;
		this.resolver = resolver;
//		maxId = this.fetchMaxId();
		Log.i(tag, "maxid:" + maxId);
	}
	
	public int fetchMaxId(){
		Cursor c = this.resolver.query(SMS_URI, new String[]{"max(_id)"}, null, null, null);
		if(c==null || c.getCount()<1)
			return 0;
		c.moveToFirst();
		return c.getInt(0);
	}

	@Override
	public boolean deliverSelfNotifications() {
		return super.deliverSelfNotifications();
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
//		cursor = this.resolver.query(SMS_URI, PROJECTION, "read=? and _id>=? and type=?", new String[]{"0", String.valueOf(maxId), String.valueOf(MESSAGE_TYPE_INBOX)}, "date desc");
		Cursor cursor = this.resolver.query(SMS_URI, PROJECTION, "read=? and type=?", new String[]{"0", String.valueOf(SmsService.MESSAGE_TYPE_INBOX)}, "date desc");
		try {
			deleteBlockSms(cursor);
		} catch (Exception e) {
			Log.e(tag, "delete sms error: "+e.getMessage(), e);
		}finally{
			if(cursor!=null)
				cursor.close();
		}
	}
	
	protected void deleteBlockSms(Cursor cursor){
		if(cursor==null || cursor.getCount()<1)
			return ;
		MessageItem item = null;
		SmsCursor smsCursor = new SmsCursor(cursor, false);
		while(smsCursor.moveToNext()){
			item = smsCursor.getMessageItem();
			try{
				String phone = item.address;
				if(phone.startsWith(SmsService.CHINA_MOBILE))
					phone = phone.substring(SmsService.CHINA_MOBILE.length());
				
				BlackPhone bpcusor = this.business.findBlackPhone(phone);
				SettingData sd = CallFirewallFactory.getSettingData();
				if(item.protocol==SmsService.PROTOCOL_SMS){
					if(sd.isBlacklistWay()){
						if(bpcusor!=null && bpcusor.isInBlacklistWay() && (bpcusor.isInterceptSms() || bpcusor.isInterceptAll())){
							Message msg = new Message();
							msg.obj = item;
							this.handler.sendMessage(msg);
						}
					}else if(sd.isWhitelistWay()){
						if(bpcusor!=null && bpcusor.isInWhitelistWay() && (bpcusor.isInterceptSms() || bpcusor.isInterceptAll())){
							//pass
						}else{
							Message msg = new Message();
							msg.obj = item;
							this.handler.sendMessage(msg);
						}
					}
				}
				if(item.id>maxId)
					maxId = item.id;
			}catch(Exception e){
				Log.e(tag, "delete sms error: ", e);
				MyAlertDialog.show("error", e.getMessage());
			}
		}
	}

}
