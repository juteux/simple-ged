package com.ged.plugins;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.connector.plugins.SimpleGedPlugin;
import com.ged.connector.plugins.SimpleGedPluginException;
import com.ged.dao.PluginDAO;
import com.ged.models.GedMessage;
import com.ged.services.MessageService;
import com.ged.services.PluginService;
import com.ged.ui.screens.SoftwareScreen;
import com.tools.DateHelper;
import com.tools.FileHelper;


/**
 * The plugin manager is managing available plugins
 * 
 * That means that this manager builds and calls plugins when it's necessary
 * 
 * @author xavier
 *
 */
public class PluginManager {

	
	private static final Logger logger = Logger.getLogger(PluginManager.class);
	
	
	/**
	 * The name of the directory which contains plugins
	 */
	public static final String PLUGINS_DIRECTORY  = "plugins/";
	
	/**
	 * The name of the manifest file (in plugin jar)
	 */
	static final String MANIFEST_FILE_NAME = "ged_plugin_manifest.properties";
	
    /**
     * The folder which is containing extracted plugins dependencies
     */
    static final String PLUGINS_DEPENDENCY_DIRECTORY = "plugin_dependency/";
    
	
	/**
	 * @return	
	 * 
	 * The list of available plugins in the plugin's directory
	 * The value can be null
	 * 
	 * @deprecated 
	 * 			use getPluginList instead
	 */
    @Deprecated
	public static Map<SimpleGedPlugin, PluginManagementInformations> getPluginMap() {
		
		FileHelper.createDirectoryIfNecessary(PLUGINS_DIRECTORY);
		FileHelper.createDirectoryIfNecessary(PLUGINS_DEPENDENCY_DIRECTORY);
		
		
		Map<SimpleGedPlugin, PluginManagementInformations> pluginMap = new HashMap<SimpleGedPlugin, PluginManagementInformations>();
		
		FilenameFilter jarFilter = new FilenameFilter() {
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".jar");
			}
		};

		File pluginsDirectory = new File(PLUGINS_DIRECTORY);
		String[] pluginsFiles = pluginsDirectory.list(jarFilter);
		
		for (String pluginFileName : pluginsFiles) {
			logger.info(pluginFileName);
			SimpleGedPlugin p = PluginFactory.loadPlugin(pluginFileName);
			if ( p == null) {
				logger.error("Couldn't load plugin : " + pluginFileName);
			} else {
				pluginMap.put(p, PluginDAO.getPluginInformations(pluginFileName));
			}
		}
		return pluginMap;
	}
	
    
    
    /**
     * Get the plugin list
     */
    public static List<PluginManagementInformations> getPluginList() {
    	
    	FileHelper.createDirectoryIfNecessary(PLUGINS_DIRECTORY);
    	
    	List<PluginManagementInformations> pluginList = new ArrayList<>();

		FilenameFilter jarFilter = new FilenameFilter() {
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".jar");
			}
		};

		File pluginsDirectory = new File(PLUGINS_DIRECTORY);
		String[] pluginsFiles = pluginsDirectory.list(jarFilter);
		
		for (String pluginFileName : pluginsFiles) {
			logger.info(pluginFileName);
			SimpleGedPlugin p = PluginFactory.loadPlugin(pluginFileName);
			if ( p == null) {
				logger.error("Couldn't load plugin : " + pluginFileName);
			} else {
				pluginList.add(PluginService.getPluginInformations(p));
			}
		}

    	return pluginList;
    }
    
    
	
	/**
	 * Launch the plugin update if necessary
	 */
	public static void launchPluginUpdate(final SoftwareScreen ss) {
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				Map<SimpleGedPlugin, PluginManagementInformations> plugins = getPluginMap();
				
				for (Entry<SimpleGedPlugin, PluginManagementInformations> e : plugins.entrySet()) {
					
					if (e.getValue() == null) {
						continue;
					}
					
					SimpleGedPlugin p = e.getKey();
					PluginManagementInformations i = e.getValue();
					
					boolean shouldUpdate = false;

					if (i.getLastUpdateDate() == null) {
						shouldUpdate = true;
					}
					else {

						GregorianCalendar c = new GregorianCalendar();
						c.setTime(i.getLastUpdateDate());
						
						c.add(Calendar.MONTH, i.getIntervalBetweenUpdates());
						c.set(Calendar.DAY_OF_MONTH, i.getDayOfMonthForUpdate());
						
						if (c.before(new GregorianCalendar())) {
							shouldUpdate = true;
						}

					}
					
					
					if (shouldUpdate) {
						try {
							MessageService.addMessage(new GedMessage("NEUTRAL", "Début de récupération de document pour le plugin :" + p.getJarFileName()));
							
							String destinationFileName = i.getDestinationFilePattern();
							
							if ( destinationFileName.contains("\\w-") && new GregorianCalendar().get(Calendar.MONTH) == 0 ) {
								destinationFileName = destinationFileName.replace("\\y", String.valueOf(new GregorianCalendar().get(Calendar.YEAR) - 1));
							}
							else {
								destinationFileName = destinationFileName.replace("\\y", String.valueOf(new GregorianCalendar().get(Calendar.YEAR)));
							}
							destinationFileName = destinationFileName.replace("\\m-", DateHelper.getMonthName(new GregorianCalendar().get(Calendar.MONTH) - 1));
							destinationFileName = destinationFileName.replace("\\m", DateHelper.getMonthName(new GregorianCalendar().get(Calendar.MONTH)));
							
							p.setDestinationFile(Profile.getInstance().getLibraryRoot() + i.getDestinationDirectory() + (i.getDestinationDirectory().isEmpty() ? "" : File.separator) + destinationFileName);
							
							p.setProperties(i.getPluginProperties());
							
							p.doGet();
							
							i.setLastUpdateDate(new GregorianCalendar().getTime());
							PluginDAO.updatePlugin(i);
							
							MessageService.addMessage(new GedMessage("INFO", "Récupération réussie pour le plugin " + p.getJarFileName() + "<br/>Nouveau fichier enregistré : " + p.getDestinationFile()));
							
						} catch (SimpleGedPluginException e1) {
							
							MessageService.addMessage(new GedMessage("ERROR", "Echec de récupération pour le plugin " + p.getJarFileName() + "<br/>Détail : " + e1.getMessage()));
							
							logger.error("[ " + p.getJarFileName() + " ] Error in plugin DoGet : " + e1.getMessage());
							e1.printStackTrace();
						} 
						finally {
							ss.notifyNewMessagesAvailable();
						}
					}
				}
			}
		});
		
		t.start();
	
	}
	
}
