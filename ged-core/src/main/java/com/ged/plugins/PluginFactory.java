package com.ged.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.ged.connector.plugins.SimpleGedPlugin;
import com.ged.connector.plugins.SimpleGedPluginProperty;
import com.tools.FileHelper;


/**
 * This class build a plugin, according to the plugin file name
 * 
 * @author xavier
 *
 */
public class PluginFactory {

	
	private static final Logger logger = Logger.getLogger(PluginFactory.class);
	
	
	/**
	 * Load the plugin for the given file name
	 * 
	 * @param pluginFileName
	 * 				The plugin file name
	 * 
	 * @return
	 * 		The plugin if loading is successful, null otherwise
	 */
	 static SimpleGedPlugin loadPlugin(String pluginFileName) {
				
		try {
			ClassLoader loader = null;
			
			Map<PluginManifestTags, String> pluginInfos = new HashMap<PluginManifestTags, String>(); // <Key, Value>
			List<SimpleGedPluginProperty> pluginProperties = new ArrayList<SimpleGedPluginProperty>();
			
			/*
			 * Load plugin properties
			 */
			
			logger.info("Loading : " + PluginManager.PLUGINS_DIRECTORY + pluginFileName);
			URL urls[] = {new File(PluginManager.PLUGINS_DIRECTORY + pluginFileName).toURI().toURL()};
			loader = URLClassLoader.newInstance(urls);//new URLClassLoader(urls);

			InputStreamReader isr = new InputStreamReader(loader.getResourceAsStream(PluginManager.MANIFEST_FILE_NAME), "utf8"); 
			BufferedReader br = new BufferedReader(isr);
	
			String line;
			while ((line=br.readLine()) != null) {
			
				line = line.replaceAll("[ \t]*=", "");

				if (line.startsWith(PluginManifestTags.fields_tag.getTagLabel())) { // special treatment
					
					line = line.replaceAll(PluginManifestTags.fields_tag.getTagLabel(), "");
					//logger.debug(line);
					
					String[] properties = line.split(";");
					
					for (String property : properties) {
						property = property.trim();
						//logger.debug(property);
						
						Pattern p = Pattern.compile("(.*)\\((.*)\\)");
						Matcher m = p.matcher(property);
						
						String key = null;
						String label = null;
						while(m.find()) {
							//logger.debug("find : " + m.group(1) + " -> " + m.group(2));
							key = m.group(1);
							label = m.group(2);
						}
						
						SimpleGedPluginProperty sgpp = new SimpleGedPluginProperty();
						
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
			 * Extract dependency if necessary
			 */
			JarFile jar = new JarFile(new File(PluginManager.PLUGINS_DIRECTORY + pluginFileName).getAbsolutePath());
			Enumeration<JarEntry> enumeration = jar.entries();
			String tmp;
			
			List<String> dependencyJarPath = new ArrayList<String>();
			
			while(enumeration.hasMoreElements()){
				
				tmp = enumeration.nextElement().toString();
				
				if( tmp.endsWith("jar") && ! tmp.endsWith("SimpleGedConnector.jar")) {
					dependencyJarPath.add(extractJar(pluginFileName, tmp));
				}
			}

            URL[] urlsWithDependency = new URL[dependencyJarPath.size() + 1];
            for (int i=0; i<dependencyJarPath.size(); ++i) {
            	urlsWithDependency[i] = new File(dependencyJarPath.get(i)).toURI().toURL();
            }
            urlsWithDependency[dependencyJarPath.size()] = new File(PluginManager.PLUGINS_DIRECTORY + pluginFileName).toURI().toURL();
            
            ClassLoader loaderWithDependency = URLClassLoader.newInstance(urlsWithDependency);
			
			
			/*
			 * We can create plugin now !
			 */

			// set plugin infos
			SimpleGedPlugin sgp = (SimpleGedPlugin) Class.forName(pluginInfos.get(PluginManifestTags.main_class_tag), true, loaderWithDependency).newInstance();

			sgp.setJarFileName(pluginFileName);
			
			sgp.setProperties(pluginProperties);
			
			for (java.util.Map.Entry<PluginManifestTags, String> e : pluginInfos.entrySet()) {
				//logger.debug(e.getKey().getTagLabel() + "=" + e.getValue());
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
			e.printStackTrace();
			return null;
		}
	}
	 
	 
	 /**
	  * Extract some jar contained in another jar
	  * 
	  * @param jarFileName
	  * 			The name of the jar file which contains another jar
	  * 
	  * @param subjarToExtractPath
	  * 			The path of the jar which need to be extracted
	  * 
	  * @return
	  * 			The path of the extracted file
	  */
	private static String extractJar(String jarFileName, String subjarToExtractPath) {
		
		FileHelper.createDirectoryIfNecessary(PluginManager.PLUGINS_DEPENDENCY_DIRECTORY + jarFileName);
		
		
		FileOutputStream extractedFile = null;
		
		try {
			JarFile jar = new JarFile(PluginManager.PLUGINS_DIRECTORY + jarFileName);
			JarEntry entry = jar.getJarEntry(subjarToExtractPath);
			InputStream entryStream = jar.getInputStream(entry);
			extractedFile = new FileOutputStream(PluginManager.PLUGINS_DEPENDENCY_DIRECTORY + jarFileName + "/" + new File(subjarToExtractPath).getName());
		
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = entryStream.read(buffer)) != -1) {
				extractedFile.write(buffer, 0, bytesRead);
				extractedFile.flush();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				extractedFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return PluginManager.PLUGINS_DEPENDENCY_DIRECTORY + jarFileName + "/" + new File(subjarToExtractPath).getName();
	}
}
