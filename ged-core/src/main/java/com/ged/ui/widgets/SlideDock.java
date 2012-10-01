package com.ged.ui.widgets;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import com.ged.Constants;
import com.ged.ui.MainWindow;
import com.ged.ui.controllers.SlideDockController;
import com.ged.ui.screens.SoftwareScreen;

/**
 * The slide dock contains quick shortcut
 */
public class SlideDock extends SoftwareScreen { // I know it's not really a software screen... But it's easier to do that (for screen swap)
	
	/**
	 * All buttons in slide dock are SlideDockButton
	 */
	private class SlideDockButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public SlideDockButton(String toolTipText, SlideDockController controller) {
			super();
			
			setIconSize(new Dimension(30, 30));
			setToolTipText(toolTipText);
			//setMaximumSize(new Dimension(32, 32));
			
			addActionListener(controller);
		}
	}
	
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Back to home screen button
	 */
	private SimpleButton btnHome;
	
	/**
	 * Browse document library button
	 */
	private SimpleButton btnBrowse;
	
	/**
	 * Add a document in library button
	 */
	private SimpleButton btnAddDoc;
	
	/**
	 * Search document in library button
	 */
	private SimpleButton btnSearch;
	
	/**
	 * About popup button
	 */
	private SimpleButton btnAbout;
	
	/**
	 * Go to setting screen button
	 */
	private SimpleButton btnSettings;
	
	/**
	 * Plugin management
	 */
	private SimpleButton btnPluginManagement;
	
	/**
	 * Messages
	 */
	private SimpleButton btnMessages;
	
	/**
	 * Quit button
	 */
	private SimpleButton btnQuit;

	
	public SlideDock(MainWindow mw) {
		super(mw);
		instantiateWidgets();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//setLayout(new FlowLayout(FlowLayout.TRAILING, 20, 30));
		
		// fill the bar
		addSeparator();
		
	    add(btnHome);

	    addSeparator();
	    
	    add(btnBrowse);
	    add(btnAddDoc);
	    add(btnSearch);
	    add(btnPluginManagement);
	    
	    addSeparator();
	    
	    add(btnMessages);
	    
	    addSeparator();
	    
	    add(btnSettings);
	    add(btnAbout);
	    
	    addSeparator();
	    
	    add(btnQuit);
	}
	
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		SlideDockController controller = new SlideDockController(this);
		
		// create buttons
		btnHome  			= new SlideDockButton(properties.getProperty("info_welcome"), controller);
		btnBrowse   		= new SlideDockButton(properties.getProperty("info_browse"), controller);
		btnAddDoc			= new SlideDockButton(properties.getProperty("info_add"), controller);
		btnSearch   		= new SlideDockButton(properties.getProperty("info_search"), controller);
		btnSettings			= new SlideDockButton(properties.getProperty("info_settings"), controller);
		btnAbout			= new SlideDockButton(properties.getProperty("info_about") + Constants.applicationName, controller);
		btnQuit				= new SlideDockButton(properties.getProperty("quit"), controller);
		btnPluginManagement	= new SlideDockButton(properties.getProperty("info_plugin_management"), controller);
		btnMessages			= new SlideDockButton(properties.getProperty("info_messages"), controller);
		
		// define associated pictures
		Map<SimpleButton, String> associatedImages = new HashMap<SimpleButton, String>();
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
		for (Map.Entry<SimpleButton, String> e : associatedImages.entrySet()) {
			e.getKey().setIcon(e.getValue());
		}

	}
	
	
	/**
	 * Update UI because there is new messages available
	 */
	public void markNewMessagesAvailable() {
		btnMessages.setIcon(properties.getProperty("ico_message_new"));
	}

	/**
	 * Update UI because there is NO new messages available
	 */
	public void markNoNewMessagesAvailable() {
		btnMessages.setIcon(properties.getProperty("ico_message"));
	}
	
	
	/**
	 * Add an empty space between two buttons groups
	 */
	private void addSeparator() {
		add(new JLabel(" "));
	}

	
	/**
	 * @return the btnHome
	 */
	public SimpleButton getBtnHome() {
		return btnHome;
	}

	/**
	 * @return the btnBrowse
	 */
	public SimpleButton getBtnBrowse() {
		return btnBrowse;
	}

	/**
	 * @return the btnAddDoc
	 */
	public SimpleButton getBtnAddDoc() {
		return btnAddDoc;
	}

	/**
	 * @return the btnSearch
	 */
	public SimpleButton getBtnSearch() {
		return btnSearch;
	}

	/**
	 * @return the btnAbout
	 */
	public SimpleButton getBtnAbout() {
		return btnAbout;
	}

	/**
	 * @return the btnSettings
	 */
	public SimpleButton getBtnSettings() {
		return btnSettings;
	}

	/**
	 * @return the btnQuit
	 */
	public SimpleButton getBtnQuit() {
		return btnQuit;
	}


	public SimpleButton getBtnPluginManagement() {
		return btnPluginManagement;
	}


	public SimpleButton getBtnMessages() {
		return btnMessages;
	}

}
