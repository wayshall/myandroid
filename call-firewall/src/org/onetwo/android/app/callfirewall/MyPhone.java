package org.onetwo.android.app.callfirewall;

import java.lang.reflect.Method;

import org.onetwo.common.exception.BaseException;

import android.os.RemoteException;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

public class MyPhone {
	
	private ITelephony telephony;
	private TelephonyManager telMgr;
	
	public MyPhone(TelephonyManager telMgr){
		this.telMgr = telMgr;
		this.connectToTelephony();
	}
	
	protected void connectToTelephony(){
		try {
			Method m = TelephonyManager.class.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			this.telephony = (ITelephony) m.invoke(telMgr);
		} catch (Exception e) {
			throw new BaseException("connect error!", e);
		} 
	}

	public void rejectCall(){
		try {
			this.telephony.silenceRinger();
			this.telephony.endCall();
			this.cancelMissedCallsNotification();
		} catch (RemoteException e) {
			throw new BaseException("rejectCall error!", e);
		}
	}

	public void cancelMissedCallsNotification(){
		try {
			this.telephony.cancelMissedCallsNotification();
		} catch (RemoteException e) {
			throw new BaseException("rejectCall error!", e);
		}
	}
}
