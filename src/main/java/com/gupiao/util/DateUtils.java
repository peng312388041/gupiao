package com.gupiao.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	// 参数为2007-03-01，返回2007-03-02
	public static String addDateOneDay(String dateString) throws ParseException {
		if (null == dateString) {
			return dateString;
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateString);
		c.setTime(date); // 设置当前日期
		c.add(Calendar.DATE, 1); // 日期加1天
		date = c.getTime();
		String newDateString = sdf.format(date);
		return newDateString;
	}

	// 把日期对象转换成yyyy-MM-dd字符串
	public static String parseDateTo10(Date date) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = sdf.format(date);
		return dateString;
	}

	// 根据传入的参数是年，月还日，确定返回哪个值
	public static int getYMDOfGupiaoDateString(String gupiaoString, YearMonthDay ymd) {
		int result = 0;
		boolean match = gupiaoString.matches("\\d{4}-\\d{2}-\\d{2}");
		// 如果传入是yyyy-MM-dd形式的字符串
		if (match) {
			String[] datas = gupiaoString.split("-");
			if (YearMonthDay.YEAR.equals(ymd)) {
				result = Integer.parseInt(datas[0]);
			} else if (YearMonthDay.MONTH.equals(ymd)) {
				result = Integer.parseInt(datas[1]);
			} else if (YearMonthDay.DAY.equals(ymd)) {
				result = Integer.parseInt(datas[2]);
			}
		}
		return result;
	}
}
