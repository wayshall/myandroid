package org.onetwo.common.bf.inject;

import java.lang.reflect.Type;

import org.onetwo.common.bf.InjectMeta;
import org.onetwo.common.bf.InnerContainer;
import org.onetwo.common.bf.ObjectInfo;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.StringUtils;

public class InjectParser implements AnnotationParser {
	
	public boolean containsAnnotation(InjectMeta meta){
		return meta.getAnnotation(Inject.class)!=null;
	}
	
	public Object parse(InjectMeta meta, InnerContainer container){
		Inject inject = meta.getAnnotation(Inject.class);
		
		String name = inject.name();
		Object val = null;
		Type type = meta.getInjectType();
		if(StringUtils.isNotBlank(name)){
			val = container.getObject(name);
		}else{
			ObjectInfo objInfo = container.getObjectInfo(meta.getName(), false);
			if(objInfo!=null && !objInfo.isMatchType(type)){
				val = null;
			}
			if(val==null){
				val = container.getObject(type);
			}
		}
		if(!inject.ignore() && val==null)
			throw new ServiceException("at least one match object["+meta.getTargetClass()+"] for the field [name="+meta.getName()+", type="+meta.getInjectType()+"]");
		return val;
	}

}
