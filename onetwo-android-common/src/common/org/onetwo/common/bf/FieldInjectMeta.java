package org.onetwo.common.bf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.onetwo.common.utils.ReflectUtils;

@SuppressWarnings("unchecked")
public class FieldInjectMeta extends AbstractInjectMeta {
	
	private Field field;
	
	public FieldInjectMeta(Field field){
		super(field.getDeclaringClass());
		this.field = field;
	}
	
	public Type getInjectType(){
		return field.getGenericType();
	}
	
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass){
		return field.getAnnotation(annotationClass);
	}
	
	public String getName(){
		return field.getName();
	}
	
	public void inject(Object bean, Object injectValue){
		ReflectUtils.setFieldValue(field, bean, injectValue);
	}

}
