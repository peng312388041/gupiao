package com.gupiao.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.gupiao.model.Share;
import com.gupiao.util.DateUtils;
import com.gupiao.util.YearMonthDay;

public class ShareService {
	//��ȡ��day��֮�ڣ��ۼ��Ƿ��ﵽrate�Ĺ�Ʊ
	public List<Share> getRecentlyUpShares(int days,float rate)
	{
		List<Share> shares=null;
		String todayString=DateUtils.parseDateTo10(new Date());
		int year=DateUtils.getYMDOfGupiaoDateString(todayString, YearMonthDay.YEAR);
		int month=DateUtils.getYMDOfGupiaoDateString(todayString, YearMonthDay.MONTH);
		int day=DateUtils.getYMDOfGupiaoDateString(todayString, YearMonthDay.DAY);
		return shares;
	}
}
