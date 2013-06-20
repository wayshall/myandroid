package org.onetwo.android.db;

import java.beans.PropertyDescriptor;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("rawtypes")
abstract public class TableBuilder {
	

	
	public static Table build(String tableName, String id){
		Table table = new Table(tableName, id);
		return table;
	}
	
	public static Table build(Class entityClass){
		String tableName = StringUtils.convert2UnderLineName(entityClass.getSimpleName());
		PropertyDescriptor[] props = ReflectUtils.desribProperties(entityClass);
		Table table = new Table(tableName);
		
		String name = null;
		SqlType sqlType = null;
		for(PropertyDescriptor prop : props){
			name = StringUtils.convert2UnderLineName("_"+prop.getName());
			sqlType = SqlType.valueOf(prop.getPropertyType());
			if("id".equals(name))
				table.id(name);
			else
				table.column(name, sqlType);
		}
		if(table.getId()==null)
			throw new ServiceException("the table must set a primary key: table=" + tableName);
		return table;
	}
	

	public static enum SqlType {
		INTEGER(Integer.class, Number.class), 
		LONG(Long.class), 
		VARCHAR(String.class), 
		DATE(java.sql.Date.class), 
		TIME(Time.class), 
		TIMESTAMP(Timestamp.class, Date.class), 
		TEXT(String.class);

		private List<Class> mapClasses = new ArrayList<Class>(1);

		SqlType(Class... classes) {
			this.mapClasses = Arrays.asList(classes);
		}

		public List<Class> getMapClasses() {
			return mapClasses;
		}

		public String toString() {
			return super.toString().toLowerCase();
		}
		
		public static SqlType valueOf(Class clazz){
			SqlType[] types = SqlType.values();
			for(SqlType type : types){
				if(type.getMapClasses().contains(clazz))
					return type;
			}
			return TEXT;
		}

	}

	public static class Column {
		public Column(String name, SqlType sqlType, boolean nullable) {
			this(name, sqlType, nullable, false, null);
		}
		public Column(String name, SqlType sqlType, boolean nullable, boolean primaryKey) {
			this(name, sqlType, nullable, primaryKey, null);
		}
		public Column(String name, SqlType sqlType, boolean nullable, boolean primaryKey, Object def) {
			this.name = name;
			this.sqlType = sqlType;
			this.nullable = nullable;
			this.primaryKey = primaryKey;
			this.defaultValue = def;
		}

		private String name;
		private SqlType sqlType;
		private boolean nullable;
		private boolean primaryKey;
		private Object defaultValue;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public SqlType getSqlType() {
			return sqlType;
		}

		public void setSqlType(SqlType sqlType) {
			this.sqlType = sqlType;
		}

		public boolean isNullable() {
			return nullable;
		}

		public void setNullable(boolean nullable) {
			this.nullable = nullable;
		}

		public boolean isPrimaryKey() {
			return primaryKey;
		}

		public void setPrimaryKey(boolean primaryKey) {
			this.primaryKey = primaryKey;
			if(this.primaryKey)
				this.setNullable(false);
		}

		public void setDefaultValue(Object defaultValue) {
			this.defaultValue = defaultValue;
		}
		public String toString() {
			StringBuilder sb = new StringBuilder(name);
			sb.append(" ").append(sqlType.toString()).append("");
			if (!nullable) {
				sb.append(" not null");
			}
			if(primaryKey){
				sb.append(" primary key autoincrement");
			}
			if(defaultValue!=null)
				sb.append(" default ").append(defaultValue.toString());
			return sb.toString();
		}
	}

	public static class Table {

		public static final String COLUMNS_KEY = "columns_array_key";
		
		private String name;
		private Column id;
		private Map<String, Column> columns = new LinkedHashMap<String, Column>();
//		private Cacheable<String, Object> cache = new DefaultCache<String, Object>();

		public Table(String name){
			this(name, null);
		}
		public Table(String name, String id){
			this.name = name;
			if(StringUtils.isNotBlank(id))
				id(id);
		}
		
		public String[] getColumnsArray(){
			/*String[] cols = (String[]) cache.get(COLUMNS_KEY);
			if(cols!=null)
				return cols;*/
			String[] cols = columns.keySet().toArray(new String[columns.size()]);
//			cache.put(COLUMNS_KEY, cols);
			return cols;
		}
		
		public String getCountSql(String...fields){
			StringBuilder sb = new StringBuilder();
			sb.append("select count(*) count_field from ").append(name);
			if(fields==null || fields.length==0)
				return sb.toString();
			sb.append(" where 1=1");
			for(String f : fields){
				if(StringUtils.hasText(f))
					sb.append(" and ").append(f).append("=?");
			}
			return sb.toString();
		}

		public Map<String, Column> getColumns() {
			return columns;
		}

		public void setColumns(Map<String, Column> columns) {
			this.columns = columns;
		}

		public Table id(String name) {
			Column column = new Column(name, SqlType.INTEGER, false, true);
			id = column;
			return column(column);
		}
		
		public Column getId() {
			return id;
		}

		public Table column(String name, SqlType sqlType) {
			Column column = new Column(name, sqlType, true, false);
			return column(column);
		}

		public Table column(String name, SqlType sqlType, boolean nullable) {
			return column(name, sqlType, nullable, null);
		}

		public Table column(String name, SqlType sqlType, boolean nullable, Object defaultValue) {
			Column column = new Column(name, sqlType, nullable, false, defaultValue);
			return column(column);
		}

		public Table column(Column column) {
			getColumns().put(column.name, column);
			return this;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String create(){
			StringBuilder sb = new StringBuilder("create table ");
			sb.append(name).append("( ");
			int index = 0;
			int size = this.columns.size();
			for(Column col : this.columns.values()){
				sb.append(col.toString());
				if(index!=size-1)
					sb.append(", ");
				index++;
			}
			sb.append(" );");
			return sb.toString();
		}
		
		public String drop(){
			StringBuilder sb = new StringBuilder("drop table");
			sb.append(" if exists ").append(name);
			return sb.toString();
		}
	}

}
