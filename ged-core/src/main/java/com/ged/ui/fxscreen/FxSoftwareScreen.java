package com.ged.ui.fxscreen;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Properties;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

import com.ged.ui.FxMainWindow;
import com.tools.PropertiesHelper;

/**
 * 
 * Any software screen must inherits from this class
 * 
 * @author xavier
 * 
 */
public abstract class FxSoftwareScreen extends HBox {

	private static final Logger logger = Logger.getLogger(FxSoftwareScreen.class);

	protected Properties properties;

	private WeakReference<FxMainWindow> mainWindow;

	/**
	 * Application screen list
	 */
	public enum Screen {
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

	public FxSoftwareScreen(FxMainWindow mw) {
		mainWindow = new WeakReference<FxMainWindow>(mw);
		properties = PropertiesHelper.getInstance().getProperties();
	}

	/**
	 * Show another screen in the central area, the previous central screen is lost
	 * 
	 * @param newCentralScreen
	 *            The new screen to display
	 */
	public void gotoScreen(FxSoftwareScreen.Screen newCentralScreen) {
		mainWindow.get().setCentralScreen(newCentralScreen);
	}

	/**
	 * Push a new screen on this screen (screen stack)
	 */
	public void pushScreen(FxSoftwareScreen.Screen screen) {
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
	 * Notify that's new message available
	 */
	// public void notifyNewMessagesAvailable() {
	// mainWindow.get().getSlideDock().markNewMessagesAvailable();
	// }

	/**
	 * Notify that's NO new message available
	 */
	// public void notifyNoNewMessagesAvailable() {
	// mainWindow.get().getSlideDock().markNoNewMessagesAvailable();
	// }

	/**
	 * Get the main window stage
	 */
	public Stage getMainStage() {
		return mainWindow.get().getMainStage();
	}

}
