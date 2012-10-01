package com.ged.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.ged.models.GedDocument;
import com.ged.models.GedDocumentFile;
import com.ged.models.GedDocumentPhysicalLocation;
import com.tools.hibernate.HibernateUtil;

/**
 * The document manager is an interface for current manipulations on a ged
 * document
 * 
 * In two words, it's a DAO interface
 * 
 * Note : all manipulate path are relative path to library root
 * 
 * @author xavier
 * 
 */
public class DocumentDAO {

	
	private static final Logger logger = Logger.getLogger(DocumentDAO.class);
	
	/**
	 * 
	 * @param filePath
	 *            The file path, relative to ged root
	 */
	public static synchronized GedDocument findDocumentbyFilePath(String filePath) {
		logger.debug("Get document for file : " + filePath);

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(GedDocumentFile.class).add(Restrictions.eq("relativeFilePath", filePath));  
		
		@SuppressWarnings("unchecked")
		List<GedDocumentFile> results = criteria.list();  

		GedDocument d = null;

		if ( ! results.isEmpty() ) {
			d = results.get(0).getDocument();
		}
		
		session.close();
		
		return d;
	}

	/**
	 * Save or update document
	 * @param document
	 * 				The document to save or to update
	 */
	public static synchronized void saveOrUpdate(GedDocument document)
	{
		logger.debug("save document");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.saveOrUpdate(document);
		session.close();
	}
	

	/**
	 * Rename a document or a directory (same treatment)
	 * 
	 * @param oldName
	 *            The old name contains the relative path to file
	 * 
	 * @param newName
	 *            The new name contains the relative path to file, with the new
	 *            name =)
	 */
	public static synchronized void updateFilePath(String oldName, String newName) {
		
		logger.debug("Rename : " + oldName + " to " + newName);

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(GedDocumentFile.class).add(Restrictions.like("relativeFilePath", oldName + "%"));  
		
		@SuppressWarnings("unchecked")
		List<GedDocumentFile> results = criteria.list();  
		
		for (GedDocumentFile file : results) {
			file.setRelativeFilePath(file.getRelativeFilePath().replaceFirst(oldName, newName));
			session.update(file);
		}
		
		session.close();
	}

	
	/**
	 * Delete a document file
	 * 
	 * If some document as not at least on attached file after delete, it's removed
	 */
	public static synchronized void deleteFile(String filePath) {
		logger.debug("Remove document : " + filePath);
		
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(GedDocumentFile.class).add(Restrictions.like("relativeFilePath", filePath + "%"));  
		
		@SuppressWarnings("unchecked")
		List<GedDocumentFile> results = criteria.list();  
		
		for (GedDocumentFile file : results) {
		
			Criteria c = session.createCriteria(GedDocumentFile.class).add(Restrictions.eq("relativeFilePath", file.getRelativeFilePath())); 
			@SuppressWarnings("unchecked")
			List<GedDocumentFile> r = c.list();  
			
			for (GedDocumentFile f : r) {
				f.getDocument().removeFile(f);
				session.save(f.getDocument());
				
				if (f.getDocument().getDocumentFiles().isEmpty()) {
					session.delete(f.getDocument());
				}
			}
			
		}
		
		session.close();
	}
	
	
	/**
	 * Get documents with the given location
	 */
	public static List<GedDocument> findDocumentbyLocation(GedDocumentPhysicalLocation location) {
		Session session = HibernateUtil.getSessionFactory().openSession();  
		
		Criteria criteria = session.createCriteria(GedDocument.class)
				.add(Restrictions.eq("location", location));  
		
		@SuppressWarnings("unchecked")
		List<GedDocument> results = criteria.list();  
		
		session.close();
		
		return results;
	}
	
}
