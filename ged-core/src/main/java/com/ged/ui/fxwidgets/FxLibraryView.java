package com.ged.ui.fxwidgets;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Properties;

import javax.swing.DropMode;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.ui.MainWindow;
import com.ged.ui.controllers.LibraryViewController;
import com.ged.ui.preview_widgets.FilePreviewerFactory.FileType;
import com.tools.FileHelper;
import com.tools.PropertiesHelper;
import com.tools.RelativeFileSystemModel;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * A view of the library, a tree with files in GED directory
 * 
 */

public class FxLibraryView extends TreeView<String> {

	/**
	 * Tree item icon size (px)
	 */
	private static final int TREE_ITEM_SIZE = 30;
	
	private static final Logger logger = Logger.getLogger(FxLibraryView.class);
	
	/**
	 * TODO : fix this value usage
	 */
	private boolean showDirectoryOnly = false;
	
	
	Properties properties = PropertiesHelper.getInstance().getProperties();
	

    public FxLibraryView() {
    	buildTree();
    	
    	com.ged.ui.fxcontrollers.LibraryViewController controller = new com.ged.ui.fxcontrollers.LibraryViewController(this);
    	
    	this.setEditable(true);
    	this.setCellFactory(controller);
    	
    	this.getSelectionModel().selectedItemProperty().addListener(controller);  
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
		
		TreeItem<String> rootItem = new TreeItem<String>(convertToNodeName(Profile.getInstance().getLibraryRoot()), getIconForNode(""));
		
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
 			
 			String extension = FileHelper.getExtension(filePath);
 			Image i = null;
 			
 			if (extension.equals("PDF")) {
 				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_pdf")));
 			}
 			else if (extension.equals("HTML")) {
 				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_html")));
 			}
 			if (extension.equals("TXT")) {
 				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_txt")));
 			}
 			else { // more complex types
 				
 				String[] pptExtensions = new String[] { "PPT", "PPTX" };
 				for (String s : pptExtensions) {
 					if (extension.equals(s)) {
 						i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_ppt")));
 						break;
 					}
 				}
 				
 				String[] docExtensions = new String[] { "DOC", "DOCX" };
 				for (String s : docExtensions) {
 					if (extension.equals(s)) {
 						i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_doc")));
 						break;
 					}
 				}
 				
 				
 				String[] imagesExtensions = new String[] { "PNG", "JPG", "JPEG", "BMP", "GIF" };
 				for (String s : imagesExtensions) {
 					if (extension.equals(s)) {
 						i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_img")));
 						break;
 					}
 				}
 			}
 			
 			if (i == null) { // we haven't found a matched type
 				i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_file_unknown")));
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
    
}
