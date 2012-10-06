package com.ged.ui.fxwidgetcontrollers;

import java.io.File;
import java.util.Properties;

import javax.swing.event.EventListenerList;

import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.services.GedDocumentService;
import com.ged.ui.fxwidgets.FxLibraryView;
import com.ged.ui.listeners.LibraryListener;
import com.tools.PropertiesHelper;

import org.apache.log4j.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	
	/**
	 * Event listener
	 */
	private final EventListenerList listeners = new EventListenerList();
	
	
	
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
		
		/*
		if (new File(Profile.getInstance().getLibraryRoot() + itemPath).isDirectory()) {
			logger.debug("a library folder : " + itemPath);
		}
		else { // some library document
			logger.debug("a library document : " + itemPath);
			GedDocument d = GedDocumentService.findDocumentbyFilePath(itemPath);
		}
		*/
		
        for(LibraryListener listener : getLibraryListeners()) {
            listener.selectionChanged(itemPath);
        }
	}
	
	
	// for externals listeners

	public void addLibraryListener(LibraryListener listener) {
		listeners.add(LibraryListener.class, listener);
	}

	public void removeLibraryListener(LibraryListener listener) {
		listeners.remove(LibraryListener.class, listener);
	}

	public LibraryListener[] getLibraryListeners() {
		return listeners.getListeners(LibraryListener.class);
	}

	
	
	private String getFilePathFromTreeItem(TreeItem<String> item) {
		
		if (item == null || item.getParent() == null) {
			return "";
		}
		
		String itemPath = item.getValue();
		TreeItem<String> parent = item.getParent();
		while (parent != null) {
			if (parent.getParent() == null) { // we wan't to exclude root from the final path
				break;
			}
			itemPath = parent.getValue() + "/" + itemPath;
			parent = parent.getParent();
		}
		return itemPath;
	}
	
	
	
	private final class TextFieldTreeCellImpl extends TreeCell<String> {

		private Properties properties = PropertiesHelper.getInstance().getProperties();
		
		private TextField textField;

		private ContextMenu addMenu = new ContextMenu();

		public TextFieldTreeCellImpl() {
            MenuItem addMenuItem = new MenuItem(properties.getProperty("add_directory"));
            addMenu.getItems().add(addMenuItem);
            addMenuItem.setOnAction(new EventHandler() {
                public void handle(Event t) {
                	
                	if ( ! new File(Profile.getInstance().getLibraryRoot() + getFilePathFromTreeItem(getTreeItem()) + "/" + properties.getProperty("new_dir")).mkdir() ) {
        				return;
        			}
                	
        			Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_library_folder")));
        			ImageView iv = new ImageView(i);
        			iv.setSmooth(true);
        			iv.setFitWidth(FxLibraryView.TREE_ITEM_SIZE);
        			iv.setFitHeight(FxLibraryView.TREE_ITEM_SIZE);
                	
                    TreeItem newFolder = new TreeItem<String>(properties.getProperty("new_dir"), iv);
                    
                    getTreeItem().setExpanded(true);
                    
                    getTreeItem().getChildren().add(newFolder);
                }
            });
		}

		@Override
		public void startEdit() {
			super.startEdit();

			if (!currentNodeIsRootOrDirectory()) {
				return;
			}
			
			if (textField == null) {
				createTextField(); 
				// TODO : open document edition
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

					if (currentNodeIsRootOrDirectory()) {
                        setContextMenu(addMenu);
                    }
					
				}
			}
		}
		
		
		private boolean currentNodeIsRootOrDirectory() {
			return getFilePathFromTreeItem(getTreeItem()).isEmpty() || new File(Profile.getInstance().getLibraryRoot() + getFilePathFromTreeItem(getTreeItem())).isDirectory();
		}
		

		private void createTextField() {
			textField = new TextField(getString());
			textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent t) {
					if (t.getCode() == KeyCode.ENTER) {
						
						logger.info("renaming from : " + getFilePathFromTreeItem(getTreeItem()) + " => " + getFilePathFromTreeItem(getTreeItem().getParent()) + "/" + textField.getText());
						GedDocumentService.renameDocumentFile(getFilePathFromTreeItem(getTreeItem()), getFilePathFromTreeItem(getTreeItem().getParent()) + "/" + textField.getText());
						
						
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
