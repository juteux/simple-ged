package com.ged.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.ui.screens.SettingsScreen;
import com.ged.ui.screens.SoftwareScreen;
import com.tools.listeners.FileChangedListener;
import com.tools.ui.FileChangedEventObject;

/**
 * This is the controller of the SettingsScreen
 * 
 * @author xavier
 *
 */
public class SettingsScreenController implements ActionListener, FileChangedListener {

	private static final Logger logger = Logger.getLogger(SettingsScreenController.class);
	
	/**
	 * The controlled dock
	 */
	private SettingsScreen settingScreen;
	
	
	public SettingsScreenController(SettingsScreen ss) {
		settingScreen = ss;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == settingScreen.getBtnReturn()) {
			settingScreen.gotoScreen(SoftwareScreen.Screen.WELCOME_SCEEN);
		}
		else if (e.getSource() == settingScreen.getBtnSave()) {
			
			Profile p = Profile.getInstance();
			p.setDocumentLibraryRoot(settingScreen.getLibraryLocationSelector().getFilePath());
			p.setTheme((String) settingScreen.getComboTheme().getSelectedItem());
			p.setDefaultPrinter((String) settingScreen.getComboPrinter().getSelectedItem());
			p.commitChanges();
			
			settingScreen.refreshLookAndFeel();
			
			settingScreen.getBtnSave().setEnabled(false);
		}
		else if (e.getSource() == settingScreen.getComboTheme()) {
			settingScreen.getBtnSave().setEnabled(true);
		}
		else if (e.getSource() == settingScreen.getComboPrinter()) {
			settingScreen.getBtnSave().setEnabled(true);
		}
		else {
			logger.warn("Not implemented yet, see SettingsScreenController.actionPerformed");
		}
	}

	@Override
	public void newFileSelected(FileChangedEventObject e) {
		settingScreen.getBtnSave().setEnabled(e.isValid());
	}



}
