package com.ged.ui.fxwidgets;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Properties;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.ui.fxscreen.FxSoftwareScreen;
import com.ged.ui.fxwidgets.eventhandler.LibraryViewEventHandler;
import com.tools.FileHelper;
import com.tools.PropertiesHelper;

/**
 * A view of the library, a tree with files in GED directory
 * 
 */

public class FxLibraryView extends TreeView<String> {

	/**
	 * Tree item icon size (px)
	 */
	public static final int TREE_ITEM_SIZE = 30;
	
	private static final Logger logger = Logger.getLogger(FxLibraryView.class);
	
	/**
	 * Not setted yet (always false)
	 */
	private boolean showDirectoryOnly = false;
	
	/**
	 * My event handler
	 */
	private LibraryViewEventHandler eventHandler;
	
	/**
	 * The software properties
	 */
	Properties properties = PropertiesHelper.getInstance().getProperties();
	
	/**
	 * My parent
	 */
	private WeakReference<FxSoftwareScreen> parentScreen;
	
	/**
	 * Root item
	 */
	private TreeItem<String> rootItem;
	

	public FxLibraryView(FxSoftwareScreen parentScreen) {
    	
    	this.parentScreen = new WeakReference<>(parentScreen);
    	
    	buildTree();
    	
    	eventHandler = new LibraryViewEventHandler(this);
    	
    	this.setEditable(true);
    	this.setCellFactory(eventHandler);
    	this.getSelectionModel().select(rootItem);
    	
    	this.getSelectionModel().selectedItemProperty().addListener(eventHandler); 
    }
    
   
    
	/**
	 * Get the path to display in a tree node
	 */
	private static String convertToNodeName(String path) {
		String[] files = path.split(File.separator.equals("\\") ? "\\\\" : File.separator);	// stupid windows fix...
		return files[files.length - 1];
	}
    
    
    /**
	 * Build the library tree, according to the registered library root
	 */
	private void buildTree() {
		
		rootItem = new TreeItem<String>(convertToNodeName(Profile.getInstance().getLibraryRoot()), getIconForNode(""));

		listFile(
				new File(Profile.getInstance().getLibraryRoot()), 
				rootItem
		);
		
		rootItem.setExpanded(true);
		this.setRoot(rootItem);
		
		/*
		tree.setDragEnabled(true);
		tree.setDropMode(DropMode.ON); 
		
		controller = new WeakReference<LibraryViewController>(new LibraryViewController(this));
		tree.addMouseListener(controller.get());
		tree.addTreeSelectionListener(controller.get());
		tree.setTransferHandler(controller.get()); 		// for drag & drop
		model.addTreeModelListener(controller.get());
		*/
	}
	
	
 	private Node getIconForNode(String filePath) {
 		
 		logger.trace(filePath);
 		
 		// root
 		if (filePath == "") {
			Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_library_root")));
			ImageView iv = new ImageView(i);
			iv.setSmooth(true);
			iv.setFitWidth(TREE_ITEM_SIZE);
			iv.setFitHeight(TREE_ITEM_SIZE);
			return iv;
 		}
 		
 		// folder
 		if (new File(filePath).isDirectory()) {
			Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_library_folder")));
			ImageView iv = new ImageView(i);
			iv.setSmooth(true);
			iv.setFitWidth(TREE_ITEM_SIZE);
			iv.setFitHeight(TREE_ITEM_SIZE);
			return iv;
 		}
 		
 		if (new File(filePath).isFile()) {
 			
 			Image i = null;
 			
 			switch (FileHelper.getFileType(filePath)) {
			case PDF_TYPE:
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_pdf")));
				break;
			case HTML_TYPE:
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_html")));
				break;
			case TEXT_TYPE :
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_txt")));
				break;
			case PPT_TYPE :
			case PPTX_TYPE :
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_ppt")));
				break;
			case DOC_TYPE :
			case DOCX_TYPE :
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_doc")));
				break;
			case PNG_TYPE :
			case JPG_TYPE :
			case BMP_TYPE :
			case GIF_TYPE :
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_img")));
				break;
			default:
				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_unknown")));
				break;
			}
 			
 			// apply image
			ImageView iv = new ImageView(i);
			iv.setSmooth(true);
			iv.setFitWidth(TREE_ITEM_SIZE);
			iv.setFitHeight(TREE_ITEM_SIZE);
			return iv;
 		}
 		
 		return null;
 	}
	
	
	/**
	 * List files in given directory, and add them in tree
	 */
	private TreeItem<String> listFile(File file, TreeItem<String> node) {

		if (file.isFile()) {
			return new TreeItem<String>(convertToNodeName(file.getName()), getIconForNode(file.getPath()));
		}

		for (File f : file.listFiles()) {

			TreeItem<String> subNode;
			if (f.isDirectory()) {
				
				subNode = new TreeItem<String>(convertToNodeName(f.getName()), getIconForNode(f.getPath()));
				listFile(f, subNode);
				
			} else {
				
				if (showDirectoryOnly) {
					continue;
				}
				
				subNode = new TreeItem<String>(convertToNodeName(f.getName()), getIconForNode(f.getPath()));
			}
						
			node.getChildren().add(subNode);
		}
		return node;
	}



	public LibraryViewEventHandler getEventHandler() {
		return eventHandler;
	}
  

    public FxSoftwareScreen getParentScreen() {
		return parentScreen.get();
	}

	
}
