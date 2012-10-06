package com.ged.ui.fxwidgets;

import java.io.File;
import java.lang.ref.WeakReference;

import javax.swing.DropMode;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.ged.Profile;
import com.ged.ui.controllers.LibraryViewController;
import com.tools.RelativeFileSystemModel;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * A view of the library, a tree with files in GED directory
 * 
 */

public class FxLibraryView extends TreeView<String> {

	
	/**
	 * TODO : fix this value usage
	 */
	private boolean showDirectoryOnly = false;
	
	
	/*
 	private final Node rootIcon = new ImageView(
        new Image(getClass().getResourceAsStream("folder_16.png"))
    );
    */
 	
    public FxLibraryView() {
    	buildTree();
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

		TreeItem<String> rootItem = new TreeItem<String>(convertToNodeName(Profile.getInstance().getLibraryRoot()));
		
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
	
	
	/**
	 * List files in given directory, and add them in tree
	 */
	private TreeItem<String> listFile(File file, TreeItem<String> node) {

		if (file.isFile()) {
			return new TreeItem<String>(convertToNodeName(file.getName()));
		}

		for (File f : file.listFiles()) {

			TreeItem<String> subNode;
			if (f.isDirectory()) {
				
				subNode = new TreeItem<String>(convertToNodeName(f.getName()));
				listFile(f, subNode);
				
			} else {
				
				if (showDirectoryOnly) {
					continue;
				}
				
				subNode = new TreeItem<String>(convertToNodeName(f.getName()));
			}
			
			node.getChildren().add(subNode);
		}
		return node;
	}
    
}
