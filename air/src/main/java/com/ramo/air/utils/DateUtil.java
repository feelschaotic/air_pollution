package com.ramo.air.utils;

import android.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转换工具类
 * 把时间转为几天前 几分钟前
 * @author ramo
 *
 */
public class DateUtil {
	private final static long minute = 60 * 1000;// 1分钟 
	private final static long hour = 60 * minute;// 1小时 
	private final static long day = 24 * hour;// 1天 
	private final static long month = 31 * day;// 月 
	private final static long year = 12 * month;// 年 
	/** 
	* 返回文字描述的日期 
	* 
	* @param date 
	* @return 
	*/
	public static String getTimeFormatText(Date date) { 
	    if (date == null) { 
	      return null; 
	    } 
	    long diff = new Date().getTime() - date.getTime(); 
	    long r = 0; 
	    if (diff > year) { 
	      r = (diff / year); 
	      return r + "年前"; 
	    } 
	    if (diff > month) { 
	      r = (diff / month); 
	      return r + "个月前"; 
	    } 
	    if (diff > day) { 
	      r = (diff / day); 
	      return r + "天前"; 
	    } 
	    if (diff > hour) { 
	      r = (diff / hour); 
	      return r + "个小时前"; 
	    } 
	    if (diff > minute) { 
	      r = (diff / minute); 
	      return r + "分钟前"; 
	    } 
	    return "刚刚"; 
	}
	/**
	* 字符串转换成日期
	* @param str
	* @return date
	*/
	public static Date StrToDate(String str,String model) {
	  
	   SimpleDateFormat format = new SimpleDateFormat(model);
	   Date date = null;
	   try {
	    date = format.parse(str);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   } catch (java.text.ParseException e) {
		e.printStackTrace();
	}
	   return date;
	}
}
