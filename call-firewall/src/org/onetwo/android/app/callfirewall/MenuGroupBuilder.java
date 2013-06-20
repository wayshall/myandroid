package org.onetwo.android.app.callfirewall;

import org.onetwo.android.view.MenuGroup;
import org.onetwo.android.view.MyAlertDialog;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

abstract public class MenuGroupBuilder {
	
	public static final int M_ORDER_ADD_PHONE = Menu.FIRST;
	
	public static final int M_ORDER_BACK = Menu.FIRST + 1;

	public static final int CM_DEL_PHONE = ContextMenu.FIRST+1;

	public static final int CM_EDIT_PHONE = ContextMenu.FIRST;

	public static MenuGroup MENU_BLACK_PHONES = new MenuGroup("black_phones_menus", 0, 0);
	
	public static MenuGroup CONTEXT_MENU_BLACK_PHONES = new MenuGroup("black_phones_context_menus", 0, 0);
	
	static{
		MENU_BLACK_PHONES.addMenuItem(M_ORDER_ADD_PHONE, R.string.menu_add_phone, "toAddPhone");
		
		MENU_BLACK_PHONES.addMenuItem(M_ORDER_BACK, R.string.menu_exit, new MenuGroup.MenuItemClickListener() {
			@Override
			public boolean onClick(Context context, MenuItem item) {
//				((GenericActivity) context).getComponent().startActivity(MainActivity.class);
				final Activity activity = (Activity) context;
				MyAlertDialog dialog = new MyAlertDialog(activity, R.string.app_alert_title, R.string.app_alert_message)
										.ok(R.string.app_alert_ok)
										.cancel(R.string.app_alert_cancel);
				dialog.show();

				return false;
			}
		});
		
		CONTEXT_MENU_BLACK_PHONES.addMenuItem(CM_EDIT_PHONE, R.string.menu_edit_phone, "editPhone")
							.addMenuItem(CM_DEL_PHONE, R.string.menu_del_phone, "delPhone");
	}

}
