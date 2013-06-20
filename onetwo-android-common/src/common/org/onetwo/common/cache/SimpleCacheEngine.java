package org.onetwo.common.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("unchecked")
public class SimpleCacheEngine<K, V> implements CacheEngine<K, V> {
	
	public static final String DEFALUT_GROUP = "default_group";
	
	private Map<String, Map<K, V>> cache = new HashMap<String, Map<K, V>>();
	
	public SimpleCacheEngine(){
		cache.put(DEFALUT_GROUP, createdMap());
	}
	
	public void put(K k, V v){
		put(k, v, false);
	}
	
	public void put(K k, V v, boolean m){
		put(k, v, m, DEFALUT_GROUP);
	}
	
	public void put(K k, V v, boolean modify, String group){
		Assert.notNull(k, "key can not be null!");
		Assert.hasText(group, "group can not be empty!");
		if(modify){
			getGroup(group).put(k, v);
			return ;
		}
		if(this.contains(k, group)){
			return ;
		}
		getGroup(group).put(k, v);
	}
	
	public V get(K k){
		return getGroup(DEFALUT_GROUP).get(k);
	}
	
	public V get(K k, String group){
		return getGroup(group).get(k);
	}
	
	public Map<K, V> getGroup(String key){
		if(StringUtils.isBlank(key))
			key = DEFALUT_GROUP;
		Map<K, V> g = getCache().get(key);
		if(g==null){
			g = createdMap();
			getCache().put(key, g);
		}
		return g;
	}
	
	protected Map<K, V> createdMap(){
		return new LinkedHashMap<K, V>();
	}
	
	public Map<String, Map<K, V>> getCache() {
		return cache;
	}
	
	public boolean contains(K k){
		return contains(k, DEFALUT_GROUP);
	}
	
	public boolean contains(K k, String group){
		return getGroup(group).containsKey(k);
	}
	
	public boolean containsInAll(K k){
		if(this.cache.isEmpty())
			return false;
		for(Map.Entry<String, Map<K, V>> entry : this.cache.entrySet()){
			if(entry.getValue().containsKey(k))
				return true;
		}
		return false;
	}
	
	public List<V> values(){
		if(this.cache.isEmpty())
			return Collections.EMPTY_LIST;
		List<V> objs = new ArrayList<V>();
		for(Map.Entry<String, Map<K, V>> entry : this.cache.entrySet()){
			objs.addAll(entry.getValue().values());
		}
		return objs;
	}
	
	public boolean isEmpty(){
		boolean rs = this.cache.isEmpty();
		if(rs)
			return true;
		for(Map.Entry<String, Map<K, V>> entry : this.cache.entrySet()){
			if(!entry.getValue().isEmpty())
				return false;
		}
		return true;
	}

	@Override
	public Map<String, Map<K, V>> getAll() {
		return getCache();
	}

	@Override
	public void clear() {
		getCache().clear();
	}

}
