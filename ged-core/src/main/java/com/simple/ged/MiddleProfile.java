package com.simple.ged;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.simple.ged.models.GedMessage;
import com.simple.ged.services.MessageService;

import fr.xmichel.toolbox.tools.PropertiesHelper;


/**
 * This is the profile which is shared between different OS.
 * 
 * 
 * @author xavier
 *
 */
public class MiddleProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	transient private static final Logger logger = Logger.getLogger(MiddleProfile.class);
	
	transient private static MiddleProfile currentMiddleProfil = null;

	transient private static String middle_profil_file = "middle.profile";
	
	/**
	 * Singleton getter
	 */
	public static synchronized MiddleProfile getInstance() {
		if (currentMiddleProfil == null) {
			File f = new File(middle_profil_file);
			if ( f.exists() ) {
				currentMiddleProfil = loadState();
			} else {
				currentMiddleProfil = new MiddleProfile();
			}
		}
		return currentMiddleProfil;
	}
	
	/**
	 * The last known version
	 */
	private double lastKnownVersion;
	
	
	
	private MiddleProfile() {
		lastKnownVersion = 3.2;			// this attribute appears in version 3.3, previous version was... 3.2 !
	}
	
	
	/**
	 * Verify if an update was done. If true, complete update instructions.
	 */
	public synchronized void completeUpdate() {
		
		Properties properties = PropertiesHelper.getInstance().getProperties();
		
		// changes on version 3.3
		if (lastKnownVersion < 3.3) { 
			logger.info("Completing update to version 3.3 ...");
			// add update informations message
			MessageService.addMessage(new GedMessage("NEUTRAL", properties.getProperty("update_33_msg")));
			lastKnownVersion = 3.3;
		}
		
		
		// changes on version 3.4
		if (lastKnownVersion < 3.4) {
			logger.info("Completing update to version 3.4 ...");
			// add update informations message
			MessageService.addMessage(new GedMessage("NEUTRAL", properties.getProperty("update_34_msg")));
			lastKnownVersion = 3.4;
		}
		
		
		// save new known version
		lastKnownVersion = Double.parseDouble(properties.getProperty("APPLICATION_VERSION"));
		commitChanges();
	}
	
	
	
	/**
	 * Save the changes
	 */
	public synchronized void commitChanges() {
		logger.info("Saving middle settings");
		saveState();
	}
	
	
	/**
	 * Save the profile
	 */
	private synchronized void saveState(){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(middle_profil_file);
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(this); 
			oos.flush();
			oos.close();
		} 
		catch (Exception e) {
			logger.error("Error while saving middle profil");
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
	private static synchronized MiddleProfile loadState(){
		MiddleProfile profil = null;
		try {
			FileInputStream fis = new FileInputStream(middle_profil_file);
			ObjectInputStream ois= new ObjectInputStream(fis);
			try {	
				profil = (MiddleProfile) ois.readObject(); 
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
			logger.error("Error while loading middle profil");
			
			// if exception, create profile
			profil = new MiddleProfile();
		} 
		return profil;
	}
	
}
