package org.onetwo.android.db.query;

import java.util.List;
import java.util.Map;

/***
 * 对sql查询语句的封装
 * 可解释含有命名参数的sql语句
 * 
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
public interface AnotherQuery {

	public void compile();

	public AnotherQuery setParameter(int index, Object value);

	public AnotherQuery setParameter(String varname, Object value);
	
	public AnotherQuery setParameters(Map<String, Object> params);

	public int getParameterCount();

	public int getActualParameterCount();

	public List getValues();

	public String getTransitionSql();

//	public String getTransitionPageSql();

	public String getTransitionCountPageSql();

	public List getPageValues() ;
	
	public AnotherQuery setFirstRecord(int firstRecord);
	
	public AnotherQuery setMaxRecord(int maxRecord);

}