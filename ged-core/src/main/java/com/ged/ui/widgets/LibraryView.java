package com.ged.ui.widgets;

import java.awt.Dimension;
import java.lang.ref.WeakReference;

import javax.swing.DropMode;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import com.ged.Profile;
import com.ged.ui.controllers.LibraryViewController;
import com.ged.ui.screens.SoftwareScreen;
import com.tools.RelativeFileSystemModel;
import com.tools.ui.AbstractWidget;



/**
 * A view of the library, a tree with files in GED directory
 * 
 * Not really beautiful, we should look for another model
 */

public class LibraryView extends AbstractWidget {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Model
	 */
	private RelativeFileSystemModel model;
	
	/**
	 * View (a tree)
	 */
	private JTree tree;
	
	/**
	 * My controller
	 */
	private WeakReference<LibraryViewController> controller;
	
	/**
	 * Parent screen
	 */
	private WeakReference<SoftwareScreen> parent;
	
	/**
	 * Create the library view, include ordinary files
	 */
	public LibraryView(SoftwareScreen parent) {
		this(false, parent);
	}
	
	/**
	 * Create the library view, the param determine if you wan't to see files or just directories
	 * 
	 * @param showDirectoryOnly
	 * 					If the param is false, we show all files. Otherwise, we show just directories.
	 */
	public LibraryView(boolean showDirectoryOnly, SoftwareScreen parent) {
		super();
		this.parent = new WeakReference<SoftwareScreen>(parent);
		buildTree(showDirectoryOnly);
		
		//setMinimumSize(new Dimension(220, 200));
		
		//add(new JScrollPane(tree));
	}

	/**
	 * Build the library tree, according to the registered library root
	 */
	private void buildTree(boolean showDirectoryOnly) {

		model = new RelativeFileSystemModel(Profile.getInstance().getLibraryRoot(), showDirectoryOnly);
		tree = new JTree(model);
		tree.setDragEnabled(true);
		tree.setDropMode(DropMode.ON); 
		
		controller = new WeakReference<LibraryViewController>(new LibraryViewController(this));
		tree.addMouseListener(controller.get());
		tree.addTreeSelectionListener(controller.get());
		tree.setTransferHandler(controller.get()); 		// for drag & drop
		model.addTreeModelListener(controller.get());
	}
	
	
	/**
	 * @return the tree
	 */
	public JTree getTree() {
		return tree;
	}

	/**
	 * @return the model
	 */
	public RelativeFileSystemModel getModel() {
		return model;
	}

	/**
	 * @return the controller
	 */
	public LibraryViewController getController() {
		return controller.get();
	}
	
	/**
	 * @return the current selection path
	 */
	public String getSelectedPath() {
		return model.getFilePath(tree.getSelectionPath());
	}

	/**
	 * Get the parent screen
	 */
	public SoftwareScreen getParentScreen() {
		return parent.get();
	}
	
}

