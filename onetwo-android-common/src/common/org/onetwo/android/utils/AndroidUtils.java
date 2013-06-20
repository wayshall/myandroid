package org.onetwo.android.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.utils.ObjectUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.ReflectUtils.PropertyTypeProcessorAdapter;
import org.onetwo.common.utils.StringUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AndroidUtils {
	
	public static class ContentValueExecutor extends PropertyTypeProcessorAdapter {

		private ContentValues content;
		private boolean ignoreNull;
		
		public ContentValueExecutor(ContentValues content, boolean ignore){
			this.content = content;
			this.ignoreNull = ignore;
		}
		
		protected String getKey(String name){
			return StringUtils.convert2UnderLineName(true, name, "_");
		}

		@Override
		public void ifInteger(Object obj, Field prop) {
			Object value = getValue(obj, prop);
			if(value==null && ignoreNull){
				return ;
			}
			this.content.put(getKey(prop.getName()), (Integer)value);
		}

		@Override
		public void ifLong(Object obj, Field prop) {
			Object value = getValue(obj, prop);
			if(value==null && ignoreNull){
				return ;
			}
			this.content.put(getKey(prop.getName()), (Long)value);
		}

		@Override
		public void ifFloat(Object obj, Field prop) {
			Object value = getValue(obj, prop);
			if(value==null && ignoreNull){
				return ;
			}
			this.content.put(getKey(prop.getName()), (Float)value);
		}

		@Override
		public void ifDouble(Object obj, Field prop) {
			Object value = getValue(obj, prop);
			if(value==null && ignoreNull){
				return ;
			}
			this.content.put(getKey(prop.getName()), (Double)value);
		}

		@Override
		public void ifBoolean(Object obj, Field prop) {
			Object value = getValue(obj, prop);
			if(value==null && ignoreNull){
				return ;
			}
			this.content.put(getKey(prop.getName()), (Boolean)value);
		}

		@Override
		public void ifString(Object obj, Field prop) {
			Object value = getValue(obj, prop);
			if(value==null && ignoreNull){
				return ;
			}
			this.content.put(getKey(prop.getName()), (String)value);
		}

	}
	
	public static class MapExecutor extends PropertyTypeProcessorAdapter {

		private Map content;
		private boolean ignoreNull;
		
		public MapExecutor(Map content, boolean ignore){
			this.content = content;
			this.ignoreNull = ignore;
		}
		
		protected String getKey(String name){
			return StringUtils.convert2UnderLineName(true, name, "_");
		}

		@Override
		public void all(Object obj, Field field) {
			Object value = getValue(obj, field);
			if(value==null && ignoreNull){
				return ;
			}
			this.content.put(getKey(field.getName()), value);
		}

	}

	public static ContentValues convert2ContentValues(String...params){
		if(ObjectUtils.isEmpty(params))
			return null;
		ContentValues values = null;
		if (params.length % 2 == 1)
			throw new IllegalArgumentException("参数个数必须是偶数个！");

		values = new ContentValues();
		int index = 0;
		String name = null;
		for (String s : params) {
			if (index % 2 == 0) {
				if (s == null || StringUtils.isBlank(s.toString()))
					throw new IllegalArgumentException("字段名称不能为空！");
				name = s;
			} else {
				values.put(name, s);
			}
			index++;
		}
		return values;
	}
	
	public static ContentValues toContentValues(Object entity){
		return toContentValues(entity, true);
	}
	
	public static ContentValues toContentValues(Object entity, boolean ignoreNull){
		if(entity==null)
			return null;
		ContentValues values = new ContentValues();
		PropertyTypeProcessorAdapter executor = null;
		executor = new ContentValueExecutor(values, ignoreNull);
		ReflectUtils.forFields(entity, executor);
		return values;
	}
	
	public static Map toMap(Object entity, boolean ignoreNull){
		if(entity==null)
			return null;
		Map values = new HashMap();
		PropertyTypeProcessorAdapter executor = null;
		executor = new MapExecutor(values, ignoreNull);
		ReflectUtils.forFields(entity, executor);
		return values;
	}
	
	/*public static Map toMap(Object entity, boolean ignoreNull){
		if(entity==null)
			return null;
		Map values = new HashMap();
		PropertyTypeProcessorAdapter executor = null;
		executor = new ContentValueExecutor(values, ignoreNull);
		ReflectUtils.forFields(entity, executor);
		return values;
	}*/
	
	public static Intent createFrom(Activity activity, Class<?> clazz){
		return createFrom(activity.getIntent(), activity, clazz);
	}

	public static Intent createFrom(Intent source, Context context, Class<?> clazz){
		Intent intent = null;
		if(source!=null)
			intent = new Intent(source);
		else
			intent = new Intent();
		if(context!=null && clazz!=null)
			intent.setClass(context, clazz);
		return intent;
	}
	
}
