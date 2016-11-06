package com.gupiao.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import com.gupiao.model.DayLine;

public class DayLineDao {
	public static Logger logger = Logger.getLogger(DayLineDao.class);
	public static final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			.configure("hibernate.cfg.xml").build();
	// 2. ���ݷ���ע���ഴ��һ��Ԫ������Դ����ͬʱ����Ԫ���ݲ�����Ӧ��һ��Ψһ�ĵ�session����
	public static SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

	public List<DayLine> getAllDayLine() {
		List<DayLine> dayLines = new ArrayList<DayLine>();
		String hql = "from DayLine";
		Session session = sessionFactory.openSession();
		Query<DayLine> query = session.createQuery(hql, DayLine.class);
		dayLines = query.getResultList();
		session.close();
		return dayLines;
	}

	public void save(DayLine dayLine) {
		Session session = sessionFactory.openSession();// �ӻỰ������ȡһ��session
		Transaction transaction = session.beginTransaction();
		session.save(dayLine);
		transaction.commit();
		session.close();
	}

	public void save(List<DayLine> dayLines) {
		Session session = sessionFactory.openSession();// �ӻỰ������ȡһ��session
		Transaction transaction = session.beginTransaction();
		for (DayLine dayLine : dayLines) {
			session.save(dayLine);
		}
		transaction.commit();
		session.close();
	}

	public List<DayLine> getLastDayLine(String code, int days) {
		Session session = sessionFactory.openSession();
		List<DayLine> dayLine = null;
		String hql = "from DayLine where code=? order by date desc";
		Query<DayLine> query = session.createQuery(hql, DayLine.class);
		query.setParameter(0, code);
		query.setMaxResults(days);
		try {
			dayLine = query.getResultList();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		session.close();
		return dayLine;
	}

	public boolean isEmpty() {
		List<DayLine> dayLines = getAllDayLine();
		return dayLines.size() == 0;
	}
}
