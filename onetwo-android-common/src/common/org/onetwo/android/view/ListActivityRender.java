package org.onetwo.android.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class ListActivityRender {
	
	private ListActivity listActivity;

	public void render(Cursor cursor, int layoutId, Map<String, Integer> map){
		if(map==null || map.isEmpty())
			return ;
		this.listActivity.startManagingCursor(cursor);
		List<String> from = new ArrayList<String>();
		List<Integer> to = new ArrayList<Integer>();
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			from.add(entry.getKey());
			to.add(entry.getValue());
		}
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(listActivity, layoutId, cursor, from.toArray(new String[from.size()]), new int[]{});
	}
}
