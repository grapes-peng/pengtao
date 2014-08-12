package com.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * 将长整型类型的日期转换为可识字符串
	 * 
	 * @param l
	 */
	public static Date checkDate(long l) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(l);
		// c.add(Calendar.MINUTE, 5);
		// System.out.println(c.getTimeInMillis());
		System.out.println("Current Value :" + c.getTime());
		// System.out.println();
		return c.getTime();
	}
	
	
	public static void main(String[] args) {
		System.out.println(checkDate(1407283200000L));
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 30);
		System.out.println("Current Value :" + c.getTimeInMillis());
	}
}
