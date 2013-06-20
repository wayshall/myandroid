package org.onetwo.android.app.callfirewall;

public class Constant {
	public static final String FILE_NAME = "black_phones.data";
	public static final String BLACK_PHONES = "black_phones";
	
	public static final String ACTION_CONTROL_SERVICE = PhoneListenerService.class.getName();
	public static final String SERVICE_CMD_KEY = "cmd";
	
	public static final int SERVICE_CMD_LISTEN_PHONE = 1;
	public static final int SERVICE_CMD_UNLISTEN_PHONE = -1;
	
	public static final int SERVICE_CMD_LISTEN_SMS = 2;
	public static final int SERVICE_CMD_UNLISTEN_SMS = -2;
	
	
	public static final String BLACK_PHONE_ID_KEY = "edit_id";

	public static final String INTERCEPTOR_TYPE = "interceptor_type";
	
}
