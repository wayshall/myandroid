package org.onetwo.android.view;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

//@SuppressWarnings({"unchecked", "rawtypes"})
public class BaseListActivity extends ListActivity implements GenericActivity{
	
	protected String tag = this.getClass().getSimpleName();
	protected MenuComponent menuCompt = null;
	protected GenericComponent component = null;
	
	public BaseListActivity(){
		menuCompt = new MenuComponentWrapper(this);
		component = new GenericComponentImpl(this);
	}

	public GenericComponent getComponent() {
		return component;
	}
	
	public String str(int resId){
		return this.getResources().getString(resId);
	}

	protected void onCreate(Bundle savedInstanceState) {
		Log.i(tag, "onCreate");
		super.onCreate(savedInstanceState);
		this.component.createStatic();
	}
	
	protected void onResume(){
		Log.i(tag, "onResume");
		super.onResume();
	}
	
	protected void onRestart(){
		Log.i(tag, "onRestart");
		super.onRestart();
	}
	
	protected void onStart(){
		Log.i(tag, "onStart");
		super.onStart();
	}
	
	protected void onPause(){
		Log.i(tag, "onPause");
		super.onPause();
	}
	
	protected void onStop(){
		Log.i(tag, "onStop");
		super.onPause();
	}
	
	protected void onDestroy(){
		Log.i(tag, "onDestroy");
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menuCompt.onCreateOptionsMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		boolean rs = super.onOptionsItemSelected(item);
		rs = menuCompt.onOptionsItemSelected(item);
		return rs;
	}
	
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menuCompt.onCreateContextMenu(menu, v, menuInfo);
	}
    
    public boolean onContextItemSelected(MenuItem item){
    	boolean rs = super.onContextItemSelected(item);
		rs = menuCompt.onContextItemSelected(item);
		return rs;
    }

	public void onAlertOk(DialogInterface dialog, int whichButton){
	}

	public void onAlertCancel(DialogInterface dialog, int whichButton){
	}

	public MenuComponent getMenuCompt() {
		return menuCompt;
	}

    public boolean onKeyDown(int keyCode, KeyEvent event)  {
    	boolean rs = getComponent().onKeyDown(keyCode, event);
    	if(rs)
    		return true;
		return super.onKeyDown(keyCode, event);
	}
}
