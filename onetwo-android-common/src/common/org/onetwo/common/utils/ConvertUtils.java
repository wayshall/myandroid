package org.onetwo.common.utils;


@SuppressWarnings({"rawtypes", "unchecked"})
abstract public class ConvertUtils {
	
	public static interface TypeExecutor {
		public Object ifNull(Class clazz);
		public Integer ifInteger(Object value);
		public Long ifLong(Object value);
		public Float ifFloat(Object value);
		public Double ifDouble(Object value);
		public Boolean ifBoolean(Object value);
		public String ifString(Object value);
		public Object ifNotMatch(Object value);
	}
	
	public static class DefaultTypeExecutor implements TypeExecutor {

		@Override
		public Object ifNull(Class clazz) {
			return null;
		}

		@Override
		public Integer ifInteger(Object value) {
			return NumberUtils.toInt(value.toString());
		}

		@Override
		public Long ifLong(Object value) {
			return NumberUtils.toLong(value.toString());
		}

		@Override
		public Float ifFloat(Object value) {
			return NumberUtils.toFloat(value.toString());
		}

		@Override
		public Double ifDouble(Object value) {
			return NumberUtils.toDouble(value.toString());
		}

		@Override
		public Boolean ifBoolean(Object value) {
			return BooleanUtils.toBooleanObject(value.toString());
		}

		@Override
		public String ifString(Object value) {
			return value.toString();
		}

		@Override
		public Object ifNotMatch(Object value) {
			return value;
		}
		
	}
	
	public static final TypeExecutor DEFAULT_TYPE_EXECUTOR = new DefaultTypeExecutor(); 

	public static Object forType(Object value, Class<?> toType, TypeExecutor executor){
		if(value==null)
			return executor.ifNull(toType);
		Object newValue = null;
		if(toType==null)
			toType = (Class)String.class;
		
		if(toType==Long.class || toType==long.class){
			newValue = executor.ifLong(value);
		}else if(toType==Integer.class || toType==int.class){
			newValue = executor.ifInteger(value);
		}else if(toType==Double.class || toType==double.class){
			newValue = executor.ifDouble(value);
		}else if(toType==Float.class || toType==float.class){
			newValue = executor.ifFloat(value);
		}else if(toType==Boolean.class || toType==boolean.class){
			newValue = executor.ifBoolean(value);
		}else if(toType==String.class){
			newValue = executor.ifString(value);
		}else{
			newValue = executor.ifNotMatch(value);
		}
		return newValue;
	}
	

	public static <T> T convert(Object val, Class<T> toType){
		return convert(val, toType, null);
	}

	public static <T> T convert(Object val, Class<T> toType, T def){
		if(val==null)
			return def;
		Object newValue = null;
		if(toType==null)
			toType = (Class<T>)String.class;
		
		if(toType.isAssignableFrom(val.getClass()))
			return (T)val;
		
		String strVal = val.toString();
		if(toType==Long.class || toType==long.class){
			newValue = NumberUtils.toLong(strVal);
		}else if(toType==Integer.class || toType==int.class){
			newValue = NumberUtils.toInt(strVal);
		}else if(toType==Double.class || toType==double.class){
			newValue = NumberUtils.toDouble(strVal);
		}else if(toType==Float.class || toType==float.class){
			newValue = NumberUtils.toFloat(strVal);
		}else if(toType==Boolean.class || toType==boolean.class){
			newValue = BooleanUtils.toBooleanObject(strVal);
		}else if(toType==String.class){
			newValue = val.toString();
		}else{
			newValue = val;
		}
		return (T)newValue;
	}
}
