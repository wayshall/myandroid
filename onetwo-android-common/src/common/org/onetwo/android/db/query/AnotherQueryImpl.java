package org.onetwo.android.db.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.onetwo.common.utils.StringUtils;

/***
 * AnotherQuery的实现
 * 
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
public class AnotherQueryImpl implements AnotherQuery {
	
//	private static final Pattern PATTERN = Pattern.compile("([\\w\\.]+)[\\s]*(=|<|>|>=|<=|!=|in|like)[\\s]*(:[\\w\\.]+|\\?)", Pattern.CASE_INSENSITIVE);
	private static final Pattern PATTERN = Pattern.compile("([\\w\\.\\(\\)]+)[\\s]*(=|<|>|>=|<=|!=|in|like)([\\s\\(]*?)(:[\\w\\.]+|\\?)[\\s]*([\\)]*)", Pattern.CASE_INSENSITIVE);
	private static final String REPLACE_SQL = "1=1 ";
	
	private static final int FIELD = 1;
	private static final int OP = 2;
	private static final int VAR = 4;//4
	private static final int LEFT = 3;
	private static final int RIGHT = 5;

	protected String originalSql;
	
	protected String transitionSql;

	protected Map<String, Object> params;
	
	protected List<Condition> conditions = new ArrayList<Condition>();
	
	protected List values ;
	
	protected SqlSymbolManager hqlSymbolManager = HqlSymbolManager.getInstance();
	
	protected int firstRecord;
	
	protected int maxRecord;
	
	public AnotherQueryImpl(String sql){
		this.originalSql = sql;
		this.params = new LinkedHashMap<String, Object>();
		this.parseQuery();
	}
	
	public void parseQuery(){
		Matcher m = PATTERN.matcher(originalSql);
		Condition cond = null;
		int count = 0;
		while(m.find()){
			cond = new Condition();
			cond.setName(m.group(FIELD));
			cond.setOp(m.group(OP));
			if(!"?".equals(m.group(OP))){
				cond.setVarname(m.group(VAR).substring(1).trim());
			}
			cond.setIndex(count++);
			conditions.add(cond);
		}
	}
	
	
	@Override
	public AnotherQuery setParameters(Map<String, Object> params) {
		for(Map.Entry<String, Object> p : params.entrySet())
			setParameter(p.getKey(), p.getValue());
		return this;
	}

	public void compile(){
		Matcher m = PATTERN.matcher(originalSql);
		int count = 0;
		StringBuffer sb = new StringBuffer();
		this.values = new ArrayList();
		Condition cond = null;
		while(m.find()){
			System.out.println("var: "+m.group(0));
			
			cond = this.conditions.get(count++);
			String str = REPLACE_SQL;
			if(cond.isAvailable()){
				str = this.hqlSymbolManager.getHqlSymbolParser(cond.getOp()).parse(cond.getName(), cond.getValue(), values);
			}
			String left = m.group(LEFT).trim();
			String right = m.group(RIGHT).trim();
			if(")".equals(right) && !"(".equals(left)){
				str = str+")";
			}
			System.out.println("left: "+left);
			System.out.println("right: "+right);
			m.appendReplacement(sb, str);
		}
		m.appendTail(sb);
		this.transitionSql = sb.toString();
	}
	
	public AnotherQuery setParameter(int index, Object value){
		this.conditions.get(index).setValue(value);
		return this;
	}
	
	public AnotherQuery setParameter(String varname, Object value){
		for(Condition cond : this.conditions){
			if(!varname.equals(cond.getVarname()))
				continue;
			cond.setValue(value);
		}
		return this;
	}
	
	public int getParameterCount(){
		return this.conditions.size();
	}
	
	public int getActualParameterCount(){
		return this.getValues().size();
	}

	public List getPageValues() {
		List pageValue = new ArrayList(getValues());
		pageValue.add(firstRecord+maxRecord-1);
		pageValue.add(firstRecord-1);
		return pageValue;
	}

	public List getValues() {
		if(values==null || values.isEmpty())
			return Collections.EMPTY_LIST;
		return values;
	}

	public String getTransitionSql() {
		return transitionSql;
	}
	

	@Override
	public String getTransitionCountPageSql() {
		String hql = transitionSql;
		if(hql.indexOf("group by")!=-1){
			hql = "select count(*) from (" + hql + ") ";
		}else{
			hql = StringUtils.substringAfter(hql, "from ");
			hql = StringUtils.substringBefore(hql, " order by ");
			hql = "select count(*) from " + hql;
		}
		return hql;
	}

	public AnotherQuery setFirstRecord(int firstRecord) {
		this.firstRecord = firstRecord;
		return this;
	}

	public AnotherQuery setMaxRecord(int maxRecord) {
		this.maxRecord = maxRecord;
		return this;
	}
	
	public boolean isPage(){
		return this.firstRecord>0 && this.maxRecord>0;
	}
}
