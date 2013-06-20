package org.onetwo.common.bf;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.onetwo.common.bf.inject.AnnotationParser;
import org.onetwo.common.bf.inject.InjectParser;
import org.onetwo.common.utils.ReflectUtils;

public class DefaultInjectProcessor implements InjectProcessor {
	
	private InnerContainer container;
	private List<AnnotationParser> annotaionParsers = new ArrayList<AnnotationParser>(3);
	
	public DefaultInjectProcessor(InnerContainer container){
		this.container = container;
		addAnnotationParser(new InjectParser());
	}
	
	public InjectProcessor addAnnotationParser(AnnotationParser parser){
		annotaionParsers.add(parser);
		return this;
	}
	
	public void inject(Object object){
		Collection<Field> fields = ReflectUtils.findFieldsFilterStatic(object.getClass());
		for(Field field : fields){
			injectByField(object, field);
		}
		PropertyDescriptor[] propDescriptors = ReflectUtils.desribProperties(object.getClass());
		for(PropertyDescriptor propDes : propDescriptors){
			this.injectByProperty(object, propDes);
		}
	}
	
	protected void injectByProperty(Object object, PropertyDescriptor propertyDescriptor){
		if(propertyDescriptor.getWriteMethod()==null)
			return ;
		InjectMeta meta = new PropertyInjectMeta(object.getClass(), propertyDescriptor);
		injectByAnnotationParser(object, meta);
	}
	
	protected void injectByField(Object object, Field field){
		InjectMeta meta = new FieldInjectMeta(field);
		injectByAnnotationParser(object, meta);
	}
	
	protected void injectByAnnotationParser(Object object, InjectMeta meta){
		Object value = null;
		for(AnnotationParser parser : this.annotaionParsers){
			if(!parser.containsAnnotation(meta))
				continue;
			value = parser.parse(meta, container);
			meta.inject(object, value);
			/*if(value!=null){
				meta.inject(object, value);
				break;
			}else
				throw new ServiceException("at least one match object["+object.getClass()+"] for the field [name="+meta.getName()+", type="+meta.getInjectType()+"]");*/
		}
	}

	public InnerContainer getContainer() {
		return container;
	}

}
