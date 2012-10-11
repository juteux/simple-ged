package com.ged.ui.fxwidgets;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Duration;

import com.ged.ui.FxMainWindow;
import com.ged.ui.fxscreen.FxSoftwareScreen;
import com.ged.ui.fxwidgetcontrollers.ToolBarController;
import com.tools.PropertiesHelper;

public class FxToolBar extends FxSoftwareScreen {


	/**
	 * All buttons in slide dock are SlideDockButton
	 */
	private class ToolBarButton extends ImageView {
		
		
		/**
		 * Initial and "normal" button opacity
		 */
		private static final double INITIAL_OPACITY = 0.5;
		
		/**
		 * Current transition
		 */
		private FadeTransition currentTransition = null;
		
		
		public ToolBarButton(String toolTipText, ToolBarController controller) {
			super();
			
			setOpacity(INITIAL_OPACITY);

			//setTooltip(new Tooltip(toolTipText));
			
			final ToolBarButton self = this;
			
			setOnMouseEntered(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					if (currentTransition != null) {
						currentTransition.stop();
					}
					currentTransition = new FadeTransition(Duration.millis(1000), self);
					currentTransition.setFromValue(getOpacity());
					currentTransition.setToValue(1);
					currentTransition.play();
				}
			});
			
			setOnMouseExited(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					if (currentTransition != null) {
						currentTransition.stop();
					}
					currentTransition = new FadeTransition(Duration.millis(500), self);
					currentTransition.setFromValue(getOpacity());
					currentTransition.setToValue(INITIAL_OPACITY);
					currentTransition.play();
				}
			});
			
			setOnMouseClicked(controller);
		}
		
		public void setImage(String imageResource) {
			Image i = new Image(getClass().getResourceAsStream(imageResource));
			setSmooth(true);
			setFitWidth(30);
			setFitHeight(30);
			setImage(i);
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
		
		this.getStyleClass().add("toolbar");
		
		instantiateWidgets();
		
		// fill the bar
		
	    addElement(btnPluginManagement);
	    
	    addElement(btnMessages);
	    
	    addElement(btnSettings);
	    addElement(btnAbout);
	    
		HBox leftBox = new HBox();
		leftBox.setPadding(new Insets(5, 0, 0, 5));
		
		centralBox.setPadding(new Insets(5, 0, 0, 5));
		
		HBox rightBox = new HBox();
		rightBox.setPadding(new Insets(5, 0, 0, 5));
		
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
		
		centralBox = new HBox(20); // space between buttons
		
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
		associatedImages.put(btnBack, 				properties.getProperty("ico_toolbar_back_button"));
		associatedImages.put(btnSettings, 			properties.getProperty("ico_toolbar_pref"));
		associatedImages.put(btnAbout,				properties.getProperty("ico_toolbar_about"));
		associatedImages.put(btnPluginManagement,	properties.getProperty("ico_toolbar_plugins"));
		associatedImages.put(btnMessages,			properties.getProperty("ico_toolbar_message_off"));
		
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
