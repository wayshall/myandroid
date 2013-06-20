package org.onetwo.android.app.callfirewall.data;

import java.util.Date;

import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptType;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptWay;
import org.onetwo.android.utils.SimpleCursor;
import org.onetwo.android.utils.SimpleCursor.CursorAdapter;

public class BlackPhone {
	
	public static class BlackPhoneAdapter implements CursorAdapter {

		@Override
		public Object adapter(SimpleCursor cursor) {
			int id = cursor.getInt(T_BLACK_PHONES.ID);
			String name = cursor.getString(T_BLACK_PHONES.NAME);
			String phone = cursor.getString(T_BLACK_PHONES.PHONE);
			int inteceptType = cursor.getInt(T_BLACK_PHONES.INTERCEPT_TYPE);
			Date btime = cursor.getDate(T_BLACK_PHONES.CREATED_AT);
			
			BlackPhone bp = new BlackPhone();
			bp.setId(id);
			bp.setName(name);
			bp.setPhone(phone);
			bp.setInterceptType(inteceptType);
			bp.setInterceptWay(cursor.getInt(T_BLACK_PHONES.INTERCEPT_WAY));
			bp.setBlockTime(btime);
			return bp;
		}
	}
	
	public static final BlackPhoneAdapter ADAPER = new BlackPhoneAdapter();
	
	private int id;
	private String name;
	private String phone;
	private int interceptWay;
	private int interceptType;
	private Date blockTime;
	
	public BlackPhone() {
	}
	
	public boolean isInterceptAll(){
		int bl = getInterceptType();
		return bl==InterceptType.ALL;
	}
	
	public boolean isInterceptPhone(){
		int bl = getInterceptType();
		return bl==InterceptType.PHONE;
	}

	public boolean isInterceptSms(){
		int bl = getInterceptType();
		return bl==InterceptType.SMS;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getInterceptType() {
		return interceptType;
	}

	public void setInterceptType(int interceptType) {
		this.interceptType = interceptType;
	}

	public Date getBlockTime() {
		return blockTime;
	}

	public void setBlockTime(Date blockTime) {
		this.blockTime = blockTime;
	}

	public int getInterceptWay() {
		return interceptWay;
	}

	public void setInterceptWay(int interceptWay) {
		this.interceptWay = interceptWay;
	}

	public boolean isInBlacklistWay(){
		return getInterceptWay()==InterceptWay.BLACK_LIST;
	}
	
	public boolean isInWhitelistWay(){
		return getInterceptWay()==InterceptWay.WHITE_LIST;
	}

}
