package com.ged.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.services.MessageService;
import com.ged.ui.screens.AddDocumentScreen;
import com.ged.ui.screens.BrowseLibraryScreen;
import com.ged.ui.screens.EditDocumentScreen;
import com.ged.ui.screens.LocationManagementScreen;
import com.ged.ui.screens.MessageScreen;
import com.ged.ui.screens.PluginManagementScreen;
import com.ged.ui.screens.PluginOptionEditionScreen;
import com.ged.ui.screens.SearchingScreen;
import com.ged.ui.screens.SettingsScreen;
import com.ged.ui.screens.SoftwareScreen;
import com.ged.ui.screens.WelcomeScreen;
import com.ged.ui.screens.SoftwareScreen.Screen;
import com.ged.ui.widgets.SlideDock;
import com.managers.ImageManager;
import com.tools.FileHelper;
import com.tools.PropertiesHelper;


/**
 * The main application window 
 * @author xavier
 *
 */
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(MainWindow.class);
	
	/**
	 * Default application width
	 */
	public static final int APP_WIDTH = 900;
	
	/**
	 * Default application height
	 */
	public static final int APP_HEIGHT = 600;
	
	/**
	 * Default application size
	 */
	public static final Dimension APP_DIM = new Dimension(APP_WIDTH, APP_HEIGHT);
	
	/**
	 * Loaded screens, you can see this as a stack with the more recent screen at the top (last element)
	 */
	private List<SoftwareScreen> screens;
	
	/**
	 * The current application screen, should be the last one of the screen list
	 */
	private SoftwareScreen currentCentralScreen = null;
	
	/**
	 * The slide dock
	 */
	private SlideDock slideDock;
	
	
	protected Properties properties;
	
	
	
	public MainWindow() {
		properties = PropertiesHelper.getInstance().getProperties();
		
		setTitle(properties.getProperty("APPLICATION_NAME"));
		setIconImage(ImageManager.getImage(properties.getProperty("ico_ico")));
		setSize(APP_DIM);
		setMinimumSize(APP_DIM);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		refreshLookAndFeel();
		
		setLayout(new BorderLayout());
		
		// dock
		slideDock = new SlideDock(this);
		getContentPane().add(slideDock, BorderLayout.WEST);
	
		if (MessageService.thereIsUnreadMessages()) {
			slideDock.markNewMessagesAvailable();
		}
		
		// central area
		screens = new ArrayList<SoftwareScreen>();
		
		// default central screen
		setCentralScreen(SoftwareScreen.Screen.WELCOME_SCEEN);
	}
	
	
	/**
	 * Define the central screen, previous central screen will be lost
	 */
	public void setCentralScreen(SoftwareScreen.Screen newCentralScreen) {
		
		// When library root isn't valid, always return on settings screen
		if ( ! FileHelper.folderExists(Profile.getInstance().getLibraryRoot())) {
			pushCentralScreen(SoftwareScreen.Screen.SETTING_SCREEN);
			return;
		}
		// ---
		
		if ( currentCentralScreen != null ) { // we've got a previous central screen to remove
			getContentPane().remove(currentCentralScreen);
		}
		
		screens.clear();
		pushCentralScreen(newCentralScreen);
	}
	
	
	/**
	 * Push a new central screen on screens stack
	 */
	public void pushCentralScreen(SoftwareScreen.Screen screen) {
		if ( currentCentralScreen != null ) { // we've got a previous central screen to remove
			getContentPane().remove(currentCentralScreen);
		}
		
		currentCentralScreen = getScreen(screen);
		screens.add(currentCentralScreen);

		getContentPane().add(currentCentralScreen, BorderLayout.CENTER);

		getContentPane().repaint();
		getContentPane().validate(); // update UI
	}
	
	
	/**
	 * Pop the central screen
	 * 
	 * If no screens left, we display the welcome screen
	 */
	public void popScreen() {
		if ( currentCentralScreen != null ) { // we've got a previous central screen to remove
			getContentPane().remove(currentCentralScreen);
		}
		screens.remove(currentCentralScreen);

		if (screens.isEmpty()) {
			setCentralScreen(Screen.WELCOME_SCEEN);
		}
		else {
			currentCentralScreen = screens.get(screens.size() - 1);
			getContentPane().add(currentCentralScreen, BorderLayout.CENTER);
			getContentPane().repaint();
			getContentPane().validate(); // update UI
		}
	}
	
	
	/**
	 * Return the wanted screen, according to the given value
	 */
	private SoftwareScreen getScreen(SoftwareScreen.Screen requestedScreen) {
		
		switch (requestedScreen) {
		
		case SETTING_SCREEN :
			return new SettingsScreen(this);
		
		case BROWSING_SCREEN :
			return new BrowseLibraryScreen(this);
			
		case ADD_SCREEN :
			return new AddDocumentScreen(this);
			
		case EDITION_SCREEN :
			return new EditDocumentScreen(this);
			
		case SEARCHING_SCREEN :
			return new SearchingScreen(this);
			
		case PLUGIN_OPTION_SCREEN :
			return new PluginOptionEditionScreen(this);
			
		case PLUGIN_MANAGEMENT_SCREEN :
			return new PluginManagementScreen(this);
			
		case MESSAGE_SCREEN :
			return new MessageScreen(this);
			
		case LOCATION_MANAGEMENT_SCREEN :
			return new LocationManagementScreen(this);
			
		case WELCOME_SCEEN :
		default:
			return new WelcomeScreen(this);
			
		}
	}
	
	/**
	 * Give extra values to top screen (current screen)
	 */
	public void putExtraToTopScreen(Map<String, Object> extra) {
		currentCentralScreen.receiveExtraValue(extra);
	}
	
	/**
	 * Refresh screens in stack
	 */
	public void refreshScreens() {
		logger.info("refreshing screens...");
		for (SoftwareScreen ss : screens) {
			ss.refresh();
		}
	}
	
	/**
	 * Refresh the application look & feel, according to the value in profile
	 */
	public void refreshLookAndFeel() {
		logger.info("refreshing look & feel");
		for (UIManager.LookAndFeelInfo i : UIManager.getInstalledLookAndFeels()) {
			if (i.getName().equals(Profile.getInstance().getThemeName())) {
				try {
					UIManager.setLookAndFeel(i.getClassName());
				} catch (ClassNotFoundException e1) {e1.printStackTrace();
				} catch (InstantiationException e1) {e1.printStackTrace();
				} catch (IllegalAccessException e1) {e1.printStackTrace();
				} catch (UnsupportedLookAndFeelException e1) {e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(this);
				
				break;
			}
		}
	}


	public SlideDock getSlideDock() {
		return slideDock;
	}
}

