package com.ged.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.ged.connector.plugins.SimpleGedPluginProperty;
import com.ged.dao.PluginDAO;
import com.ged.plugins.PluginManagementInformations;
import com.ged.plugins.PluginManager;
import com.ged.ui.listeners.LibraryListener;
import com.ged.ui.screens.PluginOptionEditionScreen;
import com.tools.PropertiesHelper;

/**
 * This class is the controller for the PluginOptionEditionScreen
 * 
 * @author xavier
 *
 */
public class PluginOptionEditionScreenController implements ActionListener, LibraryListener, KeyListener {
	
	private static final Logger logger = Logger.getLogger(PluginOptionEditionScreenController.class);
	
	/**
	 * The controlled screen
	 */
	private PluginOptionEditionScreen pluginOptionEditionScreen;
	
	protected Properties properties;
	
	
	public PluginOptionEditionScreenController(PluginOptionEditionScreen pluginOptionEditionScreen) {
		this.pluginOptionEditionScreen = pluginOptionEditionScreen;
		this.properties = PropertiesHelper.getInstance().getProperties();
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
				
		if (arg0.getSource() == pluginOptionEditionScreen.getBtnReturn()) {
			
			int option = JOptionPane.showConfirmDialog(null, properties.getProperty("dont_turn_on_plugin"), properties.getProperty("stop_activation"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (option == JOptionPane.YES_OPTION) {
				pluginOptionEditionScreen.refreshScreens();
				pluginOptionEditionScreen.finish();
			}
			
		} else if (arg0.getSource() == pluginOptionEditionScreen.getBtnSave()) {
			
			PluginManagementInformations pmi = new PluginManagementInformations();
			
			pmi.setDayOfMonthForUpdate((Integer) pluginOptionEditionScreen.getComboDayOfMonthForUpdate().getSelectedItem());
			pmi.setDestinationDirectory(pluginOptionEditionScreen.getLibraryView().getSelectedPath());
			pmi.setDestinationFilePattern(pluginOptionEditionScreen.getFieldNamePattern().getText().trim());
			pmi.setFileName(pluginOptionEditionScreen.getPluginFileName());
			pmi.setIntervalBetweenUpdates((Integer) pluginOptionEditionScreen.getComboIntervalBetweenUpdateInMonth().getSelectedItem());
			
			List<SimpleGedPluginProperty> properties = new ArrayList<SimpleGedPluginProperty>();
			
			for (Entry<SimpleGedPluginProperty, JTextField> e : pluginOptionEditionScreen.getPropertiesFieldsMap().entrySet()) {
				e.getKey().setPropertyValue(e.getValue().getText());
				properties.add(e.getKey());
			}
			pmi.setPluginProperties(properties);
			
			PluginDAO.addPlugin(pmi);
			PluginManager.launchPluginUpdate(pluginOptionEditionScreen);

			JOptionPane.showMessageDialog(null, this.properties.getProperty("plugin_is_activated"), this.properties.getProperty("information"), JOptionPane.INFORMATION_MESSAGE);
			pluginOptionEditionScreen.refreshScreens();
			pluginOptionEditionScreen.finish();
			
		} else {
			logger.warn("Not implemented yet, see PluginOptionEditionScreenController.actionPerformed");
		}
	}


	@Override
	public void selectionChanged(String relativeFilePathOfNewSelection) {
		checkValidity();
	}

	/**
	 * Check if currents values are valid, if true, the save button is available
	 */
	public void checkValidity() {
		boolean valid = true;
		
		if ( ! pluginOptionEditionScreen.getLibraryView().getController().isElementSelected() ) {
			valid = false;
		}
		
		if ( pluginOptionEditionScreen.getFieldNamePattern().getText().trim().isEmpty() ) {
			valid = false;
		}

		for (Entry<SimpleGedPluginProperty, JTextField> e : pluginOptionEditionScreen.getPropertiesFieldsMap().entrySet()) {
			if (e.getValue().getText().isEmpty()) {
				valid = false;
				break;
			}
		}
		
		pluginOptionEditionScreen.getBtnSave().setEnabled(valid);
	}


	@Override
	public void keyPressed(KeyEvent arg0) {
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		checkValidity();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}


	@Override
	public void releaseOpenedFiles() {
		// TODO Auto-generated method stub
		
	}
	
}
