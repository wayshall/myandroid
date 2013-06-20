package org.onetwo.android.app.callfirewall;

import org.onetwo.android.app.callfirewall.data.BlackPhone;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptType;
import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptWay;
import org.onetwo.android.utils.AndroidUtils;
import org.onetwo.android.view.BaseActivity;
import org.onetwo.common.utils.BothMap;
import org.onetwo.common.utils.StringUtils;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddPhoneActivity extends BaseActivity {
    /** Called when the activity is first created. */

	public static BothMap<Integer, Integer> blockTypeMap = new BothMap<Integer, Integer>();
	static {
		blockTypeMap.map(R.id.radgroup_bt_sms, InterceptType.SMS);
		blockTypeMap.map(R.id.radgroup_bt_phone, InterceptType.PHONE);
		blockTypeMap.map(R.id.radgroup_bt_all, InterceptType.ALL);
	}
	
	private TextView txtName = null;
	private TextView txtPhone = null;
	private RadioGroup radGroupBlockType = null;
	private BusinessFacade business = null;
	private Long id = null;
	private boolean create = true;
	
//	private int interceptWay = InterceptWay.BLACK_LIST;
	private TextView txtAddPhoneDesc;
	
	private Integer smsId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_phone);

        this.txtName = getComponent().findViewById(R.id.edt_name, EditText.class);
        this.txtPhone = getComponent().findViewById(R.id.edt_phone, EditText.class);
        this.radGroupBlockType = getComponent().findViewById(R.id.radgroup_block_type, RadioGroup.class);
        this.business = CallFirewallFactory.getBusinessFacade();

        this.txtAddPhoneDesc = this.getComponent().findViewById(R.id.txt_add_phone_desc, TextView.class);
        if(InterceptWay.isWhiteList(CallFirewallUtils.getInterceptWay(this)))
        	this.txtAddPhoneDesc.setText("添加白名单号码：");
		Bundle bdle = this.getIntent().getExtras();
        this.id = bdle!=null?bdle.getLong(Constant.BLACK_PHONE_ID_KEY):null;
		Log.i(tag, "edit-phone: " + id);
		this.populateFields(id, bdle);
    }
    
    protected void populateFields(Long id, Bundle bdle){
    	String name = "";
    	String number = "";
    	Integer interceptType = InterceptType.ALL;
    	if(id!=null && id>0){
    		BlackPhone c = this.business.findBlackPhone(id);
	    	/*this.startManagingCursor(c.getCursor());
	    	name = c.getString(T_BLACK_PHONES.NAME);
	    	number = c.getString(T_BLACK_PHONES.PHONE);
	    	blockType = c.getInt(T_BLACK_PHONES.BLOCK_TYPE);*/
    		name = c.getName();
    		number = c.getPhone();
    		interceptType = c.getInterceptType();
	    	this.create = false;
    	}else if(bdle!=null){
    		smsId = bdle.getInt(T_BLACK_PHONES.ID);
    		name = bdle.getString(T_BLACK_PHONES.NAME);
    		number = bdle.getString(T_BLACK_PHONES.PHONE);
    	}
    	int checkId = blockTypeMap.getKey(interceptType);
    	this.txtName.setText(name);
    	this.txtPhone.setText(number);
    	
    	if(blockTypeMap.containsKey(checkId))
    		this.radGroupBlockType.check(checkId);
    }
    
    public void addBlackPhone(View view){
    	String name = this.txtName.getText().toString();
    	String phone = this.txtPhone.getText().toString();
    	
    	if(!(ValidateUtils.NOT_NULL.validate(this, phone) && ValidateUtils.DIGIT.validate(this, phone)))
    		return ;
    	if(create && this.business.containBlackPhone(phone, null)){
    		Toast.makeText(this, R.string.validate_phone_exsit, Toast.LENGTH_SHORT).show();
    		return ;
    	}

    	if(StringUtils.isBlank(name))
    		name = phone;
    	int checkId = this.radGroupBlockType.getCheckedRadioButtonId();
    	Integer blockType = blockTypeMap.getValue(checkId);
    	
		ContentValues attrs = new ContentValues();
		attrs.put(T_BLACK_PHONES.ID, id);
		attrs.put(T_BLACK_PHONES.NAME, name);
		attrs.put(T_BLACK_PHONES.PHONE, phone);
		attrs.put(T_BLACK_PHONES.INTERCEPT_TYPE, blockType);
		attrs.put(T_BLACK_PHONES.INTERCEPT_WAY, CallFirewallUtils.getInterceptWay(this));
		
    	this.business.saveBlackPhone(attrs);
    	
    	Log.e(tag, "smsId; " + smsId);
    	if(smsId!=null){
    		this.business.deleteSms(smsId);
    	}
    	
    	Toast.makeText(this, R.string.saved_success, Toast.LENGTH_SHORT).show();
    	
    	/*Intent intent = new Intent(this, PhoneBlacklistActivity.class);
    	intent.putExtra(SettingData.INTERCEPT_WAY, this.interceptWay);*/
    	Intent intent = AndroidUtils.createFrom(this, PhoneBlacklistActivity.class);
    	getComponent().startActivity(intent);
    }
    
    public void cancel(View view){
    	getComponent().startActivity(PhoneBlacklistActivity.class);
    }
    
    public void onPause(){
    	this.txtPhone.setText("");
    	super.onPause();
    }
}