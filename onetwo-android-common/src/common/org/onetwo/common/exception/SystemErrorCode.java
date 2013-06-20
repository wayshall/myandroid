package org.onetwo.common.exception;

public interface SystemErrorCode {

	public String APP_ERROR_MESSAGE = "appErrorMessage";
	
	public String DEFAULT_SYSTEM_ERROR_CODE = "999999999";
	public String ERROR_SERVICE = "100";
	public String ERROR_WEB = "200";
	
	
	/***
	 * 登陆错误
	 */
	public String ES_LOGIN_FAILED = "100001";
	public String ES_USER_NOT_FOUND = "100001001";//找不到此用户
	public String ES_ERROR_PASSWORD = "100001005";//错误的密码
	public String ES_LOGIN_TIMEOUT  = "100001010";//登陆超时
	public String ES_NO_TOKEN  = "100001015";//没有找到此令牌的用户，请先登陆
	
	
	/***
	 * 验证错误
	 */
	public String ES_AUTH_ERROR  = "100002";//验证失败
	public String ES_AUTH_NO_LOGIN  = "200002001";//没有登陆
	
	
	
	
}
