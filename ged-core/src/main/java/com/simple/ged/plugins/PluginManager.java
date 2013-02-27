package com.simple.ged.plugins;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.simple.ged.connector.plugins.worker.SimpleGedWorkerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.Profile;
import com.simple.ged.connector.plugins.dto.GedFolderDTO;
import com.simple.ged.connector.plugins.feedback.SimpleGedPluginException;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;
import com.simple.ged.models.GedGetterPlugin;
import com.simple.ged.models.GedMessage;
import com.simple.ged.models.GedWorkerPlugin;
import com.simple.ged.plugins.PluginFactory.PluginFamily;
import com.simple.ged.services.MessageService;
import com.simple.ged.services.PluginService;
import com.simple.ged.ui.screen.SoftwareScreen;

import fr.xmichel.toolbox.tools.DateHelper;
import fr.xmichel.toolbox.tools.FileHelper;


/**
 * The plugin manager is managing available plugins
 * 
 * That means that this manager builds and calls plugins when it's necessary
 * 
 * @author xavier
 *
 */
public final class PluginManager {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(PluginManager.class);
	
	
	/**
	 * Should not be instantiated
	 */
	private PluginManager() {
	}
	
	
	/**
	 * The name of the directory which contains plugins
	 */
	public static final String PLUGINS_DIRECTORY  = "plugins/";
	
	/**
	 * The name of the manifest file (in plugin jar)
	 */
	static final String MANIFEST_FILE_NAME = "ged_plugin_manifest.properties";
	
    /**
     * Get the getter plugin list
     */
    public static List<GedGetterPlugin> getGetterPluginList() {
    	
    	FileHelper.createDirectoryIfNecessary(PLUGINS_DIRECTORY);
    	
    	List<GedGetterPlugin> pluginList = new ArrayList<>();

		FilenameFilter jarFilter = new FilenameFilter() {
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".jar");
			}
		};

		File pluginsDirectory = new File(PLUGINS_DIRECTORY);
		String[] pluginsFiles = pluginsDirectory.list(jarFilter);
		
		for (String pluginFileName : pluginsFiles) {
			
			if (PluginFactory.getPluginFamilyForPlugin(pluginFileName) != PluginFamily.GETTER_PLUGIN) {
				continue;
			}
			
			logger.info(pluginFileName);
			SimpleGedGetterPlugin p = PluginFactory.loadGetterPlugin(pluginFileName);
			if ( p == null) {
				logger.error("Couldn't load plugin : " + pluginFileName);
			} else {
				pluginList.add(PluginService.getPluginInformations(p));
			}
		}

    	return pluginList;
    }
    
    
    /**
     * Get the worker plugin list
     */
    public static List<GedWorkerPlugin> getWorkerPluginList() {
    	
    	FileHelper.createDirectoryIfNecessary(PLUGINS_DIRECTORY);
    	
    	List<GedWorkerPlugin> pluginList = new ArrayList<>();

		FilenameFilter jarFilter = new FilenameFilter() {
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".jar");
			}
		};

		File pluginsDirectory = new File(PLUGINS_DIRECTORY);
		String[] pluginsFiles = pluginsDirectory.list(jarFilter);
		
		for (String pluginFileName : pluginsFiles) {
			
			if (PluginFactory.getPluginFamilyForPlugin(pluginFileName) != PluginFamily.WORKER_PLUGIN) {
				continue;
			}
			
			logger.info(pluginFileName);
			SimpleGedWorkerPlugin p = PluginFactory.loadWorkerPlugin(pluginFileName);
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
	public static void launchGetterPluginUpdate(final SoftwareScreen ss) {
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				List<GedGetterPlugin> plugins = getGetterPluginList();
				
				for (GedGetterPlugin plugin : plugins) {
					
					if ( ! plugin.isActivated() ) {
						continue;
					}
					
					SimpleGedGetterPlugin p = plugin.getPlugin();
					GedGetterPlugin i = plugin;
					
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
							
							if ( destinationFileName.contains("\\m-") && new GregorianCalendar().get(Calendar.MONTH) == 0 ) {
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
							PluginService.addOrUpdatePlugin(i);
							
							MessageService.addMessage(new GedMessage("INFO", "Récupération réussie pour le plugin " + p.getJarFileName() + "<br/>Nouveau fichier enregistré : " + p.getDestinationFile()));
							
						} catch (SimpleGedPluginException e1) {
							
							MessageService.addMessage(new GedMessage("ERROR", "Echec de récupération pour le plugin " + p.getJarFileName() + "<br/>Détail : " + e1.getMessage()));
							
							logger.error("[ " + p.getJarFileName() + " ] Error in plugin DoGet : ", e1);
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
	
	
	 /**
	 * Launch the plugin update if necessary
	 */
	public static void launchWorkerPlugin(final GedWorkerPlugin p, final SoftwareScreen ss) {
		
		MessageService.addMessage(new GedMessage("NEUTRAL", "Lancement de l'action pour le plugin :" + p.getPlugin().getJarFileName()));
		ss.notifyNewMessagesAvailable();
		
		try {
			p.getPlugin().setProperties(p.getPluginProperties());
		
			p.getPlugin().doWork(new GedFolderDTO(Profile.getInstance().getLibraryRoot()));
			
			MessageService.addMessage(new GedMessage("INFO", "Exécution réussie pour le plugin " + p.getPlugin().getJarFileName() + "<br/>Résulat :<br/>" + "RESULTAT IS NOT RECUPERATED YET !!!"));
		}
		catch (SimpleGedPluginException e1) {
			MessageService.addMessage(new GedMessage("ERROR", "Echec d'exécution pour le plugin " + p.getPlugin().getJarFileName() + "<br/>Détail : " + e1.getMessage()));
			
			logger.error("[ " + p.getPlugin().getJarFileName() + " ] Error in plugin DoWork : ", e1);
		}
		finally {
			ss.notifyNewMessagesAvailable();
		}
		
	}
}
