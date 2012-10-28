package com.ged.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.ged.plugins.PluginManagementInformations;
import com.tools.hibernate.HibernateUtil;

/**
 * This class is the DAO for plugins
 * 
 * @author xavier
 *
 */
public class PluginDAO {

	
	private static final Logger logger = Logger.getLogger(PluginDAO.class);
	
	
	/**
	 * Get plugin informations from database
	 * 
	 * @param pluginFileName
	 *            The plugin file name
	 */
	public static synchronized PluginManagementInformations getPluginInformations(String pluginFileName) {

		// first step, found the appropriated plugin
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(PluginManagementInformations.class).add(Restrictions.eq("fileName", pluginFileName));  
		
		@SuppressWarnings("unchecked")
		List<PluginManagementInformations> results = criteria.list();  

		// second step, return the appropriated document
		PluginManagementInformations p = null;

		if ( ! results.isEmpty() ) {
			p = results.get(0);
		}
		
		session.close();
		
		return p;
	}
	
	
	/**
	 * Delete some plugin informations from database, the plugin is considered as deactivated.
	 */
	public static synchronized void delete(PluginManagementInformations pmi) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.delete(pmi);
		session.getTransaction().commit();
		session.close();
	}

	
	/**
	 * Save or update document
	 * @param document
	 * 				The document to save or to update
	 */
	public static synchronized void saveOrUpdate(PluginManagementInformations pmi)
	{
		logger.debug("save plugin");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.saveOrUpdate(pmi);
		session.getTransaction().commit();
		session.close();
	}
	
	/**
	 * Add a plugin in database
	 * 
	 * @param pmi
	 *            The new plugin
	 */
	@Deprecated
	public static synchronized void addPlugin(PluginManagementInformations pmi) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
						
			session.save(pmi);
			
			transaction.commit();
		} catch (HibernateException he) {
			logger.error("Error while inserting plugin");
			he.printStackTrace();
			if(transaction != null) {
				try { 
					transaction.rollback(); 
				} catch(HibernateException he2) {
					logger.error("Error rollback transaction");
					he2.printStackTrace(); 
				}
			}
		} finally {
			if(session != null) {
				try { 
					session.close();
				} catch(HibernateException he) {
				}
			}
		}
	}
	
	
	/**
	 * Update some plugin
	 */
	@Deprecated
	public static synchronized void updatePlugin(PluginManagementInformations pmi) {
		logger.info("update plugin");
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			session.merge(pmi);
			
			transaction.commit();
		} catch (HibernateException he) {
			logger.error("Error while updating plugin");
			he.printStackTrace();
			if(transaction != null) {
				try { 
					transaction.rollback(); 
				} catch(HibernateException he2) {
					logger.error("Error rollback transaction");
					he2.printStackTrace(); 
				}
			}
		} finally {
			if(session != null) {
				try { 
					session.close();
				} catch(HibernateException he) {
				}
			}
		}
	}
	
}
