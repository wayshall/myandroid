package org.onetwo.android.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.android.data.UserData;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

public class AndroidUtilsTest {
	
	private UserData user;
	private List<String> propNames;
	
	@Before
	public void setup(){
		user = new UserData();
		user.setName("testName");
		user.setAge(25);
		user.setBirthDay(new Date());
		user.setFloatValue(1.1f);
		propNames = ReflectUtils.desribPropertiesName(UserData.class);
	}
	
	/*@Test
	public void testToContentValue(){
		ContentValues values = AndroidUtils.toContentValues(user);
		Assert.assertEquals(propNames.size(), values.size());
		Assert.assertEquals(values.getAsString("name"), user.getName());
		Assert.assertEquals(values.getAsInteger("age"), (Integer)user.getAge());
		Assert.assertEquals(values.getAsFloat("floatValue"), (Float)user.getFloatValue());
	}*/
	
	@Test
	public void testToMap(){
		Map values = AndroidUtils.toMap(user, true);
		System.out.println("map: " + values);
	}
	
//	@Test
	public void testName(){
		String str = StringUtils.convert2UnderLineName(true, "name", "_");
		Assert.assertEquals("_name", str);
		str = StringUtils.convert2UnderLineName(true, "userName", "_");
		Assert.assertEquals("_user_name", str);
		str = StringUtils.convert2UnderLineName(true, "myUserName", "_");
		Assert.assertEquals("_my_user_name", str);
	}
	
//	@Test
	public void testByField(){
		Object val = ReflectUtils.getFieldValueByGetter(user, "name", true);
		System.out.println("val: " + val);
		ReflectUtils.setFieldValueBySetter(user, "name", "asdfsdf", true);
		System.out.println("val2: " + user.getName());
	}

}
