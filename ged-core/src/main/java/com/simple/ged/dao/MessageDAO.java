package com.simple.ged.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.simple.ged.models.GedMessage;

import fr.xmichel.toolbox.hibernate.sqlite.HibernateUtil;

/**
 * This class is the message DAO
 * 
 * @author xavier
 *
 */
public class MessageDAO {

	
	private static final Logger logger = Logger.getLogger(MessageDAO.class);
	
	
	/**
	 * Get messages, sorted by date desc
	 */
	public static synchronized List<GedMessage> getMessages() {

		Session session = HibernateUtil.getSessionFactory().openSession();  
		Query query = session.createQuery("FROM GedMessage ORDER BY date DESC");
		
		@SuppressWarnings("unchecked")
		List<GedMessage> results = query.list();  
		
		session.close();
		
		return results;
	}
	
	
	/**
	 * Is there unread messages ?
	 */
	public static boolean thereIsUnreadMessages() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(GedMessage.class).add(Restrictions.eq("read", false));  
		
		@SuppressWarnings("unchecked")
		List<GedMessage> results = criteria.list();  

		session.close();
			
		return (results.size() != 0);
	}

	/**
	 * Mark all messages as read
	 */
	public static void markAllMessagesAsRead() {
		logger.debug("mark all messages as read");
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		String hqlUpdate = "UPDATE GedMessage SET read=true";
		session.createQuery(hqlUpdate).executeUpdate();
		
		session.getTransaction().commit();
		session.close();
	}
	
	/**
	 * Insert or update a new message in database
	 */
	public static synchronized void saveOrUpdate(GedMessage message) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.saveOrUpdate(message);
		session.getTransaction().commit();
		session.close();
	}
	
}

