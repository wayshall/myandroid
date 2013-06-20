package org.onetwo.android.view;

import android.view.KeyEvent;

@SuppressWarnings("rawtypes")
public interface GenericComponent {

	public void createStatic();
	
	public abstract <T> T findViewById(int id, Class<T> clazz);

	public abstract void startActivity(Class clazz);
	 
	public void showAlert(final String title, final String message, final String ok, String cancel);
	
	public void showAlert(final String title, final String message, final String ok, final Object oklistener, String cancel, String cancellistener);

	public void showAlert(int title, int message, int ok, final Object oklistener, int cancel, String cancellistener);
	
    public void setExitDialog(MyAlertDialog exitDialog);

	public boolean onKeyDown(int keyCode, KeyEvent event);

}