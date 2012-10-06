package com.ged.ui.fxcontrollers;

import java.io.File;

import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.services.GedDocumentService;
import com.ged.ui.fxwidgets.FxLibraryView;
import org.apache.log4j.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/**
 * The controller for the tree library view
 * 
 * @author xavier
 * 
 */
public class LibraryViewController implements Callback<TreeView<String>,TreeCell<String>>, ChangeListener<TreeItem<String>>  {

	private static final Logger logger = Logger.getLogger(LibraryViewController.class);
	
	/**
	 * The controlled object
	 */
	private FxLibraryView libraryView;
	
	public LibraryViewController(FxLibraryView libraryView) {
		this.libraryView = libraryView;
	}
	
	
	/**
	 * Cell edition
	 */
	@Override
	public TreeCell<String> call(TreeView<String> arg0) {
		
		if (arg0.getSelectionModel().getSelectedItem() != null) {

			String itemPath = getFilePathFromTreeItem(arg0.getSelectionModel().getSelectedItem());
			
			// TODO : add root modification
			
			if (new File(Profile.getInstance().getLibraryRoot() + itemPath).isDirectory()) {
				logger.debug("a library folder : " + itemPath);
			}
			else { // some library document
				logger.debug("a library document : " + itemPath);
				GedDocument d = GedDocumentService.findDocumentbyFilePath(itemPath);
			}
			
			
		}

		return new TextFieldTreeCellImpl();
	}
	
	
	/**
	 * Selection changed
	 */
	@Override
	public void changed(ObservableValue<? extends TreeItem<String>> arg0, TreeItem<String> arg1, TreeItem<String> newItem) {
		// TODO Auto-generated method stub
		logger.debug("selection changed : " + newItem.getValue());
		
		String itemPath = getFilePathFromTreeItem(newItem);
		
		logger.debug("Full item path : " + itemPath);
		
		if (new File(Profile.getInstance().getLibraryRoot() + itemPath).isDirectory()) {
			logger.debug("a library folder : " + itemPath);
		}
		else { // some library document
			logger.debug("a library document : " + itemPath);
			GedDocument d = GedDocumentService.findDocumentbyFilePath(itemPath);
		}
		
	}
	
	
	
	private String getFilePathFromTreeItem(TreeItem<String> item) {
		String itemPath = item.getValue();
		TreeItem<String> parent = item.getParent();
		while (parent != null) {
			itemPath = parent.getValue() + "/" + itemPath;
			parent = parent.getParent();
		}
		return itemPath;
	}
	
	private final class TextFieldTreeCellImpl extends TreeCell<String> {

		private TextField textField;

		public TextFieldTreeCellImpl() {
		}

		@Override
		public void startEdit() {
			super.startEdit();

			if (textField == null) {
				createTextField();
			}
			setText(null);
			setGraphic(textField);
			textField.selectAll();
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();
			setText((String) getItem());
			setGraphic(getTreeItem().getGraphic());
		}

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (isEditing()) {
					if (textField != null) {
						textField.setText(getString());
					}
					setText(null);
					setGraphic(textField);
				} else {
					setText(getString());
					setGraphic(getTreeItem().getGraphic());
				}
			}
		}

		private void createTextField() {
			textField = new TextField(getString());
			textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent t) {
					if (t.getCode() == KeyCode.ENTER) {
						commitEdit(textField.getText());
					} else if (t.getCode() == KeyCode.ESCAPE) {
						cancelEdit();
					}
				}
			});
		}

		private String getString() {
			return getItem() == null ? "" : getItem().toString();
		}
	}


}
