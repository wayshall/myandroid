package org.onetwo.android.view;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

public interface MenuComponent {

	public void setMenuGroup(MenuGroup menuGroup);

	public void setContextMenuGroup(MenuGroup contextMenuGroup);
	

	public void onCreateOptionsMenu(Menu menu);
	
	public boolean onOptionsItemSelected(MenuItem item);
    
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo);
    
    public boolean onContextItemSelected(MenuItem item);
}
