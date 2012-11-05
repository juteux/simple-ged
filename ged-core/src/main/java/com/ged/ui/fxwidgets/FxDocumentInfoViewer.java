package com.ged.ui.fxwidgets;

import java.util.Properties;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.apache.log4j.Logger;

import com.ged.models.GedDocument;
import com.ged.ui.fxwidgets.eventhandler.DocumentInfoViewerEventHandler;
import com.tools.DateHelper;
import com.tools.PropertiesHelper;


public class FxDocumentInfoViewer extends GridPane {

	private static final Logger logger = Logger.getLogger(FxDocumentInfoViewer.class);
	
	/**
	 * The software properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
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
	
	/**
	 * My event handler
	 */
	private DocumentInfoViewerEventHandler eventHandler;
	
	
	public FxDocumentInfoViewer() {
		instantiateWidgets();
		
		//         	0                  1				2
		//   +------------------+---------------+---------------+
		// 0 |					|				|				|
		// 	 |		Title		|		Date	|				|
		//	 |					|				| Edit button	|
		//   |------------------+---------------+	(btnBox)	|
		//   |									|				|
		// 1 |			Description             |				|
		//   |           						|				|
		//   +----------------------------------+---------------+
		
		
		this.add(title, 0, 0);
		title.getStyleClass().add("document-info-title");
		
		this.add(date, 1, 0);
		date.getStyleClass().add("document-info-date");
		
		VBox btnBox = new VBox(10);
		btnBox.setPadding(new Insets(5,5,5,25));
		btnBox.getChildren().addAll(btnEditDocument);
		
		this.add(btnBox, 2, 0, 1, 2);
		date.getStyleClass().add("document-info-date");
		
		this.add(description, 0, 1, 2, 1);
		description.getStyleClass().add("document-info-description");
		
		GridPane.setHgrow(title, Priority.ALWAYS);
		GridPane.setHgrow(date, Priority.NEVER);
		GridPane.setHgrow(description, Priority.ALWAYS);
		GridPane.setHgrow(btnEditDocument, Priority.NEVER);
	}
	
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		eventHandler = new DocumentInfoViewerEventHandler(this);
		
		title = new Label();
		description = new Label();
		date = new Label();
		
		btnEditDocument = new Button(properties.getProperty("modify"));
		btnEditDocument.setDisable(true);
		btnEditDocument.setOnAction(eventHandler);
		btnEditDocument.setPrefWidth(150);
		
		Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_edit_doc")));
		ImageView iv = new ImageView(i);
		iv.setSmooth(true);
		iv.setFitWidth(32);
		iv.setFitHeight(32);
		btnEditDocument.setGraphic(iv);
		
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


	public Button getBtnEditDocument() {
		return btnEditDocument;
	}


	public DocumentInfoViewerEventHandler getEventHandler() {
		return eventHandler;
	}
	
	
}

