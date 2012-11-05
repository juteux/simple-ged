package com.ged.ui.fxwidgets.eventhandler;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.models.GedDocumentFile;
import com.ged.services.GedDocumentService;
import com.ged.ui.fxscreen.FxSoftwareScreen.Screen;
import com.ged.ui.fxwidgets.FxLibraryView;
import com.ged.ui.listeners.DocumentInfoViewerListener;
import com.ged.ui.listeners.LibraryListener;
import com.ged.ui.listeners.QuickSearchListener;
import com.ged.ui.listeners.LibraryListener.LIBRARY_FILE_TYPE;
import com.tools.PropertiesHelper;
import com.tools.javafx.ModalConfirm;
import com.tools.javafx.ModalConfirmResponse;

/**
 * The event handler for the tree library view
 * 
 * @author xavier
 * 
 */
public class LibraryViewEventHandler implements Callback<TreeView<String>,TreeCell<String>>, ChangeListener<TreeItem<String>>, DocumentInfoViewerListener, QuickSearchListener {

	/**
	 * Global properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	private static final Logger logger = Logger.getLogger(LibraryViewEventHandler.class);
	
	/**
	 * The controlled object
	 */
	private WeakReference<FxLibraryView> libraryView;
	
	/**
	 * Event listener
	 */
	private final EventListenerList listeners = new EventListenerList();
	
