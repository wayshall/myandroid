package org.onetwo.android.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.StringUtils;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SimpleCursor implements Cursor {
	
	public static interface CursorAdapter {
		Object adapter(SimpleCursor cursor);
	}
	
	protected String tag = this.getClass().getSimpleName();

	private boolean createNew = true;
	protected Cursor cursor;
	protected CursorAdapter adapter;
	
	public SimpleCursor(Cursor cursor){
		this(cursor, true);
	}
	
	public SimpleCursor(Cursor cursor, CursorAdapter adapter){
		this(cursor, false);
		this.adapter = adapter;
	}
	
	public SimpleCursor(Cursor cursor, boolean autoMoveToFirst){
		this.cursor = cursor;
		if(cursor!=null && autoMoveToFirst)
			moveToFirst();
	}
	
	public Object getSingle(){
		if(adapter==null)
			throw new ServiceException("adapter is null!");
		Object result = null;
		try {
			if(getCount()<1){
				return null;
			}
			moveToFirst();
			result = adapter.adapter(this);
		} catch (Exception e) {
			Log.e(tag, "adapter error", e);
		}finally{
			close();
		}
		return result;
	}
	
	public List getList(){
		List results = null;
		try {
			while(moveToNext()){
				if(results==null)
					results = new ArrayList();
				Object o = adapter.adapter(this);
				results.add(o);
			}
		} catch (Exception e) {
			Log.e(tag, "adapter error", e);
		}finally{
			close();
		}
		return results;
	}
	
	public boolean isCreateNew() {
		return createNew;
	}

	public void setCreateNew(boolean createNew) {
		this.createNew = createNew;
	}
	


	public Date getDate(String name){
		return getDate(name, null);
	}

	public Date getDate(String name, Date def){
		long dateLong = getLong(name);
		if(dateLong<1)
			return def;
		Date date = new Date(dateLong);
		return date;
	}
	

	public String getDateString(String name){
		Date date = getDate(name, null);
		if(date==null)
			return "";
		String dateStr = DateUtil.formatDateTime(date);
		return dateStr;
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
	
	public int getInt(String name, int def){
		int index = cursor.getColumnIndex(name);
		if(index==-1)
			return def;
		int value = cursor.getInt(index);
		return value;
	}
	
	public long getLong(String name){
		long value = cursor.getLong(cursor.getColumnIndexOrThrow(name));
		return value;
	}

	public Cursor getCursor() {
		return cursor;
	}
	
	public void close(){
		this.cursor.close();
	}

	public int getCount() {
		return cursor.getCount();
	}

	public int getPosition() {
		return cursor.getPosition();
	}

	public boolean move(int offset) {
		setCreateNew(true);
		return cursor.move(offset);
	}

	public boolean moveToPosition(int position) {
		setCreateNew(true);
		return cursor.moveToPosition(position);
	}

	public boolean moveToFirst() {
		setCreateNew(true);
		return cursor.moveToFirst();
	}

	public boolean moveToLast() {
		setCreateNew(true);
		return cursor.moveToLast();
	}

	public boolean moveToNext() {
		setCreateNew(true);
		return cursor.moveToNext();
	}

	public boolean moveToPrevious() {
		setCreateNew(true);
		return cursor.moveToPrevious();
	}

	public boolean isFirst() {
		return cursor.isFirst();
	}

	public boolean isLast() {
		return cursor.isLast();
	}

	public boolean isBeforeFirst() {
		return cursor.isBeforeFirst();
	}

	public boolean isAfterLast() {
		return cursor.isAfterLast();
	}

	public int getColumnIndex(String columnName) {
		return cursor.getColumnIndex(columnName);
	}

	public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
		return cursor.getColumnIndexOrThrow(columnName);
	}

	public String getColumnName(int columnIndex) {
		return cursor.getColumnName(columnIndex);
	}

	public String[] getColumnNames() {
		return cursor.getColumnNames();
	}

	public int getColumnCount() {
		return cursor.getColumnCount();
	}

	public byte[] getBlob(int columnIndex) {
		return cursor.getBlob(columnIndex);
	}

	public String getString(int columnIndex) {
		return cursor.getString(columnIndex);
	}

	public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
		cursor.copyStringToBuffer(columnIndex, buffer);
	}

	public short getShort(int columnIndex) {
		return cursor.getShort(columnIndex);
	}

	public int getInt(int columnIndex) {
		return cursor.getInt(columnIndex);
	}

	public long getLong(int columnIndex) {
		return cursor.getLong(columnIndex);
	}

	public float getFloat(int columnIndex) {
		return cursor.getFloat(columnIndex);
	}

	public double getDouble(int columnIndex) {
		return cursor.getDouble(columnIndex);
	}

	public boolean isNull(int columnIndex) {
		return cursor.isNull(columnIndex);
	}

	public void deactivate() {
		cursor.deactivate();
	}

	public boolean requery() {
		return cursor.requery();
	}

	public boolean isClosed() {
		return cursor.isClosed();
	}

	public void registerContentObserver(ContentObserver observer) {
		cursor.registerContentObserver(observer);
	}

	public void unregisterContentObserver(ContentObserver observer) {
		cursor.unregisterContentObserver(observer);
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		cursor.registerDataSetObserver(observer);
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		cursor.unregisterDataSetObserver(observer);
	}

	public void setNotificationUri(ContentResolver cr, Uri uri) {
		cursor.setNotificationUri(cr, uri);
	}

	public boolean getWantsAllOnMoveCalls() {
		return cursor.getWantsAllOnMoveCalls();
	}

	public Bundle getExtras() {
		return cursor.getExtras();
	}

	public Bundle respond(Bundle extras) {
		return cursor.respond(extras);
	}
	
}
