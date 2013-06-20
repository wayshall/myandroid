package org.onetwo.android.db.query;

import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("unchecked")
public class Condition {

	protected String name;
	protected String varname;
	protected String op;
	protected Object value;
	protected int index;
	
	public Condition(){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getVarname() {
		return varname;
	}

	public void setVarname(String varname) {
		this.varname = varname;
	}
	
	public boolean isNamedParameter(){
		return StringUtils.isNotBlank(varname);
	}
	
	public boolean isAvailable(){
		return value!=null;
	}
	
}
