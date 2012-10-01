package com.ged.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

import com.ged.ui.screens.MessageScreen;
import com.ged.ui.screens.SoftwareScreen;

/**
 * The message screen controller
 * 
 * @author xavier
 *
 */
public class MessageScreenController implements ActionListener {

	private static final Logger logger = Logger.getLogger(MessageScreenController.class);
	
	/**
	 * The controlled object
	 */
	private MessageScreen messageScreen;

	
	public MessageScreenController(MessageScreen messageScreen) {
		this.messageScreen = messageScreen;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == messageScreen.getBtnReturn()) {
			messageScreen.gotoScreen(SoftwareScreen.Screen.WELCOME_SCEEN);
		}
		else {
			logger.warn("Not implemented yet, see MessageScreenController.actionPerformed");
		}
	}
	
}
