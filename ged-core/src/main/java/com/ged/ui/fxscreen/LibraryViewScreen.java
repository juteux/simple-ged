package com.ged.ui.fxscreen;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.ged.ui.FxMainWindow;
import com.ged.ui.fxscreen.eventhandler.LibraryViewScreenEventHandler;
import com.ged.ui.fxwidgets.DocumentPreviewer;
import com.ged.ui.fxwidgets.FxDocumentInfoViewer;
import com.ged.ui.fxwidgets.FxLibraryView;

/**
 * Library view screen
 * 
 * @author xavier
 * 
 */
public class LibraryViewScreen extends FxSoftwareScreen {

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

	public LibraryViewScreen(FxMainWindow parent) {
		super(parent);

		instanciateWidgets();
		
		VBox rightLayoutBoxing = new VBox();
		rightLayoutBoxing.getChildren().addAll(documentInfoViewerWidget, documentPreviewer);
		
		rightLayoutBoxing.setPadding(new Insets(0, 5, 5, 5));
		rightLayoutBoxing.setSpacing(5);
		
		HBox mainLayout = new HBox();
		mainLayout.getChildren().addAll(libraryWidget, rightLayoutBoxing);
		
		this.getChildren().add(mainLayout);
		
		HBox.setHgrow(rightLayoutBoxing, Priority.ALWAYS);
		HBox.setHgrow(mainLayout, Priority.ALWAYS);
	}


	private void instanciateWidgets() {

		LibraryViewScreenEventHandler eventHandler = new LibraryViewScreenEventHandler(this);

		libraryWidget = new FxLibraryView(this);
		libraryWidget.getEventHandler().addLibraryListener(eventHandler);

		documentInfoViewerWidget = new FxDocumentInfoViewer();
		documentPreviewer = new DocumentPreviewer();
		
		libraryWidget.getEventHandler().addLibraryListener(eventHandler);
		
		// intialize previewer
		eventHandler.selectionChanged(libraryWidget.getEventHandler().getCurrentItemRelativePath());
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
