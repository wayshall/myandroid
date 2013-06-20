package org.onetwo.common.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("serial")
public class BaseException extends RuntimeException implements SystemErrorCode, Serializable{

	protected static final String DefaultMsg = "occur error";
	protected static final String Prefix = "[ERROR]:";
	protected String code = DEFAULT_SYSTEM_ERROR_CODE;

	protected List<Throwable> list = null;

	public BaseException() {
		super(DefaultMsg);
	}

	public BaseException(String msg) {
		super(Prefix + msg);
	}

	public BaseException(String msg, String code) {
		this(msg);
		this.code = code;
	}

	public BaseException(Throwable cause) {
		super(DefaultMsg, cause);
	}

	public BaseException(Throwable cause, String code) {
		this(cause);
		this.code = code;
	}

	public BaseException(String msg, Throwable cause) {
		super("[" + cause.getMessage() + "] : " + msg, cause);
	}

	public BaseException(String msg, Throwable cause, String code) {
		this(msg, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void addException(Exception e){
		if(list==null)
			list = new ArrayList<Throwable>(3);
		list.add(e);
	}

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isNotBlank(super.getMessage()))
        	sb.append(super.getMessage());
        
        if(list!=null){
        	for(Throwable e : list){
        		if(StringUtils.isNotBlank(e.getMessage()))
        			sb.append("[").append(e.getMessage()).append("] ");
        	}
        }
        
        return sb.toString();
    }

	public List<Throwable> getList() {
		return list;
	}

}
