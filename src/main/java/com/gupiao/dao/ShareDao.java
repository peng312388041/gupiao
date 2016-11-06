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

import com.gupiao.model.Share;

public class ShareDao {
	public static Logger logger = Logger.getLogger(ShareDao.class);
	public static final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			.configure("hibernate.cfg.xml").build();
	// 2. ���ݷ���ע���ഴ��һ��Ԫ������Դ����ͬʱ����Ԫ���ݲ�����Ӧ��һ��Ψһ�ĵ�session����
	public static SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

	public void save(Share share) {
		Session session = sessionFactory.openSession();// �ӻỰ������ȡһ��session
		Transaction transaction = session.beginTransaction();// ����һ���µ�����
		session.save(share);
		transaction.commit();// �ύ����
		session.close();
	}

	public void update(Share share) {
		Session session = sessionFactory.openSession();// �ӻỰ������ȡһ��session
		Transaction transaction = session.beginTransaction();// ����һ���µ�����
		session.update(share);
		transaction.commit();// �ύ����
		session.close();
	}

	public List<Share> getAllShare() {
		List<Share> shares = new ArrayList<Share>();
		String hql = "from Share";
		Session session = sessionFactory.openSession();
		Query<Share> query = session.createQuery(hql, Share.class);
		shares = query.getResultList();
		session.close();
		return shares;
	}

	// �洢��Ʊ�б����ݿ�
	public void save(List<Share> shares) {
		Session session = sessionFactory.openSession();// �ӻỰ������ȡһ��session
		Transaction transaction = session.beginTransaction();// ����һ���µ�����
		for (Share share : shares) {
			session.save(share);
		}
		transaction.commit();// �ύ����
		session.close();
	}

	// ����codeȡ��Ʊ
	public Share get(String code) {
		Share share = null;
		Session session = sessionFactory.openSession();// �ӻỰ������ȡһ��session
		String hql = "from Share where code=?";
		Query<Share> query = session.createQuery(hql, Share.class);
		query.setParameter(0, code);
		query.setMaxResults(1);
		try {
			share = query.getSingleResult();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		session.close();
		return share;
	}

	public boolean isEmpty() {
		List<Share> shares = getAllShare();
		return shares.size() == 0;
	}
}
