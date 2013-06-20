package org.onetwo.common.cache;

import java.util.List;
import java.util.Map;

public interface CacheEngine<K, V> {

	void put(K k, V v);

	void put(K k, V v, boolean m);

	void put(K k, V v, boolean modify, String group);

	V get(K k);

	V get(K k, String group);
	
	public Map<K, V> getGroup(String group);

	boolean contains(K k);

	boolean contains(K k, String group);

	boolean containsInAll(K k);
	
	public List<V> values();
	
	public Map<String, Map<K, V>> getAll();
	
	public void clear();
	
	public boolean isEmpty();

}