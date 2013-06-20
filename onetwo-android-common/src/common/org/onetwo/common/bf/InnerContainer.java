package org.onetwo.common.bf;

import java.lang.reflect.Type;
import java.util.Map;

public interface InnerContainer extends Container {

	public ObjectInfo getObjectInfo(String key, boolean throwIfNull);
	public Object getObject(Type type);
	public Map<String, Object> getObjects(Type type);
	public void setBinder(ObjectBinder binder);
	
}
