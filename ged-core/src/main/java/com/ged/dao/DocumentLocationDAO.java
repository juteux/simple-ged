package com.ged.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.ged.models.GedDocumentPhysicalLocation;
import com.tools.hibernate.HibernateUtil;

/**
 * This class is the document location DAO
 * 
 * @author xavier
 *
 */
public class DocumentLocationDAO {

	
	private static final Logger logger = Logger.getLogger(DocumentLocationDAO.class);
	
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
		session.saveOrUpdate(location);
		session.close();
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
		session.delete(location);
		session.close();
	}
	
}

