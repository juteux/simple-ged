package com.ged.ui.screens;

import java.lang.ref.WeakReference;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.ged.ui.MainWindow;
import com.ged.ui.controllers.BrowseLibraryScreenController;
import com.ged.ui.widgets.DocumentInfoViewer;
import com.ged.ui.widgets.DocumentPreview;
import com.ged.ui.widgets.LibraryView;


/**
 * This screen is for browsing and consulting documents in library
 */

public class BrowseLibraryScreen extends SoftwareScreen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The view of the library (the tree)
	 */
	private LibraryView libraryView;
	
	/**
	 * The view of document informations
	 */
	private DocumentInfoViewer documentInfoViewer;
	
	/**
	 * The document preview
	 */
	private DocumentPreview documentPreview;
	
	/**
	 * My controller
	 */
	private WeakReference<BrowseLibraryScreenController> controller;
	
	
	public BrowseLibraryScreen(MainWindow mw) {
		super(mw);
		instantiateWidgets();
		
		MigLayout layout = new MigLayout(	
				"wrap",
				"[grow 15,fill,center][grow 85,fill,center]",	// columns
				"[grow 25,fill,center][grow 75,fill,center]"	// rows
			);
		
		JPanel container = new JPanel(layout);

		// add widget in main layout
		container.add(libraryView, "span 1 2");
		container.add(documentInfoViewer);
		container.add(documentPreview);
		
		// main layout
		add(container);
	}

	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		libraryView = new LibraryView(this);
		documentInfoViewer = new DocumentInfoViewer();
		documentPreview = new DocumentPreview();
		documentPreview.getBtnUnlink().setVisible(false);
		
		controller = new WeakReference<BrowseLibraryScreenController>(new BrowseLibraryScreenController(this));
		libraryView.getController().addLibraryListener(controller.get());	// listen for changes in tree
	}


	public LibraryView getLibraryView() {
		return libraryView;
	}


	public DocumentInfoViewer getDocumentInfoViewer() {
		return documentInfoViewer;
	}

	public DocumentPreview getDocumentPreview() {
		return documentPreview;
	}

	@Override
	public void refresh() {
		controller.get().refreshScreen();
	}
	
}
