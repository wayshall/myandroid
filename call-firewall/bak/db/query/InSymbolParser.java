package org.onetwo.android.db.query;

import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;

/****
 * 对in操作符的解释
 * 
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
public class InSymbolParser extends CommonHqlSymbolParser implements HqlSymbolParser {
	
	public InSymbolParser(SqlSymbolManager manager){
		super("in", manager);
	}
	
	public String parse(String field, Object value, Object paramValues){
		if(value==null || (value instanceof String && StringUtils.isBlank(value.toString())))
			return null;
		
		List list = MyUtils.asList(value, true);
		if(list==null || list.isEmpty())
			return null;

//		FieldValueProcessor processor = this.getFieldValueProcessor(paramValues);
		StringBuilder hql = new StringBuilder();
		hql.append(field).append(" ").append(symbol).append(" ( ");
		for(int i=0; i<list.size(); i++){
			Object v = list.get(i);
			this.process(field, symbol, i, v, hql, paramValues);
			if(i!=list.size()-1)
				hql.append(", ");
		}
		hql.append(" ) ");
		
		return hql.toString();
	}
	
	protected void process(String field, String symbol, int index, Object value, StringBuilder sqlScript, Object paramValues){
		if(paramValues instanceof List){
			List list = (List) paramValues;
			sqlScript.append("?");
			list.add(getValue(value));
		}
		if(paramValues instanceof Map){
			Map map = (Map) paramValues;
			int size = map.entrySet().size();
			sqlScript.append(" :").append(field).append(size).append(" ");
			map.put(field+size, getValue(value));
		}
	}

}
