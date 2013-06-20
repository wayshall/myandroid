package org.onetwo.common.cache;

public class CacheFactory<K, V> {
	
	public CacheEngine<K, V> createDefaultCache(){
		CacheEngine<K, V> cache = new SimpleCacheEngine<K, V>();
		return cache;
	}

}
