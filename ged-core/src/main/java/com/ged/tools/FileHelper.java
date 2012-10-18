package com.ged.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.models.GedDocumentFile;

/**
 * This class provide tools for managing files
 * 
 * @author xavier
 * 
 */
public class FileHelper {

	
	private static final Logger logger = Logger.getLogger(FileHelper.class);
	
	
	/**
	 * Copy the file in the library if necessary (if the file is not even in
	 * library)
	 * 
	 * @param file
	 *            The file which need to be copied
	 * 
	 * @param directoryTarget
	 *            Where does I need to copy the file ?
	 * 
	 * @param fileName
	 *            The file name
	 * 
	 * @return The file copied (or not), with relative file path
	 */
	private static GedDocumentFile copyFileIfNecessary(File file,
			File directoryTarget, String fileName) {

		if (file.getAbsolutePath().startsWith(Profile.getInstance().getLibraryRoot())) {
			// nothing to copy
			return new GedDocumentFile(file.getAbsolutePath().replaceFirst(Profile.getInstance().getLibraryRoot().replace("\\", "\\\\"), "").replace('\\', '/'));
		}
	
		int mid = file.getAbsolutePath().lastIndexOf(".");
		String extension = file.getAbsolutePath().substring(mid + 1, file.getAbsolutePath().length());
		
		// we need to copy the file
		String fullPath = directoryTarget.getAbsolutePath() + "/" + fileName + "." + extension;
		
		int preadd = 2;	// if the file ever exists, do not erase, additional number
		while (fileExists(fullPath)) {
			fullPath = directoryTarget.getAbsolutePath() + "/" + fileName + " (" + preadd + ")." + extension;
			preadd++;
		}
		
		File target = new File(fullPath);
		try { 
			logger.debug("Copy : " + target.getAbsolutePath());
			copyFile(file, target);
		} catch (IOException e) {
			logger.error("Cannot copy file : " + file.getAbsolutePath() + " to " + target.getAbsolutePath());
		}
		return new GedDocumentFile(target.getAbsolutePath().replaceFirst(Profile.getInstance().getLibraryRoot().replace("\\", "\\\\"), "").replace('\\', '/'));
	}

	/**
	 * Copy the file in the library if necessary (if the file is not even in
	 * library)
	 * 
	 * @param filesToCopy
	 *            The list of files which need do be copied
	 * 
	 * @param directoryTarget
	 *            Where does I need to copy the file ?
	 * 
	 * @param fileName
	 *            The target file name
	 * 
	 * @return The list of copied files (with relative path)
	 */
	public static List<GedDocumentFile> copyFilesIfNecessary(
			List<File> filesToCopy, File directoryTarget, String fileName) {

		List<GedDocumentFile> l = new ArrayList<GedDocumentFile>();

		if (filesToCopy.size() <= 1) {
			l.add(copyFileIfNecessary(filesToCopy.get(0), directoryTarget, cleanFileName(fileName)));
		} else {
			int i = 1;
			String cleanedName = cleanFileName(fileName);
			for (File f : filesToCopy) {
				l.add(copyFileIfNecessary(f, directoryTarget, cleanedName + " - " + i));
				i++;
			}
		}

		return l;
	}

	/**
	 * File copy
	 * 
	 * @see : http://java.developpez.com/sources/?page=fluxFichiers#copieFichier
	 */
	public static void copyFile(File src, File dest) throws IOException {
		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(dest);

		java.nio.channels.FileChannel channelSrc = fis.getChannel();
		java.nio.channels.FileChannel channelDest = fos.getChannel();

		channelSrc.transferTo(0, channelSrc.size(), channelDest);

		fis.close();
		fos.close();
	}

	/**
	 * Replace forbidden file char per nothing
	 */
	public static String cleanFileName(String fileName) {
		 char[] forbiddenChars = new char[] {'/', '\\', ':', '*', '?', '"', '<', '>', '|'};
		 for (char c : forbiddenChars) {
			 fileName = fileName.replace(c, ' ');
		 }
		 return fileName;
	}
	
	/**
	 * Delete file (recursive)
	 * 
	 * @see http://java.developpez.com/sources/?page=fluxFichiers#manipFile
	 */
	public static void recursifDelete(File path) throws IOException {
	      if (!path.exists()) throw new IOException(
	         "File not found '" + path.getAbsolutePath() + "'");
	      if (path.isDirectory()) {
	         File[] children = path.listFiles();
	         for (int i=0; children != null && i<children.length; i++)
	            recursifDelete(children[i]);
	         if (!path.delete()) throw new IOException(
	            "No delete path '" + path.getAbsolutePath() + "'");
	      }
	      else if (!path.delete()) throw new IOException(
	         "No delete file '" + path.getAbsolutePath() + "'");
	   }
	
	
	/**
	 * Move or rename some file
	 */
	public static void move(String source, String destination) {
		File from = new File(source);
		File to   = new File(destination);
		if (! from.renameTo(to)) {
			logger.error("Error while renaming/moving file");
		}
	} 
	
	/**
	 * Does a file ever exists ?
	 */
	public static boolean fileExists(String path) {
		return new File(path).exists();
	}
}
