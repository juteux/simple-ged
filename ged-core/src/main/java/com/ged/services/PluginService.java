package com.ged.services;

import com.ged.connector.plugins.SimpleGedPlugin;
import com.ged.dao.PluginDAO;
import com.ged.plugins.PluginManagementInformations;

/**
 * 
 * Plugin service
 * 
 * @author xavier
 *
 */
public class PluginService {

	/**
	 * Get plugin informations from database
	 * 
	 * @param pluginFileName
	 *            The plugin file name
	 *            
	 * If the plugin is 
	 */
	public static synchronized PluginManagementInformations getPluginInformations(SimpleGedPlugin plugin) {
		
		PluginManagementInformations pmi = PluginDAO.getPluginInformations(plugin.getJarFileName());
		
		if (pmi == null) {
			pmi = new PluginManagementInformations();
		}

		pmi.setPlugin(plugin);
		
		return pmi;
	}
	
}
