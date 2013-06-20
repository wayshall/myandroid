package org.onetwo.android.db.query;

import java.util.List;
import java.util.Map;

/***
 * sql操作符管理
 * 
 * @author weishao
 *
 */
public interface SqlSymbolManager {
	
	public static class Dialect {
		public static final String SQLITE = "sqlite";
	}
	
	public static class Key {
		public static final String ORDER_BY = "order-by";
	}
	public String getOrderBy(Map params, String def);
	public AbstractSqlSymbolManager register(String symbol, HqlSymbolParser parser);
	
	public HqlSymbolParser getHqlSymbolParser(String symbol);
	
	public String createHql(Map properties, List values) ;

	public String getDialet() ;
	public void setDialet(String dialet);
}
