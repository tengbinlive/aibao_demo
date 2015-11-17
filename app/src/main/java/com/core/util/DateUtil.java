package com.core.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间工具类.
 * 
 * @author bin.teng
 */
public class DateUtil {

	/** 秒的毫秒值 1000 */
	public static final long m_second = 1000;

	/** 分钟的毫秒值 1000 * 60 */
	public static final long m_minute = m_second * 60;

	/** 小时的毫秒值 1000 * 60* 60 */
	public static final long m_hour = m_minute * 60;

	/** 每天的毫秒值 1000 * 60 * 60 * 24 */
	public static final long m_day = m_hour * 24;

	/** 时间格式 yyyy-MM-dd HH:mm:ss */
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/** 时间格式 MM-dd HH:mm */
	public static final String MM_DD_HH_MM = "MM-dd HH:mm";

	/** 时间格式 yyyy-MM-dd */
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	/** 时间格式 yyyyMMddHHmmss */
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	private DateUtil() {
	}

	/**
	 * 获得当前时间
	 * 
	 * @return 返回当前时间的long类型结果
	 */
	public static final long getTimeMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * 获得当前时间
	 * 
	 * @return 返回当前时间的Date类型结果
	 */
	public static final Date getDate() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 获得当前时间
	 * 
	 * @return 返回当前时间的Timestamp类型结果
	 */
	public static final Timestamp geTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

    /**
     * 获得当前时间, 并格式化为指定格式
     *
     * @param format 格式化模板
     * @return 返回格式化后的当前时间
     */
    public static final String getDateString(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(System.currentTimeMillis());
    }

	/**
	 * 获得当前时间, 并格式化为指定格式
	 * 
	 * @param timeMillis 时间的long值
	 * @param format 格式化模板
	 * @return 返回格式化后的当前时间
	 */
	public static final String getDateString(long timeMillis, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
		return formatter.format(timeMillis);
	}

	/***
	 * @param date
	 * @return 1,2,3,4,5,6,7
	 */
	private static int[] chweek = new int[] { 0, 7, 1, 2, 3, 4, 5, 6 };

	/**
	 * @param date
	 * @return 1,2,3,4,5,6,7
	 */
	public static Integer getWeek(Date date) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.setTime(date);
		return chweek[cal.get(Calendar.DAY_OF_WEEK)];
	}

	private static String[] cnweek = new String[] { "", "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
	private static String[] cnSimpleweek = new String[] { "", "日", "一", "二", "三", "四", "五", "六" };

	/**
	 * @param date
	 * @return "周日", "周一", "周二", "周三", "周四", "周五", "周六"
	 */
	public static String getCnWeek(Date date) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.setTime(date);
		return cnweek[cal.get(Calendar.DAY_OF_WEEK)];
	}

	public static String getCnSimpleWeek(Date date) {
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.setTime(date);
		return cnSimpleweek[cal.get(Calendar.DAY_OF_WEEK)];
	}

	// 把日期转为字符串
	public static String ConverToString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
		return df.format(date);
	}

	// 把日期转为字符串
	public static String ConverToString(Long date,String type) {
		SimpleDateFormat df = new SimpleDateFormat(type);
		return df.format(date);
	}

	// 把字符串转为日期
	public static Date ConverToDate(String strDate,String type) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat(type);
		return df.parse(strDate);
	}

	// 将日期格式的字符串以指定格式输出
	public static String formatToString(String date, String format) {
		try {
			Date dt = parseStringToDate(date);
			java.text.SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date parseStringToDate(String date) throws ParseException{
		Date result=null;
		String parse=date;
		parse=parse.replaceFirst("^[0-9]{4}([^0-9]?)", "yyyy$1");
		parse=parse.replaceFirst("^[0-9]{2}([^0-9]?)", "yy$1");
		parse=parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1MM$2");
		parse=parse.replaceFirst("([^0-9]?)[0-9]{1,2}( ?)", "$1dd$2");
		parse=parse.replaceFirst("( )[0-9]{1,2}([^0-9]?)", "$1HH$2");
		parse=parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1mm$2");
		parse=parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1ss$2");

		SimpleDateFormat format= new SimpleDateFormat(parse);

		result=format.parse(date);

		return result;
	}

}
