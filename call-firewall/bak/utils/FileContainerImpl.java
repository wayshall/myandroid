package org.onetwo.android.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import android.content.Context;
import android.util.Log;


/****
 * 
 * [header]
 * [list=datalist]
 * [properties=props]
 * 
 * @author zengweishao
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class FileContainerImpl implements Serializable{

	public static final int HEADER_INDEX = 1;
	public static final String HEADER = "[header]";

	public static final int DATA_LIST_INDEX = 2;
	public static final String DATA_LIST = "[list=${name}]";

	public static final int PROPERTIES_INDEX = 3;
	public static final String PROPERTIES = "[properties=${name}]";
	
	protected static class Header{
		public static final String HEADER_COUNT_POSTFIX = "_count";
		
		private Map<String, Object> headDatas = new HashMap<String, Object>();
		
		public Header(){
			this.reset();
		}
		
		protected void setDataCount(String key, int count){
			this.headDatas.put(key + HEADER_COUNT_POSTFIX, count);
		}
		
		public void setDataCountIncrease(String key, int count){
			this.setDataCount(key, this.getDataCount(key)+count);
		}
		
		public int getDataCount(String key){
			return this.getInt(key + HEADER_COUNT_POSTFIX);
		}
		
		public void setPropertiesCount(String key, int count){
			this.headDatas.put(key+HEADER_COUNT_POSTFIX, count);
		}
		
		public void setPropertiesCountIncrease(String key, int count){
			this.setPropertiesCount(key, this.getPropertiesCount(key)+count);
		}
		
		public int getPropertiesCount(String key){
			return getInt(key+HEADER_COUNT_POSTFIX);
		}
		
		public void put(String key, Object value){
			this.headDatas.put(key, value);
		}
		
		public Object get(String key){
			return this.headDatas.get(key);
		}
		
		public void setInt(String key, String value){
			int intValue = Integer.parseInt(value);
			put(key, intValue);
		}
		
		public int getInt(String key){
			Object v = get(key);
			if(v==null)
				return 0;
			int intv = Integer.parseInt(v.toString());
			return intv;
		}
		
		public void reset(){
			this.headDatas.clear();
		}

		public Map<String, Object> getHeadDatas() {
			return headDatas;
		}
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			for(Map.Entry<String, Object> entry : headDatas.entrySet()){
				sb.append(entry.getKey()).append("=").append(entry.getValue().toString()).append("\n");
			}
			return sb.toString();
		}
		
	}
	
	protected Map<String, Object> fileData = new LinkedHashMap<String, Object>();
	protected String fileName;
	protected Context context;
	
	public FileContainerImpl(Context context, String fname){
		this.context = context;
		this.fileName = fname;
		
		this.initFileData();
		this.init();
	}
	
	protected void init(){
		if(FileUtils.createEmptyFile(context, this.fileName))
			return ;
		
		List<String> datas = readDatas();
		if(datas==null)
			return ;
		
		int step = 0;
		String currentKey = null;
		for(String data : datas){
			Log.i(this.getClass().getName(), "data: " + data);
			if(data.startsWith("[") && data.endsWith("]")){
				int start = 1;
				int end = data.length()-1;
				if(data.equals(HEADER)){
					step = HEADER_INDEX;
				}
				else if(data.startsWith("[list=") && data.endsWith("]")){
					step = DATA_LIST_INDEX;
					start = 6;
				}
				else if(data.startsWith("[properties=") && data.endsWith("]")){
					step = PROPERTIES_INDEX;
					start = 12;
				}

				currentKey = data.substring(start, end);
				continue;
			}
			
			if(step==HEADER_INDEX){
				String[] p = data.split("=");
				getHeader().put(p[0].trim(), p[1].trim());
			}
			else if(step==DATA_LIST_INDEX){
				this.addData(currentKey, data);
			}
			else if(step==PROPERTIES_INDEX){
				String[] p = data.split("=");
				this.addProperty(currentKey, p[0].trim(), p[1].trim());
			}
		}
	}
	
	public List<String> readDatas(){
		return FileUtils.read(context, fileName);
	}
	
	protected void initFileData(){
		this.setHeader(new Header());
	}
	
	public void commit(){
		FileUtils.write(context, this.fileName, getFileData());
	}
	
	public void setHeader(Header value){
		getFileData().put(HEADER, value);
	}
	
	public Header getHeader(){
		return (Header) this.fileData.get(HEADER);
	}
	
	public void setData(String key, List<String> value){
		getFileData().put(getDataKey(key), value);
	}
	
	protected String getDataKey(String key){
		return DATA_LIST.replace("${name}", key);
	}
	
	protected String getPropertiesKey(String key){
		return PROPERTIES.replace("${name}", key);
	}
	
	public FileContainerImpl addData(String key, String value){
		getData(key).add(value);
		this.getHeader().setDataCountIncrease(key, 1);
		return this;
	}
	
	public boolean deleteData(String key, String value){
		List<String> list = getData(key);
		if(list.contains(value)){
			list.remove(value);
			this.getHeader().setDataCountIncrease(key, -1);
			return true;
		}else{
			return false;
		}
	}
	
	public List<String> getData(String key){
		String rKey = getDataKey(key);
		List<String> list = (List<String>) getFileData().get(rKey);
		if(list==null){
			list = new ArrayList<String>();
			getFileData().put(rKey, list);
		}
		return list;
	}
	
	public FileContainerImpl setProperty(String key, Properties value){
		getFileData().put(getPropertiesKey(key), value);
		return this;
	}
	
	public Properties getProperties(String key){
		String rKey = getPropertiesKey(key);
		Properties properties = (Properties) getFileData().get(rKey);
		if(properties==null){
			properties = new Properties();
			getFileData().put(rKey, properties);
		}
		return properties;
	}
	
	public void setProperties(String key, Properties properties){
		String rKey = getPropertiesKey(key);
		getFileData().put(rKey, properties);
	}
	
	public FileContainerImpl addProperty(String groupKey, String name, String value){
		getProperties(groupKey).setProperty(name, value);
		this.getHeader().setPropertiesCountIncrease(groupKey, 1);
		return this;
	}
	
	public FileContainerImpl deleteProperty(String groupKey, String name){
		Properties propertis = this.getProperties(groupKey);
		if(propertis.containsKey(name)){
			propertis.remove(name);
			this.getHeader().setPropertiesCountIncrease(groupKey, -1);
		}
		return this;
	}
	
	public FileContainerImpl clear(){
		this.getHeader().reset();
		this.getFileData().clear();
		this.commit();
		return this;
	}

	public Map<String, Object> getFileData() {
		return fileData;
	}

}
