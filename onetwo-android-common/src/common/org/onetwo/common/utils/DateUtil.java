package org.onetwo.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 时间通用方法
 * @author cg
 *
 */
@SuppressWarnings({"serial","deprecation"})
abstract public class DateUtil {
	public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
	public static final String YYYYMMDD = "yyyy-MM-dd";
	public static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS= new SimpleDateFormat(YYYYMMDDHHMMSS);
	public static final SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat(YYYYMMDDHHMM);
	public static final SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat(YYYYMMDD);
	
	private static final Map<String, SimpleDateFormat> cache = new HashMap<String, SimpleDateFormat>(){
		{
			put(YYYYMMDDHHMMSS, YYYY_MM_DD_HH_MM_SS);
			put(YYYYMMDDHHMM, YYYY_MM_DD_HH_MM);
			put(YYYYMMDD, YYYY_MM_DD);
		}
	};
	
	/**
	 * 获取跟date间隔days天的时间
	 * @param date 时间
	 * @param days 间隔天数
	 */
	public static Date getDiffDay(Date date,long days){
		long datetime=date.getTime();
		long difftime=days*24*60*60*1000;
		
		return new Date(datetime+difftime);
	}
	
	public static String formatDateByPattern(Date date, String p){
		SimpleDateFormat sdf = new SimpleDateFormat(p);
		sdf.setTimeZone(TimeZone.getDefault());
		String rs = null;
		try{
			rs = sdf.format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return rs;
	}
	
	public static String formatDateTime(Date date){
		return formatDateByPattern(date, YYYYMMDDHHMMSS);
	}
	
	public static String format(String pattern, Date date){
		SimpleDateFormat sdf = getDateFormat(pattern);
		return format(sdf, date);
	}
	
	public static String format(SimpleDateFormat format, Date date){
		return format.format(date);
	}
	
	public static Date parse(SimpleDateFormat format, String dateStr){
		Date date = null;
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
		}
		return date;
	}
	
	public static Date parse(String dateStr, SimpleDateFormat...dateFormats){
		if(dateFormats==null || dateFormats.length==0)
			return parse(YYYY_MM_DD_HH_MM_SS, dateStr);
		Date date = null;
		for(SimpleDateFormat sdf : dateFormats){
			date = parse(sdf, dateStr);
			if(date!=null)
				return date;
		}
		return date;
	}
	
	public static Date parse(String dateStr, String...patterns){
		if(patterns==null || patterns.length==0)
			return parse(YYYY_MM_DD_HH_MM_SS, dateStr);
		Date date = null;
		for(String p : patterns){
			SimpleDateFormat sdf = getDateFormat(p);
			date = parse(sdf, dateStr);
			if(date!=null)
				return date;
		}
		return date;
	}
	
	public static Date parse(String dateStr){
		return parse(dateStr, YYYY_MM_DD_HH_MM_SS, YYYY_MM_DD_HH_MM, YYYY_MM_DD);
	}
	
	public static SimpleDateFormat getDateFormat(String p){
		if(StringUtils.isBlank(p))
			p = YYYYMMDD;
		SimpleDateFormat sdf = cache.get(p);
		if(sdf==null){
			sdf = new SimpleDateFormat(p);
			cache.put(p, sdf);
		}
		return sdf;
	}
	
    public static Date addMinutes(Date date, int amount) {
        return add(date, Calendar.MINUTE, amount);
    }
	
    public static Date addSeconds(Date date, int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }
	
	public static Date setDateStart(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return setDateStart(calendar);
	}
	
	public static Date setDateStart(Calendar calendar){
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date setDateEnd(Calendar calendar){
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	public static Date addDay(Calendar calendar, int numb){
		calendar.add(Calendar.DAY_OF_MONTH, numb);
		return calendar.getTime();
	}
	
	public static Date addDay(Date date, int numb){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, numb);
		return calendar.getTime();
	}
	
	public static void main(String[] args){
		Date date = null;
		date = parse("2010-10-10", "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd");
		System.out.println(date.toLocaleString());
		date = parse("2010-12-10", "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd");
		System.out.println(date.toLocaleString());
	}
}
