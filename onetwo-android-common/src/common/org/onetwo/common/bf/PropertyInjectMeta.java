package org.onetwo.common.bf;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.onetwo.common.exception.ServiceException;

@SuppressWarnings("unchecked")
public class PropertyInjectMeta extends AbstractInjectMeta {

	private PropertyDescriptor property;
	private Method writeMethod;
	
	public PropertyInjectMeta(Class targetClass, PropertyDescriptor property){
		super(targetClass);
		this.property = property;
		this.writeMethod = property.getWriteMethod();
	}
	
	public Type getInjectType(){
		return property.getPropertyType();
	}
	
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass){
		return writeMethod.getAnnotation(annotationClass);
	}
	
	public String getName(){
		return property.getName();
	}
	
	public void inject(Object bean, Object injectValue){
		try {
			writeMethod.invoke(bean, injectValue);
		} catch (Exception e) {
			throw new ServiceException("inject error: name=" + getName(), ", method:"+this.writeMethod.toGenericString());
		} 
	}

}
