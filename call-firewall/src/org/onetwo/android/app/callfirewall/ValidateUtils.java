package org.onetwo.android.app.callfirewall;

import java.util.regex.Pattern;

import org.onetwo.android.app.callfirewall.R;
import org.onetwo.common.utils.StringUtils;

import android.content.Context;
import android.widget.Toast;

public abstract class ValidateUtils {

	public static final String STR_DIGIT = "\\d{#min, #max}";
	public static final Pattern PATTERN_DIGIT = Pattern.compile("\\d+");
	
	public static interface Validator {
		boolean validate(Context context, String value, Object...objects);
	}
	
	public static Validator NOT_NULL = new Validator() {
		@Override
		public boolean validate(Context context, String value, Object...objects) {
	    	if(ValidateUtils.hasText(value))
		    	return true;
    		Toast.makeText(context, R.string.validate_input_empty, Toast.LENGTH_SHORT).show();
    		return false;
		}
	};
	
	public static Validator DIGIT = new Validator() {
		@Override
		public boolean validate(Context context, String value, Object...objects) {
	    	if(objects==null || objects.length==0){
	    		if(ValidateUtils.isDigit(value))
	    			return true;
	    		Toast.makeText(context, R.string.validate_only_numberic, Toast.LENGTH_SHORT).show();
	    		return false;
	    	}
	    	int min = (Integer) objects[0];
	    	int max = (Integer) objects[1];
	    	if(ValidateUtils.isDigit(value, min, max))
		    	return true;
    		Toast.makeText(context, R.string.validate_only_numberic, Toast.LENGTH_SHORT).show();
    		return false;
		}
	};

	public static boolean hasText(String text){
		return StringUtils.hasText(text);
	}
	
	public static boolean isDigit(String str){
		return PATTERN_DIGIT.matcher(str).matches();
	}
	
	public static boolean isDigit(String str, Integer minLength, Integer maxLength){
		String regex = STR_DIGIT.replace("#min", minLength.toString()).replace("#max", maxLength.toString());
		return Pattern.matches(regex, str);
	}

}
