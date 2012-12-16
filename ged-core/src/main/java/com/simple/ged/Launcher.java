package com.simple.ged;


import java.io.File;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.apache.log4j.Logger;

import com.simple.ged.services.GedDocumentLocationService;
import com.simple.ged.ui.MainWindow;
import com.simple.ged.update.DoUpdate;
import com.simple.ged.update.UpdateHelper;
import com.simple.ged.update.UpdateInformations;

import fr.xmichel.javafx.dialog.Dialog;
import fr.xmichel.toolbox.hibernate.sqlite.HibernateUtil;
import fr.xmichel.toolbox.tools.PropertiesHelper;


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
		PropertiesHelper.getInstance().load("properties/update_message.properties");
		
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
						try {
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
					
					Dialog.buildConfirmation("Nouvelle version disponible",
							"Vous utilisez la version " + PropertiesHelper.getInstance().getProperties().getProperty("APPLICATION_VERSION") 
							+ " de Simple GED or la version " + onlineVersion + " est disponible.\n"
							+ "Voulez-vous faire la mise à jour ? (recommandé)"
					)
			        .addYesButton(new EventHandler<ActionEvent>() {
			            @Override
			            public void handle(ActionEvent arg0) {
			            	try {
						        Runtime.getRuntime().exec("java -jar simpleGedUpdateSystem.jar");
						        System.exit(0);
							} catch (Exception e) {
								e.printStackTrace();
								Dialog.showError( "Erreur", "Impossible de lancer l'assistant de mise à jour");
							}
			            }
			        })
			        .addNoButton(null)
			        .addCancelButton(null)
			        .build()
			        .show();
				}
			}
		});
		t.start();
	
	
		// The main window
		Application.launch(MainWindow.class, args);
	}

}
