package com.ged.ui.fxwidgets;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import com.ged.ui.widgets.SimpleButton;
import com.tools.PropertiesHelper;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;

public class FxToolBar extends ToolBar {

	/**
	 * Back to home screen button
	 */
	private Button btnHome;
	
	/**
	 * Browse document library button
	 */
	private Button btnBrowse;
	
	/**
	 * Add a document in library button
	 */
	private Button btnAddDoc;
	
	/**
	 * Search document in library button
	 */
	private Button btnSearch;
	
	/**
	 * About popup button
	 */
	private Button btnAbout;
	
	/**
	 * Go to setting screen button
	 */
	private Button btnSettings;
	
	/**
	 * Plugin management
	 */
	private Button btnPluginManagement;
	
	/**
	 * Messages
	 */
	private Button btnMessages;
	
	/**
	 * Quit button
	 */
	private Button btnQuit;
	
	
	
	public FxToolBar() {
		
		instantiateWidgets();
		
		// fill the bar
		
		addElement(btnHome);

	    addElement(new Separator());
	    
	    addElement(btnBrowse);
	    addElement(btnAddDoc);
	    addElement(btnSearch);
	    addElement(btnPluginManagement);
	    
	    addElement(new Separator());
	    
	    addElement(btnMessages);
	    
	    addElement(new Separator());
	    
	    addElement(btnSettings);
	    addElement(btnAbout);
	    
	    addElement(new Separator());
	    
	    addElement(btnQuit);
	}
	
	
	/**
	 * Add a button an element the tool bar
	 */
	private void addElement(Node n) {
		getItems().add(n);
	}
	
	
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		Properties properties = PropertiesHelper.getInstance().getProperties();
		
		// create buttons
		btnHome  			= new Button(properties.getProperty("info_welcome")/*, controller*/);
		btnBrowse   		= new Button(properties.getProperty("info_browse")/*, controller*/);
		btnAddDoc			= new Button(properties.getProperty("info_add")/*, controller*/);
		btnSearch   		= new Button(properties.getProperty("info_search")/*, controller*/);
		btnSettings			= new Button(properties.getProperty("info_settings")/*, controller*/);
		btnAbout			= new Button(properties.getProperty("info_about") + properties.getProperty("APPLICATION_NAME")/*, controller*/);
		btnQuit				= new Button(properties.getProperty("quit")/*, controller*/);
		btnPluginManagement	= new Button(properties.getProperty("info_plugin_management")/*, controller*/);
		btnMessages			= new Button(properties.getProperty("info_messages")/*, controller*/);
		
		// define associated pictures
		Map<Button, String> associatedImages = new HashMap<>();
		associatedImages.put(btnHome, 				properties.getProperty("ico_home"));
		associatedImages.put(btnBrowse,				properties.getProperty("ico_library"));
		associatedImages.put(btnAddDoc,				properties.getProperty("ico_add_doc"));
		associatedImages.put(btnSearch,				properties.getProperty("ico_search"));
		associatedImages.put(btnSettings, 			properties.getProperty("ico_settings"));
		associatedImages.put(btnAbout,				properties.getProperty("ico_about"));
		associatedImages.put(btnQuit, 				properties.getProperty("ico_exit"));
		associatedImages.put(btnPluginManagement,	properties.getProperty("ico_plugin_management"));
		associatedImages.put(btnMessages,			properties.getProperty("ico_message"));
		
		// set pictures
		for (Map.Entry<Button, String> e : associatedImages.entrySet()) {
			//e.getKey().setIcon(e.getValue());
		}
	}
	
}
