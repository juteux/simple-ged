package com.simple.ged.update;

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
	public static final String GED_CORE_UPDATE_DESCRIPTOR_PATH = "http://www.xaviermichel.info/data/ged/last_version.xml";
	
	/**
	 * Path to xml which describe update for updater
	 */
	public static final String UPDATER_UPDATE_DESCRIPTOR_PATH = "http://www.xaviermichel.info/data/ged/updater_last_version.xml";
	
}
