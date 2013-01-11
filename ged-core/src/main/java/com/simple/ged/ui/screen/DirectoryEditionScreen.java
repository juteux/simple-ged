package com.simple.ged.ui.screen;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.ui.MainWindow;

import fr.xmichel.toolbox.tools.FileHelper;

/**
 * 
 * This screen is used for directory customization (like icon choice)
 * 
 * @author xavier
 *
 */
public class DirectoryEditionScreen extends SoftwareScreen {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(DirectoryEditionScreen.class);
	
	/**
	 * The directory which contains directories custom icons
	 */
	public static final String DIRECTORY_ICON_DIRECTORY = "directory_icons/"; 
	
	/**
	 * Directory relative location
	 */
	private String directoryRelativeLocation;
	
	public DirectoryEditionScreen(MainWindow mw) {
		super(mw);
		
		// make sure the target exists
		FileHelper.createDirectoryIfNecessary(DIRECTORY_ICON_DIRECTORY);
	}


	@Override
	public void pullExtraValues(Map<String, Object> extras) {
		directoryRelativeLocation = (String) extras.get("relative-directory-root");
		logger.trace("Pull extra : {}", directoryRelativeLocation);
		
	}

	
	
}
