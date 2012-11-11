package com.simple.ged.ui.screen;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.simple.ged.models.GedDocument;
import com.simple.ged.ui.MainWindow;
import com.simple.ged.ui.screen.eventhandler.AddDocumentScreenEventHandler;
import com.simple.ged.ui.widgets.DocumentPreviewer;
import com.simple.ged.ui.widgets.DocumentInfoEditor;

/**
 * The view for adding a document in the ged
 * 
 * @author xavier
 *
 */
public class DocumentConfigurationScreen extends SoftwareScreen {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(DocumentConfigurationScreen.class);
	
	/**
	 * Button add from file system
	 */
	private Button btnAddFromFS;
	
	/**
	 * Button add from scanner
	 */
	private Button btnAddFromScanner;
	
	/**
	 * Button to submit new document
	 */
	private Button btnSubmit;
	
	/**
	 * Document info editor
	 */
	private DocumentInfoEditor docInfoEditor;
	
	/**
	 * Document preview
	 */
	private DocumentPreviewer documentPreviewer;
	
	/**
	 * My event handler
	 */
	private AddDocumentScreenEventHandler eventHandler;
	
	/**
	 * My target directory (relative path)
	 */
	private String documentRelativeDirectory; 
	
	/**
	 * 
	 * My child will be allowed to modify this layout
	 */
	private HBox controlButtonsLayout;
	
	/**
	 * The current manipulated document
	 * This document is null by default (a new document)
	 */
	private GedDocument document;
	
	
	/**
	 * 
	 * @param mw
	 */
	public DocumentConfigurationScreen(MainWindow mw) {
		super(mw);
	
		instanciateWidgets();
		
		VBox leftBox = new VBox(5); // space between layout components
		leftBox.setAlignment(Pos.CENTER);
		leftBox.setPadding(new Insets(25, 25, 25, 25));
		
		controlButtonsLayout = new HBox(5);
		controlButtonsLayout.getChildren().addAll(btnSubmit);
		
		leftBox.getChildren().addAll(btnAddFromFS, btnAddFromScanner, docInfoEditor, controlButtonsLayout);

		HBox.setHgrow(documentPreviewer, Priority.ALWAYS);
		VBox.setVgrow(documentPreviewer, Priority.ALWAYS);
		
		this.getChildren().addAll(leftBox, documentPreviewer);
	}
	
	
	/**
	 * Get our target directory
	 */
	@Override
	public void pullExtraValues(Map<String, Object> extras) {
		setDocumentRelativeDirectory((String) extras.get("relative-document-root"));
		
		document = (GedDocument)extras.get("ged-document");
		logger.info("ged-document : " + (document == null ? "null" : "not null"));
		
		docInfoEditor.getEventHandler().setDocument(document);
		documentPreviewer.getEventHandler().setDocument(document);
		
		if (extras.containsKey("system-file")) {
			documentPreviewer.addFile((File)extras.get("system-file"));
			logger.info("system-file : " + ((File)extras.get("system-file")).getAbsolutePath());
		}
	}

	
	private void instanciateWidgets() {

		eventHandler = new AddDocumentScreenEventHandler(this);
		
		btnAddFromFS = new Button(properties.getProperty("add_from_hard_drive"));
		btnAddFromFS.setPrefSize(300, 80);
		btnAddFromFS.setOnAction(eventHandler);
		
		Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_library_root")));
		ImageView iv = new ImageView(i);
		iv.setSmooth(true);
		iv.setFitWidth(64);
		iv.setFitHeight(64);
		btnAddFromFS.setGraphic(iv);
		
		
		btnAddFromScanner = new Button(properties.getProperty("add_scan"));
		btnAddFromScanner.setPrefSize(300, 80);
		btnAddFromScanner.setOnAction(eventHandler);
		
		Image i2 = new Image(getClass().getResourceAsStream(properties.getProperty("ico_scan")));
		ImageView iv2 = new ImageView(i2);
		iv2.setSmooth(true);
		iv2.setFitWidth(64);
		iv2.setFitHeight(64);
		btnAddFromScanner.setGraphic(iv2);
		
		
		docInfoEditor = new DocumentInfoEditor();
		docInfoEditor.getEventHandler().addDocumentInfoEditorListener(eventHandler);
		
		btnSubmit = new Button(properties.getProperty("save"));
		btnSubmit.setPrefSize(250, 80);
		btnSubmit.setOnAction(eventHandler);
		
		Image i3 = new Image(getClass().getResourceAsStream(properties.getProperty("ico_save")));
		ImageView iv3 = new ImageView(i3);
		iv3.setSmooth(true);
		iv3.setFitWidth(64);
		iv3.setFitHeight(64);
		btnSubmit.setGraphic(iv3);
		
		btnSubmit.setDisable(true);
		
		
		documentPreviewer = new DocumentPreviewer();
		documentPreviewer.getEventHandler().addDocumentPreviewListener(eventHandler);
		documentPreviewer.setEditionMode(true);
	}


	public Button getBtnAddFromFS() {
		return btnAddFromFS;
	}

	public Button getBtnAddFromScanner() {
		return btnAddFromScanner;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public DocumentPreviewer getDocumentPreviewer() {
		return documentPreviewer;
	}

	public DocumentInfoEditor getDocInfoEditor() {
		return docInfoEditor;
	}

	public String getDocumentRelativeDirectory() {
		return documentRelativeDirectory;
	}

	/**
	 * Only enable for my children
	 */
	protected HBox getControlButtonsLayout() {
		return controlButtonsLayout;
	}

	protected void setDocumentRelativeDirectory(String documentRelativeDirectory) {
		this.documentRelativeDirectory = documentRelativeDirectory;
	}

	public GedDocument getDocument() {
		return document;
	}
	
}
