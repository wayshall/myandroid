package org.onetwo.android.app.callfirewall.service;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.android.app.callfirewall.data.BlackPhone;
import org.onetwo.android.app.callfirewall.data.InterceptLog;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptType;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptWay;
import org.onetwo.android.db.DatabaseHelper;
import org.onetwo.android.db.TableBuilder;
import org.onetwo.android.db.TableBuilder.SqlType;
import org.onetwo.android.db.TableBuilder.Table;
import org.onetwo.android.db.query.SqlSymbolManager.Key;
import org.onetwo.android.utils.SimpleCursor;
import org.onetwo.common.utils.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class BlackPhoneService {
	public static final String TAG = BlackPhoneService.class.getSimpleName();
	
	public static final String DB_NAME = "call_firewall.db";
	public static final int DB_VERSION = 12;//updated

	@SuppressWarnings("serial")
	public static class T_BLACK_PHONES {

		public static class InterceptWay {
			public static int BLACK_LIST = 1;
			public static int WHITE_LIST = 2;
			
			private static List<Integer> values = new ArrayList<Integer>(){
				{
					add(BLACK_LIST);
					add(WHITE_LIST);
				}
			};
			
			public static boolean contains(Integer val){
				return values.contains(val);
			}
			
			public static boolean isBackList(int way){
				return BLACK_LIST == way;
			}
			
			public static boolean isWhiteList(int way){
				return WHITE_LIST == way;
			}
		}
		
		public static class InterceptType {
			public static final Integer PHONE = 1;
			public static final Integer SMS = 2;
			public static final Integer ALL = 100;
			
			private static List<Integer> values = new ArrayList<Integer>(){
				{
					add(PHONE);
					add(SMS);
					add(ALL);
				}
			};
			
			public static boolean contains(Integer val){
				return values.contains(val);
			}
		}
			
		public static final String TABLE_NAME = "T_BLACK_PHONES";
		
		public static final String ID = "_id";
		public static final String NAME = "_name";
		public static final String PHONE = "_phone";
		public static final String INTERCEPT_WAY = "_intercept_way";
		public static final String INTERCEPT_TYPE = "_intercept_type";
		public static final String CREATED_AT = "_created_at";
		
		public static final Table TABLE = TableBuilder.build(TABLE_NAME, ID)
												.column(NAME, SqlType.VARCHAR, false)
												.column(PHONE, SqlType.VARCHAR, false)
												.column(INTERCEPT_WAY, SqlType.INTEGER, false)
												.column(INTERCEPT_TYPE, SqlType.INTEGER, false, InterceptType.ALL)
//												.column(CREATED_AT, SqlType.TIMESTAMP, false, "(datetime('now','localtime'))");
												.column(CREATED_AT, SqlType.TIMESTAMP, false, "CURRENT_TIMESTAMP");
	
	}
	
	public static class T_INTERCEPT_LOG {
		public static final String TABLE_NAME = "T_INTERCEPT_LOG";
		
		public static final String ID = "_id";
		public static final String NAME = "_name";
		public static final String PHONE = "_phone";
		public static final String INTERCEPT_TYPE = "_intercept_type";
		public static final String CONTENT = "_content";
		public static final String CREATED_AT = "_created_at";
		
		public static final Table TABLE = TableBuilder.build(TABLE_NAME, ID)
		.column(NAME, SqlType.VARCHAR, false)
		.column(PHONE, SqlType.VARCHAR, false)
		.column(INTERCEPT_TYPE, SqlType.INTEGER, false, T_BLACK_PHONES.InterceptType.ALL)
		.column(CONTENT, SqlType.VARCHAR, true)
//		.column(CREATED_AT, SqlType.TIMESTAMP, false, "(datetime('now','localtime'))");
		.column(CREATED_AT, SqlType.TIMESTAMP, false, "CURRENT_TIMESTAMP");
	}
	
	private DatabaseHelper database;
	
	public BlackPhoneService(Context context){
		this.database = new DatabaseHelper(context, DB_NAME, DB_VERSION);
		this.database.addTable(T_BLACK_PHONES.TABLE);
		this.database.addTable(T_INTERCEPT_LOG.TABLE);
	}
	
	public Cursor findInterceptLogs(int interceptType){
		if(!InterceptType.contains(interceptType))
			throw new IllegalArgumentException("interceptType is error: " + interceptType);
		Cursor cursor = null;
		if(InterceptType.ALL==interceptType)
			cursor = this.database.find(T_INTERCEPT_LOG.TABLE_NAME, Key.ORDER_BY);
		else
			cursor = this.database.find(T_INTERCEPT_LOG.TABLE_NAME, T_INTERCEPT_LOG.INTERCEPT_TYPE, interceptType);
		return cursor;
	}
	
	public void saveInterceptLog(InterceptLog log){
		this.database.save(T_INTERCEPT_LOG.TABLE_NAME, log);
	}
	
	public void deleteInterceptLogs(int interceptType){
		if(!InterceptType.contains(interceptType))
			throw new IllegalArgumentException("interceptType is error: " + interceptType);
		this.database.delete(T_INTERCEPT_LOG.TABLE_NAME, T_INTERCEPT_LOG.INTERCEPT_TYPE, interceptType);
	}
	
	public Cursor findAll(int interceptWay){
		if(!InterceptWay.contains(interceptWay))
			throw new IllegalArgumentException("interceptWay is error:" + interceptWay);
        Cursor cursor = this.database.find(T_BLACK_PHONES.TABLE_NAME, T_BLACK_PHONES.INTERCEPT_WAY, interceptWay);
		return cursor;
	}
	
	public void delete(long id){
		Log.i(TAG, "id: " + id);
//		this.database.delete(T_BLACK_PHONES.TABLE_NAME, T_BLACK_PHONES.PHONE_KEY, phone);
		this.database.deleteById(T_BLACK_PHONES.TABLE_NAME, id);
	}
	
	public BlackPhone find(long id){
		Log.i(TAG, "id: " + id);
//		this.database.delete(T_BLACK_PHONES.TABLE_NAME, T_BLACK_PHONES.PHONE_KEY, phone);
		Cursor cursor = this.database.findById(T_BLACK_PHONES.TABLE_NAME, id);
		SimpleCursor simple = new SimpleCursor(cursor, BlackPhone.ADAPER);
		BlackPhone bp = (BlackPhone)simple.getSingle();
		return bp;
	}
	
	public BlackPhone findByPhone(String phone){
		Log.i(TAG, "phone: " + phone);
//		this.database.delete(T_BLACK_PHONES.TABLE_NAME, T_BLACK_PHONES.PHONE_KEY, phone);
		Cursor cursor = this.database.find(T_BLACK_PHONES.TABLE_NAME, T_BLACK_PHONES.PHONE, phone);
		if(cursor==null)
			return null;
		SimpleCursor simple = new SimpleCursor(cursor, BlackPhone.ADAPER);
		BlackPhone bp = (BlackPhone)simple.getSingle();
		return bp;
	}


	public void save(ContentValues attrs){
		Integer id = attrs.getAsInteger(T_BLACK_PHONES.ID);
		if(id==null || id<1){
			attrs.remove(T_BLACK_PHONES.ID);
			this.database.save(T_BLACK_PHONES.TABLE_NAME, attrs);
		}else
			this.database.update(T_BLACK_PHONES.TABLE_NAME, id.toString(), attrs);
	}

	public void save(Integer id, String name, String phone, Integer interceptWay, Integer interceptType){
		if(!StringUtils.hasText(name))
			name = phone;
		ContentValues attrs = new ContentValues();
		attrs.put(T_BLACK_PHONES.ID, id);
		attrs.put(T_BLACK_PHONES.NAME, name);
		attrs.put(T_BLACK_PHONES.PHONE, phone);
		attrs.put(T_BLACK_PHONES.INTERCEPT_WAY, interceptWay);
		attrs.put(T_BLACK_PHONES.INTERCEPT_TYPE, interceptType);
		this.save(attrs);
	}
	
	public boolean contains(String phone, Object excludeId){
		boolean exist = this.database.count(T_BLACK_PHONES.TABLE_NAME, T_BLACK_PHONES.PHONE, phone, T_BLACK_PHONES.ID+":!=", excludeId)>0;
		return exist;
	}

}
