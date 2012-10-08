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

import com.ged.ui.fxwidgetcontrollers.LibraryViewController;
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
	private WeakReference<LibraryViewController> libraryController;
	
	
	public AddDocumentPreviewer(TreeItem<String> parentNode, LibraryViewController libraryController) {
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
		
		Button btnNewLibraryFolder = new Button(properties.getProperty("add_directory"));
		btnNewLibraryFolder.getStyleClass().add("btn-on-add-document-previewer");
		
		btnNewLibraryFolder.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				libraryController.get().addLibraryFolderUnderNode(parentNode);
			}
		});
		
		// TODO : clean this
		
		Button btnAddDocument = new Button(properties.getProperty("goto_add_document"));
		btnAddDocument.getStyleClass().add("btn-on-add-document-previewer");
		
		Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_add_doc")));
		ImageView iv = new ImageView(i);
		iv.setSmooth(true);
		iv.setFitWidth(50);
		iv.setFitHeight(50);
		
		ImageView iv2 = new ImageView(i);
		iv2.setSmooth(true);
		iv2.setFitWidth(50);
		iv2.setFitHeight(50);
		
		btnNewLibraryFolder.setGraphic(iv2);
		btnAddDocument.setGraphic(iv);
		
		VBox mainLayout = new VBox();
		mainLayout.getChildren().addAll(btnNewLibraryFolder, btnAddDocument);
		
		this.getChildren().add(mainLayout);
	}

	@Override
	public boolean isPrintable() {
		return false;
	}

}
