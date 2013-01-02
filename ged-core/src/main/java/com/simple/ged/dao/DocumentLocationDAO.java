package com.simple.ged.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.simple.ged.models.GedDocumentPhysicalLocation;

import fr.xmichel.toolbox.hibernate.sqlite.HibernateUtil;


/**
 * This class is the document location DAO
 * 
 * @author xavier
 *
 */
public final class DocumentLocationDAO {

	
	private static final Logger logger = LoggerFactory.getLogger(DocumentLocationDAO.class);
	
	
	/**
	 * Should not be instantiated
	 */
	private DocumentLocationDAO() {
	}
	
	
	/**
	 * Get locations, sorted by ID 
	 */
	public static synchronized List<GedDocumentPhysicalLocation> getLocations() {

		Session session = HibernateUtil.getSessionFactory().openSession();  
		Query query = session.createQuery("FROM GedDocumentPhysicalLocation ORDER BY id");
		
		@SuppressWarnings("unchecked")
		List<GedDocumentPhysicalLocation> results = query.list();  
		
		session.close();
		
		return results;
	}
	
	
	public static synchronized void saveOrUpdate(GedDocumentPhysicalLocation location)
	{
		logger.debug("save location");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.saveOrUpdate(location);
		session.getTransaction().commit();
		session.close();
	}
	
	
	/**
	 * Get a location by tge string
	 * 
	 * @return
	 * 			The location if there exists, null otherwise
	 */
	public static GedDocumentPhysicalLocation getLocationByName(String name) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(GedDocumentPhysicalLocation.class).add(Restrictions.eq("label", name));  
		
		@SuppressWarnings("unchecked")
		List<GedDocumentPhysicalLocation> results = criteria.list();  

		GedDocumentPhysicalLocation d = null;

		if ( ! results.isEmpty() ) {
			d = results.get(0);
		}
		
		session.close();
		
		return d;
	}
	

	/**
	 * Get the location for the specified id
	 */
	public static synchronized GedDocumentPhysicalLocation findLocationById(int id) {

		// first step, found the appropriated location
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(GedDocumentPhysicalLocation.class).add(Restrictions.eq("id", id)).setMaxResults(1);  
		
		@SuppressWarnings("unchecked")
		List<GedDocumentPhysicalLocation> results = criteria.list();  

		// second step, return the appropriated location
		GedDocumentPhysicalLocation l = null;

		if ( ! results.isEmpty() ) {
			l = results.get(0);
		}
		
		session.close();
		
		return l;
	}
	

	
	/**
	 * delete a location
	 */
	public static synchronized void delete(GedDocumentPhysicalLocation location) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.delete(location);
		session.getTransaction().commit();
		session.close();
	}
	
}

