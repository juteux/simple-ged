package com.ged.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;


import com.ged.ui.AboutPopup;
import com.ged.ui.screens.SoftwareScreen;
import com.ged.ui.widgets.SlideDock;

/**
 * Controller for slide dock
 */
public class SlideDockController implements ActionListener {
	
	private static final Logger logger = Logger.getLogger(SlideDockController.class);
	
	/**
	 * The controlled dock
	 */
	private SlideDock slideDock;
	
	
	public SlideDockController(SlideDock sd) {
		slideDock = sd;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
			
		if (e.getSource() == slideDock.getBtnHome()) {
			slideDock.gotoScreen(SoftwareScreen.Screen.WELCOME_SCEEN);
		}
		else if (e.getSource() == slideDock.getBtnSettings()) {
			slideDock.gotoScreen(SoftwareScreen.Screen.SETTING_SCREEN);
		}
		else if (e.getSource() == slideDock.getBtnBrowse()) {
			slideDock.gotoScreen(SoftwareScreen.Screen.BROWSING_SCREEN);
		}
		else if (e.getSource() == slideDock.getBtnAddDoc()) {
			slideDock.gotoScreen(SoftwareScreen.Screen.ADD_SCREEN);
		}
		else if (e.getSource() == slideDock.getBtnSearch()) {
			slideDock.gotoScreen(SoftwareScreen.Screen.SEARCHING_SCREEN);
		}
		else if (e.getSource() == slideDock.getBtnAbout()) {
			new AboutPopup().setVisible(true);
		}
		else if (e.getSource() == slideDock.getBtnQuit()) {
			slideDock.quit();
		}
		else if (e.getSource() == slideDock.getBtnPluginManagement()) {
			slideDock.gotoScreen(SoftwareScreen.Screen.PLUGIN_MANAGEMENT_SCREEN);
		}
		else if (e.getSource() == slideDock.getBtnMessages()) {
			slideDock.gotoScreen(SoftwareScreen.Screen.MESSAGE_SCREEN);
		}
		else {
			logger.warn("Not implemented yet, see SlideDockControler.actionPerformed");
		}
	}
	
}

