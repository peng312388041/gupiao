package com.gupiao.util;

import java.io.IOException;
import java.util.List;

import com.gupiao.dao.ShareDao;
import com.gupiao.model.Share;

public class Main {
	public static void main(String[] args) throws IOException {
		//测试取股票每日信息
//		List<DayLine> gupiaos = DataManager.getDayLineFromInternet("sh600113", "2016-05-02", "2016-11-03");
//		for (DayLine dayLine : gupiaos) {
//			System.out.println(dayLine);
//		}
//		
//		new DayLineDao().save(gupiaos);
		
		
		long begin=System.currentTimeMillis();
		DataManager.initDayLine();
		long end=System.currentTimeMillis();
		long msecond=end-begin;
		long totalsecond=msecond/1000;
		long minute=totalsecond/60;
		long second=totalsecond-minute*60;
		System.out.println("运行结束");
		System.out.println(msecond+"毫秒");
		System.out.println(totalsecond+"秒");
		System.out.println(minute+"分"+second+"秒");
		
		
		// TODO 执行 init 时先判断表是否为空，如果不为空，则不进行Init 操作。
		//测试取股票
//		List<Share> shares = DataManager.getShareFromInternet();
//		for (Share share : shares) {
//			System.out.println(share);
//		}
//		
//		new ShareDao().save(shares);
	}
}
