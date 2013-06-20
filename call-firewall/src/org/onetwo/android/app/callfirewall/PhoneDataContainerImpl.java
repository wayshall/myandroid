package org.onetwo.android.app.callfirewall;

import java.util.List;

import org.onetwo.android.utils.FileContainerImpl;
import org.onetwo.android.utils.FileUtils;
import org.onetwo.android.view.OneTwoContext;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.ObjectUtils;

public class PhoneDataContainerImpl{
	
	protected FileContainerImpl container ;
	
	public PhoneDataContainerImpl(){
		onInitialize();
	}
	
	public void onInitialize() {
        FileContainerImpl container = FileUtils.getFileContainer(Constant.FILE_NAME, OneTwoContext.get().context(true));
		if(container==null)
			throw new BaseException("init error: container can not be nul");
		this.container = container;
		
	}
	
	public List<String> getData(){
		List<String> datas = this.container.getData(Constant.BLACK_PHONES);
		return datas;
	}
	
	public boolean contains(String phone){
		return getData().contains(phone);
	}
	
	public PhoneDataContainerImpl addData(String... phones){
		if(ObjectUtils.isEmpty(phones))
			throw new BaseException("phone can not be empty!");
		for(String phone : phones)
			this.container.addData(Constant.BLACK_PHONES, phone);
		this.container.commit();
		return this;
	}
	
	public boolean deleteData(String phone){
		boolean rs = this.container.deleteData(Constant.BLACK_PHONES, phone);
		if(rs==true)
			this.container.commit();
		return rs;
	}

}
