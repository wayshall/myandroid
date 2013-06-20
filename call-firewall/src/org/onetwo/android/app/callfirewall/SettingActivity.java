package org.onetwo.android.app.callfirewall;

import org.onetwo.android.app.callfirewall.service.BlackPhoneService.T_BLACK_PHONES.InterceptWay;
import org.onetwo.android.view.BaseActivity;
import org.onetwo.common.utils.BothMap;

import android.os.Bundle;
import android.widget.RadioGroup;

public class SettingActivity extends BaseActivity {

	public static BothMap<Integer, Integer> INTERCEPT_WAY = new BothMap<Integer, Integer>();
	static {
		INTERCEPT_WAY.map(R.id.radbtn_intercept_way_blacklist, InterceptWay.BLACK_LIST);
		INTERCEPT_WAY.map(R.id.radbtn_intercept_way_whitelist, InterceptWay.WHITE_LIST);
	}
	
	private RadioGroup interceptWayGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setting);
		
		getMenuCompt().setMenuGroup(MenuGroupBuilder.MENU_BLACK_PHONES.subGroup(MenuGroupBuilder.M_ORDER_BACK));
		
		this.interceptWayGroup = getComponent().findViewById(R.id.radgroup_intercept_way, RadioGroup.class);
		int interceptWay = CallFirewallFactory.getSettingData().getInterceptWay();
		int wayId = INTERCEPT_WAY.getKey(interceptWay);
		if(INTERCEPT_WAY.containsKey(wayId))
			this.interceptWayGroup.check(wayId);
	}
	
	public void finish(){
		int checkedId = this.interceptWayGroup.getCheckedRadioButtonId();
		int way = INTERCEPT_WAY.getValue(checkedId);
		CallFirewallFactory.getSettingData().setInterceptWay(way);
		super.finish();
	}
	
	/*public void setBlackWay(View view){
		CallFirewallFactory.getSettingData().setInterceptWay(InterceptWay.BLACK_LIST);
	}
	
	public void setWhiteWay(View view){
		CallFirewallFactory.getSettingData().setInterceptWay(InterceptWay.WHITE_LIST);
	}*/
	
    public void onDestroy(){
    	super.onDestroy();
    }
}
