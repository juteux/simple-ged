package com.ged.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.dao.DocumentDAO;
import com.ged.models.GedDocument;
import com.ged.models.GedDocumentPhysicalLocation;
import com.ged.tools.FileHelper;

/**
 * @class GedDocumentService
 * 
 * Provide method for document manipulation
 * 
 * @author xavier
 *
 */
public class GedDocumentService {

	private static final Logger logger = Logger.getLogger(GedDocumentService.class);

	/**
	 * 
	 * @param filePath
	 *            The file path, relative to ged root
	 */
	public static GedDocument findDocumentbyFilePath(String filePath) {
		return DocumentDAO.findDocumentbyFilePath(filePath);
	}
	
	
	/**
	 * 
	 * @param filePath
	 *            The file path, relative to ged root
	 */
	public static GedDocument getDocumentFromFile(String filePath)
	{
		return DocumentDAO.findDocumentbyFilePath(filePath);
	}
	
	/**
	 * Add a document
	 */
	public static void addDocument(GedDocument doc)
	{
		DocumentDAO.saveOrUpdate(doc);
	}
	
	/**
	 * Update document
	 */
	public static void updateDocument(GedDocument doc)
	{
		DocumentDAO.saveOrUpdate(doc);
	}
	
	/**
	 * Rename some ged document file (give relative file path)
	 * 
	 * @param oldName
	 * 				The old file name
	 * @param newName
	 * 				The new file name
	 */
	public static void renameDocumentFile(String oldName, String newName)
	{
		if (newName.isEmpty()) {
			return;
		}
		
		// physical renaming
		File oldFile = new File(Profile.getInstance().getLibraryRoot() + oldName);
		oldFile.renameTo(new File(Profile.getInstance().getLibraryRoot() + newName));
		
		// rename in database
		DocumentDAO.updateFilePath(oldName, newName);
	}
	
	
	/**
	 * Delete some document
	 */
	public static void deleteDocumentFile(String filePath)
	{
		try {
			FileHelper.recursifDelete(new File(Profile.getInstance()
					.getLibraryRoot() + filePath));
		} catch (IOException e) {
			logger.error("Delete error");
		}
		
		DocumentDAO.deleteFile(filePath);
	}
	
	/**
	 * Get documents with the given location
	 */
	public static List<GedDocument> findDocumentbyLocation(GedDocumentPhysicalLocation location) {
		return DocumentDAO.findDocumentbyLocation(location);
	}
}
