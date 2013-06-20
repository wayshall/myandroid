package org.onetwo.common.utils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@SuppressWarnings("unchecked")
public class MyUtils {

	public static final char CHOMP = '\r';
	public static final char LINE = '\n';
	public static final char SPACE = ' ';
	public static final String HTML_BLANK = "&nbsp;";
	public static final String HTML_NEWLINE = "<br/>";
	public static final String REGEX = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	public static final Pattern USERNAME_PATTER = Pattern.compile(REGEX);

	private MyUtils() {
	}


	public final static String htmlEncode(String s) {
		if (StringUtils.isBlank(s))
			return "";

		StringBuilder str = new StringBuilder();

		for (int j = 0; j < s.length(); j++) {
			char c = s.charAt(j);

			// encode standard ASCII characters into HTML entities where needed
			if (c < '\200') {
				switch (c) {
				case '"':
					str.append("&quot;");

					break;

				case '&':
					str.append("&amp;");

					break;

				case '<':
					str.append("&lt;");

					break;

				case '>':
					str.append("&gt;");

					break;

				case '\n':
					str.append("<BR/>");

					/*
					 * case ' ':
					 * if(Locale.CHINA.getLanguage().equals(StrutsUtils.getCurrentSessionLocale().getLanguage()))
					 * str.append(" "); else str.append(" "); break;
					 */

				default:
					str.append(c);
				}
			} else if (c < '\377') {
				String hexChars = "0123456789ABCDEF";
				int a = c % 16;
				int b = (c - a) / 16;
				String hex = "" + hexChars.charAt(b) + hexChars.charAt(a);
				str.append("&#x" + hex + ";");
			} else {
				str.append(c);
			}
		}

		return str.toString();
	}

	public static Map convertParamMap(Object... params) {
		Map properties = null;
		if (params != null) {
			if (params.length % 2 == 1)
				throw new IllegalArgumentException("param must be even!");

			properties = new LinkedHashMap<Object, Object>();
			int index = 0;
			Object name = null;
			for (Object s : params) {
				if (index % 2 == 0) {
					if (s == null || StringUtils.isBlank(s.toString()))
						throw new IllegalArgumentException("the field can not be empty!");
					name = s;
				} else {
					properties.put(name, s);
				}
				index++;
			}
		}
		return properties;
	}

	public static String getLikeString(String str) {
		if (str.startsWith("%") || str.endsWith("%"))
			return str;
		StringBuilder sb = new StringBuilder();
		return sb.append("%").append(str).append("%").toString();
	}

	public static String appendPathSeparator(String path) {
		if (!path.endsWith(String.valueOf(File.separatorChar)))
			path += File.separatorChar;
		return path;
	}

	public static String append(String... strings) {
		if (strings == null || strings.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (String str : strings)
			if (!StringUtils.isEmpty(str))
				sb.append(str);
		return sb.toString();
	}

	public static boolean isAllowUserName(String userName) {
		return USERNAME_PATTER.matcher(userName).matches();
	}

	public static List asList(Object array) {
		return asList(array, true);
	}

	public static List asList(Object array, boolean trimNull) {
		if (array == null)
			return null;
		List list = null;
		if (array instanceof Collection) {
			if (trimNull)
				list = stripNull((Collection) array);
			else {// 本else块由mrs添加
				list = new ArrayList();
				list.addAll((Collection) array);
			}
		} else if (array.getClass().isArray()) {
			int length = Array.getLength(array);
			list = new ArrayList();
			for (int i = 0; i < length; i++) {
				Object value = Array.get(array, i);
				if (value == null || (value instanceof String && StringUtils.isBlank(value.toString())))
					continue;
				list.add(value);
			}
		} else {
			list = new ArrayList();
			list.add(array);
		}
		return (list == null || list.isEmpty()) ? null : list;
	}

	public static int getSize(Object array) {
		int size = 0;
		if (array == null)
			return size;
		if (array instanceof Collection) {
			size = ((Collection) array).size();
		} else if (array.getClass().isArray()) {
			size = Array.getLength(array);
		} else if (array instanceof Map) {
			size = ((Map) array).size();
		}
		return size;
	}

	public static boolean isArray(Object value) {
		return value != null && (value instanceof Collection || value.getClass().isArray());
	}

	public static List stripNull(Collection collection) {
		if (collection == null || collection.size() < 1)
			return null;
		List list = null;
		for (Object obj : collection) {
			if (obj == null || (obj instanceof String && StringUtils.isBlank(obj.toString())))
				continue;
			if (list == null)
				list = new ArrayList();
			list.add(obj);
		}
		return list;
	}

	public static List array2List(Object[] array) {
		if (array == null || array.length < 1)
			return null;
		List list = new ArrayList();
		for (Object obj : array) {
			if (obj == null || (obj instanceof String && StringUtils.isBlank(obj.toString())))
				continue;
			list.add(obj);
		}
		return list;
	}

	public static String nounderLineName(String str, boolean isFirstUpper) {
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

	public static String underLineName(String name) {
		StringBuffer table = new StringBuffer();
		char[] chars = name.toCharArray();
		table.append(Character.toLowerCase(chars[0]));
		for (int i = 1; i < chars.length; i++) {
			char ch = chars[i];
			if (Character.isUpperCase(ch)) {
				table.append("_");
				ch = Character.toLowerCase(ch);
			}
			table.append(ch);
		}
		return table.toString();
	}
}
