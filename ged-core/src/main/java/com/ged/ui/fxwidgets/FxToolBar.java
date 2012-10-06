package com.ged.ui.fxwidgets;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.ged.ui.fxcontrollers.ToolBarController;
import com.tools.PropertiesHelper;

public class FxToolBar extends ToolBar {

	
	/**
	 * The software properties
	 */
	Properties properties = PropertiesHelper.getInstance().getProperties();
	
	
	/**
	 * All buttons in slide dock are SlideDockButton
	 */
	private class ToolBarButton extends Button {
		
		public ToolBarButton(String toolTipText, ToolBarController controller) {
			super();
			
			setTooltip(new Tooltip(toolTipText));
			
			setOnAction(controller);
		}
		
		public void setImage(String imageResource) {
			Image i = new Image(getClass().getResourceAsStream(imageResource));
			ImageView iv = new ImageView(i);
			iv.setSmooth(true);
			iv.setFitWidth(30);
			iv.setFitHeight(30);
			this.setGraphic(iv);
		}
	}
	
	
	/**
	 * Back to home screen button
	 */
	private ToolBarButton btnHome;
	
	/**
	 * Browse document library button
	 */
	private ToolBarButton btnBrowse;
	
	/**
	 * Add a document in library button
	 */
	private ToolBarButton btnAddDoc;
	
	/**
	 * Search document in library button
	 */
	private ToolBarButton btnSearch;
	
	/**
	 * About popup button
	 */
	private ToolBarButton btnAbout;
	
	/**
	 * Go to setting screen button
	 */
	private ToolBarButton btnSettings;
	
	/**
	 * Plugin management
	 */
	private ToolBarButton btnPluginManagement;
	
	/**
	 * Messages
	 */
	private ToolBarButton btnMessages;
	
	/**
	 * Quit button
	 */
	private ToolBarButton btnQuit;
	
	
	
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
		
		ToolBarController controller = new ToolBarController(this);
		
		Properties properties = PropertiesHelper.getInstance().getProperties();
		
		// create buttons
		btnHome  			= new ToolBarButton(properties.getProperty("info_welcome"), controller);
		btnBrowse   		= new ToolBarButton(properties.getProperty("info_browse"), controller);
		btnAddDoc			= new ToolBarButton(properties.getProperty("info_add"), controller);
		btnSearch   		= new ToolBarButton(properties.getProperty("info_search"), controller);
		btnSettings			= new ToolBarButton(properties.getProperty("info_settings"), controller);
		btnAbout			= new ToolBarButton(properties.getProperty("info_about") + properties.getProperty("APPLICATION_NAME"), controller);
		btnQuit				= new ToolBarButton(properties.getProperty("quit"), controller);
		btnPluginManagement	= new ToolBarButton(properties.getProperty("info_plugin_management"), controller);
		btnMessages			= new ToolBarButton(properties.getProperty("info_messages"), controller);
		
		// define associated pictures
		Map<ToolBarButton, String> associatedImages = new HashMap<>();
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
		for (Map.Entry<ToolBarButton, String> e : associatedImages.entrySet()) {
			e.getKey().setImage(e.getValue());
		}
	}


	public ToolBarButton getBtnHome() {
		return btnHome;
	}


	public ToolBarButton getBtnBrowse() {
		return btnBrowse;
	}


	public ToolBarButton getBtnAddDoc() {
		return btnAddDoc;
	}


	public ToolBarButton getBtnSearch() {
		return btnSearch;
	}


	public ToolBarButton getBtnAbout() {
		return btnAbout;
	}


	public ToolBarButton getBtnSettings() {
		return btnSettings;
	}


	public ToolBarButton getBtnPluginManagement() {
		return btnPluginManagement;
	}


	public ToolBarButton getBtnMessages() {
		return btnMessages;
	}


	public ToolBarButton getBtnQuit() {
		return btnQuit;
	}
	
}
