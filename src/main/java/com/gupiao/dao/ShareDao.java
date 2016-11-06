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
	// 2. 根据服务注册类创建一个元数据资源集，同时构建元数据并生成应用一般唯一的的session工厂
	public static SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

	public void save(Share share) {
		Session session = sessionFactory.openSession();// 从会话工厂获取一个session
		Transaction transaction = session.beginTransaction();// 开启一个新的事务
		session.save(share);
		transaction.commit();// 提交事务
		session.close();
	}

	public void update(Share share) {
		Session session = sessionFactory.openSession();// 从会话工厂获取一个session
		Transaction transaction = session.beginTransaction();// 开启一个新的事务
		session.update(share);
		transaction.commit();// 提交事务
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

	// 存储股票列表到数据库
	public void save(List<Share> shares) {
		Session session = sessionFactory.openSession();// 从会话工厂获取一个session
		Transaction transaction = session.beginTransaction();// 开启一个新的事务
		for (Share share : shares) {
			session.save(share);
		}
		transaction.commit();// 提交事务
		session.close();
	}

	// 根据code取股票
	public Share get(String code) {
		Share share = null;
		Session session = sessionFactory.openSession();// 从会话工厂获取一个session
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
