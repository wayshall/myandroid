package org.onetwo.android.db;

import org.junit.Test;
import org.onetwo.android.db.TableBuilder.SqlType;
import org.onetwo.android.db.TableBuilder.Table;

public class TableBuilderTest {

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
		.column(INTERCEPT_TYPE, SqlType.INTEGER, false, 100)
		.column(CONTENT, SqlType.VARCHAR, true)
		.column(CREATED_AT, SqlType.TIMESTAMP, false, "(datetime('now','localtime'))");
	}
	
	@Test
	public void testBuilder(){
		String str = T_INTERCEPT_LOG.TABLE.create();
		System.out.println("str: " + str);
		
		String[] snames = T_INTERCEPT_LOG.TABLE.getSelectNames();
		for(String name : snames){
			System.out.println(name);
		}
	}

}
