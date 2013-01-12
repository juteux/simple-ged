package com.simple.ged.services;

import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.dao.DirectoryDAO;
import com.simple.ged.models.GedDirectory;

/**
 * 
 * Provide method for directory manipulation
 * 
 * See also GedDocumentService for delete and rename methods
 * 
 * @author xavier
 * 
 */
public final class GedDirectoryService {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(GedDirectoryService.class);

	
	/**
	 * Should not be instantiated
	 */
	private GedDirectoryService() {	
	}
	
	
	/**
	 * Replace \\ by /, to keep unix like path in database
	 */
	private static String forceUnixSeparator(String s) {
		return s.replaceAll(Matcher.quoteReplacement("\\"), Matcher.quoteReplacement("/"));
	}
	
	
	/**
	 * 
	 * @param directoryPath
	 *            The directory path, relative to ged root
	 */
	public static GedDirectory findDirectorybyDirectoryPath(String directoryPath) {
		logger.debug("Fin directory for path : {}", directoryPath);
		return DirectoryDAO.findDirectorybyDirectoryPath(forceUnixSeparator(directoryPath));
	}

	
	/**
	 * Add or update the given document
	 */
	public static void addOrUpdateDirectory(GedDirectory directory)
	{
		DirectoryDAO.saveOrUpdate(directory);
	}
	
}
