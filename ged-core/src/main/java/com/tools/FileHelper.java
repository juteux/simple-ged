package com.tools;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * File manipulation tools
 * @author xavier
 *
 */
public class FileHelper {

	
	private static final Logger logger = Logger.getLogger(FileHelper.class);
	
	
	/**
	 * Get the extension of the given file (without leading dot)
	 * 
	 * The extension is returned in upper case
	 */
	public static String getExtension(String fileName) {
		int mid = fileName.lastIndexOf(".");
		return fileName.substring(mid + 1, fileName.length()).toUpperCase();
	}
	
	
	/**
	 * Create the given directory if it not exists yet
	 */
	public static void createDirectoryIfNecessary(String dirPath) {
		File f = new File(dirPath);
		if ( ! f.exists() ) {
			try {
				f.mkdir();
			} catch (Exception e) {
				logger.error("Error while creating directory : " + dirPath);
			}
		}
	}
	
	/**
	 * Does this folder exits ? 
	 * 
	 * @return true if the folder exists, false otherwise.
	 */
	public static boolean folderExists(String folderAbsolutePath) {
		File f = new File(folderAbsolutePath);
		return (f.exists() && f.isDirectory());
	}
}
