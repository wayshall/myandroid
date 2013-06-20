package org.onetwo.android.app.callfirewall.data;

import org.onetwo.android.app.callfirewall.service.SmsService;
import org.onetwo.android.utils.SimpleCursor;

import android.database.Cursor;
import android.provider.BaseColumns;

public class SmsCursor extends SimpleCursor {

	public static class MessageItem {
		public int id;
		public int type;
		public String address;
		public String body;
		public int read;
		public int protocol;
		public int date;
		public int person;
		
		public MessageItem(int id, int type, String address, String body, int read, int protocol, int date, int person) {
			super();
			this.id = id;
			this.type = type;
			this.body = body;
			this.read = read;
			this.protocol = protocol;
			this.date = date;
			this.person = person;
			if(address.startsWith(SmsService.CHINA_MOBILE))
				address = address.substring(SmsService.CHINA_MOBILE.length());
			this.address = address;
		}
	}
	
	public static class SmsField {
		public static final String ID = BaseColumns._ID;
		public static final String PERSON = "person";
		public static final String TYPE = "type";
		public static final String ADDRESS = "address";
		public static final String BODY = "body";
		public static final String READ = "read";
		public static final String PROTOCOL = "protocol";
		public static final String DATE = "date";
		
		public static final String[] PROJECTION = new String[]{
			SmsField.ID, SmsField.TYPE, SmsField.ADDRESS, SmsField.BODY, SmsField.READ, SmsField.PROTOCOL, SmsField.DATE, SmsField.PERSON
		} ;
	}
	
	private MessageItem item;
	
	public SmsCursor(Cursor cursor) {
		super(cursor);
	}
	
	public SmsCursor(Cursor cursor, boolean moveToFirst) {
		super(cursor, moveToFirst);
	}
	
	public MessageItem getMessageItem(){
		if(item!=null && !isCreateNew())
			return item;
		int colIndex = 0;
		item = new MessageItem(
				cursor.getInt(colIndex++), 
				cursor.getInt(colIndex++), 
				cursor.getString(colIndex++), 
				cursor.getString(colIndex++), 
				cursor.getInt(colIndex++),  
				cursor.getInt(colIndex++), 
				cursor.getInt(colIndex), 
				cursor.getInt(colIndex)
			);
		setCreateNew(false);
		return item;
	}

}
