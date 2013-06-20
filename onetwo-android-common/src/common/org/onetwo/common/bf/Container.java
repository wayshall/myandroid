package org.onetwo.common.bf;

import java.util.Map;


@SuppressWarnings({"unchecked","hiding"})
public interface Container {

	public void build(BfModule...modules);

	public void inject(Object object);
	

	public interface Initializable{
		
		public void afterPropertySet();
		
	}
	
	public interface Destroyable {
		
		public void onDestroy();
		
	}
	
	public ObjectInfo registerObject(String name, Object val);

	public ObjectInfo registerList(String name, Object...objs);

	public ObjectInfo registerObject(String name, Class clazz, Object... objects);
	

	public boolean contains(String name);
	
	public <T> Map<String, T> getObjects(Class<T> clazz);
	
	public <T> T getObject(Class<T> clazz);
	
	public <T> T getObject(String key);
	public <T> T getObject(String key, boolean throwIfNull);
	
	public <T>T getObject(String name, Class<T> clazz);
	

	public void destroy();
}
