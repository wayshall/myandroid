package org.onetwo.common.bf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public interface InjectMeta {

	public Class getTargetClass();
	
	public Type getInjectType();

	public String getName();

	public void inject(Object bean, Object injectValue);
	
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass);

}