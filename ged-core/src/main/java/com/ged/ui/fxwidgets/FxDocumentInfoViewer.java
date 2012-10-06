package com.ged.ui.fxwidgets;

import java.util.Properties;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.apache.log4j.Logger;

import com.ged.models.GedDocument;
import com.tools.DateHelper;
import com.tools.PropertiesHelper;


public class FxDocumentInfoViewer extends VBox {

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
	
	
	public FxDocumentInfoViewer() {
		instantiateWidgets();
		
		this.getChildren().add(title);
		title.getStyleClass().add("document-info-title");
		
		this.getChildren().add(date);
		date.getStyleClass().add("document-info-date");
		
		this.getChildren().add(description);
		description.getStyleClass().add("document-info-description");
	}
	
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		title = new Label("Titre de test");
		description = new Label("Description de test");
		date = new Label("00/00/0000");
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

