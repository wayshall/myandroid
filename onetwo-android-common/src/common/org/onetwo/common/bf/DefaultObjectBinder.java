package org.onetwo.common.bf;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.utils.StringUtils;


@SuppressWarnings("unchecked")
public class DefaultObjectBinder implements ObjectBinder {
	
	private Container container;
	
	private Map<String, ObjectInfo> mapper = new HashMap<String, ObjectInfo>();
	
	public DefaultObjectBinder(Container container) {
		super();
		this.container = container;
	}
	
	protected boolean validate(String name, Object value){
		return true;
	}

	public void bind(Class clazz){
		bind(StringUtils.uncapitalize(clazz.getSimpleName()), clazz);
	}

	public void bind(String name, String value){ 
		if(!validate(name, value))
			return ;
		ObjectInfo objInfo = this.container.registerObject(name, value);
		mapper.put(name, objInfo);
	}

	public void bind(String name, Valuer valuer){
		if(!validate(name, valuer))
			return ;
		ObjectInfo objInfo = this.container.registerObject(name, valuer);
		mapper.put(name, objInfo);
	}

	public void bind(String name, Class clazz, Object...objects){
		if(!validate(name, clazz))
			return ;
		ObjectInfo objInfo = this.container.registerObject(name, clazz, objects);
		mapper.put(name, objInfo);
	}

	public void bindAsList(String name, Object... value){
		ObjectInfo objInfo = this.container.registerList(name, value);
		mapper.put(name, objInfo);
	}

	public Map<String, ObjectInfo> getMapper() {
		return mapper;
	}
	
}
