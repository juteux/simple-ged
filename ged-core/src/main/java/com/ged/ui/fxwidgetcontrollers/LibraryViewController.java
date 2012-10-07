package com.ged.ui.fxwidgetcontrollers;

import java.io.File;
import java.util.Properties;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.services.GedDocumentService;
import com.ged.ui.fxwidgets.FxLibraryView;
import com.ged.ui.listeners.LibraryListener;
import com.tools.PropertiesHelper;

/**
 * The controller for the tree library view
 * 
 * @author xavier
 * 
 */
public class LibraryViewController implements Callback<TreeView<String>,TreeCell<String>>, ChangeListener<TreeItem<String>>  {

	private Properties properties = PropertiesHelper.getInstance().getProperties();
	
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
				//GedDocument d = GedDocumentService.findDocumentbyFilePath(itemPath);
			}
			
			
		}
		
		/*
		arg0.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            	Dragboard db = new Dragboard();
                ClipboardContent content = new ClipboardContent();
                content.putString("Hello!");
                db.setContent(content);
                mouseEvent.consume();
            }
        });
		*/
		
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
	
	
	public void addLibraryFolderUnderNode(TreeItem<String> node) {
    	if ( ! new File(Profile.getInstance().getLibraryRoot() + getFilePathFromTreeItem(node) + "/" + properties.getProperty("new_dir")).mkdir() ) {
			return;
		}
    	
		Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_library_folder")));
		ImageView iv = new ImageView(i);
		iv.setSmooth(true);
		iv.setFitWidth(FxLibraryView.TREE_ITEM_SIZE);
		iv.setFitHeight(FxLibraryView.TREE_ITEM_SIZE);
    	
        TreeItem<String> newFolder = new TreeItem<>(properties.getProperty("new_dir"), iv);
        
        node.setExpanded(true);
        
        node.getChildren().add(newFolder);
	}
	
	
	private final class TextFieldTreeCellImpl extends TreeCell<String> {
		
		private TextField textField;

		private ContextMenu addMenu = new ContextMenu();

		public TextFieldTreeCellImpl() {
            MenuItem addMenuItem = new MenuItem(properties.getProperty("add_directory"));
            addMenu.getItems().add(addMenuItem);
            addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                	addLibraryFolderUnderNode(getTreeItem());
                }
            });
		}

		@Override
		public void startEdit() {
			super.startEdit();

			if (!currentNodeIsDirectory()) {
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

					if (currentNodeIsRoot() || currentNodeIsDirectory()) {
                        setContextMenu(addMenu);
                    }
					
				}
			}
		}
		
		/**
		 * The root is excluded from this list, please call currentNodeIsRoot if you wan't to know if it is
		 */
		private boolean currentNodeIsDirectory() {
			if (currentNodeIsRoot()) {
				return false;
			}
			return new File(Profile.getInstance().getLibraryRoot() + getFilePathFromTreeItem(getTreeItem())).isDirectory();
		}

		private boolean currentNodeIsRoot() {
			return getFilePathFromTreeItem(getTreeItem()).isEmpty();
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
