package org.onetwo.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.exception.ServiceException;

@SuppressWarnings({"unchecked"})
public class BothMap<K, V> {
	
	private List<K> keys;
	private List<V> values;
	
	public BothMap(){
		keys = new ArrayList<K>();
		values = new ArrayList<V>();
	};
	
	public BothMap(Object[] keys, Object[] values){
		this.keys = MyUtils.asList(keys);
		this.values = MyUtils.asList(values);
		this.checkSize();
	}
	
	private void checkSize(){
		if(this.keys.size()!=this.values.size())
			throw new ServiceException("the keys size is not equals values size: keys="+this.keys.size()+", values="+this.values.size());
	}
	
	public BothMap map(K key, V value){
		this.keys.add(key);
		this.values.add(value);
		return this;
	}
	
	public boolean containsKey(K key){
		return this.keys.contains(key);
	}
	
	public boolean contaisValue(V value){
		return this.values.contains(value);
	}
	
	public K getKey(V value){
		if(!contaisValue(value))
			return null;
		int index = this.values.indexOf(value);
		return keys.get(index);
	}
	
	public V getValue(K key){
		if(!containsKey(key))
			return null;
		int index = this.keys.indexOf(key);
		return values.get(index);
	}

}
