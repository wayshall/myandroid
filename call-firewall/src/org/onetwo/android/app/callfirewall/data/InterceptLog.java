package org.onetwo.android.app.callfirewall.data;

import java.util.Date;

import org.onetwo.android.app.callfirewall.data.SmsCursor.MessageItem;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_INTERCEPT_LOG;
import org.onetwo.android.utils.SimpleCursor;
import org.onetwo.android.utils.SimpleCursor.CursorAdapter;

public class InterceptLog {
	
	public static class InterceptLogsAdapter implements CursorAdapter {

		@Override
		public Object adapter(SimpleCursor cursor) {
			InterceptLog log = new InterceptLog();
			log.setId(cursor.getInt(T_INTERCEPT_LOG.ID));
			log.setName(cursor.getString(T_INTERCEPT_LOG.NAME));
			log.setPhone(cursor.getString(T_INTERCEPT_LOG.PHONE));
			log.setInterceptType(cursor.getInt(T_INTERCEPT_LOG.INTERCEPT_TYPE));
			log.setContent(cursor.getString(T_INTERCEPT_LOG.CONTENT));
			log.setCreateAt(cursor.getDate(T_INTERCEPT_LOG.CREATED_AT));
			return log;
		}
		
	}
	
	public static final InterceptLogsAdapter ADAPTER = new InterceptLogsAdapter();
	
	public static InterceptLog create(MessageItem mi){
		InterceptLog log = new InterceptLog();
		log.setName(mi.address);
		log.setPhone(mi.address);
		log.setContent(mi.body);
		return log;
	}

	private int id;
	private String name;
	private String phone;
	private int interceptType;
	private String content;
	private Date createAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getInterceptType() {
		return interceptType;
	}

	public void setInterceptType(int interceptType) {
		this.interceptType = interceptType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
