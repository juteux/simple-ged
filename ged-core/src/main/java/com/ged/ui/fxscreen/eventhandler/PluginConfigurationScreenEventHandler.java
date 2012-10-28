package com.ged.ui.fxscreen.eventhandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.ged.connector.plugins.SimpleGedPluginProperty;
import com.ged.dao.PluginDAO;
import com.ged.models.GedPlugin;
import com.ged.plugins.PluginManager;
import com.ged.services.PluginService;
import com.ged.ui.fxscreen.PluginConfigurationScreen;
import com.ged.ui.screens.PluginOptionEditionScreen;
import com.tools.PropertiesHelper;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


/**
 * 
 * This class is the event handler for PluginConfigurationScreen
 * 
 * @author xavier
 *
 */
public class PluginConfigurationScreenEventHandler implements EventHandler<Event> {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(PluginConfigurationScreenEventHandler.class);
	
	/**
	 * Properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	/**
	 * The watched screen
	 */
	private WeakReference<PluginConfigurationScreen> pluginConfigurationScreen;
	
	
	public PluginConfigurationScreenEventHandler(PluginConfigurationScreen pluginConfigurationScreen) {
		this.pluginConfigurationScreen = new WeakReference<>(pluginConfigurationScreen);
	}
	
	
	/**
	 * Front controller, distribute events according to there type
	 */
	@Override
	public void handle(Event event) {
		
		if (event instanceof ActionEvent) {
			actionEventDelegate((ActionEvent) event);
		}
		else if (event instanceof KeyEvent) {
			keyEventDelegate((KeyEvent) event);
		}
		else {
			logger.warn("Unknow event type. Won't dispatch");
		}
	}

	
	private void actionEventDelegate(ActionEvent e) {
		
		if (e.getSource() == pluginConfigurationScreen.get().getSave()) {
			
			GedPlugin p = pluginConfigurationScreen.get().getPlugin();
		
			p.setDayOfMonthForUpdate((Integer) pluginConfigurationScreen.get().getComboDayOfMonthForUpdate().getSelectionModel().getSelectedItem());
			p.setDestinationDirectory(pluginConfigurationScreen.get().getLibraryView().getEventHandler().getCurrentItemRelativePath());
			p.setDestinationFilePattern(pluginConfigurationScreen.get().getFieldNamePattern().getText().trim());
			p.setFileName(p.getPlugin().getJarFileName());
			p.setIntervalBetweenUpdates((Integer) pluginConfigurationScreen.get().getComboIntervalBetweenUpdateInMonth().getSelectionModel().getSelectedItem());
			
			List<SimpleGedPluginProperty> properties = new ArrayList<SimpleGedPluginProperty>();
			
			for (Entry<SimpleGedPluginProperty, TextField> entry : pluginConfigurationScreen.get().getPropertiesFieldsMap().entrySet()) {
				entry.getKey().setPropertyValue(entry.getValue().getText());
				properties.add(entry.getKey());
			}
			p.setPluginProperties(properties);

			PluginService.addOrUpdateDocument(p);
			PluginManager.launchPluginUpdate(pluginConfigurationScreen.get());

			JOptionPane.showMessageDialog(null, this.properties.getProperty("plugin_is_activated"), this.properties.getProperty("information"), JOptionPane.INFORMATION_MESSAGE);
			
			pluginConfigurationScreen.get().refreshScreens();
			pluginConfigurationScreen.get().finish();
			
		}
		else {
			logger.warn("Not implemented yet");
		}
		
	}
	
	private void keyEventDelegate(KeyEvent e) {
		
	}
}
