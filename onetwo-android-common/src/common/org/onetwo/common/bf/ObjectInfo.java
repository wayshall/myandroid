package org.onetwo.common.bf;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

@SuppressWarnings("unchecked")
public class ObjectInfo {
	
	private String name;
	private Object value;
	private Type rawType;
	private Type[] actualTypes;
	private boolean collection = false;
	private boolean classValue = false;
	
	public ObjectInfo(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
		this.rawType = value.getClass();
		if(Collection.class.isAssignableFrom(value.getClass())){
			collection = true;
			actualTypes = new Type[]{((Collection)value).iterator().next().getClass()};
		}else if(value instanceof Class){
			this.classValue = true;
			actualTypes = new Type[]{(Class)value};
		}else{
			actualTypes = new Type[]{rawType};
		}
	}
	
	public boolean isCollection(){
		return collection;
	}
	
	public boolean isMatchRawTypeClass(Class clazz){
		return clazz.isAssignableFrom((Class)rawType);
	}
	
	public boolean isMatchType(Type type){
		boolean match = false;
		if(Class.class.isAssignableFrom(type.getClass())){
			match = isMatchRawTypeClass((Class)type);
		}else if(ParameterizedType.class.isAssignableFrom(type.getClass())){
			ParameterizedType ptype = (ParameterizedType) type;
			match = isMatchRawType(ptype.getRawType()) && isMatchActualType(ptype.getActualTypeArguments());
		}else{
			match = rawType.equals(type);
		}
		return match;
	}
	
	protected boolean isMatchRawType(Type type){
		if(Class.class.isAssignableFrom(type.getClass())){
			return isMatchRawTypeClass((Class)type);
		}else{
			return this.rawType.equals(type);
		}
	}
	
	protected boolean isMatchActualType(Type[] types){
		if(actualTypes==types)
			return true;
		if(actualTypes.length!=types.length)
			return false;
		int index = 0;
		for(Type t : actualTypes){
			if(Class.class.isAssignableFrom(t.getClass()) && Class.class.isAssignableFrom(types[index].getClass())){
				Class supClass = (Class)types[index];
				if(supClass.isAssignableFrom((Class)t))
					continue;
			}
			if(!t.equals(types[index])){
				return false;
			}
			index++;
		}
		return true;
	}

	public boolean isClassValue() {
		return classValue;
	}

	public String getName() {
		return name;
	}
	
	public Object getValue() {
		return value;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("object info: [name=").append(name)
			.append(", valueType=").append(rawType.toString())
			.append("]");
		return sb.toString();
	}

	
}
