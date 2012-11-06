package com.ged.ui.fxscreen.eventhandler;

import java.lang.ref.WeakReference;

import org.apache.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import com.ged.Profile;
import com.ged.ui.fxscreen.SettingsScreen;
import com.tools.listeners.FileChangedListener;
import com.tools.ui.FileChangedEventObject;

/**
 * 
 * This is the settings screen event handler
 * 
 * @author xavier
 *
 */
public class SettingsScreenEventHandler implements EventHandler<ActionEvent>, FileChangedListener {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(SettingsScreenEventHandler.class);
	
	/**
	 * The watched screen
	 */
	private WeakReference<SettingsScreen> settingsScreen;
	
	
	public SettingsScreenEventHandler(SettingsScreen settingsScreen) {
		this.settingsScreen = new WeakReference<>(settingsScreen);
	}


	@Override
	public void handle(ActionEvent action) {

		if (action.getSource() == settingsScreen.get().getBtnSubmit()) {
			Profile p = Profile.getInstance();
			p.setDocumentLibraryRoot(settingsScreen.get().getLibraryLocationSelector().getFilePath());
			p.setDefaultPrinter((String) settingsScreen.get().getComboPrinter().getSelectionModel().getSelectedItem());
			p.commitChanges();

			settingsScreen.get().getBtnSubmit().setDisable(true);
		}
		else if (action.getSource() == settingsScreen.get().getComboPrinter()) {
			settingsScreen.get().getBtnSubmit().setDisable(false);
		}
		else {
			logger.warn("Not implement yet (SettingsScreenEventHandler.handle)");
		}
	}


	@Override
	public void newFileSelected(FileChangedEventObject e) {
		settingsScreen.get().getBtnSubmit().setDisable(! e.isValid());
	}
	
}
