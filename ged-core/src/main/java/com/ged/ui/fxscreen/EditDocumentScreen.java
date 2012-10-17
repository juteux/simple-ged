package com.ged.ui.fxscreen;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

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
	 * A button to cancel modification
	 */
	private Button btnBack;
	
	/**
	 * Event handler
	 */
	private EditDocumentScreenEventHandler eventHandler; 
	
	
	public EditDocumentScreen(FxMainWindow mw) {
		super(mw);
		
		instanciateWidgets();
		
		HBox btnLayout = super.getControlButtonsLayout();
		btnLayout.getChildren().add(0, btnBack);
	}
	
	private void instanciateWidgets() {

		eventHandler = new EditDocumentScreenEventHandler(this);
		
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

	public Button getBtnBack() {
		return btnBack;
	}

}
