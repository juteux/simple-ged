package com.ged.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.ui.fxscreen.FxSoftwareScreen;
import com.ged.ui.fxscreen.LibraryViewScreen;
import com.ged.ui.fxwidgets.FxDocumentInfoViewer;
import com.ged.ui.fxwidgets.FxLibraryView;
import com.ged.ui.fxwidgets.FxToolBar;
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
import com.tools.FileHelper;
import com.tools.PropertiesHelper;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 

/**
 * The new main window, with javafx power !
 * 
 * @author xavier
 *
 */
public class FxMainWindow extends Application {

	
	private static final Logger logger = Logger.getLogger(MainWindow.class);
	
	/**
	 * Default application width
	 */
	public static final int APP_WIDTH = 800;
	
	/**
	 * Default application height
	 */
	public static final int APP_HEIGHT = 600;
	
	
	/**
	 * Proporties loaded from configuration files
	 */
	protected Properties properties;
	
	/**
	 * Loaded screens, you can see this as a stack with the more recent screen at the top (last element)
	 */
	private List<FxSoftwareScreen> screens;
	
	/**
	 * The current application screen, should be the last one of the screen list
	 */
	private FxSoftwareScreen currentCentralScreen = null;
	
	/**
	 * Central screen node
	 */
	private BorderPane mainLayout;
	
	
    @Override
    public void start(Stage primaryStage) {
    	
    	properties = PropertiesHelper.getInstance().getProperties();
    	screens = new ArrayList<>();
    	
        primaryStage.setTitle(properties.getProperty("APPLICATION_NAME"));
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        
        ToolBar toolBar = new FxToolBar(); 
        int height = 40;
        toolBar.setPrefHeight(height);
        toolBar.setMinHeight(height);
        toolBar.setMaxHeight(height);
        
        mainLayout = new BorderPane();
        mainLayout.setTop(toolBar);
        

        Scene scene = new Scene(mainLayout, APP_WIDTH, APP_HEIGHT);
        scene.getStylesheets().add("templates/style.css");
        
        primaryStage.setScene(scene);
        
        primaryStage.show();
        
		// default central screen
		setCentralScreen(FxSoftwareScreen.Screen.BROWSING_SCREEN);
    }
    

	/**
	 * Define the central screen, previous central screen will be lost
	 */
 
	public void setCentralScreen(FxSoftwareScreen.Screen newCentralScreen) {
		
		// When library root isn't valid, always return on settings screen
		if ( ! FileHelper.folderExists(Profile.getInstance().getLibraryRoot())) {
			pushCentralScreen(FxSoftwareScreen.Screen.SETTING_SCREEN);
			return;
		}
		// ---
		
		/*
		if ( currentCentralScreen != null ) { // we've got a previous central screen to remove
			mainLayout.getCenter().remove(currentCentralScreen);
		}
		*/
		
		screens.clear();
		pushCentralScreen(newCentralScreen);
	}
	
	
	/**
	 * Push a new central screen on screens stack
	 */
	public void pushCentralScreen(FxSoftwareScreen.Screen screen) {
		
		logger.info("Pushing a central screen");
		
		/*
		if ( currentCentralScreen != null ) { // we've got a previous central screen to remove
			mainLayout.getChildren().remove(currentCentralScreen);
		}
		*/
		
		currentCentralScreen = getScreen(screen);
		screens.add(currentCentralScreen);

		mainLayout.setCenter(currentCentralScreen);
	}
	
	
	/**
	 * Pop the central screen
	 * 
	 * If no screens left, we display the welcome screen
	 */
	public void popScreen() {
		
		/*
		if ( currentCentralScreen != null ) { // we've got a previous central screen to remove
			getContentPane().remove(currentCentralScreen);
		}
		*/
		
		screens.remove(currentCentralScreen);

		if (screens.isEmpty()) {
			setCentralScreen(FxSoftwareScreen.Screen.BROWSING_SCREEN);
		}
		else {
			currentCentralScreen = screens.get(screens.size() - 1);
			
			mainLayout.setCenter(currentCentralScreen);
		}
	}
	
	
	/**
	 * Return the wanted screen, according to the given value
	 */
	private FxSoftwareScreen getScreen(FxSoftwareScreen.Screen requestedScreen) {
		
		switch (requestedScreen) {
		/*
		case SETTING_SCREEN :
			return new SettingsScreen(this);
	
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
		*/
		case BROWSING_SCREEN :
		default:
			return new LibraryViewScreen(this);
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
		for (FxSoftwareScreen ss : screens) {
			ss.refresh();
		}
	}

}
