package com.ged.ui.fxscreen.eventhandler;

import java.lang.ref.WeakReference;

import org.apache.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
		logger.warn("Not implement yet (SettingsScreenEventHandler.handle)");
	}


	@Override
	public void newFileSelected(FileChangedEventObject e) {
		logger.warn("Not implement yet (SettingsScreenEventHandler.newFileSelected)");
		
	}
	
}
