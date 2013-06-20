package org.onetwo.common.bf;


public class ObjectBinderFacotry {
	
	Container container;
	
	public ObjectBinderFacotry(Container container){
		this.container = container;
	}
	
	public ObjectBinder getDefaultObjectBinder(){
		return new DefaultObjectBinder(container);
	}
	
}
