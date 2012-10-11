package com.ged.ui.fxscreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

import com.ged.ui.FxMainWindow;
import com.ged.ui.fxwidgets.DocumentPreviewer;
import com.ged.ui.fxwidgets.FxDocumentInfoEditor;

public class AddDocumentScreen extends FxSoftwareScreen {

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
	private FxDocumentInfoEditor docInfoEditor;
	
	/**
	 * Document preview
	 */
	private DocumentPreviewer documentPreviewer;
	
	
	/**
	 * 
	 * @param mw
	 */
	public AddDocumentScreen(FxMainWindow mw) {
		super(mw);
	
		instanciateWidgets();
		
		VBox leftBox = new VBox();
		leftBox.setAlignment(Pos.CENTER);
		leftBox.setPadding(new Insets(25, 25, 25, 25));
		
		leftBox.getChildren().addAll(btnAddFromFS, btnAddFromScanner, docInfoEditor, btnSubmit);

		this.getChildren().addAll(leftBox);
	}

	
	private void instanciateWidgets() {

		//this.getStylesheets().add("templates/tools/calendarstyle.css");

		btnAddFromFS = new Button(properties.getProperty("add_from_hard_drive"));
		
		
		btnAddFromScanner = new Button(properties.getProperty("add_scan"));
		
		docInfoEditor = new FxDocumentInfoEditor(this);
		
		btnSubmit = new Button(properties.getProperty("save"));
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

}
