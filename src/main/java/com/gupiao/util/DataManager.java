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
		// 尝试取数据的次数
		int count = 3;
		Document document = null;
		for (int i = 0; i < TRY_COUNT; i++) {
			try {
				document = Jsoup.connect(url).timeout(60 * 1000).get();
				break;
			} catch (SocketTimeoutException e) {
				count--;
				if (count > 0) {
					logger.debug("取数据失败，开始第" + (4 - count) + "次尝试");
					document = Jsoup.connect(url).timeout(60 * 1000).get();
				}
			}
		}
		// 如果取到了数据
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
						// 日期
						gupiao.setDate((e.child(0).text()));
						// 收盘价
						gupiao.setSpj(Double.parseDouble(e.child(1).text()));
						// 涨跌额
						gupiao.setZde(Double.parseDouble(e.child(2).text()));
						// 涨跌幅
						gupiao.setZdf(Double.parseDouble(e.child(3).text().split("%")[0]));
						// 开盘价
						gupiao.setKpj(Double.parseDouble(e.child(4).text()));
						// 最高价
						gupiao.setMaxPrice(Double.parseDouble(e.child(5).text()));
						// 最低价
						gupiao.setMinPrice(Double.parseDouble(e.child(6).text()));
						// 成交量
						gupiao.setCjl(Long.parseLong(e.child(7).text().split("手")[0]));
						// 成交额
						gupiao.setCje(Long.parseLong(e.child(8).text().split("万")[0]));
						// 设置股票代码
						gupiao.setCode(share.getCode());
						// 设置股票名称
						gupiao.setName(share.getName());
						dayLines.add(gupiao);
					} catch (Exception e2) {
						logger.debug("产生了运行时异常");
						logger.debug("url:" + url);
						logger.debug("股票信息:" + e.text());
						logger.debug(e2.getStackTrace());
					}
				}
			} else {
				logger.info("此股票暂时无法获取其信息:" + code);
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
			// 股票数目一般大于100
			if (e.children().size() > 100) {
				// e的每个孩子是一个股票li
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
					// 0表示股票信息还未被初始化，即还未存入到dayline表中
					share.setInited(0);
					share.setLastupdate("0000-00-00");
					shares.add(share);
				}
			}
		}
		return shares;
	}

	// 初始化股票每日的历史信息
	public static void initDayLine() throws IOException {
		if (!daylineDao.isEmpty()) {
			logger.debug("dayline表不为空，不能进行初始化操作");
			return;
		}
		List<Share> shares = shareDao.getAllShare();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);// 获取年份
		for (Share share : shares) {
			String shareCode = share.getCode();
			if (shareCode.startsWith("0") || shareCode.startsWith("3") || shareCode.startsWith("6")) {
				String code = getTypeOfShare(shareCode) + shareCode;
				String beginDate = year + "-" + 1 + "-" + 31;
				String endDate = year + "-" + 12 + "-" + 31;
				List<DayLine> dayLines = getDayLineFromInternet(share, code, beginDate, endDate);
				// 如果能取到当前股票数据
				if (dayLines.size() > 0) {
					daylineDao.save(dayLines);
					share.setInited(1);
					shareDao.update(share);
				}
			}
		}
	}

	// 初始化股票信息
	public static void initShare() throws IOException {
		if (!shareDao.isEmpty()) {
			logger.debug("share表不为空，不能进行初始化操作");
			return;
		}
		List<Share> shares = getShareFromInternet();
		shareDao.save(shares);
	}

	public static void updateShare() throws IOException {
		List<Share> shares = getShareFromInternet();
		for (Share share : shares) {
			Share s = shareDao.get(share.getCode());
			if (s == null) // 数据库中不存在
			{
				shareDao.save(share);
			}
		}
	}

	// 更新股票每日的历史信息
	public static void updateDayLine() throws IOException {
		// 首先更新股票信息
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
						// 获取的股票的信息的开始日期是最后一次取得信息的第二天，结束日期是当前日期
						String beginDate = DateUtils.addDateOneDay(dateString);
						String endDate = DateUtils.parseDateTo10(new Date());
						List<DayLine> dayLines = getDayLineFromInternet(share, code, beginDate, endDate);
						// 如果能取到当前股票数据
						if (dayLines.size() > 0) {
							daylineDao.save(dayLines);
							share.setInited(1);
							shareDao.update(share);
						}
						// 取不到就算了，表示没有信息，周末或停牌或已经退市
					} catch (Exception e) {
						logger.debug("获取股票信息失败");
						logger.debug(e.getMessage());
					}
				}
			}
		}
	}

	// 获取股票类型
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
