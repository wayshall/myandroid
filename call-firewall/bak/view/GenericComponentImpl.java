package org.onetwo.android.view;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

@SuppressWarnings({"rawtypes", "unchecked"})
public class GenericComponentImpl implements GenericComponent {
	
	public static final String tag = GenericComponentImpl.class.getSimpleName();
	
	protected Activity activity;
	protected MyAlertDialog exitDialog;
	
	public GenericComponentImpl(Activity activity){
		this.activity = activity;
	}

	public void createStatic(){
		Log.i(tag, "GenericComponentImpl createStatic: " + this);
		OneTwoContext.get().activity(this.activity);
	}

	@Override
	public <T> T findViewById(int id, Class<T> clazz) {
		T view = (T) activity.findViewById(id);
		return view;
	}

	@Override
	public void startActivity(Class clazz) {
		Intent intent = new Intent(activity, clazz);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(intent);
	}
	 
	public void showAlert(int title, int message, int ok, int cancel) {
		this.showAlert(title, message, ok, MyAlertDialog.ALERT_OK_LISTNER, cancel, MyAlertDialog.ALERT_CANCEL_LISTNER);
	}
	 
	public void showAlert(final String title, final String message, final String ok, String cancel) {
		this.showAlert(title, message, ok, MyAlertDialog.ALERT_OK_LISTNER, cancel, MyAlertDialog.ALERT_CANCEL_LISTNER);
	}
	 
	public void showAlert(final String title, final String message, final String ok, final Object oklistener, String cancel, String cancellistener) {
		new MyAlertDialog(activity, title, message)
						.ok(ok, oklistener)
						.cancel(cancel, cancellistener)
						.show();
	}
	 
	public void showAlert(int title, int message, int ok, final Object oklistener, int cancel, String cancellistener) {
		new MyAlertDialog(activity, title, message)
						.ok(ok, oklistener)
						.cancel(cancel, cancellistener)
						.show();
	}
	
    public void setExitDialog(MyAlertDialog exitDialog) {
		this.exitDialog = exitDialog;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if(this.exitDialog!=null && keyCode==KeyEvent.KEYCODE_BACK){
			Log.i(tag, "KEYCODE_BACK");
			exitDialog.show();
			return true;
		}else
			return false;
	}
	

}
