package com.simple.ged.ui.screen;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.simple.ged.ui.MainWindow;
import com.simple.ged.ui.screen.eventhandler.LibraryViewScreenEventHandler;
import com.simple.ged.ui.widgets.DocumentPreviewer;
import com.simple.ged.ui.widgets.FxDocumentInfoViewer;
import com.simple.ged.ui.widgets.FxLibraryView;
import com.simple.ged.ui.widgets.QuickSearchBar;

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
	 * On the top left, searching box
	 */
	private QuickSearchBar quickSearchBar;
	
	/**
	 * On the right too, the document (or file) preview
	 */
	private DocumentPreviewer documentPreviewer;

	
	public LibraryViewScreen(MainWindow parent) {
		super(parent);

		instanciateWidgets();
		
		VBox rightLayoutBoxing = new VBox();
		rightLayoutBoxing.getChildren().addAll(documentInfoViewerWidget, documentPreviewer);
		rightLayoutBoxing.setPadding(new Insets(0, 5, 5, 5));
		rightLayoutBoxing.setSpacing(5);
		
		VBox leftLayoutBoxing = new VBox();
		leftLayoutBoxing.getChildren().addAll(quickSearchBar, libraryWidget);
		leftLayoutBoxing.setPadding(new Insets(5, 5, 5, 5));
		leftLayoutBoxing.setSpacing(5);
		
		HBox mainLayout = new HBox();
		mainLayout.getChildren().addAll(leftLayoutBoxing, rightLayoutBoxing);
		
		this.getChildren().add(mainLayout);
		
		HBox.setHgrow(rightLayoutBoxing, Priority.ALWAYS);
		HBox.setHgrow(mainLayout, Priority.ALWAYS);
		VBox.setVgrow(libraryWidget, Priority.ALWAYS);
		HBox.setHgrow(quickSearchBar, Priority.ALWAYS);
	}


	private void instanciateWidgets() {

		LibraryViewScreenEventHandler eventHandler = new LibraryViewScreenEventHandler(this);

		libraryWidget = new FxLibraryView(this);
		libraryWidget.getEventHandler().addLibraryListener(eventHandler);

		documentInfoViewerWidget = new FxDocumentInfoViewer();
		documentInfoViewerWidget.getEventHandler().addDocumentInfoViewerListener(libraryWidget.getEventHandler());
		
		documentPreviewer = new DocumentPreviewer();
		
		libraryWidget.getEventHandler().addLibraryListener(eventHandler);
		
		quickSearchBar = new QuickSearchBar();
		quickSearchBar.getEventHandler().addQuickSearchListener(libraryWidget.getEventHandler());
		
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
