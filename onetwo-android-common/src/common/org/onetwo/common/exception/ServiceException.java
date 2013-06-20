package org.onetwo.common.exception;



public class ServiceException extends BaseException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7280411050853219784L;

	public ServiceException() {
		super();
		this.setErrorCode();
	}


	public ServiceException(String msg, String code) {
		super(msg, code);
	}


	public ServiceException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}


	public ServiceException(String msg, Throwable cause) {
		super(msg, cause);
		this.setErrorCode();
	}


	public ServiceException(String msg) {
		super(msg);
		this.setErrorCode();
	}


	public ServiceException(Throwable cause, String code) {
		super(cause, code);
	}


	public ServiceException(Throwable cause) {
		super(cause);
		this.setErrorCode();
	}
	
	protected void setErrorCode(){
		this.code = ERROR_SERVICE;
	}

}
