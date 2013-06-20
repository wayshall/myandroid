package org.onetwo.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class StringUtils {

	public static final String EMPTY = "";

	public static boolean isBlank(String str) {
		return str == null || str.trim().equals("");
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	public static String getLastName(String clsName, String sep) {
		int index = clsName.lastIndexOf(sep);
		if (index != -1)
			return clsName.substring(index);
		else
			return clsName;
	}

	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static String append(String... strings) {
		if (strings == null || strings.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (String str : strings)
			sb.append(str);
		return sb.toString();
	}

	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuffer(strLen)
				.append(Character.toTitleCase(str.charAt(0)))
				.append(str.substring(1)).toString();
	}

	public static String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StringBuffer(strLen)
				.append(Character.toLowerCase(str.charAt(0)))
				.append(str.substring(1)).toString();
	}

	public static String toJavaName(String str, boolean isFirstUpper) {
		if (str.indexOf('_') == -1) {
			if (isFirstUpper && Character.isLowerCase(str.charAt(0))) {
				return str.substring(0, 1).toUpperCase() + str.substring(1);
			} else {
				return str;
			}
		}
		char[] chars = str.toCharArray();
		StringBuilder newStr = new StringBuilder();
		boolean needUpper = isFirstUpper;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (needUpper) {
				c = Character.toUpperCase(c);
				needUpper = false;
			}
			if (c == '_') {
				needUpper = true;
				continue;
			}
			newStr.append(c);
		}
		return newStr.toString();
	}

	public static String convert2UnderLineName(String name) {
		return convert2UnderLineName(name, "_");
	}

	public static String convert2UnderLineName(String name, String op) {
		return convert2UnderLineName(false, name, op);
	}

	public static String convert2UnderLineName(boolean firstAppendOp, String name, String op) {
		StringBuffer table = new StringBuffer();
		char[] chars = name.toCharArray();
		if(firstAppendOp)
			table.append(op);
		table.append(Character.toLowerCase(chars[0]));
		for (int i = 1; i < chars.length; i++) {
			char ch = chars[i];
			if (Character.isUpperCase(ch)) {
				table.append(op);
				ch = Character.toLowerCase(ch);
			}
			table.append(ch);
		}
		return table.toString();
	}

	public static String getSimpleBeanName(String clsName) {
		if (clsName.indexOf('.') == -1)
			return clsName;
		int index = clsName.lastIndexOf('.');
		String sn = clsName.substring(index + 1);
		return uncapitalize(sn);
	}

	public static String[] split(String str, char separatorChar) {
		return splitWorker(str, separatorChar, false);
	}

	private static String[] splitWorker(String str, char separatorChar,
			boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)

		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		List list = new ArrayList();
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		while (i < len) {
			if (str.charAt(i) == separatorChar) {
				if (match || preserveAllTokens) {
					list.add(str.substring(start, i));
					match = false;
					lastMatch = true;
				}
				start = ++i;
				continue;
			} else {
				lastMatch = false;
			}
			match = true;
			i++;
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static String[] split(String str, String separatorChars) {
		return splitWorker(str, separatorChars, -1, false);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static String substringAfter(String str, String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (separator == null) {
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	public static String substringBefore(String str, String separator) {
		if (isEmpty(str) || separator == null) {
			return str;
		}
		if (separator.length() == 0) {
			return EMPTY;
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	public static String[] splitWorker(String str, String separatorChars,
			int max, boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)
		// Direct code is quicker than StringTokenizer.
		// Also, StringTokenizer uses isSpace() not isWhitespace()

		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		List list = new ArrayList();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			// Null separator means use whitespace
			while (i < len) {
				if (Character.isWhitespace(str.charAt(i))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				} else {
					lastMatch = false;
				}
				match = true;
				i++;
			}
		} else if (separatorChars.length() == 1) {
			// Optimise 1 character case
			char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				} else {
					lastMatch = false;
				}
				match = true;
				i++;
			}
		} else {
			// standard case
			while (i < len) {
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				} else {
					lastMatch = false;
				}
				match = true;
				i++;
			}
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static int indexOfAny(String str, String... searchStrs) {
		if ((str == null) || (searchStrs == null)) {
			return -1;
		}
		int sz = searchStrs.length;

		// String's can't have a MAX_VALUEth index.
		int ret = Integer.MAX_VALUE;

		int tmp = 0;
		for (int i = 0; i < sz; i++) {
			String search = searchStrs[i];
			if (search == null) {
				continue;
			}
			tmp = str.indexOf(search);
			if (tmp == -1) {
				continue;
			}

			if (tmp < ret) {
				ret = tmp;
			}
		}

		return (ret == Integer.MAX_VALUE) ? -1 : ret;
	}

	public static String join(Object[] array, String separator) {
		if (array == null) {
			return null;
		}
		return join(array, separator, 0, array.length);
	}
	public static String join(Collection collection, String separator) {
		if (collection == null) {
			return null;
		}
		return join(collection.iterator(), separator);
	}
	

    public static String join(Iterator iterator, String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return EMPTY;
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
        }

        // two or more elements
        StringBuffer buf = new StringBuffer(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }
    

	public static String join(Object[] array, String separator, int startIndex,
			int endIndex) {
		if (array == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}

		// endIndex - startIndex > 0: Len = NofStrings *(len(firstString) +
		// len(separator))
		// (Assuming that all Strings are roughly equally long)
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		bufSize *= ((array[startIndex] == null ? 16 : array[startIndex]
				.toString().length()) + separator.length());

		StringBuffer buf = new StringBuffer(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		System.out.println(indexOfAny("private static transient test()",
				"static", "aaaaaa"));
	}
}
