package com.ged.services;

import java.util.List;

import com.ged.connector.plugins.SimpleGedPlugin;
import com.ged.dao.PluginDAO;
import com.ged.plugins.PluginManagementInformations;
import com.ged.plugins.PluginManager;

/**
 * 
 * Plugin service
 * 
 * @author xavier
 *
 */
public class PluginService {

	/**
	 * Load and returns all plugins
	 */
	public static List<PluginManagementInformations> getAllPlugins() {
		return PluginManager.getPluginList();
	}
	
	
	
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
	
	
	/**
	 * Add or update the given plugin
	 */
	public static void addOrUpdateDocument(PluginManagementInformations pmi)
	{
		PluginDAO.saveOrUpdate(pmi);
	}
	
	
	/**
	 * Desactivate and delete saved informations of the given plugin
	 */
	public static void desactivatePlugin(PluginManagementInformations pmi)
	{
		PluginDAO.delete(pmi);
	}
}
