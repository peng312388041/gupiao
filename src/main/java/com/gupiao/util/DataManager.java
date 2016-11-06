package com.gupiao.util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gupiao.dao.DayLineDao;
import com.gupiao.dao.ShareDao;
import com.gupiao.model.DayLine;
import com.gupiao.model.Share;

public class DataManager {
	public static DayLineDao daylineDao = new DayLineDao();
	public static ShareDao shareDao = new ShareDao();
	public static final int TRY_COUNT = 3;
	private static Logger logger = Logger.getLogger(DataManager.class);

	public static List<DayLine> getDayLineFromInternet(Share share, String code, String beginDate, String endDate)
			throws IOException {
		List<DayLine> dayLines = new ArrayList<DayLine>();
		String url = "http://app.finance.china.com.cn/stock/quote/history.php?code=" + code + "&begin_day=" + beginDate
				+ "&end_day=" + endDate;
		System.out.println(url);
		// ����ȡ���ݵĴ���
		int count = 3;
		Document document = null;
		for (int i = 0; i < TRY_COUNT; i++) {
			try {
				document = Jsoup.connect(url).timeout(60 * 1000).get();
				break;
			} catch (SocketTimeoutException e) {
				count--;
				if (count > 0) {
					logger.debug("ȡ����ʧ�ܣ���ʼ��" + (4 - count) + "�γ���");
					document = Jsoup.connect(url).timeout(60 * 1000).get();
				}
			}
		}
		// ���ȡ��������
		if (document != null) {
			Elements listDiv = document.getElementsByAttributeValue("class", "right_table");
			if (listDiv.size() > 0) {
				Element element = listDiv.get(0);
				Elements elements = element.select("tr");
				for (Element e : elements) {
					if (e.equals(elements.get(0))) {
						continue;
					}
					try {
						DayLine gupiao = new DayLine();
						// ����
						gupiao.setDate((e.child(0).text()));
						// ���̼�
						gupiao.setSpj(Double.parseDouble(e.child(1).text()));
						// �ǵ���
						gupiao.setZde(Double.parseDouble(e.child(2).text()));
						// �ǵ���
						gupiao.setZdf(Double.parseDouble(e.child(3).text().split("%")[0]));
						// ���̼�
						gupiao.setKpj(Double.parseDouble(e.child(4).text()));
						// ��߼�
						gupiao.setMaxPrice(Double.parseDouble(e.child(5).text()));
						// ��ͼ�
						gupiao.setMinPrice(Double.parseDouble(e.child(6).text()));
						// �ɽ���
						gupiao.setCjl(Long.parseLong(e.child(7).text().split("��")[0]));
						// �ɽ���
						gupiao.setCje(Long.parseLong(e.child(8).text().split("��")[0]));
						// ���ù�Ʊ����
						gupiao.setCode(share.getCode());
						// ���ù�Ʊ����
						gupiao.setName(share.getName());
						dayLines.add(gupiao);
					} catch (Exception e2) {
						logger.debug("����������ʱ�쳣");
						logger.debug("url:" + url);
						logger.debug("��Ʊ��Ϣ:" + e.text());
						logger.debug(e2.getStackTrace());
					}
				}
			} else {
				logger.info("�˹�Ʊ��ʱ�޷���ȡ����Ϣ:" + code);
			}
		}
		return dayLines;
	}

	public static List<Share> getShareFromInternet() throws IOException {
		// public static void GetShareFromInternet() throws IOException {
		List<Share> shares = new ArrayList<Share>();
		String url = "http://quote.eastmoney.com/stocklist.html";
		Document document = Jsoup.connect(url).timeout(60 * 1000).get();
		Elements elements = document.select("ul");
		for (Element e : elements) {
			// ��Ʊ��Ŀһ�����100
			if (e.children().size() > 100) {
				// e��ÿ��������һ����Ʊli
				for (Element element : e.children()) {
					Element li = element.getElementsByAttributeValue("target", "_blank").get(0);
					System.out.println(li.text());
					String shareMessage = li.text();
					String shareName = shareMessage.split("\\(")[0];
					String shareCode = shareMessage.split("\\(")[1].split("\\)")[0];
					Share share = new Share();
					//
					share.setCode(shareCode);
					share.setName(shareName);
					// 0��ʾ��Ʊ��Ϣ��δ����ʼ��������δ���뵽dayline����
					share.setInited(0);
					share.setLastupdate("0000-00-00");
					shares.add(share);
				}
			}
		}
		return shares;
	}

