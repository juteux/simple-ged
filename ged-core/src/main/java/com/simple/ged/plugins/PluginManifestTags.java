package com.simple.ged.plugins;

/**
 * This file describes avaliable tags in manifest
 * 
 * @author xavier
 */
public enum PluginManifestTags {

	// infos
	name_tag		(	"name", 		"pluginName"),			// plugin's name
	author_tag		(	"author", 		"pluginAuthor"),		// plugin's author
	version_tag		(	"version", 		"pluginVersion"),		// plugin's version
	date_tag		(	"date", 		"pluginDate"),			// plugin's date
	description_tag	(	"description", 	"pluginDescription"),	// plugin's description
	
	// actions
	main_class_tag	(	"main_class", 	null),					// plugin's launcher class
	fields_tag		( 	"fields",		null);					// ged fields
	
	/**
	 * The tag name in manifest
	 */
	private String tagLabel;
	
	/**
	 * The name of the property in the class (null if not exists)
	 */
	private String attributeName;
	
	
	PluginManifestTags(String tagLabel, String attributeName) {
		this.tagLabel = tagLabel;
		this.attributeName = attributeName;
	}
	
	
	public String getTagLabel() { 
		return tagLabel;
	}
	
	public String getAttributeName() {
		return attributeName;
	}
}
