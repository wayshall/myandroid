package org.onetwo.android.app.callfirewall.data;

import java.util.Date;

import org.onetwo.android.utils.SimpleCursor;
import org.onetwo.android.utils.SimpleCursor.CursorAdapter;

import android.provider.CallLog.Calls;

public class CallLogData {
	
	public static class CallLogAdapter implements CursorAdapter {

		@Override
		public Object adapter(SimpleCursor cursor) {
			CallLogData call = new CallLogData();
//			call.setId(cursor.getInt(Calls._ID));
			call.setName(cursor.getString(Calls.CACHED_NAME));
			call.setNumberLabel(cursor.getString(Calls.CACHED_NUMBER_LABEL));
			call.setNumber(cursor.getString(Calls.NUMBER));
			call.setDate(cursor.getDate(Calls.DATE));
			return call;
		}
		
	}
	
	public static final CallLogAdapter ADAPTER = new CallLogAdapter();

	private int id;
	private String name;
	private String numberLabel;
	private String number;
	private Date date;

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

	public String getNumberLabel() {
		return numberLabel;
	}

	public void setNumberLabel(String numberLabel) {
		this.numberLabel = numberLabel;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