	// ��ʼ����Ʊÿ�յ���ʷ��Ϣ
	public static void initDayLine() throws IOException {
		if (!daylineDao.isEmpty()) {
			logger.debug("dayline��Ϊ�գ����ܽ��г�ʼ������");
			return;
		}
		List<Share> shares = shareDao.getAllShare();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);// ��ȡ���
		for (Share share : shares) {
			String shareCode = share.getCode();
			if (shareCode.startsWith("0") || shareCode.startsWith("3") || shareCode.startsWith("6")) {
				String code = getTypeOfShare(shareCode) + shareCode;
				String beginDate = year + "-" + 1 + "-" + 31;
				String endDate = year + "-" + 12 + "-" + 31;
				List<DayLine> dayLines = getDayLineFromInternet(share, code, beginDate, endDate);
				// �����ȡ����ǰ��Ʊ����
				if (dayLines.size() > 0) {
					daylineDao.save(dayLines);
					share.setInited(1);
					shareDao.update(share);
				}
			}
		}
	}

	// ��ʼ����Ʊ��Ϣ
	public static void initShare() throws IOException {
		if (!shareDao.isEmpty()) {
			logger.debug("share��Ϊ�գ����ܽ��г�ʼ������");
			return;
		}
		List<Share> shares = getShareFromInternet();
		shareDao.save(shares);
	}

	public static void updateShare() throws IOException {
		List<Share> shares = getShareFromInternet();
		for (Share share : shares) {
			Share s = shareDao.get(share.getCode());
			if (s == null) // ���ݿ��в�����
			{
				shareDao.save(share);
			}
		}
	}

	// ���¹�Ʊÿ�յ���ʷ��Ϣ
	public static void updateDayLine() throws IOException {
		// ���ȸ��¹�Ʊ��Ϣ
		updateShare();
		List<Share> shares = shareDao.getAllShare();
		for (Share share : shares) {
			String shareCode = share.getCode();
			if (shareCode.startsWith("0") || shareCode.startsWith("3") || shareCode.startsWith("6")) {
				DayLine dayLine = daylineDao.getLastDayLine(shareCode);
				if (dayLine != null) {
					String dateString = dayLine.getDate();
					String code = getTypeOfShare(shareCode) + shareCode;
					try {
						// ��ȡ�Ĺ�Ʊ����Ϣ�Ŀ�ʼ���������һ��ȡ����Ϣ�ĵڶ��죬���������ǵ�ǰ����
						String beginDate = DateUtils.addDateOneDay(dateString);
						String endDate = DateUtils.parseDateTo10(new Date());
						List<DayLine> dayLines = getDayLineFromInternet(share, code, beginDate, endDate);
						// �����ȡ����ǰ��Ʊ����
						if (dayLines.size() > 0) {
							daylineDao.save(dayLines);
							share.setInited(1);
							shareDao.update(share);
						}
						// ȡ���������ˣ���ʾû����Ϣ����ĩ��ͣ�ƻ��Ѿ�����
					} catch (Exception e) {
						logger.debug("��ȡ��Ʊ��Ϣʧ��");
						logger.debug(e.getMessage());
					}
				}
			}
		}
	}

	// ��ȡ��Ʊ����
	private static String getTypeOfShare(String code) {
		String type = "others";
		if (code.startsWith("0") || code.startsWith("3")) {
			return "sz";
		} else if (code.startsWith("6")) {
			return "sh";
		}
		return type;
	}
}
