package com.ged.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

import com.ged.ui.screens.WelcomeScreen;
import com.ged.ui.screens.SoftwareScreen.Screen;

/**
 * The welcome screen controller
 */
public class WelcomeScreenController implements ActionListener {
	
	private static final Logger logger = Logger.getLogger(WelcomeScreenController.class);
	
	/**
	 * Controlled screen
	 */
	private WelcomeScreen welcomeScreen;
	
	
	public WelcomeScreenController(WelcomeScreen ws) {
		welcomeScreen = ws;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == welcomeScreen.getBtnBrowse()) {
			welcomeScreen.gotoScreen(Screen.BROWSING_SCREEN);
		}
		else if (e.getSource() == welcomeScreen.getBtnAdd()) {
			welcomeScreen.gotoScreen(Screen.ADD_SCREEN);
		}
		else if (e.getSource() == welcomeScreen.getBtnSearch()) {
			welcomeScreen.gotoScreen(Screen.SEARCHING_SCREEN);
		}
		else if (e.getSource() == welcomeScreen.getBtnPluginsManagement()) {
			welcomeScreen.gotoScreen(Screen.PLUGIN_MANAGEMENT_SCREEN);		
		}
		else {
			logger.warn("Not implemented yet, see WelcomeScreenController.actionPerformed");
		}
	}
	
}
