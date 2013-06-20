package org.onetwo.common.utils;

import java.lang.reflect.Array;

/**********
 * copy from apache lang
 *
 */

@SuppressWarnings("unchecked")
abstract public class ArrayUtils {

	public static final int INDEX_NOT_FOUND = -1;
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	public static boolean isNull(Object[] objs) {
		if (objs != null) {
			for (Object o : objs) {
				if (o != null) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isNotNull(Object[] objs) {
		return !isNull(objs);
	}

    public static Object[] clone(Object[] array) {
        if (array == null) {
            return null;
        }
        return (Object[]) array.clone();
    }

	public static Object[] remove(Object[] array, int index) {
		return (Object[]) remove((Object) array, index);
	}

	public static Object[] removeElement(Object[] array, Object element) {
		int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	private static Object remove(Object array, int index) {
		int length = getLength(array);
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
		}

		Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
		System.arraycopy(array, 0, result, 0, index);
		if (index < length - 1) {
			System.arraycopy(array, index + 1, result, index, length - index - 1);
		}

		return result;
	}

	public static int getLength(Object array) {
		if (array == null) {
			return 0;
		}
		return Array.getLength(array);
	}
	

    public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (objectToFind == null) {
            for (int i = startIndex; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = startIndex; i < array.length; i++) {
                if (objectToFind.equals(array[i])) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static int indexOf(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }
    
    public static boolean contains(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind) != INDEX_NOT_FOUND;
    }
    
	public static boolean isAssignableFrom(Class[] classes, Class clazz){
        if (classes == null || clazz==null) 
            return false;
        for(Class cls : classes){
        	if(cls.isAssignableFrom(clazz))
        		return true;
        }
        return false;
    }

    public static Object[] addAll(Object[] array1, Object[] array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        Object[] joinedArray = (Object[]) Array.newInstance(array1.getClass().getComponentType(),
                                                            array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static Object[] add(Object[] array, Object element) {
        Class type = (array != null ? array.getClass() : (element != null ? element.getClass() : Object.class));
        Object[] newArray = (Object[]) copyArrayGrow1(array, type);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    private static Object copyArrayGrow1(Object array, Class newArrayComponentType) {
        if (array != null) {
            int arrayLength = Array.getLength(array);
            Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
            return newArray;
        }
        return Array.newInstance(newArrayComponentType, 1);
    }
}
