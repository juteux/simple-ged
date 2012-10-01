package com.ged.plugins;

import java.util.Date;
import java.util.List;

import com.ged.connector.plugins.SimpleGedPluginProperty;

/**
 * The plugin controller is a class which is required to save plugin informations in database
 * 
 * This is different of SimpleGedPlugin because this class has an access to the informations in
 * database.
 * 
 * @author xavier
 *
 */
public class PluginManagementInformations {

    /**
     * Plugin ID
     */
    private int id;
	
    /**
     * Plugin's file name
     */
    private String fileName;
 
    /**
     * Last update date
     */
    private Date lastUpdateDate;
    
    /**
     * The day of month for update
     */
    private int dayOfMonthForUpdate;
    
    /**
     * The destination directory (relative path)
     */
    private String destinationDirectory;
    
    /**
     * The destination file name pattern
     */
    private String destinationFilePattern;
    
    /**
     * Interval between updates (in month)
     */
    private int intervalBetweenUpdates;
    
    /**
     * Properties attached to this plugin
     */
    private List<SimpleGedPluginProperty> pluginProperties;

    
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public int getDayOfMonthForUpdate() {
		return dayOfMonthForUpdate;
	}

	public void setDayOfMonthForUpdate(int dayOfMonthForUpdate) {
		this.dayOfMonthForUpdate = dayOfMonthForUpdate;
	}

	public String getDestinationFilePattern() {
		return destinationFilePattern;
	}

	public void setDestinationFilePattern(String destinationFilePattern) {
		this.destinationFilePattern = destinationFilePattern;
	}

	public int getIntervalBetweenUpdates() {
		return intervalBetweenUpdates;
	}

	public void setIntervalBetweenUpdates(int intervalBetweenUpdates) {
		this.intervalBetweenUpdates = intervalBetweenUpdates;
	}

	public List<SimpleGedPluginProperty> getPluginProperties() {
		return pluginProperties;
	}

	public void setPluginProperties(List<SimpleGedPluginProperty> pluginProperties) {
		this.pluginProperties = pluginProperties;
	}

	public String getDestinationDirectory() {
		return destinationDirectory;
	}

	public void setDestinationDirectory(String destinationDirectory) {
		this.destinationDirectory = destinationDirectory;
	}
}
