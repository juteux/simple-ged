package com.ged.ui.fxscreen;

import com.ged.ui.fxscreencontroller.LibraryViewScreenController;
import com.ged.ui.fxwidgets.DocumentPreviewer;
import com.ged.ui.fxwidgets.FxDocumentInfoViewer;
import com.ged.ui.fxwidgets.FxLibraryView;

import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
	
	/**
	 * On the right too, the document (or file) preview
	 */
	private DocumentPreviewer documentPreviewer;
	
	
	public LibraryViewScreen() {
		
		instanciateWidgets();
		
        this.getChildren().add(libraryWidget);
        
        VBox rightLayoutBoxing = new VBox();
        rightLayoutBoxing.getChildren().add(documentInfoViewerWidget);
        rightLayoutBoxing.getChildren().add(new Separator());
        rightLayoutBoxing.getChildren().add(documentPreviewer);
        
        this.getChildren().add(rightLayoutBoxing);
	}
	
	
	/**
	 * If some widgets needs to occupe all the available place, it's now
	 */
	public void refreshLayoutSize() {
		documentPreviewer.setPrefSize(getWidth() - libraryWidget.getWidth(), getHeight() - documentInfoViewerWidget.getHeight());
	}
	
	
	private void instanciateWidgets() {
		
		LibraryViewScreenController controller = new LibraryViewScreenController(this);
		
		libraryWidget = new FxLibraryView();
		libraryWidget.getController().addLibraryListener(controller);
		
		documentInfoViewerWidget = new FxDocumentInfoViewer();
		documentPreviewer = new DocumentPreviewer();
	}


	public FxLibraryView getLibraryWidget() {
		return libraryWidget;
	}

	public FxDocumentInfoViewer getDocumentInfoViewerWidget() {
		return documentInfoViewerWidget;
	}

	public DocumentPreviewer getDocumentPreviewer() {
		return documentPreviewer;
	}
	
}
