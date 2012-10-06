package com.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * This class is a helper which load some properties file and give back the loaded values
 * 
 * There is a cache so the file isn't loaded each time you need it
 * 
 * @author xavier
 *
 */
public class PropertiesHelper {

	
	private static final Logger logger = Logger.getLogger(PropertiesHelper.class);
	
	
	/**
	 * The map with file & content
	 */
	private Properties properties;
	
	
	/**
	 * My instance
	 */
	private static PropertiesHelper instance = null;
	
	
	/**
	 * Get the instance of PropertiesHelper
	 */
	public static PropertiesHelper getInstance() {
		
		if (instance == null) {
			instance = new PropertiesHelper();
		}
		
		return instance;
	}
	
	
	private PropertiesHelper() {
		properties = new Properties();
	}
	
	
	/**
	 * Load the given file
	 */
	public void load(String filename) {
		InputStream input = null;
		try {
			input = PropertiesHelper.class.getResourceAsStream("/" + filename);

			properties.load(input);
		} 
		catch (Exception e) {
			logger.error("Error while loading " + filename);
			e.printStackTrace();
		}
		finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Get the properties map
	 */
	public Properties getProperties() {
		return properties;
	}
	
}
