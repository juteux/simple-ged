package com.simple.ged.ui.screen;

import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

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
	 * Remember the folder name
	 */
	private Label folderNameLabel;
	
	/**
	 * Label action : change icon
	 */
	private Label labelChangeIcon;
	
	/**
	 * Label "download from Internet"
	 */
	private Label downloadIconFromWebLabel;
	
	/**
	 * Online icon selector
	 */
	private TextField editIconOnlineLocation;
	
	/**
	 * Online icon download button
	 */
	private Button downloadOnlineIcon;
	
	
	/**
	 * Directory relative location
	 */
	private String directoryRelativeLocation;
	
	public DirectoryEditionScreen(MainWindow mw) {
		super(mw);
		
		// make sure the target exists
		FileHelper.createDirectoryIfNecessary(DIRECTORY_ICON_DIRECTORY);
		
		
		instantiateWidgets();
	
		HBox httpDownloadBox = new HBox();
		httpDownloadBox.getChildren().addAll(downloadIconFromWebLabel, editIconOnlineLocation, downloadOnlineIcon);
		
		HBox.setHgrow(httpDownloadBox, Priority.ALWAYS);
		
		VBox mainLayout = new VBox();
		mainLayout.getChildren().addAll(folderNameLabel, labelChangeIcon, httpDownloadBox);
		
		this.getChildren().addAll(mainLayout);
	}


	@Override
	public void pullExtraValues(Map<String, Object> extras) {
		directoryRelativeLocation = (String) extras.get("relative-directory-root");
		logger.trace("Pull extra : {}", directoryRelativeLocation);
		
		folderNameLabel.setText(properties.getProperty("folder_edition_of").replace("{0}", directoryRelativeLocation));
	}


	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		folderNameLabel = new Label(properties.getProperty("folder_edition_of").replace("{0}", "?"));
		folderNameLabel.getStyleClass().add("title-style");
		
		labelChangeIcon = new Label(properties.getProperty("change_icon"));
		labelChangeIcon.getStyleClass().add("subtitle-style");
		
		downloadIconFromWebLabel = new Label(properties.getProperty("icon_from_internet_download"));
		
		editIconOnlineLocation = new TextField();
		editIconOnlineLocation.setPromptText("http://");
		
		downloadOnlineIcon = new Button(properties.getProperty("download_folder_icon"));
	}
	
	
}
