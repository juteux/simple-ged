package com.ged.ui.fxscreen;

import com.ged.ui.fxwidgets.FxDocumentInfoViewer;
import com.ged.ui.fxwidgets.FxLibraryView;

import javafx.scene.layout.HBox;

/**
 * Library view screen
 * 
 * @author xavier
 *
 */
public class LibraryViewScreen extends HBox {

	/**
	 * On the left, the tree with the library content
	 */
	private FxLibraryView libraryWidget;
	
	/**
	 * On the right, the document informations
	 */
	private FxDocumentInfoViewer documentInfoViewerWidget;
	
	
	public LibraryViewScreen() {
		
		instanciateWidgets();
		
        this.getChildren().add(libraryWidget);
        this.getChildren().add(documentInfoViewerWidget);
	}
	
	
	private void instanciateWidgets() {
		libraryWidget = new FxLibraryView();
		documentInfoViewerWidget = new FxDocumentInfoViewer();
	}
	
}