	/**
	 * Drag & drop format 
	 */
	public static final DataFormat dataFormatLibraryTreeItem =  new DataFormat("x-ged-library-tree-item");

	
	private TreeItem<String> draggedItem;
	
	
	public LibraryViewEventHandler(FxLibraryView libraryView) {
		this.libraryView = new WeakReference<>(libraryView);
	}
	
	
	/**
	 * Cell edition or drag & drop
	 */
	@Override
	public TreeCell<String> call(final TreeView<String> arg0) {		
		
		arg0.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
            	logger.info("drag started");
            	
            	// forbidden on root
            	if (getFilePathFromTreeItem(libraryView.get().getSelectionModel().getSelectedItem()).isEmpty()) {
            		mouseEvent.consume();
            		return;
            	}
            	
            	draggedItem = libraryView.get().getSelectionModel().getSelectedItem();
            	logger.info(draggedItem.getValue());
            	
            	Dragboard db = libraryView.get().startDragAndDrop(TransferMode.MOVE);
                
                TreeItem<String> item = libraryView.get().getSelectionModel().getSelectedItem();
                ClipboardContent content = new ClipboardContent();
                content.put(dataFormatLibraryTreeItem, item.toString());            
                
                db.setContent(content);
                
                mouseEvent.consume();
            }
        });
		
		
		return new TextFieldTreeCellImpl();
	}
	
	

	/**
	 * Selection changed
	 */
	@Override
	public void changed(ObservableValue<? extends TreeItem<String>> arg0, TreeItem<String> arg1, TreeItem<String> newItem) {

		if (newItem == null) {
			return;
		}
		
		logger.debug("selection changed : " + newItem.getValue());
		
		String itemPath = getFilePathFromTreeItem(newItem);
		
		logger.debug("Full item path : " + itemPath);
		
		LIBRARY_FILE_TYPE type = LIBRARY_FILE_TYPE.LIBRARY_FILE;
		if (nodeIsRoot(newItem)) {
			type = LIBRARY_FILE_TYPE.LIBRARY_ROOT;
		} 
		else if (nodeIsDirectory(newItem)) {
			type = LIBRARY_FILE_TYPE.LIBRARY_DIR;
		}
		
        for(LibraryListener listener : getLibraryListeners()) {
            listener.selectionChanged(itemPath);
            listener.selectionChanged(type);
        }
	}
	
	
	public String getCurrentItemRelativePath() {
		return getFilePathFromTreeItem(libraryView.get().getSelectionModel().getSelectedItem());
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
			itemPath = parent.getValue() + File.separatorChar + itemPath;
			parent = parent.getParent();
		}
		return itemPath;
	}
	
	
	/**
	 * The root is excluded from this list, please call currentNodeIsRoot if you wan't to know if it is
	 */
	private boolean nodeIsDirectory(TreeItem<String> node) {
		if (nodeIsRoot(node)) {
			return false;
		}
		return new File(Profile.getInstance().getLibraryRoot() + getFilePathFromTreeItem(node)).isDirectory();
	}

	private boolean nodeIsRoot(TreeItem<String> node) {
		return getFilePathFromTreeItem(node).isEmpty();
	}
	
	
	
	
	public void addLibraryFolderUnderNode(TreeItem<String> node) {
    	if ( ! new File(Profile.getInstance().getLibraryRoot() + getFilePathFromTreeItem(node) + File.separatorChar + properties.getProperty("new_dir")).mkdir() ) {
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
        libraryView.get().getSelectionModel().select(newFolder);
	}
	
	
	public void addDocumentUnderNode(TreeItem<String> node) {
		
		libraryView.get().getParentScreen().pushScreen(Screen.ADD_DOC_SCREEN);
		
		Map<String, Object> extras = new HashMap<>();
		extras.put("relative-document-root", getFilePathFromTreeItem(node));
		
		libraryView.get().getParentScreen().pushExtraValues(extras);
	}
	
	
	public void openEditionForNode(TreeItem<String> node) {
		
		if (nodeIsRoot(node)) {
			return; // we never edit the root !
		}
		
		if (nodeIsDirectory(node)) {
			// will come later
		}
		else { // node is a file
			
			libraryView.get().getParentScreen().pushScreen(Screen.EDIT_DOC_SCREEN);

			Map<String, Object> extras = new HashMap<>();
			extras.put("relative-document-root", getFilePathFromTreeItem(node));
			extras.put("ged-document", GedDocumentService.getDocumentFromFile(getFilePathFromTreeItem(node)));
			
			if (GedDocumentService.getDocumentFromFile(getFilePathFromTreeItem(node)) == null) {
				extras.put("system-file", new File(Profile.getInstance().getLibraryRoot() + getCurrentItemRelativePath()));
			}
			
			libraryView.get().getParentScreen().pushExtraValues(extras);
			
		}
		
	}
	
	
	private final class TextFieldTreeCellImpl extends TreeCell<String> {
		
		private TextField textField;

		
		private ContextMenu directoryContextMenu = new ContextMenu();

		private ContextMenu rootContextMenu = new ContextMenu();
		
		private MenuItem directoryAddMenuItem;
		
		private MenuItem directoryDeleteMenuItem;
		
		private MenuItem directoryRenameMenuItem;
		
		private MenuItem directoryAddDocumentItem;
		
		
		public TextFieldTreeCellImpl() {
            
			directoryAddMenuItem = new MenuItem(properties.getProperty("add_directory"));
			directoryRenameMenuItem = new MenuItem(properties.getProperty("rename"));
            directoryDeleteMenuItem = new MenuItem(properties.getProperty("delete"));
            directoryAddDocumentItem = new MenuItem(properties.getProperty("add_document"));
            
            rootContextMenu.getItems().addAll(directoryAddDocumentItem, directoryAddMenuItem);
            directoryContextMenu.getItems().addAll(directoryAddDocumentItem, directoryAddMenuItem, directoryRenameMenuItem, directoryDeleteMenuItem);
            
            directoryAddMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            	@Override
                public void handle(ActionEvent t) {
                	addLibraryFolderUnderNode(getTreeItem());
                }
            });
            
            directoryRenameMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            	@Override
                public void handle(ActionEvent t) {
                	startEdit();
                }
            });
            
            directoryDeleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            	@Override
                public void handle(ActionEvent t) {
                	ModalConfirm.show(libraryView.get().getParentScreen().getMainStage(), new ModalConfirmResponse() {
            			@Override
            			public void confirm() {
            				GedDocumentService.deleteDocumentFile(getFilePathFromTreeItem(getTreeItem()));
            				getTreeItem().getParent().getChildren().remove(getTreeItem());
            			}
            			@Override
            			public void cancel() {
            				// do nothing
            			}
            		}, properties.getProperty("wanna_delete_item_named").replace("{0}", getString()));
                }
            });
            
            directoryAddDocumentItem.setOnAction(new EventHandler<ActionEvent>() {
            	@Override
                public void handle(ActionEvent t) {
                	addDocumentUnderNode(getTreeItem());
                }
            });
            
            
            
            final TextFieldTreeCellImpl self = this;

            this.setOnDragOver(new EventHandler<DragEvent>() {

				@Override
				public void handle(DragEvent event) {
					
					// move from the tree
					if (event.getDragboard().hasContent(dataFormatLibraryTreeItem)) {

						boolean dropEnable = true;
						
						// me on me ?
						if (draggedItem == getTreeItem()) {
							dropEnable = false;
						}
							
						// the target is a dir ?
						if (currentNodeIsFile()) {
							dropEnable = false;
						}
						
						if (dropEnable) {
							event.acceptTransferModes(TransferMode.MOVE);
							self.getStyleClass().add("over-element");
						}
					}
					else { // move from system ?
						//event.acceptTransferModes(TransferMode.MOVE);
					}
					
				}
			});
            
            
            this.setOnDragExited(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					self.getStyleClass().remove("over-element");
				}
			});
            

    		this.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                	logger.info("drop event");
                	
                	TreeItem<String> sourceItem = draggedItem;
                	TreeItem<String> targetItem = getTreeItem();
                	
                	String sourcePath = getFilePathFromTreeItem(sourceItem);
                	String targetPath = getFilePathFromTreeItem(targetItem);

                    for(LibraryListener listener : getLibraryListeners()) {
                        listener.releaseOpenedFiles();
                    }
                	
                	File f = new File(Profile.getInstance().getLibraryRoot() + sourcePath);
                	
                	GedDocumentService.renameDocumentFile(sourcePath, targetPath + File.separatorChar + f.getName());
                	
                	sourceItem.getParent().getChildren().remove(sourceItem);
                	targetItem.getChildren().add(sourceItem);
                	
                	//targetItem.setExpanded(true);
                    //libraryView.getSelectionModel().select(targetItem);
                	
    				event.acceptTransferModes(TransferMode.MOVE);    
    				event.setDropCompleted(true);
    				
                	event.consume();
                }
            });
    		
		}

		
		
		@Override
		public void startEdit() {
			super.startEdit();

			if (currentNodeIsFile()) {
				openEditionForNode(getTreeItem());
				return;
			}
			
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

					if (currentNodeIsDirectory()) {
                        setContextMenu(directoryContextMenu);
                    }
					else if (currentNodeIsRoot()) {
						setContextMenu(rootContextMenu);
					}
					
				}
			}
		}
		
		/**
		 * The root is excluded from this list, please call currentNodeIsRoot if you wan't to know if it is
		 */
		private boolean currentNodeIsDirectory() {
			return nodeIsDirectory(getTreeItem());
		}

		private boolean currentNodeIsRoot() {
			return nodeIsRoot(getTreeItem());
		}
		
		private boolean currentNodeIsFile() {
			return ! (currentNodeIsDirectory() || currentNodeIsRoot());
		}
		

		private void createTextField() {
			textField = new TextField(getString());
			
			textField.setPrefHeight(FxLibraryView.TREE_ITEM_SIZE);
			
			textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent t) {
					if (t.getCode() == KeyCode.ENTER) {
						
						logger.info("renaming from : " + getFilePathFromTreeItem(getTreeItem()) + " => " + getFilePathFromTreeItem(getTreeItem().getParent()) + File.separatorChar + textField.getText());
						GedDocumentService.renameDocumentFile(getFilePathFromTreeItem(getTreeItem()), getFilePathFromTreeItem(getTreeItem().getParent()) + File.separatorChar + textField.getText());
						
						
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


	@Override
	public void askForDocumentEdition() {
		openEditionForNode(libraryView.get().getSelectionModel().getSelectedItem());
	}


	@Override
	public void search(String pattern) {
		
		if (pattern.isEmpty()) {
			libraryView.get().buildTree();
		}
		
		List<GedDocumentFile> files = GedDocumentService.searchForWords(pattern);
		
		if (files.size() == 0) {
			logger.info("No matching document for pattern : "+ pattern);
		}
		
		libraryView.get();
		TreeItem<String> newRoot = new TreeItem<>(FxLibraryView.convertToNodeName(Profile.getInstance().getLibraryRoot()), libraryView.get().getIconForNode(""));
		newRoot.setExpanded(true);
		
		for (GedDocumentFile f : files) {
			logger.debug("Matching file : " + f.getRelativeFilePath());
		
			String[] items = f.getRelativeFilePath().split("/");
			String stack = "";
			
			TreeItem<String> child = newRoot;
			for (String item : items) {
				logger.debug("item : " + relativeToAbsolultPath(item));
				
				// we create the child if it doesn't exists
				if (! hasChild(child, item)) {
					child.getChildren().add(new TreeItem<String>(FxLibraryView.convertToNodeName(item), libraryView.get().getIconForNode(relativeToAbsolultPath(stack + item))));
				}
				
				// get the child
				child = getChild(child, item);
				child.setExpanded(true);
				
				stack += item + "/";
			}
		}
		libraryView.get().setRoot(newRoot);
	}

	
	/**
	 * Convert a relative file path to library root to absolute path (including library root)
	 * 
	 * @param relativePath
	 * 				The path to convert (relative to library root)
	 * 
	 * @return
	 * 				The absolute file path
	 */
	private String relativeToAbsolultPath(String relativePath) {
		return Profile.getInstance().getLibraryRoot() + relativePath;
	}
	
	
	/**
	 * Has item the direct child s ?
	 * 
	 * @param item
	 * 			The parent node
	 * @param s
	 * 			The child we're looking for
	 * 
	 * @return
	 * 			True if s is a direct child of item, false otherwise
	 */
	private boolean hasChild(TreeItem<String> item, String s) {
		for (TreeItem<String> i : item.getChildren()) {
			if (i.getValue().equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the direct child of item named by s
	 * 
	 * @param item
	 * 			The parent node
	 * @param s
	 * 			The child we're looking for
	 * 
	 * @return
	 * 			The searched child if it's exists, null otherwise
	 */
	private TreeItem<String> getChild(TreeItem<String> item, String s) {
		for (TreeItem<String> i : item.getChildren()) {
			if (i.getValue().equals(s)) {
				return i;
			}
		}
		return null;
	}
	
}
