package org.onetwo.android.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.StringUtils;

import android.content.Context;
import android.util.Log;

@SuppressWarnings("unchecked")
abstract public class FileUtils {
	
	final static String TAG = FileUtils.class.getName();
	final static String SEPARATER_NEWLINE = "\n";
	final static String EQUALS_KEY = "=";

	private static Map<String, FileContainerImpl> DATAS = new HashMap<String, FileContainerImpl>();

	public static interface FileInputSource {
		FileInputStream getFileInput(String name);
	}

	public static interface FileOutputSource {
		FileOutputStream getFileOutput(String name);
	}
	
	
	public static FileContainerImpl getFileContainer(String name, Context context){
		FileContainerImpl container = DATAS.get(name);
		if(container==null){
			container = new FileContainerImpl(context, name);
			DATAS.put(name, container);
		}
		return container;
	}
	
	public static FileContainerImpl getFileContainer(String name){
		return DATAS.get(name);
	}
	
	public static FileContainerImpl clearFileContainer(String name){
		FileContainerImpl fc = DATAS.remove(name);
		return fc;
	}
	
	public static void clearFileContainer(){
		DATAS.clear();
	}
	
	public static List<String> read(Context context, String name){
		return read(context, name, null);
	}
	
	public static List<String> read(Context context, String name, FileInputSource source){
		BufferedReader br = null;
		List<String> datas = new ArrayList<String>();
		try {
			FileInputStream fin = null;
			if(source!=null)
				fin = source.getFileInput(name);
			else
				fin = context.openFileInput(name);
			
			if(fin==null)
				Log.e(TAG, "no file found!");
			
			br = new BufferedReader(new InputStreamReader(fin));
			String line = null;
			while((line = br.readLine())!=null){
				if(StringUtils.hasText(line))
					datas.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		} finally{
			close(br);
		}
		return datas;
	}
	
	@SuppressWarnings("serial")
	public static void append(Context context, String name, final String data){
		write(context, name, new ArrayList<String>(){
			{
			add(data);
			}
		}, SEPARATER_NEWLINE, Context.MODE_APPEND);
	}
	
	public static void write(Context context, String name, Object datas){
		write(context, name, datas, SEPARATER_NEWLINE);
	}
	
	public static void write(Context context, String name, Object datas, String separater){
		write(context, name, datas, separater, Context.MODE_PRIVATE);
	}
	
	public static void write(final Context context, String name, Object datas, String separater, final int mode){
		write(name, datas, separater, new FileOutputSource() {
			
			@Override
			public FileOutputStream getFileOutput(String name) {
				try {
					return context.openFileOutput(name, mode);
				} catch (FileNotFoundException e) {
					throw new BaseException(e);
				}
			}
		});
	}
	
	public static void write(String name, Object datas, String separater, FileOutputSource source){
		BufferedWriter bw = null;
		try {
			FileOutputStream fout = null;
			fout = source.getFileOutput(name);
			if(fout==null)
				Log.e(TAG, "no file found!");
			if(separater==null)
				separater = "";
			bw = new BufferedWriter(new OutputStreamWriter(fout));
			if(datas==null)
				bw.write("");
			write(bw, datas, separater);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} finally{
			close(bw);
		}
	}
	

	public static void write(BufferedWriter bw, Object datas, String separater) throws IOException{
		if(datas instanceof List){
			for(String line : (List<String>)datas){
				if(StringUtils.hasText(line))
					bw.append(line).append(separater);
			}
		}
		else if(datas instanceof Map){
			for(Map.Entry<String, Object> entry : ((Map<String, Object>) datas).entrySet()){
				String key = entry.getKey();
				Object value = entry.getValue();
				if(!StringUtils.hasText(key))
					continue;
				if(key.startsWith("[") && key.endsWith("]")){
					bw.append(key).append(separater);
					write(bw, value, separater);
					bw.append(separater);
				}else	
					bw.append(key).append(EQUALS_KEY).append(value.toString()).append(separater);
			}
		}else{
			if(datas!=null)
				bw.append(datas.toString()).append(separater);
		}
	}
	
	
	public static void delete(Context context, String name, String data){
		List<String> datas = FileUtils.read(context, name);
		if(datas==null || datas.isEmpty())
			return ;
		for(String d : datas){
			if(d!=null && d.equals(data)){
				datas.remove(d);
				break;
			}
		}
		write(context, name, datas);
	}
	
	public static boolean checkExist(Context context, String name){
		String[] flist = context.fileList();
		if(flist==null)
			return false;
		for(String f : flist){
			if(f.equals(name))
				return true;
		}
		return false;
	}
	
	/***
	 * 返回false表示文件已存在
	 * 返回true表示文件不存在，创建了新的空白文件
	 * @param context
	 * @param name
	 * @return
	 */
	public static boolean createEmptyFile(Context context, String name){
		if(checkExist(context, name))
			return false;
		FileOutputStream fout = null;
		try {
			fout = context.openFileOutput(name, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} finally{
			close(fout);
		}
		return true;
	}
	
	public static void close(Closeable closeable){
		if(closeable!=null){
			try {
				closeable.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
