package org.onetwo.android.utils;

import org.onetwo.common.utils.StringUtils;

import android.database.Cursor;

public class SimpleCursor {
	
	protected Cursor cursor;
	
	public SimpleCursor(Cursor cursor){
		this.cursor = cursor;
	}
	
	public String getString(String name){
		return getString(name, "");
	}
	
	public String getString(String name, String def){
		String value = cursor.getString(cursor.getColumnIndexOrThrow(name));
		if(StringUtils.isBlank(value))
			value = def;
		return value;
	}
	
	public int getInt(String name){
		int value = cursor.getInt(cursor.getColumnIndexOrThrow(name));
		return value;
	}
	
	public long getLong(String name){
		long value = cursor.getLong(cursor.getColumnIndexOrThrow(name));
		return value;
	}

	public Cursor getCursor() {
		return cursor;
	}
	
}
