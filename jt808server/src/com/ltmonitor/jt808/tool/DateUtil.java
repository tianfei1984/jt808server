/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ltmonitor.jt808.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * ���ڹ�����
 * 
 * @author zhengxin created 2005-4-27 9:45:44 version 1.0
 */
public class DateUtil {

	public static final int SECOND = 1000;

	public static final int MINUTE = SECOND * 60;

	public static final int HOUR = MINUTE * 60;

	public static final int DAY = HOUR * 24;

	public static final int WEEK = DAY * 7;

	public static final int YEAR = DAY * 365; // or 366 ???

	/**
	 * һ���е�����
	 */
	public static long millionSecondsOfDay = 86400000;

	/**
	 * Creates a Date, at 00:00:00 on the given day.
	 * 
	 * @param month
	 *            1-12 (0 = January)
	 * @param date
	 * @param year
	 */
	public static Date newDate(int month, int date, int year) {
		Calendar inst = Calendar.getInstance();
		inst.clear();
		inst.set(year, month - 1, date);
		return inst.getTime();
	}
	
	/**
	 * 测试日期是否在某�?��日期之间
	 * @param date
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean between(Date date, Date start, Date end){
		return getDay(start, date) >= 0 && getDay(date, end) <= 0;
	}

	/**
	 * �õ����ڵ�ǰ����
	 * 
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date getDate(Date date, int i) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, i);

		return calendar.getTime();

	}
	
	public static Date getDate(Date date, int field, int i) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, i);

		return calendar.getTime();

	}
	

	public static int compare(Date date1, Date date2) {
		return getDay(date1, date2);
	}

	/**
	 * �õ����ڵ�ǰ����
	 * 
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date getDateByHour(Date date, int hour) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hour);

		return calendar.getTime();

	}

	/**
	 * �õ�}������֮�������?}ͷ����,ȡ����ݺ󣬿��Ը����Ҫ�ټ�
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDay(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;

		date1 = getDate(date1);
		date2 = getDate(date2);

		return (int) ((date2.getTime() - date1.getTime()) / millionSecondsOfDay);
	}

	/**
	 * �������ȡ�������ڼ�?
	 * 
	 * @param date
	 *            Date
	 * @return int ����1-7
	 */
	public static int getWeekOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return (calendar.get(Calendar.DAY_OF_WEEK) - 1) == 0 ? 7 : calendar
				.get(Calendar.DAY_OF_WEEK) - 1;
	}
	
	/**
	 * 日期是否符合某一星期
	 * @param date
	 * @param week
	 * @return
	 */
	public static boolean isMatchWeek(Date date, int week){
		return getWeekOfDate(date) == week;
	}
	
	
	public static boolean isMatchWeek(Date date, Integer[] weeks){
		
		
		int len = weeks.length;
		
		for(int m = 0; m < len; m++){
			
			int week = weeks[m];
			
			if (isMatchWeek(date, week))
				return true;
		}
		
		
		return false;
	}

	public static java.sql.Date getSqlDate(Date date) {
		if (date == null)
			return null;
		return new java.sql.Date(date.getTime());
	}

	public static Date getDate(Date date) {
		if (date == null)
			return null;
		return getDate(DateUtil.dateToString(date));

	}
	
	public static Date now()
	{
		return new Date();
	}

	public static Date getDefaultDateTime(Date date) {
		if (date == null)
			return null;
		String str = dateToString(date) + " 12:00";

		return stringToDatetime(str, "yyyy-MM-dd HH:mm");
	}

	public static List getDates(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return new ArrayList();

		int day = getDay(date1, date2);

		List dates = new ArrayList();

		for (int i = 0; i <= day; i++) {
			Date date = getDate(date1, i);
			dates.add(date);
		}
		return dates;
	}

	public static String dateToString(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String datetimeToString(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String toStringByFormat(Date date, String format) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}
		return "";
	}

	public static Date toDateByFormat(String str, String format) {
		if (str == null || str.equals(""))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date stringToDate(String str) {
		if (str == null || str.equals(""))
			return null;
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date stringToDateTime(String str) {
		if (str == null || str.equals(""))
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date stringToDatetime(String str, String format) {
		if (str == null || str.equals(""))
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date getDate(String str) {
		if (str == null || str.equals("") || str.length() < 8)
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getTimeString(int duration) {
		int hours = duration / DateUtil.HOUR;
		int remain = duration - (hours * DateUtil.HOUR);
		int minutes = remain / DateUtil.MINUTE;
		StringBuffer time = new StringBuffer(64);
		if (hours != 0) {
			if (hours == 1) {
				time.append("1 hour and ");
			} else {
				time.append(hours).append(" hours and ");
			}
		}
		if (minutes == 1) {
			time.append("1 minute");
		} else {
			// what if minutes == 0 ???
			time.append(minutes).append(" minute(s)");
		}
		return time.toString();
	}

	/**
	 * �õ�ICS�涨�����ڸ�ʽ�� 20050826 ת���� 26AUG05�ĸ�ʽ
	 * 
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	// public static String getICSDate(String str) throws ParseException {
	// if (str == null || str.equals(""))
	// return null;
	//
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	//
	// Date date = sdf.parse(str);
	//
	// return com.sune365.air.bookingEngine.util.DateUtil.dateToString(date,
	// "ddMMMyy");
	// }
	/**
	 * ȡϵͳʱ�����ݣ���2005��Ϊ����2005��2006��Ϊ����2006
	 * 
	 * @return int
	 */
	public static int getYearOfSysTime() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * ȡϵͳʱ����·ݣ���һ���?...ʮ����Ϊ12 ��calendar.get(Calendar.MONTH)Ϊ0-11����+1
	 * 
	 * @return int
	 */
	public static int getMonthOfSysTime() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * ������
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("2007-06-09".compareTo(formatDateToSQLString(getSystemDate())));		
	}
    
    public static Date getSystemDate(){
    	return new Date();
    }	
    
    public static String formatDateToSQLString(Date srcDate){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	return sdf.format(srcDate);
    }
    
    public static String formatTimeToString(Date srcDate){
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    	return sdf.format(srcDate);
    }
    
    public static String weeksToString(String[] week){
    	if (week==null){
    		return "";
    	}
    	StringBuffer sb= new StringBuffer();
    	String result="";
    	for (int i=0; i<week.length;i++){
    		if (i==week.length-1){
    			sb.append(week[i]);
    		}else{
    		  sb.append(week[i]).append(",");
    		}
    		result = sb.toString();
    	}
    	return result;
    }
}
