package com.simple.ged;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.log4j.Logger;

import fr.xmichel.toolbox.tools.OSHelper;


/**
 * This class is a singleton for accessing to global program preferences, some kind of profile
 */
public class Profile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	transient private static Profile currentProfil = null;

	transient private static String profil_file = OSHelper.getOSName() + ".profile";
	
	transient private static final Logger logger = Logger.getLogger(Profile.class);
	
	
	/**
	 * Singleton getter
	 */
	public static synchronized Profile getInstance() {
		if (currentProfil == null) {
			File f = new File(profil_file);
			if ( f.exists() ) {
				currentProfil = loadState();
			} else {
				currentProfil = new Profile();
			}
		}
		return currentProfil;
	}

	/**
	 * Library root
	 */
	private String libraryRoot;

	/**
	 * Current theme name
	 */
	private String currentTheme;
	
	/**
	 * Default printer
	 */
	private String defaultPrinter;
	
	
	private Profile() {
		libraryRoot = "Non défini";
		currentTheme = "Metal";			// default theme
		defaultPrinter = "";
	}
	
	
	/**
	 * Define and save library root
	 * 
	 * Note that the final file separator is automatically added
	 * 
	 * Warning : the value isn't save, please call commit to save changes !
	 */
	public synchronized void setDocumentLibraryRoot(String newRoot) {
		libraryRoot = newRoot + (libraryRoot.endsWith(File.separator) ? "" : File.separator);
	}

	/**
	 * Define and save current theme
	 * 
	 * Warning : the value isn't save, please call commit to save changes !
	 */
	public synchronized void setTheme(String newTheme) {
		currentTheme = newTheme;
	}
	
	/**
	 * Define and save default printer
	 * 
	 * Warning : the value isn't save, please call commit to save changes !
	 */
	public synchronized void setDefaultPrinter(String printerName) {
		defaultPrinter = printerName;
	}
	
	
	/**
	 * Save the changes
	 */
	public synchronized void commitChanges() {
		logger.info("Saving settings");
		saveState();
	}
	
	
	/**
	 * Get the library root
	 * 
	 * Note that the result should contains a final file separator
	 */
	public String getLibraryRoot() {
		return libraryRoot + (libraryRoot.endsWith(File.separator) ? "" : File.separator);
	}

	
	/**
	 * Get the current theme name
	 */
	public String getThemeName() {
		return currentTheme;
	}
	
	
	/**
	 * Get the default printer name
	 */
	public String getDefaultPrinterName() {
		return defaultPrinter;
	}
	
	
	/**
	 * Save the profile
	 */
	private synchronized void saveState(){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(profil_file);
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(this); 
			oos.flush();
			oos.close();
		} 
		catch (Exception e) {
			logger.error("Error while saving profil");
		}
		finally {
			try {
				fos.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * Load the profile
	 * @return
	 * 		The loaded profile
	 */
	private static synchronized Profile loadState(){
		Profile profil = null;
		try {
			FileInputStream fis = new FileInputStream(profil_file);
			ObjectInputStream ois= new ObjectInputStream(fis);
			try {	
				profil = (Profile) ois.readObject(); 
			} 
			finally{
				try{
					ois.close();
				} 
				finally {
					fis.close();
				}
			}
		} 
		catch(Exception e) {
			logger.error("Error while loading profil");
			
			profil = new Profile();
		} 
		return profil;
	}
	
}
