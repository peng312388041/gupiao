package com.gupiao.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	// ����Ϊ2007-03-01������2007-03-02
	public static String addDateOneDay(String dateString) throws ParseException {
		if (null == dateString) {
			return dateString;
		}
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateString);
		c.setTime(date); // ���õ�ǰ����
		c.add(Calendar.DATE, 1); // ���ڼ�1��
		date = c.getTime();
		String newDateString = sdf.format(date);
		return newDateString;
	}

	// �����ڶ���ת����yyyy-MM-dd�ַ���
	public static String parseDateTo10(Date date) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = sdf.format(date);
		return dateString;
	}

	// ���ݴ���Ĳ������꣬�»��գ�ȷ�������ĸ�ֵ
	public static int getYMDOfGupiaoDateString(String gupiaoString, YearMonthDay ymd) {
		int result = 0;
		boolean match = gupiaoString.matches("\\d{4}-\\d{2}-\\d{2}");
		// ���������yyyy-MM-dd��ʽ���ַ���
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
