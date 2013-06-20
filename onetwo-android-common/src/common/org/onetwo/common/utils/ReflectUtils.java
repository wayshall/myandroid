package org.onetwo.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ReflectUtils {

	public static interface FieldTypeProcessor {
		public void ifByte(Object obj, Field prop);
		public void ifCharacter(Object obj, Field prop);
		public void ifInteger(Object obj, Field prop);
		public void ifLong(Object obj, Field prop);
		public void ifFloat(Object obj, Field prop);
		public void ifDouble(Object obj, Field prop);
		public void ifBoolean(Object obj, Field prop);
		public void ifString(Object obj, Field prop);
		public void ifList(Object obj, Field prop);
		public void ifMap(Object obj, Field prop);
		public void other(Object obj, Field prop);
		public void all(Object obj, Field prop);
	}
	
	public static class PropertyTypeProcessorAdapter implements FieldTypeProcessor {

		public Object getValue(Object obj, Field field){
			return ReflectUtils.getFieldValueByGetter(obj, field, false);
		}
		
		@Override
		public void ifByte(Object obj, Field field) {
			Object value = getValue(obj, field);
			if(value==null){
				ifNull(field, value);
				return ;
			}
			doByte(field, (Byte)value);
		}
		
		public void doByte(Field field, Byte value){
		}

		@Override
		public void ifCharacter(Object obj, Field field) {
			Object value = getValue(obj, field);
			if(value==null){
				ifNull(field, value);
				return ;
			}
			doCharacter(field, (Character)value);
		}
		
		public void doCharacter(Field field, Character value){
		}

		@Override
		public void ifInteger(Object obj, Field field) {
			Object value = getValue(obj, field);
			if(value==null){
				ifNull(field, value);
				return ;
			}
			doInteger(field, (Integer)value);
		}
		
		public void doInteger(Field field, Integer value){
		}

		@Override
		public void ifLong(Object obj, Field field) {
			Object value = getValue(obj, field);
			if(value==null){
				ifNull(field, value);
				return ;
			}
			doLong(field, (Long)value);
		}
		
		public void doLong(Field field, Long value){
		}

		@Override
		public void ifFloat(Object obj, Field field) {
			Object value = getValue(obj, field);
			if(value==null){
				ifNull(field, value);
				return ;
			}
			doFloat(field, (Float)value);
		}
		
		public void doFloat(Field field, Float value){
		}

		@Override
		public void ifDouble(Object obj, Field field) {
			Object value = getValue(obj, field);
			if(value==null){
				ifNull(field, value);
				return ;
			}
			doDouble(field, (Double)value);
		}
		
		public void doDouble(Field field, Double value){
		}

		@Override
		public void ifBoolean(Object obj, Field field) {
			Object value = getValue(obj, field);
			if(value==null){
				ifNull(field, value);
				return ;
			}
			doBoolean(field, (Boolean)value);
		}
		
		public void doBoolean(Field field, Boolean value){
		}

		@Override
		public void ifString(Object obj, Field field) {
			Object value = getValue(obj, field);
			if(value==null){
				ifNull(field, value);
				return ;
			}
			doString(field, (String)value);
		}
		
		public void doString(Field field, String value){
		}

		@Override
		public void ifList(Object obj, Field field) {
			Object value = getValue(obj, field);
			if(value==null){
				ifNull(field, value);
				return ;
			}
			doList(field, (List)value);
		}
		
		public void doList(Field field, List value){
		}

		@Override
		public void ifMap(Object obj, Field field) {
			Object value = getValue(obj, field);
			if(value==null){
				ifNull(field, value);
				return ;
			}
			doMap(field, (Map)value);
		}
		
		public void doMap(Field field, Map value){
		}

		public void ifNull(Field field, Object value) {
		}

		@Override
		public void other(Object obj, Field field) {
		}

		@Override
		public void all(Object obj, Field field) {
		}
		
		
	}
	
	public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
    private static WeakHashMap<Class, PropertyDescriptor[]> DESCRIPTORS_CACHE = new WeakHashMap<Class, PropertyDescriptor[]>();
//    private static WeakHashMap<Class, Collection<Field>> FIELDS_CACHE = new WeakHashMap<Class, Collection<Field>>();
	
	private ReflectUtils() {
	}

	public static <T, ID> void mergeByCheckedIds(final Collection<T> srcObjects, final Collection<ID> checkedIds, final Class<T> clazz, final String idName) {

		Assert.notNull(srcObjects, "srcObjects不能为null");
		Assert.hasText(idName, "idName不能为null");
		Assert.notNull(clazz, "clazz不能为null");

		if (checkedIds == null) {
			srcObjects.clear();
			return;
		}

		Iterator<T> srcIterator = srcObjects.iterator();
		try {

			while (srcIterator.hasNext()) {
				T element = srcIterator.next();
				Object id;
				id = getProperty(element, idName);

				if (!checkedIds.contains(id)) {
					srcIterator.remove();
				} else {
					checkedIds.remove(id);
				}
			}

			for (ID id : checkedIds) {
				T obj = clazz.newInstance();
				setProperty(obj, idName, id);
				srcObjects.add(obj);
			}
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}
	

	public static Object getProperty(Object element, String propName) {
		return getProperty(element, propName, true);
	}

	public static Object getProperty(Object element, String propName, boolean throwIfError) {
		if(element instanceof Map){
			return getValue((Map)element, propName);
		}
		PropertyDescriptor prop = getPropertyDescriptor(element, propName);
		Object value = null;
		if(prop!=null)
			value = invokeMethod(throwIfError, prop.getReadMethod(), element);
		return value;
	}

	public static Object getProperty(Object element, PropertyDescriptor prop) {
		return invokeMethod(false, prop.getReadMethod(), element);
	}
	
	protected static Object getValue(Map map, String propName){
		return ((Map)map).get(propName);
	}
	
	public static void setProperty(Object element, String propName, Object value) {
		setProperty(element, propName, value, true);
	}

	public static void setProperty(Object element, String propName, Object value, boolean throwIfError) {
		try {
			if(element instanceof Map){
				((Map)element).put(propName, value);
				return ;
			}
			PropertyDescriptor prop = getPropertyDescriptor(element, propName);
			if(prop==null)
				throw new BaseException("can not find the property : " + propName);
			invokeMethod(prop.getWriteMethod(), element, value);
		} catch (Exception e) {
			if(throwIfError)
				handleReflectionException(e);
		}
	}

	public static void tryToSetProperty(Object element, String propName, Object value) {
		boolean exp = false;
		try {
			setProperty(element, propName, value, true);
		} catch (Exception e) {
			exp = true;
		}
		if(exp){
			exp = false;
			try {
				String setMethodName = "set"+propName.substring(0, 1).toUpperCase()+propName.substring(1);
				invokeMethod(setMethodName, element, value);
			} catch (Exception e) {
				exp = true;
			}
		}
		if(exp){
			exp = false;
			try {
				setFieldValue(propName, element, value);
			} catch (Exception e) {
				exp = true;
			}
		}
		
		if(exp)
			throw new ServiceException("can not set the property["+propName+"]'s value");
	}
	
	

	public static PropertyDescriptor getPropertyDescriptor(Object element, String propName) {
		PropertyDescriptor[] props = desribProperties(element.getClass());
		for (PropertyDescriptor prop : props) {
			if (prop.getName().equals(propName))
				return prop;
		}
		return null;
	}

	public static List convertElementPropertyToList(final Collection collection, final String propertyName) {
		if (collection == null || collection.isEmpty())
			return null;
		List list = new ArrayList();

		try {
			for (Object obj : collection) {
				if (obj == null)
					continue;
				list.add(getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			handleReflectionException(e);
		}

		return list;
	}

	public static String convertElementPropertyToString(final Collection collection, final String propertyName, final String separator) {
		List list = convertElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, Object.class);
	}

	public static <T> Class<T> getSuperClassGenricType(final Class clazz, final Class stopClass) {
		return getSuperClassGenricType(clazz, 0, stopClass);
	}

	/***************************************************************************
	 * GenricType handler, copy form spring-side
	 * 
	 * @param clazz
	 * @param index
	 * @param stopClass
	 * @return
	 */
	public static Class getSuperClassGenricType(final Class clazz, final int index, final Class stopClass) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			if (stopClass.isAssignableFrom((Class) genType)) {
				while (!(genType instanceof ParameterizedType)) {
					genType = ((Class) genType).getGenericSuperclass();
					if (genType == null)
						return Object.class;
				}
			} else {
				return Object.class;
			}
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}

		return (Class) params[index];
	}

	public static Class getGenricType(final Object obj, final int index) {

		Class clazz = obj.getClass();
		Type genType = (Type) clazz;

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}

		return (Class) params[index];
	}

	public static Class getListGenricType(final Class clazz) {
		Class genClass = null;
		if (List.class.isAssignableFrom(clazz)) {
			Method method = findMethod(clazz, "get", int.class);
			if (method != null) {
				Type rtype = method.getGenericReturnType();
				if (ParameterizedType.class.isAssignableFrom(rtype.getClass())) {
					ParameterizedType ptype = (ParameterizedType) rtype;
					genClass = (Class) ptype.getActualTypeArguments()[0];
				}
			}
		}
		return genClass;
	}
	

	public static Method findMethod(Class objClass, String name, Class... paramTypes) {
		return findMethod(false, objClass, name, paramTypes);
	}

	public static Method findMethod(boolean ignoreIfNotfound, Class objClass, String name, Class... paramTypes) {
		Assert.notNull(objClass, "objClass must not be null");
		Assert.notNull(name, "Method name must not be null");
		try {
			Class searchType = objClass;
			while (!Object.class.equals(searchType) && searchType != null) {
				Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
				for (int i = 0; i < methods.length; i++) {
					Method method = methods[i];
//					System.out.println("method:"+method);
//					if (name.equals(method.getName()) && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					if (name.equals(method.getName()) && (paramTypes == null || matchParameterTypes(paramTypes, method.getParameterTypes()))) {
//						System.out.println("method match");
						return method;
					}
				}
				searchType = searchType.getSuperclass();
			}
		} catch (Exception e) {
			if(ignoreIfNotfound)
				return null;
			handleReflectionException(e);
		}
		return null;
	}
	

	public static boolean matchParameterTypes(Class[] sourceTypes, Class[] targetTypes) {
		if (sourceTypes.length != targetTypes.length)
			return false;
		int index = 0;
		for (Class cls : targetTypes) {
			if (!cls.isAssignableFrom(sourceTypes[index]))
				return false;
			index++;
		}
		return true;
	}

	public static List<Method> findAnnotationMethods(Class objClass, Class<? extends Annotation> annoClasses) {
		Assert.notNull(annoClasses);
		List<Method> methodList = null;
		try {
			Method[] methods = objClass.getMethods();
			for (Method method : methods) {
				if (method.getAnnotation(annoClasses) == null)
					continue;
				if (methodList == null)
					methodList = new ArrayList<Method>();
				methodList.add(method);
			}
		} catch (Exception e) {
			handleReflectionException(e);
		}
		return methodList;
	}
	


	public static Method findUniqueAnnotationMethod(Class objClass, Class<? extends Annotation> annoClasses, boolean throwIfMore) {
		Assert.notNull(annoClasses);
		Method method = null;
		try {
			Method[] methods = objClass.getMethods();
			for (Method m : methods) {
				if (m.getAnnotation(annoClasses) == null)
					continue;
				if(method!=null && throwIfMore)
					throw new ServiceException("the class["+objClass+"] has more than one method has the annotaiton["+annoClasses+"]");
				method = m;
			}
		} catch (Exception e) {
			handleReflectionException(e);
		}
		return method;
	}

	/***************************************************************************
	 * reflectionException handle, copy from spring
	 * 
	 * @param ex
	 */
	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method: " + ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		handleUnexpectedException(ex);
	}

	public static void handleInvocationTargetException(InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}

	public static void rethrowRuntimeException(Throwable ex) {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		handleUnexpectedException(ex);
	}

	private static void handleUnexpectedException(Throwable ex) {
		IllegalStateException isex = new IllegalStateException("Unexpected exception thrown");
		isex.initCause(ex);
		throw isex;
	}

	public static <T> T newInstance(Class<T> clazz) {
		T instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			throw new ServiceException("instantce class error : " + clazz, e);
		}
		return instance;
	}

	public static Class<?> loadClass(String className) {
		Class<?> clz = null;
		try {
			clz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ServiceException("class not found : " + className, e);
		}
		return clz;
	}

	public static <T> T newInstance(String className) {
		return (T) newInstance(loadClass(className));
	}

	public static <T> List<T> toInstanceList(String clsNames) {
		if(StringUtils.isBlank(clsNames))
			return Collections.EMPTY_LIST;
		String[] cls = StringUtils.split(clsNames, ",");
		T inst = null;
		List list = new ArrayList();
		for(String c : cls){
			inst = ReflectUtils.newInstance(c);
			if(inst!=null)
				list.add(inst);
		}
		return list;
	}

	public static <T> T newInstance(Class<T> clazz, Object... objects) {
		T instance = null;
		try {
			Constructor<T>[] constructs = (Constructor<T>[]) clazz.getDeclaredConstructors();
			boolean appropriateConstractor = false;
			for (Constructor<T> constr : constructs) {
				if (matchConstructor(constr, objects)) {
					constr.setAccessible(true);
					instance = constr.newInstance(objects);
					appropriateConstractor = true;
					break;
				}
			}
			if (appropriateConstractor == false && instance == null) {
				StringBuilder sb = new StringBuilder("can not find the appropriate constructor, class: ").append(clazz.getName()).append(", args: ");
				if (objects != null) {
					for (Object o : objects)
						sb.append(o.getClass().getName()).append(" ");
				}
				throw new ServiceException(sb.toString());
			}
		} catch (Exception e) {
			throw new ServiceException("instantce class error : " + clazz, e);
		}
		return instance;
	}

	public static boolean matchConstructor(Constructor constr, Object... objects) {
		Class[] pclass = constr.getParameterTypes();
		if (objects.length != pclass.length)
			return false;
		int index = 0;
		for (Class cls : pclass) {
			if (!hasImplements(objects[index], cls))
				return false;
			index++;
		}
		return true;
	}

	public static boolean hasImplements(Object obj, Class clazz) {
		return clazz.isAssignableFrom(obj.getClass());

	}

	public static PropertyDescriptor findProperty(Class<?> clazz, String propName) {
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz, Object.class);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		for (PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
			if (propName.equals(prop.getName()))
				return prop;
		}
		return null;
	}

	public static PropertyDescriptor[] desribProperties(Class<?> clazz) {
		PropertyDescriptor[] props = DESCRIPTORS_CACHE.get(clazz);
		if(props!=null)
			return props;
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz, Object.class);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		props = beanInfo.getPropertyDescriptors();
		if(props!=null){
			DESCRIPTORS_CACHE.put(clazz, props);
		}
		return props;
	}

	public static Map toMap(Object obj) {
		if(obj==null)
			return Collections.EMPTY_MAP;
		PropertyDescriptor[] props = desribProperties(obj.getClass());
		if(props==null || props.length==0)
			return Collections.EMPTY_MAP;
		Map rsMap = new HashMap();
		Object val = null;
		for(PropertyDescriptor prop : props){
			val = getProperty(obj, prop);
			if(val!=null)
				rsMap.put(prop.getName(), val);
		}
		return rsMap;
	}

	public static void copy(Object source, Object target) {
		if(source==null)
			return ;
		Collection<String> propNames = getPropertiesName(source);
		Object value = null;
		for(String prop : propNames){
			value = getProperty(source, prop);
			setProperty(target, prop, value);
		}
	}

	public static Map field2Map(Object obj) {
		if(obj==null)
			return Collections.EMPTY_MAP;
		Collection<Field> fields = findFieldsFilterStatic(obj.getClass());
		if(fields==null || fields.isEmpty())
			return Collections.EMPTY_MAP;
		Map rsMap = new HashMap();
		Object val = null;
		for(Field field : fields){
			val = getFieldValue(field, obj, false);
			if(val!=null)
				rsMap.put(field.getName(), val);
		}
		return rsMap;
	}

	public static List<PropertyDescriptor> desribProperties(Class<?> clazz, Class<? extends Annotation> excludeAnnoClass) {
		PropertyDescriptor[] props = desribProperties(clazz);
		List<PropertyDescriptor> propList = new ArrayList<PropertyDescriptor>();
		Method method = null;
		for (PropertyDescriptor prop : props) {
			method = prop.getReadMethod();
			if (method == null || method.getAnnotation(excludeAnnoClass) != null)
				continue;
			propList.add(prop);
		}
		return propList;
	}

	public static Collection<Field> findFieldsFilterStatic(Class clazz) {
		return findFieldsExcludeAnnotationStatic(clazz, null);
	}

	public static Collection<Field> findFieldsExcludeAnnotationStatic(Class clazz, Class<? extends Annotation> excludeAnnotation) {
		return findFieldsExclude(clazz, new Class[] { excludeAnnotation }, " static ", " transient ");
	}

	public static Collection<Field> findFieldFilter(Class clazz, String... filterString) {
		return findFieldsExclude(clazz, null, filterString);
	}

	public static Collection<Field> findFieldsExclude(Class clazz, Class<? extends Annotation>[] excludeAnnoClasses, String... filterString) {
		List<Class> classes = findSuperClasses(clazz);
		classes.add(0, clazz);

		Collection<Field> fields = new HashSet<Field>();
		Field[] fs = null;
		for (Class cls : classes) {
			fs = cls.getDeclaredFields();
			for (Field f : fs) {
				if (filterString != null && StringUtils.indexOfAny(f.toString(), filterString) != -1)
					continue;
				if (excludeAnnoClasses != null) {
					for (Class<? extends Annotation> annoCls : excludeAnnoClasses) {
						if (annoCls == null || f.getAnnotation(annoCls) != null)
							continue;
					}
				}
				fields.add(f);
			}
		}
		
		return fields;
	}
	


	public static Field findField(Class clazz, String fieldName) {
		return findField(clazz, fieldName, false);
	}

	public static Field findField(Class clazz, String fieldName, boolean throwIfNotfound) {
		List<Class> classes = findSuperClasses(clazz);
		classes.add(0, clazz);

		Field[] fs = null;
		for (Class cls : classes) {
			fs = cls.getDeclaredFields();
			for (Field f : fs) {
				if (f.getName().equals(fieldName))
					return f;
			}
		}
		if(throwIfNotfound)
			throw new ServiceException("can not find class["+clazz+"]'s field [" + fieldName + "]");
		return null;
	}
	
	public static List<String> getPropertiesName(Object obj) {
		if(obj instanceof Map){
			return new ArrayList<String>(((Map)obj).keySet());
		}else{
			return desribPropertiesName(obj.getClass());
		}
	}

	public static List<String> desribPropertiesName(Class<?> clazz) {
		PropertyDescriptor[] props = desribProperties(clazz);
		if (props == null)
			return null;
		List<String> propsName = new ArrayList();
		for (PropertyDescriptor p : props) {
			propsName.add(p.getName());
		}
		return propsName;
	}

	public static List<Class> findSuperClasses(Class clazz) {
		return findSuperClasses(clazz, Object.class);
	}

	public static List<Class> findSuperClasses(Class clazz, Class stopClass) {
		List<Class> classes = new ArrayList<Class>();
		Class parent = clazz.getSuperclass();
		while (parent != null && !parent.equals(stopClass)) {
			classes.add(parent);
			parent = parent.getSuperclass();
		}
		return classes;

	}
	
	public static Object invokeMethod(String methodName, Object target, Object... args) {
		return invokeMethod(findMethod(target.getClass(), methodName, findTypes(args)), target, args);
	}
	
	protected static Class[] findTypes(Object...args){
		Class[] argTypes = null;
		if(args!=null){
			for(Object arg : args){
				if(arg==null)
					continue;
				Class t = arg.getClass();
				ArrayUtils.add(argTypes, t);
			}
		}
		return argTypes;
	}
	
	public static Object invokeMethod(String methodName, Object target) {
		return invokeMethod(findMethod(target.getClass(), methodName, (Class[])null), target, (Object[]) null);
	}

	public static Object invokeMethod(boolean throwIfError, Method method, Object target) {
		return invokeMethod(throwIfError, method, target, (Object[]) null);
	}

	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, (Object[]) null);
	}

	public static Object invokeMethod(Method method, Object target, Object... args) {
		return invokeMethod(true, method, target, args);
	}

	public static Object invokeMethod(boolean throwIfError, Method method, Object target, Object... args) {
		try {
			if(!method.isAccessible())
				method.setAccessible(true);
			return method.invoke(target, args);
		} catch (Exception ex) {
			if(throwIfError)
				throw new ServiceException("invoke method error: " + ex.getMessage(), ex);
			else
				return null;
		}
	}
	


	public static void setFieldValue(String fieldName, Object obj, Object value) {
		setBean(obj, fieldName, value);
	}
	
	public static void setBean(Object obj, String fieldName, Object value) {
		Field field = findField(obj.getClass(), fieldName);
		setFieldValue(field, obj, value);
	}

	public static void setFieldValue(Field f, Object obj, Object value) {
		setBean(obj, f, value);
	}

	public static void setBean(Object obj, Field f, Object value) {
		try {
			if (!f.isAccessible())
				f.setAccessible(true);
			f.set(obj, value);
		} catch (Exception ex) {
			throw new ServiceException("invoke method error: " + ex.getMessage(), ex);
		}
	}

	public static Object getFieldValue(Object obj, String fieldName) {
		if(obj instanceof Map){
			return getValue((Map)obj, fieldName);
		}
		return getFieldValue(obj, fieldName, true);
	}

	public static Object getFieldValue(Object obj, String fieldName, boolean throwIfError) {
		Field f = findField(obj.getClass(), fieldName);
		return getFieldValue(f, obj, throwIfError);
	}

	public static Object getFieldValue(Field f, Object obj, boolean throwIfError) {
		try {
			if (!f.isAccessible())
				f.setAccessible(true);
			return f.get(obj);
		} catch (Exception ex) {
			if(throwIfError)
				throw new ServiceException("get value of field["+f+"] error: " + ex.getMessage(), ex);
			else
				return null;
		}
	}
	

	public static void setFieldValueBySetter(Object obj, String fieldName, Object value, boolean throwIfError) {
		Field f = findField(obj.getClass(), fieldName);
		setFieldValueBySetter(obj, f, value, throwIfError);
	}
	public static Object getFieldValueByGetter(Object obj, String fieldName, boolean throwIfError) {
		Field f = findField(obj.getClass(), fieldName);
		return getFieldValueByGetter(obj, f, throwIfError);
	}

	public static Object getFieldValueByGetter(Object obj, Field f, boolean throwIfError) {
		try {
			String getterName = "get" + StringUtils.toJavaName(f.getName(), true);
			Method getter = findMethod(obj.getClass(), getterName);
			Object val = invokeMethod(getter, obj);
			return val;
		} catch (Exception ex) {
			if(throwIfError)
				throw new ServiceException("get value of field["+f+"] error: " + ex.getMessage(), ex);
			else
				return null;
		}
	}

	public static void setFieldValueBySetter(Object obj, Field f, Object value, boolean throwIfError) {
		try {
			String setterName = "set" + StringUtils.toJavaName(f.getName(), true);
			Method setter = findMethod(obj.getClass(), setterName, f.getType());
			invokeMethod(setter, obj, value);
		} catch (Exception ex) {
			if(throwIfError)
				throw new ServiceException("get value of field["+f+"] error: " + ex.getMessage(), ex);
		}
	}

	public static Object getStaticFieldValue(Class clazz, String fieldName) {
		Field field = findField(clazz, fieldName, true);
		return getFieldValue(field, clazz, true);
	}

	public static boolean isEqualsMethod(Method method) {
		if (method == null || !method.getName().equals("equals")) {
			return false;
		}
		Class[] paramTypes = method.getParameterTypes();
		return (paramTypes.length == 1 && paramTypes[0] == Object.class);
	}

	public static boolean isHashCodeMethod(Method method) {
		return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
	}

	public static boolean isToStringMethod(Method method) {
		return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
	}
	
	public static void forFields(Object obj, FieldTypeProcessor executor){
		Collection<Field> fields = ReflectUtils.findFieldsFilterStatic(obj.getClass());
		if(fields==null || fields.isEmpty())
			return ;
		Class toType = null;
		for(Field prop : fields){
			toType = prop.getType();
			executor.all(obj, prop);
			if(toType==Long.class || toType==long.class){
				executor.ifLong(obj, prop);
			}else if(toType==Character.class || toType==char.class){
				executor.ifCharacter(obj, prop);
			}else if(toType==Byte.class || toType==byte.class){
				executor.ifByte(obj, prop);
			}else if(toType==Integer.class || toType==int.class){
				executor.ifInteger(obj, prop);
			}else if(toType==Double.class || toType==double.class){
				executor.ifDouble(obj, prop);
			}else if(toType==Float.class || toType==float.class){
				executor.ifFloat(obj, prop);
			}else if(toType==Boolean.class || toType==boolean.class){
				executor.ifBoolean(obj, prop);
			}else if(toType==String.class){
				executor.ifString(obj, prop);
			}else if(toType==List.class){
				executor.ifList(obj, prop);
			}else if(toType==Map.class){
				executor.ifMap(obj, prop);
			}else{
				executor.other(obj, prop);
			}
		}
	}
	/*
	public static void forProperties(Object obj, PropertyTypeProcessor executor){
		PropertyDescriptor[] props = ReflectUtils.desribProperties(obj.getClass());
		if(props==null || props.length==0)
			return ;
		Class toType = null;
		for(PropertyDescriptor prop : props){
			toType = prop.getPropertyType();
			executor.all(obj, prop);
			if(toType==Long.class || toType==long.class){
				executor.ifLong(obj, prop);
			}else if(toType==Character.class || toType==char.class){
				executor.ifCharacter(obj, prop);
			}else if(toType==Byte.class || toType==byte.class){
				executor.ifByte(obj, prop);
			}else if(toType==Integer.class || toType==int.class){
				executor.ifInteger(obj, prop);
			}else if(toType==Double.class || toType==double.class){
				executor.ifDouble(obj, prop);
			}else if(toType==Float.class || toType==float.class){
				executor.ifFloat(obj, prop);
			}else if(toType==Boolean.class || toType==boolean.class){
				executor.ifBoolean(obj, prop);
			}else if(toType==String.class){
				executor.ifString(obj, prop);
			}else if(toType==List.class){
				executor.ifList(obj, prop);
			}else if(toType==Map.class){
				executor.ifMap(obj, prop);
			}else{
				executor.other(obj, prop);
			}
		}
	}*/

	public static void main(String[] args) {
		
	}

}
