package org.onetwo.android.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.android.db.TableBuilder.Table;
import org.onetwo.android.db.query.HqlSymbolManager;
import org.onetwo.android.db.query.SqlSymbolManager;
import org.onetwo.android.utils.AndroidUtils;
import org.onetwo.common.bf.Container.Destroyable;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.ObjectUtils;
import org.onetwo.common.utils.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DatabaseHelper extends SQLiteOpenHelper implements Destroyable {

	
	private static final String TAG = DatabaseHelper.class.getSimpleName();

	private Map<String, Table> tables = new HashMap<String, Table>();
	private SqlSymbolManager sqlSymbol = HqlSymbolManager.getInstance();

	public DatabaseHelper(Context context, String name) {
		this(context, name, 2);
	}

	public DatabaseHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Collection<Table> tables = getTables().values();
		for(Table t : tables){
			String createSql = t.create();
			Log.i(TAG, "create_sql: " + createSql);
			db.execSQL(createSql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Collection<Table> tables = getTables().values();
		for(Table t : tables){
			String dropSql = t.drop();
			Log.i(TAG, "drop_sql: " + dropSql);
			db.execSQL(dropSql);
		}
		onCreate(db);
	}

    public SQLiteDatabase getDatabase() throws SQLException {
        return getWritableDatabase();
    }
	
	public Cursor find(String table, Object...args){
		Table t = getTable(table);
		Cursor cursor = null;
		if(ObjectUtils.isEmpty(args)){
			cursor = this.getDatabase().query(table, t.getSelectNames(), null, null, null, null, t.getId().getName());
			return cursor;
		}
		Map properties = MyUtils.convertParamMap(args);
		List values = new ArrayList();
		String where = this.sqlSymbol.createHql(properties, values);
		
		String orderBy = this.sqlSymbol.getOrderBy(properties, t.getId().getName()+" desc");
		if(StringUtils.hasText(where))
			cursor = this.getDatabase().query(table, t.getSelectNames(), where, (String[])values.toArray(new String[values.size()]), null, null, orderBy);
		else
			cursor = this.getDatabase().query(table, t.getSelectNames(), null, null, null, null, orderBy);
		return cursor;
	}
	
	public Cursor findById(String table, Object id, String[] columns){
		Table t = getTable(table);
		Cursor cursor = this.getDatabase().query(table, columns, t.getId().getName()+"=?", new String[]{id.toString()}, null, null, null, null);
		return cursor;
		
	}
	
	public Cursor findById(String table, Object id){
		Table t = getTable(table);
		Cursor cursor = this.getDatabase().query(table, t.getSelectNames(), t.getId().getName()+"=?", new String[]{id.toString()}, null, null, null, null);
		return cursor;
	}
	
	public long save(String table, ContentValues values){
		return this.getDatabase().insert(table, null, values);
	}
	
	public long save(String table, Object entity){
		ContentValues values = AndroidUtils.toContentValues(entity);
		if(values.containsKey("_id")){
			values.remove("_id");
		}
		return this.getDatabase().insert(table, null, values);
	}
	
	public long save(String table, String...params){
		return this.getDatabase().insert(table, null, AndroidUtils.convert2ContentValues(params));
	}
	
	public long update(String table, String id, String...params){
		Table t = getTable(table);
		return this.getDatabase().update(table, AndroidUtils.convert2ContentValues(params), t.getId().getName()+"=?", new String[]{id});
	}
	
	public long update(String table, String id, ContentValues values){
		Table t = getTable(table);
		return this.getDatabase().update(table, values, t.getId().getName()+"=?", new String[]{id});
	}
	
	public void delete(String table, Object...params){
		if(ObjectUtils.isEmpty(params)){
			this.getDatabase().delete(table, null, null);
			return ;
		}
		Map properties = MyUtils.convertParamMap(params);
		List values = new ArrayList();
		String where = this.sqlSymbol.createHql(properties, values);
		if(StringUtils.hasText(where))
			this.getDatabase().delete(table, where, (String[])values.toArray(new String[values.size()]));
		else
			this.getDatabase().delete(table, null, null);
	}
	
	public long count(String table, Object...fields){
		Table t = getTable(table);
		String sql = t.getCountSql((String[])null);
		Cursor cursor = null;
		if(fields==null){
			cursor = getDatabase().rawQuery(sql, null);
		}else{
			Map properties = MyUtils.convertParamMap(fields);
			List values = new ArrayList();
			String where = this.sqlSymbol.createHql(properties, values);
			if(StringUtils.hasText(where)){
				cursor = getDatabase().rawQuery(MyUtils.append(sql, " where ", where), (String[])values.toArray(new String[values.size()]));
			}else
				cursor = getDatabase().rawQuery(sql, null);
		}
		int count = 0;
		if(cursor.moveToNext()){
			int index = cursor.getColumnIndex("count_field");
			Log.i(TAG, "index: " + index);
			count = cursor.getInt(index);
		}
		return count;
	}
	
	public void deleteById(String table, Object id){
		Table t = getTable(table);
		this.getDatabase().delete(table, t.getId().getName()+"=?", new String[]{id.toString()});
	}
	
	public Table getTable(String table){
		Table t = getTables().get(table);
		if(t==null)
			throw new BaseException("can not find the table : " + table);
		return t;
	}
	
	public void onDestroy() {
		super.close();
	}

	public DatabaseHelper addTable(Table table){
		getTables().put(table.getName(), table);
		return this;
	}

	public Map<String, Table> getTables() {
		return tables;
	}

	public void setTables(Map<String, Table> tables) {
		this.tables = tables;
	}
	
}
