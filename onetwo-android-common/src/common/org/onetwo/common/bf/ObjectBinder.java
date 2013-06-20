package org.onetwo.common.bf;

import java.util.Map;

@SuppressWarnings("unchecked")
public interface ObjectBinder {

	public void bind(Class clazz);

	public void bind(String name, String value);

	public void bind(String name, Valuer valuer);

	public void bind(String name, Class clazz, Object... objects);

	public void bindAsList(String name, Object... value);
	
	public Map<String, ObjectInfo> getMapper();

}