package com.simple.ged.services;

import java.util.List;

import com.ged.connector.plugins.SimpleGedPlugin;
import com.simple.ged.dao.PluginDAO;
import com.simple.ged.models.GedPlugin;
import com.simple.ged.plugins.PluginManager;

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
	public static List<GedPlugin> getAllPlugins() {
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
	public static synchronized GedPlugin getPluginInformations(SimpleGedPlugin plugin) {
		
		GedPlugin pmi = PluginDAO.getPluginInformations(plugin.getJarFileName());
		
		if (pmi == null) {
			pmi = new GedPlugin();
		}

		pmi.setPlugin(plugin);
		
		return pmi;
	}
	
	
	/**
	 * Add or update the given plugin
	 */
	public static void addOrUpdateDocument(GedPlugin pmi)
	{
		PluginDAO.saveOrUpdate(pmi);
	}
	
	
	/**
	 * Desactivate and delete saved informations of the given plugin
	 */
	public static void desactivatePlugin(GedPlugin pmi)
	{
		PluginDAO.delete(pmi);
	}
}
