package com.ged.ui.screens;

import java.awt.Frame;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Properties;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import com.ged.ui.MainWindow;
import com.tools.PropertiesHelper;


/**
 * Any software screen must inherits from this class
 */
public abstract class SoftwareScreen extends JPanel {

	private static final Logger logger = Logger.getLogger(SoftwareScreen.class);
	
	protected Properties properties;
	
	private static final long serialVersionUID = 1L;

	private WeakReference<MainWindow> mainWindow;
	
	/**
	 * Application screen list
	 */
	public enum Screen {
		WELCOME_SCEEN,
		SETTING_SCREEN,
		BROWSING_SCREEN,
		ADD_SCREEN,
		EDITION_SCREEN,
		SEARCHING_SCREEN,
		PLUGIN_MANAGEMENT_SCREEN,
		PLUGIN_OPTION_SCREEN,
		MESSAGE_SCREEN,
		LOCATION_MANAGEMENT_SCREEN
	}


	public SoftwareScreen(MainWindow mw) {
		super(
				new MigLayout(	
							"wrap",
							"[grow,fill,center]",
							"[grow,fill,center]"
				)
			);
		mainWindow = new WeakReference<MainWindow>(mw);
		properties = PropertiesHelper.getInstance().getProperties();
	}

	/**
	 * Show another screen in the central area, the previous central screen is lost
	 * 
	 * @param newCentralScreen
	 * 					The new screen to display
	 */
	public void gotoScreen(SoftwareScreen.Screen newCentralScreen) {
		mainWindow.get().setCentralScreen(newCentralScreen);
	}
	
	/**
	 * Push a new screen on this screen (screen stack)
	 */
	public void pushScreen(SoftwareScreen.Screen screen) {
		mainWindow.get().pushCentralScreen(screen);
	}
	
	/**
	 * When a screen has finished his work, he's pop and we're back to the previous screen
	 */
	public void finish() {
		mainWindow.get().popScreen();
	}
	
	/**
	 * Give an extra value to top screen
	 * 
	 * <key, value>
	 */
	public void putExtra(Map<String, Object> extra) {
		mainWindow.get().putExtraToTopScreen(extra);
	}
	
	/**
	 * Receive extra values
	 * 
	 * Should be override by child classes
	 */
	public void receiveExtraValue(Map<String, Object> extra) {
		logger.warn("Warning : the child screen doesn't take care about extra values");
	}
	
	/**
	 * Refresh the screen content
	 * 
	 * Should be override by child classes, or not
	 */
	public void refresh() {
	}
	
	/**
	 * Quit the application
	 */
	public void quit() {
		System.exit(0);
	}
	
	/**
	 * Refresh all screens in the screen stack
	 */
	public void refreshScreens() {
		mainWindow.get().refreshScreens();
	}
	
	/**
	 * Refresh the theme, the look & feel !
	 */
	public void refreshLookAndFeel() {
		mainWindow.get().refreshLookAndFeel();
	}
	
	/**
	 * Notify that's new message available
	 */
	public void notifyNewMessagesAvailable() {
		mainWindow.get().getSlideDock().markNewMessagesAvailable();
	}
	
	/**
	 * Notify that's NO new message available
	 */
	public void notifyNoNewMessagesAvailable() {
		mainWindow.get().getSlideDock().markNoNewMessagesAvailable();
	}
	
	/**
	 * Get the main window as frame
	 */
	public Frame getMainFrame() {
		return mainWindow.get();
	}
	
}

