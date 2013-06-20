package org.onetwo.common.bf;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.cache.CacheEngine;
import org.onetwo.common.cache.CacheFactory;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ObjectUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

import android.util.Log;


@SuppressWarnings({"unchecked", "hiding"})
public class DefaultObjectFactory implements InnerContainer {
	
	private CacheEngine<String, ObjectInfo> pool = new CacheFactory<String, ObjectInfo>().createDefaultCache();
	private InjectProcessor injectProcessor;
	private ObjectBinder binder;
	
	public DefaultObjectFactory(){
		injectProcessor = new DefaultInjectProcessor(this);
		binder = new ObjectBinderFacotry(this).getDefaultObjectBinder();
	}
	
	public void setBinder(ObjectBinder binder) {
		this.binder = binder;
	}

	public ObjectInfo registerObject(String name, Object val){
		Object t = initObject(val);
		if(t==null)
			throw new ServiceException("no object name : " + val);
		return put(name, t);
	}
	
	protected Object initObject(Object val){
		return BfUtils.initObject(val, this);
	}

	@Override
	public void inject(Object object) {
		try {
			this.injectProcessor.inject(object);
		} catch (Exception e) {
			throw new ServiceException("inject object error: " + object.getClass(), e);
		}
		if(Initializable.class.isAssignableFrom(object.getClass()))
			((Initializable)object).afterPropertySet();
	}

	public ObjectInfo registerList(String name, Object...args){
		Assert.notEmpty(args);
		List objs = new ArrayList();
		Object obj = null;
		for(Object o : args){
			obj = initObject(o);
			objs.add(obj);
		}
		return put(name, objs);
	}

	public ObjectInfo registerObject(String name, Class clazz, Object...objects){
		Object obj = null;
		if(!ObjectUtils.isEmpty(objects)){
			obj = ReflectUtils.newInstance(clazz, objects);
		}else{
			obj = ReflectUtils.newInstance(clazz);
		}
		return put(name, obj);
	}
	
	public boolean contains(String name){
		return this.pool.contains(name);
	}
	
	public <T> Map<String, T> getObjects(Class<T> clazz){
		Map<String, T> beans = new LinkedHashMap<String, T>();
		ObjectInfo objInfo = null;
		for(Map.Entry<String, ObjectInfo> entry : this.pool.getGroup(null).entrySet()){
			objInfo = entry.getValue();
			if(objInfo.isMatchRawTypeClass(clazz))
				beans.put((String)entry.getKey(), (T)objInfo.getValue());
		}
		return beans;
	}
	
	public <T> T getObject(Class<T> clazz) {
		 Map<String, T> maps = getObjects(clazz);
		 for(Map.Entry<String, T> t : maps.entrySet()){
			 return t.getValue();
		 }
		 return null;
	}

	public Map<String, Object> getObjects(Type type) {
		Map<String, Object> beans = new LinkedHashMap<String, Object>();
		ObjectInfo objInfo = null;
		for(Map.Entry<String, ObjectInfo> entry : this.pool.getGroup(null).entrySet()){
			objInfo = entry.getValue();
			if(objInfo.isMatchType(type))
				beans.put((String)entry.getKey(), objInfo.getValue());
		}
		return beans;
	}
	
	public Object getObject(Type type) {
		 Map<String, Object> maps = getObjects(type);
		 for(Map.Entry<String, Object> t : maps.entrySet()){
			 return t.getValue();
		 }
		 return null;
	}
	
	public <T> T getObject(String key){
		return (T)getObject(key, false);
	}
	
	public <T> T getObject(String key, boolean throwIfNull){
		ObjectInfo objInfo =  pool.get(key);
		if(objInfo==null){
			if(throwIfNull)
				throw new ServiceException("can not find the object: named["+key+"]");
			else
				return null;
		}
		return (T)objInfo.getValue();
	}
	
	public ObjectInfo getObjectInfo(String key, boolean throwIfNull){
		ObjectInfo objInfo =  pool.get(key);
		if(objInfo==null && throwIfNull)
			throw new ServiceException("can not find the objectInfo: named["+key+"]");
		return objInfo;
	}
	
	protected ObjectInfo put(Object value){
		return put(value.getClass().getName(), value);
	}
	
	protected ObjectInfo put(String name, Object value){
		if(StringUtils.isBlank(name))
			name = value.getClass().getSimpleName();
		if(contains(name))
			throw new ServiceException("object name has esixt. name:" + name+", class:"+value.getClass());
		ObjectInfo objInfo = new ObjectInfo(name, value);
		pool.put(name, objInfo);
		return objInfo;
	}


	@Override
	public void build(BfModule... modules) {
		if(modules==null)
			return ;
		for(BfModule bfm : modules){
			if(ContainerAware.class.isAssignableFrom(bfm.getClass()))
				((ContainerAware)bfm).setContainer(this);
			bfm.build(binder);
		}
		Map<String, ObjectInfo> objectMapper = binder.getMapper();
		if(objectMapper!=null){
			ObjectInfo obj = null;
			
			for(Map.Entry<String, ObjectInfo> entry : objectMapper.entrySet()){
				Log.i(this.getClass().getSimpleName(), "build object: ["+entry.getKey()+" = "+entry.getValue()+"]");
				obj = entry.getValue();
				if(obj.isCollection()){
					for(Object o : (Collection)obj.getValue())
						inject(o);
				}else{
					inject(obj.getValue());
				}
			}
		}
	}
	
	@Override
	public <T> T getObject(String name, Class<T> clazz) {
		return getObjects(clazz).get(name);
	}

	@Override
	public void destroy() {
		if(pool.isEmpty())
			return ;
		Collection<ObjectInfo> objects = pool.values();
		for(ObjectInfo obj : objects){
			if(obj.getValue() instanceof Destroyable)
				((Destroyable)obj.getValue()).onDestroy();
		}
		pool.clear();
	}

}
