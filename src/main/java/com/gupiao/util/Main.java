package com.gupiao.util;

import java.io.IOException;
import java.util.List;

import com.gupiao.dao.ShareDao;
import com.gupiao.model.Share;

public class Main {
	public static void main(String[] args) throws IOException {
		//����ȡ��Ʊÿ����Ϣ
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
		System.out.println("���н���");
		System.out.println(msecond+"����");
		System.out.println(totalsecond+"��");
		System.out.println(minute+"��"+second+"��");
		
		
		// TODO ִ�� init ʱ���жϱ��Ƿ�Ϊ�գ������Ϊ�գ��򲻽���Init ������
		//����ȡ��Ʊ
//		List<Share> shares = DataManager.getShareFromInternet();
//		for (Share share : shares) {
//			System.out.println(share);
//		}
//		
//		new ShareDao().save(shares);
	}
}
