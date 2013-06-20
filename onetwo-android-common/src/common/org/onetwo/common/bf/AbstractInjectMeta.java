package org.onetwo.common.bf;

@SuppressWarnings("unchecked")
abstract public class AbstractInjectMeta implements InjectMeta {

	protected Class targetClass;
	
	public AbstractInjectMeta(Class targetClass){
		this.targetClass = targetClass;
	}

	public Class getTargetClass() {
		return targetClass;
	}

}
