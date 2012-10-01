package com.ged.ui.controllers;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;

import com.ged.connector.plugins.SimpleGedPlugin;
import com.ged.dao.PluginDAO;
import com.ged.plugins.PluginManagementInformations;
import com.ged.plugins.PluginManager;
import com.ged.ui.screens.PluginManagementScreen;
import com.ged.ui.screens.SoftwareScreen;
import com.tools.DateHelper;
import com.tools.PropertiesHelper;

/**
 * This class is the controller for the PluginManagementScreen
 * 
 * @author xavier
 *
 */
public class PluginManagementScreenController implements ActionListener, TableModelListener {

	private static final Logger logger = Logger.getLogger(PluginManagementScreenController.class);
	
	/**
	 * The controlled object
	 */
	private PluginManagementScreen pluginManagementScreen;

	/**
	 * List of plugin, sort with the order of apparition in list in UI
	 */
	private List<SimpleGedPlugin> pluginList;
	
	/**
	 * Plugin map, plugin & infos
	 */
	private Map<SimpleGedPlugin, PluginManagementInformations> plugins;
	
	
	protected Properties properties;
	
	
	public PluginManagementScreenController(PluginManagementScreen pluginManagementScreen) {
		this.pluginManagementScreen = pluginManagementScreen;
		
		pluginList = new ArrayList<SimpleGedPlugin>();
		this.properties = PropertiesHelper.getInstance().getProperties();
	}
	
	
	/**
	 * Fill the table of plugins
	 * 
	 * The table is cleared before !
	 */
	public void fillPluginTable() {
		
		pluginList.clear();
		while (pluginManagementScreen.getTablePluginModel().getRowCount() > 0) {
			pluginManagementScreen.getTablePluginModel().removeRow(0);
		}
		plugins = PluginManager.getPluginMap();
		
		// show only activated plugins
		for (Entry<SimpleGedPlugin, PluginManagementInformations> p : plugins.entrySet()) {
			if (p.getValue() == null) {
				continue;
			}
			addPluginInTable(p.getKey(), p.getValue());
		}
		// show non activated plugins
		for (Entry<SimpleGedPlugin, PluginManagementInformations> p : plugins.entrySet()) {
			if (p.getValue() != null) {
				continue;
			}
			addPluginInTable(p.getKey(), p.getValue());
		}
	}
	
	/**
	 * Add some plugin in the plugin table
	 */
	private void addPluginInTable(SimpleGedPlugin p, PluginManagementInformations i) {
		
		pluginList.add(p);
		
		JLabel title = new JLabel();
		title.setText(
					"<html>" +
							"<center><h2>" + p.getPluginName() + "</h2></center>" + 
							properties.getProperty("Version") + " " + p.getPluginVersion() + " " + properties.getProperty("released_the") + " " + DateHelper.calendarToString(p.getPluginDate()) + "<br/>" +
							properties.getProperty("by") + " " + p.getPluginAuthor() + 
							"<br/><center><i>(" + p.getJarFileName() + ")</i></center>" 
						+ "</html>"
		);
		
		
		JLabel description = new JLabel();
		if (i == null) {
			description.setText("<html>" + p.getPluginDescription() + "</html>");
		}
		else {
			description.setText("<html>" +
									p.getPluginDescription()
									+ "<hr>"
									+ properties.getProperty("actionned_on") + " " + i.getDayOfMonthForUpdate() + 
												(i.getIntervalBetweenUpdates() == 1 ? properties.getProperty("each_month") : properties.getProperty("every") + " " + i.getIntervalBetweenUpdates() + properties.getProperty("month"))
									+ "<br/>"
									+ properties.getProperty("last_action") + " " + (i.getLastUpdateDate() != null ? properties.getProperty("the") + " " + DateHelper.calendarToString(i.getLastUpdateDate()) : " " + properties.getProperty("never"))
								+ "</html>"
			);
		}
		
		Object[] data = new Object[]{title, description, new Boolean(i != null)};
		
		pluginManagementScreen.getTablePluginModel().addRow(data);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == pluginManagementScreen.getBtnReturn()) {
			pluginManagementScreen.gotoScreen(SoftwareScreen.Screen.WELCOME_SCEEN);
		}
		else if (e.getSource() == pluginManagementScreen.getBtnOpenPluginDirectory()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.OPEN)) {
				try {
					desktop.open(new File(PluginManager.PLUGINS_DIRECTORY));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}	
		}
		else if (e.getSource() == pluginManagementScreen.getBtnRefresh()) {
			fillPluginTable();
		}
		else {
			logger.warn("Not implemented yet, see PluginManagementScreenController.actionPerformed");
		}
		
	}


	@Override
	public void tableChanged(TableModelEvent arg0) {
		
		int row = pluginManagementScreen.getTablePlugin().getSelectedRow();
		
		if (row == -1) {
			return;
		}
		if (pluginList.isEmpty()) {
			return;
		}
		
		SimpleGedPlugin p = pluginList.get(row);

		PluginManagementInformations i = plugins.get(p);

		if ( i != null ) {
			// Deactivate plugin
			PluginDAO.delete(i);
			fillPluginTable();
			return;
		}
		
		// activating plugin
		pluginManagementScreen.pushScreen(SoftwareScreen.Screen.PLUGIN_OPTION_SCREEN);
		
		Map<String, Object> extra = new HashMap<String, Object>();
		extra.put("plugin", p);
		pluginManagementScreen.putExtra(extra);		
	}

}
