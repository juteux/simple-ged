package com.ged.ui.fxwidgets;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import com.ged.ui.FxMainWindow;
import com.ged.ui.fxscreen.FxSoftwareScreen;
import com.ged.ui.fxwidgetcontrollers.ToolBarController;
import com.tools.PropertiesHelper;

public class FxToolBar extends FxSoftwareScreen {


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
	 * Back to previous screen
	 */
	private ToolBarButton btnBack;
	
	
	/**
	 * The box which contains action buttons
	 */
	private HBox centralBox;
	
	public FxToolBar(FxMainWindow w) {
		super(w);
		
		instantiateWidgets();
		
		// fill the bar
		
	    addElement(btnPluginManagement);
	    
	    addElement(new Separator());
	    
	    addElement(btnMessages);
	    
	    addElement(new Separator());
	    
	    addElement(btnSettings);
	    addElement(btnAbout);
	    
		HBox leftBox = new HBox();
		HBox rightBox = new HBox();
		
		HBox.setHgrow(leftBox, Priority.ALWAYS);
		HBox.setHgrow(rightBox, Priority.ALWAYS);
		
		leftBox.getChildren().add(btnBack);
		
		this.getChildren().addAll(leftBox, centralBox, rightBox);
	}
	
	
	/**
	 * Add a button an element the tool bar
	 */
	private void addElement(Node n) {
		centralBox.getChildren().add(n);
	}
	
	
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		centralBox = new HBox();
		
		ToolBarController controller = new ToolBarController(this);
		
		Properties properties = PropertiesHelper.getInstance().getProperties();
		
		// create buttons
		btnBack  			= new ToolBarButton(properties.getProperty("back"), controller);
		btnSettings			= new ToolBarButton(properties.getProperty("info_settings"), controller);
		btnAbout			= new ToolBarButton(properties.getProperty("info_about") + properties.getProperty("APPLICATION_NAME"), controller);
		btnPluginManagement	= new ToolBarButton(properties.getProperty("info_plugin_management"), controller);
		btnMessages			= new ToolBarButton(properties.getProperty("info_messages"), controller);
		
		// define associated pictures
		Map<ToolBarButton, String> associatedImages = new HashMap<>();
		associatedImages.put(btnSettings, 			properties.getProperty("ico_settings"));
		associatedImages.put(btnAbout,				properties.getProperty("ico_about"));
		associatedImages.put(btnPluginManagement,	properties.getProperty("ico_plugin_management"));
		associatedImages.put(btnMessages,			properties.getProperty("ico_message"));
		
		// set pictures
		for (Map.Entry<ToolBarButton, String> e : associatedImages.entrySet()) {
			e.getKey().setImage(e.getValue());
		}
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


	public ToolBarButton getBtnBack() {
		return btnBack;
	}
	
	
}
