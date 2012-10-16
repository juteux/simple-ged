package com.ged.ui.fxscreen;

import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import com.ged.models.GedDocument;
import com.ged.ui.FxMainWindow;
import com.ged.ui.fxscreen.eventhandler.EditDocumentScreenEventHandler;

/**
 * This screen is for document edition
 * 
 * @author xavier
 *
 */
public class EditDocumentScreen extends AddDocumentScreen {

	/**
	 * The current manipulated document
	 */
	private GedDocument document;
	
	/**
	 * A button for saving
	 */
	private Button btnSave;
	
	/**
	 * A button to cancel modification
	 */
	private Button btnBack;
	
	/**
	 * Event handler
	 */
	private EditDocumentScreenEventHandler eventHandler; 
	
	
	public EditDocumentScreen(FxMainWindow mw) {
		super(mw);
		
		HBox btnLayout = super.getControlButtonsLayout();
		btnLayout.getChildren().clear();
		btnLayout.getChildren().addAll(btnBack, btnSave);
		
		instanciateWidgets();
	}
	
	
	/**
	 * @see com.ged.ui.fxscreen.AddDocumentScreen#receiveExtraValue(java.util.Map)
	 */
	public void receiveExtraValue(Map<String, Object> extras) {
		
		setDocumentRelativeDirectory((String) extras.get("relative-document-root"));
		getDocumentPreviewer().setEditionMode(extras.get("open-in-edition-mode") != null && (boolean)extras.get("open-in-edition-mode"));
		
		document = (GedDocument)extras.get("ged-document");
		
		// TODO : fill fields
	}

	
	private void instanciateWidgets() {
		
		eventHandler = new EditDocumentScreenEventHandler(this);
		
		btnSave = new Button(properties.getProperty("save"));
		btnSave.setPrefSize(250, 80);
		btnSave.setOnAction(eventHandler);
		
		Image i1 = new Image(getClass().getResourceAsStream(properties.getProperty("ico_save")));
		ImageView iv1 = new ImageView(i1);
		iv1.setSmooth(true);
		iv1.setFitWidth(64);
		iv1.setFitHeight(64);
		btnSave.setGraphic(iv1);
		
		btnSave.setDisable(true);
		
		
		btnBack = new Button(properties.getProperty("back"));
		btnBack.setPrefSize(250, 80);
		btnBack.setOnAction(eventHandler);
		
		Image i2 = new Image(getClass().getResourceAsStream(properties.getProperty("ico_back")));
		ImageView iv2 = new ImageView(i2);
		iv2.setSmooth(true);
		iv2.setFitWidth(64);
		iv2.setFitHeight(64);
		btnBack.setGraphic(iv2);
	}


	public GedDocument getDocument() {
		return document;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public Button getBtnBack() {
		return btnBack;
	}

}
