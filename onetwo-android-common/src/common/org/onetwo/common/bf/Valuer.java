package org.onetwo.common.bf;

public class Valuer {
	
	private Object value;
	
	public static Valuer val(Object val){
		return new Valuer(val);
	}
	
	private Valuer(Object value){
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
	
}
