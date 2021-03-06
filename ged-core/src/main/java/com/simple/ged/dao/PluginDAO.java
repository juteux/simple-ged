package com.simple.ged.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.simple.ged.models.GedPlugin;

import fr.xmichel.toolbox.hibernate.sqlite.HibernateUtil;

/**
 * This class is the DAO for plugins
 * 
 * @author xavier
 *
 */
public final class PluginDAO {

	
	private static final Logger logger = LoggerFactory.getLogger(PluginDAO.class);
	
	
	/**
	 * Should not be instantiated
	 */
	private PluginDAO() {
	}
	
	
	/**
	 * Get plugin informations from database
	 * 
	 * @param pluginFileName
	 *            The plugin file name
	 */
	public static synchronized GedPlugin getPluginInformations(String pluginFileName) {

		// first step, found the appropriated plugin
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(GedPlugin.class).add(Restrictions.eq("fileName", pluginFileName));  
		
		@SuppressWarnings("unchecked")
		List<GedPlugin> results = criteria.list();  

		// second step, return the appropriated document
		GedPlugin p = null;

		if ( ! results.isEmpty() ) {
			p = results.get(0);
		}
		
		session.close();
		
		return p;
	}
	
	
	/**
	 * Delete some plugin informations from database, the plugin is considered as deactivated.
	 */
	public static synchronized void delete(GedPlugin pmi) {
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
	public static synchronized void saveOrUpdate(GedPlugin pmi)
	{
		logger.debug("save plugin");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.saveOrUpdate(pmi);
		session.getTransaction().commit();
		session.close();
	}
}
