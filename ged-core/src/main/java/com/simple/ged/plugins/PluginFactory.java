package com.simple.ged.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.xmichel.javafx.dialog.Dialog;
import org.apache.commons.beanutils.PropertyUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPluginProperty;



/**
 * This class build a plugin, according to the plugin file name
 * 
 * @author xavier
 *
 */
public final class PluginFactory {

	
	private static final Logger logger = LoggerFactory.getLogger(PluginFactory.class);
	
	
	/**
	 * Should not be instantiated
	 */
	private PluginFactory() {
	}
	
	
	/**
	 * Load the plugin for the given file name
	 * 
	 * @param pluginFileName
	 * 				The plugin file name
	 * 
	 * @return
	 * 		The plugin if loading is successful, null otherwise
	 */
	 static SimpleGedGetterPlugin loadPlugin(String pluginFileName) {
				
		try {
			ClassLoader loader = null;
			
			Map<PluginManifestTags, String> pluginInfos = new HashMap<>();
			List<SimpleGedGetterPluginProperty> pluginProperties = new ArrayList<>();
			
			/*
			 * Load plugin properties
			 */
			
			logger.info("Loading : " + PluginManager.PLUGINS_DIRECTORY + pluginFileName);
			URL urls[] = {new File(PluginManager.PLUGINS_DIRECTORY + pluginFileName).toURI().toURL()};
			loader = URLClassLoader.newInstance(urls);

			InputStreamReader isr = new InputStreamReader(loader.getResourceAsStream(PluginManager.MANIFEST_FILE_NAME), "utf8"); 
			BufferedReader br = new BufferedReader(isr);
	
			String line;
			while ((line=br.readLine()) != null) {
			
				line = line.replaceAll("[ \t]*=", "");

				if (line.startsWith(PluginManifestTags.fields_tag.getTagLabel())) { // special treatment
					
					line = line.replaceAll(PluginManifestTags.fields_tag.getTagLabel(), "");
					logger.trace(line);
					
					String[] properties = line.split(";");
					
					for (String property : properties) {
						property = property.trim();
						logger.trace(property);
						
						Pattern p = Pattern.compile("(.*)\\((.*)\\)");
						Matcher m = p.matcher(property);
						
						String key = null;
						String label = null;
						while(m.find()) {
							logger.trace("find : " + m.group(1) + " -> " + m.group(2));
							key = m.group(1);
							label = m.group(2);
						}
						
						SimpleGedGetterPluginProperty sgpp = new SimpleGedGetterPluginProperty();
						
						if (key.contains("*")) {
							key = key.replace("*", "");
							sgpp.setHidden(true);
						}
						
						sgpp.setPropertyKey(key.trim());
						sgpp.setPropertyLabel(label.trim());
						
						pluginProperties.add(sgpp);
					}

				} else {	// it's not field tag
			
					for (PluginManifestTags tag : PluginManifestTags.values()) {
						if (line.startsWith(tag.getTagLabel())) {
							pluginInfos.put(tag, line.replaceAll(tag.getTagLabel(), "").trim());
						}
					}
				}
			
			} // end while readline

			
			
			/*
			 * We can create plugin now !
			 */

			// set plugin infos
			SimpleGedGetterPlugin sgp = (SimpleGedGetterPlugin) Class.forName(pluginInfos.get(PluginManifestTags.main_class_tag), true, loader).newInstance();

			sgp.setJarFileName(pluginFileName);
			
			sgp.setProperties(pluginProperties);
			
			for (java.util.Map.Entry<PluginManifestTags, String> e : pluginInfos.entrySet()) {
				
				logger.trace(e.getKey().getTagLabel() + "=" + e.getValue());
				
				if (e.getKey().getAttributeName() != null) {
					if (e.getKey().getAttributeName().contains("date") || e.getKey().getAttributeName().contains("Date")) {
						String[] dateSplit = e.getValue().split("-");
						PropertyUtils.setSimpleProperty(
								sgp, 
								e.getKey().getAttributeName(), 
								(new GregorianCalendar(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]))).getTime()
						);
					} else {
						PropertyUtils.setSimpleProperty(sgp, e.getKey().getAttributeName(), e.getValue());
					}
				}
			}
			return sgp;
			
		} catch (Exception e) {
			logger.error("Failed to load plugin " + pluginFileName, e);
            Dialog.showThrowable("Erreur", "Le chargement du plugin " + pluginFileName + "a échoué", e);
		}
		
		return null;
	}
	
}
