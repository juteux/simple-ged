package com.simple.ged;


import java.io.File;
import java.util.Map.Entry;

import javafx.application.Application;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.ged.update.DoUpdate;
import com.ged.update.UpdateHelper;
import com.ged.update.UpdateInformations;
import com.simple.ged.services.GedDocumentLocationService;
import com.simple.ged.ui.MainWindow;
import com.tools.PropertiesHelper;
import com.tools.hibernate.HibernateUtil;

/**
 * Application launcher
 * @author xavier
 * 
 */
public class Launcher {

	
	private static final Logger logger = Logger.getLogger(Launcher.class);
	
	
	public static void main(String[] args) {
				
		// load properties
		PropertiesHelper.getInstance().load("properties/strings.properties");
		PropertiesHelper.getInstance().load("properties/icons.properties");
		PropertiesHelper.getInstance().load("properties/constants.properties");
		
		// create or update database
		HibernateUtil.getSessionFactory().openSession();
		
		GedDocumentLocationService.makeSurAtLeastOneDocumentLocationExists();
		MiddleProfile.getInstance().completeUpdate();
		
		
		// check for updates
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				
				/*
				 * Update the updater if necessary
				 */
				
				String onlineUpdaterVersion = UpdateHelper.getVersionNumber(UpdateInformations.UPDATER_UPDATE_DESCRIPTOR_PATH);
				
				if (Float.parseFloat(onlineUpdaterVersion) <= Float.parseFloat(DoUpdate.UPDATER_VERSION)) {
					logger.info("Updater : no new version");
				}
				else {
					logger.info("Updater need to be updated");
					
					for (Entry<String, String> e : UpdateHelper.getFilesToDownloadMap(UpdateInformations.UPDATER_UPDATE_DESCRIPTOR_PATH).entrySet()) {
						logger.debug(e.getKey() + " => " + System.getProperty("user.dir") + File.separator + e.getValue());
						try {;
							UpdateHelper.downloadAndReplaceFile(e.getKey(), System.getProperty("user.dir") + File.separator + e.getValue());
						} 
						catch (Exception e2) {
							e2.printStackTrace();
							logger.error("Error while downloading : " + e.getKey());
						}
					}
				}
				
				
				/*
				 * Now update the core if necessary
				 */
				
				String onlineVersion = UpdateHelper.getVersionNumber(UpdateInformations.GED_CORE_UPDATE_DESCRIPTOR_PATH);
				
				if (Float.parseFloat(onlineVersion) <= Float.parseFloat(PropertiesHelper.getInstance().getProperties().getProperty("APPLICATION_VERSION"))) {
					logger.info("UpdateManager : No new version");
					return;
				}
				
				logger.info("UpdateManager : New version (" + onlineVersion + ") is avaliable");
				
				if (onlineVersion != null) {
					
					// some new version is available		
					int option = JOptionPane.showConfirmDialog(null, "Vous utilisez la version " 
									+ PropertiesHelper.getInstance().getProperties().getProperty("APPLICATION_VERSION") + " de Simple GED or la version " 
									+ onlineVersion + " est disponible.\n"
									+ "Voulez-vous faire la mise à jour ? (recommandé)", 
									"Nouvelle version disponible", 
									JOptionPane.YES_NO_OPTION, 
									JOptionPane.QUESTION_MESSAGE);
								
					if(option == JOptionPane.OK_OPTION) {
	
						try {
					        Runtime.getRuntime().exec("java -jar simpleGedUpdateSystem.jar");
					        System.exit(0);
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, "Erreur", "Impossible de lancer l'assistant de mise à jour", JOptionPane.ERROR_MESSAGE);
						}
						
					}
					
				}
			}
		});
		t.start();
	
	
		// The main window
		Application.launch(MainWindow.class, args);
	}

}
