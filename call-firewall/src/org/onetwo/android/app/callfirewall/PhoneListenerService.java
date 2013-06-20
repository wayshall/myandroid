package org.onetwo.android.app.callfirewall;

import java.util.Date;

import org.onetwo.android.app.callfirewall.data.BlackPhone;
import org.onetwo.android.app.callfirewall.data.CallLogData;
import org.onetwo.android.app.callfirewall.data.InterceptLog;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptType;
import org.onetwo.android.app.callfirewall.service.SmsService;
import org.onetwo.android.utils.SimpleCursor;
import org.onetwo.android.view.BaseService;
import org.onetwo.common.utils.DateUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneListenerService extends BaseService{
	
	public static final String TAG = PhoneListenerService.class.getSimpleName();
	
	private class CommandReceiver extends BroadcastReceiver{
		private String tag = CommandReceiver.class.getSimpleName();

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(tag, "intent.getAction():" + intent.getAction());
			if(!Constant.ACTION_CONTROL_SERVICE.equals(intent.getAction()))
				return ;
			int cmd = intent.getIntExtra(Constant.SERVICE_CMD_KEY, 0);
			if(cmd==Constant.SERVICE_CMD_LISTEN_PHONE){
				Log.i(tag, "SERVICE_CMD_LISTEN_PHONE");
				PhoneListenerService.this.listenPhone();
				
			}else if(cmd== Constant.SERVICE_CMD_UNLISTEN_PHONE){
				Log.i(tag, "SERVICE_CMD_UNLISTEN_PHONE");
				PhoneListenerService.this.unlistenPhone();
				
			}/*else if(cmd == Constant.SERVICE_CMD_LISTEN_SMS){
				Log.i(tag, "SERVICE_CMD_LISTEN_SMS");
				PhoneListenerService.this.listenSms();
				
			}else if(cmd == Constant.SERVICE_CMD_UNLISTEN_SMS){
				Log.i(tag, "SERVICE_CMD_UNLISTEN_SMS");
				PhoneListenerService.this.unlistenSms();
				
			}*/else
				Log.i(tag, "nothing");
		}
		
	};
	
	public static class PhoneHandler extends Handler {
		public String tag = this.getClass().getSimpleName();
		
		private BusinessFacade business;
		public PhoneHandler(BusinessFacade business){
			this.business = business;
		}

		@Override
		public void handleMessage(Message msg) {
			Log.i(tag, "PhoneHandler message");
			String phone = (String) msg.obj;
			
			SimpleCursor cursor = business.findLastedNewCallLog(phone);
			CallLogData data = (CallLogData) cursor.getSingle();
			if(data==null){
				data = new CallLogData();
				data.setNumber(phone);
				data.setName(phone);
				data.setDate(new Date());
			}
			/*Uri idUri = ContentUris.withAppendedId(CallLog.Calls.CONTENT_URI, data.getId());
			OneTwoContext.get().context().getContentResolver().delete(idUri, null, null);*/
			
			/*String where = "type="+CallLog.Calls.MISSED_TYPE+" and new=1";
			ContentValues values = new ContentValues();
			values.put(CallLog.Calls.NEW, 0);
			OneTwoContext.get().context().getContentResolver().update(CallLog.Calls.CONTENT_URI, values, where, null);
			*/
			InterceptLog log = new InterceptLog();
			log.setName(data.getName());
			log.setPhone(data.getNumber());
			log.setInterceptType(InterceptType.PHONE);
			log.setContent(data.getNumberLabel());
			log.setCreateAt(data.getDate());
			Log.e(tag, "data.getDate():"+ DateUtil.formatDateTime(data.getDate()));
			
			this.business.saveInterceptLog(log);
		}
		
	}

	private MyPhone phone = null;
	
	private PhoneStateListener callListener = null;
	
//	private ContentObserver observer ;
	private BusinessFacade businessFacade;
	
//	private CommandReceiver cmdReceiver = null;
	
	private Handler handler;
	
//	String ns = Context.NOTIFICATION_SERVICE;

    public static void startPhoneListenerService(Context context){
    	Intent intent = new Intent(context, PhoneListenerService.class);
    	context.startService(intent);
    }
    
    public static void stopPhoneListenerService(Context context){
    	Intent intent = new Intent(context, PhoneListenerService.class);
    	context.stopService(intent);
    }

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
//        this.cmdReceiver = new CommandReceiver();

//	    NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
	    
        callListener = new PhoneStateListener() {
    		@Override
    		public void onCallStateChanged(int state, String incomingNumber) {
    			Log.i(TAG, "PhoneStateListener: "+incomingNumber);
				if(incomingNumber.startsWith(SmsService.CHINA_MOBILE))
					incomingNumber = incomingNumber.substring(SmsService.CHINA_MOBILE.length());
				
    			if(state==TelephonyManager.CALL_STATE_RINGING){
    				BlackPhone cursor = null;
    				try {
    					SettingData sd = CallFirewallFactory.getSettingData();
        				cursor = businessFacade.findBlackPhone(incomingNumber);
    					Log.i(TAG, "PhoneStateListener: intercept way : " + sd.getInterceptWay());
    					if(sd.isBlacklistWay()){
	        				if(cursor!=null && cursor.isInBlacklistWay() && (cursor.isInterceptPhone() || cursor.isInterceptAll())){
	    						Log.i(TAG, "PhoneStateListener: cut call " + incomingNumber);
	    						phone.rejectCall();
	    						handler.sendMessage(createMessage(incomingNumber));
	        				}
    					}else{
    						if(cursor!=null && cursor.isInWhitelistWay() &&(cursor.isInterceptPhone() || cursor.isInterceptAll())){
    							//pass
    						}else{
	    						Log.i(TAG, "PhoneStateListener: cut call " + incomingNumber);
	    						phone.rejectCall();
	    						handler.sendMessage(createMessage(incomingNumber));
    						}
    					}
					} catch (Exception e) {
						Log.e(tag, "reject call error :"+e.getMessage());
					} 
    			}
    		}
        };
    	this.listenPhone();

		super.onCreate();
		businessFacade = CallFirewallFactory.getBusinessFacade();
		this.handler = new PhoneHandler(businessFacade);
        /*ContentResolver resolver = getContentResolver();
        Handler handler = new SmsHandler();
        observer = new SmsContentObserver(resolver, handler);
    	this.listenSms();*/
    	
	}
	
	protected Message createMessage(String phone){
		Message msg = new Message();
		msg.obj = phone;
		return msg;
	}
	
	protected void listenPhone(){
        this.listenCall(PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	protected void unlistenPhone(){
        this.listenCall(PhoneStateListener.LISTEN_NONE);
	}
	
	protected void listenCall(int listSate){
        TelephonyManager telMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        telMgr.listen(callListener, listSate);
        
        this.phone = new MyPhone(telMgr);
	}
	
	/*protected void listenSms(){
        getContentResolver().registerContentObserver(SmsContentObserver.SMS_URI, true, observer);
	}
	
	protected void unlistenSms(){
		getContentResolver().unregisterContentObserver(observer);
	}*/

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		
		/*IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_CONTROL_SERVICE);
		this.registerReceiver(cmdReceiver, filter);*/
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
//		this.unregisterReceiver(cmdReceiver);
		
        this.unlistenPhone();
//        this.unlistenSms();
        
		super.onDestroy();
	}
	

}
