package com.ged.ui.fxpreviewwidgets;

import java.lang.ref.WeakReference;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import com.ged.ui.fxwidgets.eventhandler.LibraryViewEventHandler;
import com.tools.PropertiesHelper;

/**
 * Not really a previewer, I know...
 * 
 * @author xavier
 *
 */
public class AddDocumentPreviewer extends AbstractFilePreviewer {

	/**
	 * The node we're on
	 */
	private TreeItem<String> parentNode;
	
	/**
	 * The library controller contains method to make actions on the node
	 */
	private WeakReference<LibraryViewEventHandler> libraryController;
	
	
	public AddDocumentPreviewer(TreeItem<String> parentNode, LibraryViewEventHandler libraryController) {
		super("");
		
		this.parentNode = parentNode;
		this.libraryController = new WeakReference<>(libraryController);
		
		try {
			this.load();
		} catch (CannotCreatePreviewerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load() throws CannotCreatePreviewerException {
		
		Properties properties = PropertiesHelper.getInstance().getProperties();
		
		// --
		
		Button btnNewLibraryFolder = new Button(properties.getProperty("add_directory"));
		btnNewLibraryFolder.getStyleClass().add("btn-on-add-document-previewer");
		
		btnNewLibraryFolder.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				libraryController.get().addLibraryFolderUnderNode(parentNode);
			}
		});
		
		Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_new_folder")));
		ImageView iv = new ImageView(i);
		btnNewLibraryFolder.setGraphic(iv);
		
		btnNewLibraryFolder.setPrefSize(300, 80);
		
		// --
		
		Button btnAddDocument = new Button(properties.getProperty("goto_add_document"));
		btnAddDocument.getStyleClass().add("btn-on-add-document-previewer");
		
		btnAddDocument.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				libraryController.get().addDocumentUnderNode(parentNode);
			}
		});
		
		Image i2 = new Image(getClass().getResourceAsStream(properties.getProperty("ico_new_doc")));
		ImageView iv2 = new ImageView(i2); 
		btnAddDocument.setGraphic(iv2);
		
		btnAddDocument.setPrefSize(300, 80);
		
		// --
		
		VBox mainLayout = new VBox(30); // space between buttons
		mainLayout.getChildren().addAll(btnNewLibraryFolder, btnAddDocument);
	
		this.getChildren().add(mainLayout);
	}

	@Override
	public boolean isPrintable() {
		return false;
	}

	@Override
	public boolean isOpenable() {
		return false;
	}
	
}
