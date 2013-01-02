package com.simple.ged.update;

import fr.xmichel.toolbox.tools.PropertiesHelper;

/**
 * This file contains address of the online xml files which describes updates
 * 
 * @author xavier
 *
 */
public final class UpdateInformations {

	/**
	 * Should not be instantiated
	 */
	private UpdateInformations() {
	}
	
	/**
	 * Path to xml which describe update for GED core
	 */
	public static final String GED_CORE_UPDATE_DESCRIPTOR_PATH;
	
	/**
	 * Path to xml which describe update for updater
	 */
	public static final String UPDATER_UPDATE_DESCRIPTOR_PATH;
	
	/**
	 * The file which contains values
	 */
	public static final String CONSTANT_PROPERTIES_FILE_PATH = "properties/updater_constants.properties";
	
	
	static {
		PropertiesHelper.getInstance().load(CONSTANT_PROPERTIES_FILE_PATH);
		GED_CORE_UPDATE_DESCRIPTOR_PATH = PropertiesHelper.getInstance().getProperties().getProperty("GED_CORE_UPDATE_DESCRIPTOR_PATH");
		UPDATER_UPDATE_DESCRIPTOR_PATH  = PropertiesHelper.getInstance().getProperties().getProperty("UPDATER_UPDATE_DESCRIPTOR_PATH");
	}
	
}
