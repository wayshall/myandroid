package org.onetwo.android.view;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MenuComponentWrapper implements MenuComponent {
	
	protected MenuGroup menuGroup = null;
	protected MenuGroup contextMenuGroup = null;
	
	protected Activity activity;
	
	public MenuComponentWrapper(Activity context){
		this.activity = context;
	}
	
 
	public void setMenuGroup(MenuGroup menuGroup) {
		this.menuGroup = menuGroup;
	}
	
	public void setContextMenuGroup(MenuGroup contextMenuGroup) {
		this.contextMenuGroup = contextMenuGroup;
	}

	public void onCreateOptionsMenu(Menu menu) {
		if(menuGroup!=null)
			menuGroup.appendTo(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		boolean rs = false;
		if(menuGroup!=null)
			rs = menuGroup.invokeItemOnClick(activity, item);
		return rs;
	}
    
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if(contextMenuGroup!=null)
			contextMenuGroup.appendTo(menu);
	}
    
    public boolean onContextItemSelected(MenuItem item){
    	boolean rs = false;
		if(contextMenuGroup!=null)
			rs = contextMenuGroup.invokeItemOnClick(activity, item);
		return rs;
    }
}
