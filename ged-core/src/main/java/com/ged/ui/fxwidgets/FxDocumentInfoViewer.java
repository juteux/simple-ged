package com.ged.ui.fxwidgets;

import java.util.Properties;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.apache.log4j.Logger;

import com.ged.models.GedDocument;
import com.tools.DateHelper;
import com.tools.PropertiesHelper;


public class FxDocumentInfoViewer extends GridPane {

	private static final Logger logger = Logger.getLogger(FxDocumentInfoViewer.class);
	
	/**
	 * The software properties
	 */
	Properties properties = PropertiesHelper.getInstance().getProperties();

	
	
	/**
	 * This label contains document description
	 */
	private Label title;
	
	/**
	 * This label display document details
	 */
	private Label description;
	
	/**
	 * This label display document date
	 */
	private Label date;
	
	/**
	 * This button allow document modification
	 */
	private Button btnEditDocument;
	
	
	public FxDocumentInfoViewer() {
		instantiateWidgets();
		
		this.add(title, 0, 0);
		title.getStyleClass().add("document-info-title");
		
		this.add(date, 1, 0);
		date.getStyleClass().add("document-info-date");
		
		this.add(description, 0, 1, 2, 1);
		description.getStyleClass().add("document-info-description");
		
		GridPane.setHgrow(title, Priority.ALWAYS);
	}
	
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		title = new Label();
		description = new Label();
		date = new Label();
		
		setGedDocument(null);
	}
	
	
	/**
	 * Display document infos
	 * @param document
	 */
	public void setGedDocument(GedDocument document) {
		
		logger.debug("document selected changed");
		
		if (document == null) {
			title.setText("");
			description.setText("");
			date.setText("");
			return;
		}
		
		title.setText(document.getName());
		date.setText(DateHelper.calendarToString(document.getDate()));
		description.setText(document.getDescription());
	}
}

