package org.onetwo.android.db.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("unchecked")
public class HqlSymbolManager extends AbstractSqlSymbolManager {

	public static SqlSymbolManager instance = new HqlSymbolManager().initParser();

	public static SqlSymbolManager getInstance(){
		return instance;
	}
	
	public HqlSymbolManager() {
		super();
	}

	public String createHql(Map properties, List parmaValues) {
		if (properties == null || properties.isEmpty())
			return null;

		StringBuilder hql = new StringBuilder();
		boolean first = true;
		for (Map.Entry entry : (Set<Map.Entry>)properties.entrySet()) {
			Object fields = entry.getKey();
			Object values = entry.getValue();
			if (StringUtils.isBlank(fields.toString()) || values == null || StringUtils.isBlank(values.toString()))
				continue;

			List valueList = (List<String>) MyUtils.asList(values);
			if(valueList==null || valueList.isEmpty())
				continue;
			List<String> fieldList = (List<String>) MyUtils.asList(fields);
			
			StringBuilder hqlScript = new StringBuilder();
			int index = 0;
			String h = null;
			
			for (int i=0; i<fieldList.size(); i++) {
				String p = fieldList.get(i);
				Object v = null;
				try{
					if(fieldList.size()==1)
						v = valueList;
					else
						v = valueList.get(i);
				}catch(IndexOutOfBoundsException e){
					v = valueList.get(0);
				}
				String[] sp = StringUtils.split(p, SPLIT_SYMBOL);

				if (sp.length == 1)
					h = getHqlSymbolParser("=").parse(p, v, parmaValues);
				else if (sp.length == 2)
					h = getHqlSymbolParser(sp[1]).parse(sp[0], v, parmaValues);
				else
					throw new BaseException("error symbol : " + p);

				if (StringUtils.isBlank(h))
					continue;
				
				if(index>0)
					hqlScript.append("or ");
				
				hqlScript.append(h);
				index++;
			}
			
			if(index>1){
				hqlScript.insert(0, "(");
				hqlScript.insert(hqlScript.length()-1, ")");
			}
			
			if (first) {
				hql.append("where ");
				first = false;
			} else if(index>0){
				hql.append("and ");
			}
			hql.append(hqlScript);

		}
		return hql.toString();
	}


}
