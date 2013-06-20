package org.onetwo.android.view;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.ObjectUtils;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

public class MenuGroup {
	
	protected String name;
	protected int groupId;
	protected int order;
	
	protected Map<Integer, SimpleMenuItem> itemMaps = new HashMap<Integer, SimpleMenuItem>();

	public static interface MenuItemClickListener {
		
		public boolean onClick(Context context, MenuItem item);
		
	}
	
	public static class SimpleMenuItem {
		

		public static SimpleMenuItem createItem(int groupId, int itemId, CharSequence title, int order, Object listener){
			SimpleMenuItem item = new SimpleMenuItem(groupId, itemId, title, order, listener);
			return item;
		}
		public static SimpleMenuItem createItem(int groupId, int itemId, int titleRes, int order, Object listener){
			SimpleMenuItem item = new SimpleMenuItem(groupId, itemId, titleRes, order, listener);
			return item;
		}
		
		public SimpleMenuItem(int groupId, int itemId, CharSequence title, int order, Object listener){
			this(groupId, itemId, 0, order, listener);
			this.title = title;
		}
		
		public SimpleMenuItem(int groupId, int itemId, int titleRes, int order, Object listener){
			this.groupId = groupId;
			this.itemId = itemId;
			this.titleRes = titleRes;
			this.order = order;
			this.listener = listener;
		}
		public int groupId;
		public int itemId;
		public CharSequence title;
		public int titleRes;
		public int order;
		public Object listener;
		
	}
	
	public MenuGroup(String name, int groupId, int order){
		this.name = name;
		this.groupId = groupId;
		this.order = order;
	}
	
	public MenuGroup addMenuItem(int itemId, int titleRes){
		return this.addMenuItem(itemId, titleRes, (String)null);
	}
	
	public MenuGroup addMenuItem(int itemId, CharSequence title){
		return this.addMenuItem(itemId, title, (String)null);
	}
	
	public MenuGroup addMenuItem(int itemId, int titleRes, String listener){
		SimpleMenuItem item = SimpleMenuItem.createItem(order, itemId, titleRes, order, listener);
		addMenuItem(item);
		return this;
	}
	
	public MenuGroup addMenuItem(int itemId, CharSequence title, String listener){
		SimpleMenuItem item = SimpleMenuItem.createItem(order, itemId, title, order, listener);
		addMenuItem(item);
		return this;
	}
	
	public MenuGroup addMenuItem(int itemId, int titleRes, MenuItemClickListener listener){
		SimpleMenuItem item = SimpleMenuItem.createItem(order, itemId, titleRes, order, listener);
		addMenuItem(item);
		return this;
	}
	
	public MenuGroup addMenuItem(int itemId, CharSequence title, MenuItemClickListener listener){
		SimpleMenuItem item = SimpleMenuItem.createItem(order, itemId, title, order, listener);
		addMenuItem(item);
		return this;
	}
	
	public MenuGroup addMenuItem(int itemId, int titleRes, MenuItem.OnMenuItemClickListener listener){
		SimpleMenuItem item = SimpleMenuItem.createItem(order, itemId, titleRes, order, listener);
		addMenuItem(item);
		return this;
	}
	
	public MenuGroup addMenuItem(int itemId, CharSequence title, MenuItem.OnMenuItemClickListener listener){
		SimpleMenuItem item = SimpleMenuItem.createItem(order, itemId, title, order, listener);
		addMenuItem(item);
		return this;
	}
	
	public MenuGroup addMenuItem(int itemId, int titleRes, int order, Object listener){
		SimpleMenuItem item = SimpleMenuItem.createItem(order, itemId, titleRes, order, listener);
		addMenuItem(item);
		return this;
	}
	
	public MenuGroup addMenuItem(SimpleMenuItem item){
		this.itemMaps.put(item.itemId, item);
		this.order = item.order;
		this.order++;
		return this;
	}
	
	public boolean invokeItemOnClick(Context context, MenuItem item){
		Object listener = this.itemMaps.get(item.getItemId()).listener;
		boolean rs = false;
		if(listener instanceof MenuItemClickListener)
			rs = ((MenuItemClickListener) listener).onClick(context, item);
		else if(listener instanceof MenuItem.OnMenuItemClickListener)
			rs = ((MenuItem.OnMenuItemClickListener) listener).onMenuItemClick(item);
		else
			rs = this.invokeClickMethod(context, (String)listener, item);
		return rs;
	}
	
	protected boolean invokeClickMethod(Context context, String methodName, MenuItem menuItem){
		Object rs = null;
		try {
			Method method = context.getClass().getMethod(methodName, MenuItem.class);
			rs = method.invoke(context, menuItem);
			if(rs==null || !(rs instanceof Boolean))
				rs = false;
		} catch (Exception e) {
			throw new BaseException("invoke method error ["+context+"."+methodName+"]: " + e.getMessage(), e);
		} 
		return (Boolean)rs;
	}
	
	public void appendTo(Menu menu, Integer...excludeItemIds){
		if(this.itemMaps==null)
			return ;
		for(SimpleMenuItem item : this.itemMaps.values()){
			if(ObjectUtils.containsElement(excludeItemIds, item.itemId))
				continue;
			if(item.title != null && item.title.length() > 0)
				menu.add(groupId, item.itemId, order, item.titleRes);
			if(item.titleRes != 0)
				menu.add(groupId, item.itemId, order, item.titleRes);
		}
	}
	
	public void setListener(int itemId, Object listener){
		SimpleMenuItem mi = this.itemMaps.get(itemId);
		if(mi==null)
			throw new BaseException("can not find the menuitem : " + itemId);
		mi.listener = listener;
	}
	
	public MenuGroup subGroup(Integer...itemIds){
		MenuGroup group = new MenuGroup(name, groupId, 0);
		for(SimpleMenuItem item : this.itemMaps.values()){
			if(!ObjectUtils.containsElement(itemIds, item.itemId))
				continue;
			group.itemMaps.put(item.itemId, item);
			group.order++;
		}
		return group;
	}
	
	public MenuGroup subGroupExclude(Integer...excludeItemIds){
		MenuGroup group = new MenuGroup(name, groupId, 0);
		for(SimpleMenuItem item : this.itemMaps.values()){
			if(ObjectUtils.containsElement(excludeItemIds, item.itemId))
				continue;
			group.itemMaps.put(item.itemId, item);
			group.order++;
		}
		return group;
	}

	public Map<Integer, SimpleMenuItem> getItemMaps() {
		return itemMaps;
	}

}
