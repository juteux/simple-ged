package com.ged.ui.fxscreen;

import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.ged.ui.FxMainWindow;
import com.ged.ui.fxscreen.eventhandler.AddDocumentScreenEventHandler;
import com.ged.ui.fxwidgets.DocumentPreviewer;
import com.ged.ui.fxwidgets.FxDocumentInfoEditor;

/**
 * The view for adding a document in the ged
 * 
 * @author xavier
 *
 */
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
	 * My event handler
	 */
	private AddDocumentScreenEventHandler eventHandler;
	
	/**
	 * My target directory (relative path)
	 */
	private String documentRelativeDirectory; 
	
	
	/**
	 * 
	 * @param mw
	 */
	public AddDocumentScreen(FxMainWindow mw) {
		super(mw);
	
		instanciateWidgets();
		
		VBox leftBox = new VBox(5); // space between layout components
		leftBox.setAlignment(Pos.CENTER);
		leftBox.setPadding(new Insets(25, 25, 25, 25));
		
		leftBox.getChildren().addAll(btnAddFromFS, btnAddFromScanner, docInfoEditor, btnSubmit);

		HBox.setHgrow(documentPreviewer, Priority.ALWAYS);
		VBox.setVgrow(documentPreviewer, Priority.ALWAYS);
		
		this.getChildren().addAll(leftBox, documentPreviewer);
	}
	
	
	/**
	 * Get our target directory
	 */
	@Override
	public void receiveExtraValue(Map<String, Object> extra) {
		documentRelativeDirectory = (String) extra.get("relative-document-root");
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
		
		
		docInfoEditor = new FxDocumentInfoEditor();
		docInfoEditor.getEventHandler().addDocumentInfoEditorListener(eventHandler);
		
		btnSubmit = new Button(properties.getProperty("save"));
		btnSubmit.setPrefSize(300, 80);
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

	public FxDocumentInfoEditor getDocInfoEditor() {
		return docInfoEditor;
	}

	public String getDocumentRelativeDirectory() {
		return documentRelativeDirectory;
	}
	
}
