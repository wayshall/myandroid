package org.onetwo.android.utils;

import org.onetwo.common.utils.ObjectUtils;
import org.onetwo.common.utils.StringUtils;

import android.content.ContentValues;

public abstract class AndroidUtils {

	public static ContentValues convert2ContentValues(String...params){
		if(ObjectUtils.isEmpty(params))
			return null;
		ContentValues values = null;
		if (params.length % 2 == 1)
			throw new IllegalArgumentException("参数个数必须是偶数个！");

		values = new ContentValues();
		int index = 0;
		String name = null;
		for (String s : params) {
			if (index % 2 == 0) {
				if (s == null || StringUtils.isBlank(s.toString()))
					throw new IllegalArgumentException("字段名称不能为空！");
				name = s;
			} else {
				values.put(name, s);
			}
			index++;
		}
		return values;
	}
	
}
