package org.onetwo.android.view;

import java.lang.reflect.Method;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

public class MyAlertDialog {
	
	public static final String TAG = MyAlertDialog.class.getSimpleName();
	
	public static final String ALERT_OK_LISTNER = "onAlertOk";
	public static final String ALERT_CANCEL_LISTNER = "onAlertCancel";
	
	public static void show(String title, String message){
		new MyAlertDialog(OneTwoContext.get().activity()).build(title, message, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		}).ok("ok", null).show();
	}
	
	public static void show(String title, String message, String ok, String cancel){
		new MyAlertDialog(OneTwoContext.get().activity()).build(title, message, null).ok(ok, ALERT_OK_LISTNER).cancel(cancel, ALERT_CANCEL_LISTNER).show();
	}
	
	public MyAlertDialog(Activity context){
		this.context = context;
	}
	
	public MyAlertDialog(Activity context, String title, String message){
		this.context = context;
		build(title, message, null);
	}
	
	public MyAlertDialog(Activity context, int titleRes, int messageRes){
		this.context = context;
		String message = null;
		String title = null;
		if(context instanceof GenericActivity){
			title = str(titleRes);
			message = str(messageRes);
		}
		build(title, message, null);
	}
	
	protected Activity context = null;
	protected AlertDialog alert = null;
	protected DialogInterface.OnClickListener listener = null;
	
	public MyAlertDialog build(String title, String message, DialogInterface.OnClickListener listener){
		alert = new AlertDialog.Builder(context).create();
		if(StringUtils.hasText(title))
			alert.setTitle(title);
		if(StringUtils.hasText(message))
			alert.setMessage(message);
		this.listener = listener;
		return this;
	}
	
	public String str(int res){
		return context.getResources().getString(res);
	}
	
	public MyAlertDialog ok(int title){
		return ok(title, ALERT_OK_LISTNER);
	}
	
	public MyAlertDialog ok(int title, Object listener){
		return ok(str(title), listener);
	}
	
	public MyAlertDialog ok(String title, Object listener){
		return setButton(DialogInterface.BUTTON_POSITIVE, title, listener);
	}
	
	public MyAlertDialog cancel(int title){
		return cancel(str(title), ALERT_CANCEL_LISTNER);
	}
	
	public MyAlertDialog cancel(int title, Object listener){
		return cancel(str(title), listener);
	}
	
	public MyAlertDialog cancel(String title, Object listener){
		return setButton(DialogInterface.BUTTON_NEGATIVE, title, listener);
	}
	
	public MyAlertDialog neutral(String title, Object listener){
		return setButton(DialogInterface.BUTTON_NEUTRAL, title, listener);
	}
	
	public MyAlertDialog setButton(int whichBtn, String title, Object listener){
		if(listener==null)
			listener = this.listener;
		if(!(listener instanceof DialogInterface.OnClickListener || listener instanceof String ))
			throw new BaseException("error listenner : " + listener);
		if(listener instanceof DialogInterface.OnClickListener)
			alert.setButton(whichBtn, title, (DialogInterface.OnClickListener)listener);
		else{
			final String btnListener = (String) listener;
			alert.setButton(whichBtn, title, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					MyAlertDialog.this.invokeClickMethod(btnListener, dialog, whichButton);
					alert.dismiss();
				}
			});
		}
		return this;
	}
	
	protected void invokeClickMethod(String methodName, DialogInterface alert, int whichButton){
		Method method = ReflectUtils.findMethod(context.getClass(), methodName, new Class[]{DialogInterface.class, int.class});
		Log.i(TAG, "method: " + method);
		ReflectUtils.invokeMethod(method, context, alert, whichButton);
	}
	
	public void show(){
		Runnable showAlert = new Runnable() {
			public void run() {
				alert.show();
			}
		};
		context.runOnUiThread(showAlert);
	}
	
}
