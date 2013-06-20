package org.onetwo.common.bf;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;


@SuppressWarnings({"unused", "unchecked"})
abstract public class BfUtils {
	
	public static Object initObject(Object val, Container container){
		Object realVal = null;
		if(String.class.isAssignableFrom(val.getClass())){
			String str = (String) val;
			if(str.indexOf(":")!=-1){
				String[] varArray = StringUtils.split(str, ":");
				String varName = varArray[0];
				String value = varArray[1];
				//TODO
			}else if(str.startsWith("&")){
				realVal = container.getObject(str.substring(1), true);
			}else{
				realVal = ReflectUtils.newInstance(val.toString());
			}
		}else if(Class.class.isAssignableFrom(val.getClass())){
			realVal = ReflectUtils.newInstance((Class)val);
		}else if(Valuer.class.isAssignableFrom(val.getClass())){
			realVal = ((Valuer)val).getValue();
		}
		else{
			throw new ServiceException("value must be a class or a valuer : " + val);
		}
		return realVal;
	}
}
