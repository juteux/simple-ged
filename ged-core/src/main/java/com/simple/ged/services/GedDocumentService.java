package com.simple.ged.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.Profile;
import com.simple.ged.dao.DirectoryDAO;
import com.simple.ged.dao.DocumentDAO;
import com.simple.ged.models.GedDocument;
import com.simple.ged.models.GedDocumentFile;
import com.simple.ged.models.GedDocumentPhysicalLocation;
import com.simple.ged.tools.FileHelper;

import fr.xmichel.javafx.dialog.Dialog;

/**
 * @class GedDocumentService
 * 
 * Provide method for document AND directory manipulation
 * 
 * @author xavier
 *
 */
public final class GedDocumentService {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(GedDocumentService.class);

	
	/**
	 * Should not be instantiated
	 */
	private GedDocumentService() {	
	}
	
	
	/**
	 * Replace \\ by /, to keep unix like path in database
	 */
	private static String forceUnixSeparator(String s) {
		return s.replaceAll(Matcher.quoteReplacement("\\"), Matcher.quoteReplacement("/"));
	}
	
	
	/**
	 * 
	 * @param filePath
	 *            The file path, relative to ged root
	 */
	public static GedDocument findDocumentbyFilePath(String filePath) {
		return DocumentDAO.findDocumentbyFilePath(forceUnixSeparator(filePath));
	}
	
	
	/**
	 * 
	 * @param filePath
	 *            The file path, relative to ged root
	 */
	public static GedDocument getDocumentFromFile(String filePath)
	{
		return DocumentDAO.findDocumentbyFilePath(forceUnixSeparator(filePath));
	}
	
	/**
	 * Add or update the given document
	 */
	public static void addOrUpdateDocument(GedDocument doc)
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
		Path oldFilePath = Paths.get(Profile.getInstance().getLibraryRoot() + oldName);
		Path newFilePath = Paths.get(Profile.getInstance().getLibraryRoot() + newName);
		
		if (oldFilePath.equals(newFilePath)) {
			return;
		}
		
		try {
			logger.info("Move : (" + oldFilePath + " => " + newFilePath);
			Files.move(oldFilePath, newFilePath);
		} catch (IOException e) {
			logger.error("Move failed : (" + oldFilePath + " => " + newFilePath);
			logger.error("Impossible de déplacer le fichier", e);
			Dialog.showThrowable("Impossible de renommer/déplacer le fichier", "Le renommage/déplacement du fichier a échoué :", e);
			return;
		}
		
		String oldNameUnixStyle = forceUnixSeparator(oldName);
		String newNameUnixStyle = forceUnixSeparator(newName);
		
		if (oldNameUnixStyle.startsWith("/")) {
			oldNameUnixStyle = oldNameUnixStyle.replaceFirst("/", "");
		}
		if (newNameUnixStyle.startsWith("/")) {
			newNameUnixStyle = newNameUnixStyle.replaceFirst("/", "");
		}
		
		logger.debug("Old name : {}", oldNameUnixStyle);
		logger.debug("New name : {}", newNameUnixStyle);
		
		// rename in database
		DirectoryDAO.updateDirectoryPath(oldNameUnixStyle, newNameUnixStyle);
		DocumentDAO.updateFilePath(oldNameUnixStyle, newNameUnixStyle);
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
			logger.error("Delete error", e);
			Dialog.showThrowable("Impossible de supprimer le fichier", "La suppression du fichier a échoué :", e);
			return;
		}
		
		DirectoryDAO.deleteDirectory(forceUnixSeparator(filePath));
		DocumentDAO.deleteFile(forceUnixSeparator(filePath));
	}
	
	/**
	 * Get documents with the given location
	 */
	public static List<GedDocument> findDocumentbyLocation(GedDocumentPhysicalLocation location) {
		return DocumentDAO.findDocumentbyLocation(location);
	}
	
	/**
	 * Search for the given words
	 * 
	 * Words is a string where word are splited by space, and a matching item must match with any of the given words
	 */
	public static synchronized List<GedDocumentFile> searchForWords(String searchedWords) {
		String[] words = searchedWords.split(" ");
		
		// convert to java list
		List<String> wordList = new ArrayList<>();
        Collections.addAll(wordList, words);
		
		return DocumentDAO.getDocumentWhichContainsEveryWords(wordList);
	}
	
	
	/**
	 * Get relative file path from the absolute path
	 */
	public static String getRelativeFromAbsloutePath(String absolutePath) {
		return absolutePath.replaceFirst(Pattern.quote(Profile.getInstance().getLibraryRoot()), "");
	}
	
}
