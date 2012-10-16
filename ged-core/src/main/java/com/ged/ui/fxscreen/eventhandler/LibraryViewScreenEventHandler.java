package com.ged.ui.fxscreen.eventhandler;

import java.io.File;
import java.lang.ref.WeakReference;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.services.GedDocumentService;
import com.ged.ui.fxpreviewwidgets.AddDocumentPreviewer;
import com.ged.ui.fxscreen.LibraryViewScreen;
import com.ged.ui.listeners.LibraryListener;

/**
 * The event handler of the LibrayViewScreen
 * 
 * @author xavier
 *
 */
public class LibraryViewScreenEventHandler implements LibraryListener {

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(LibraryViewScreenEventHandler.class);
	
	/**
	 * The controlled object
	 */
	private WeakReference<LibraryViewScreen> libraryViewScreen;
	
	public LibraryViewScreenEventHandler(LibraryViewScreen libraryViewScreen) {
		this.libraryViewScreen = new WeakReference<>(libraryViewScreen);
	}
	
	@Override
	public void selectionChanged(String relativeFilePathOfNewSelection) {
	
		logger.debug("selectionChanged fired : " + relativeFilePathOfNewSelection);
		
		// special case
		if (new File(Profile.getInstance().getLibraryRoot() + relativeFilePathOfNewSelection).isDirectory()) {
			libraryViewScreen.get().getDocumentInfoViewerWidget().setGedDocument(null);
			libraryViewScreen.get().getDocumentPreviewer().setSpecialPreviewer(new AddDocumentPreviewer(libraryViewScreen.get().getLibraryWidget().getSelectionModel().getSelectedItem(), libraryViewScreen.get().getLibraryWidget().getEventHandler()));
			return;
		}
		
		// try to show doc
		
		GedDocument d = GedDocumentService.findDocumentbyFilePath(relativeFilePathOfNewSelection);
		
		if (d == null) {
			logger.debug("no document found for this file");
			libraryViewScreen.get().getDocumentInfoViewerWidget().setGedDocument(null);
			libraryViewScreen.get().getDocumentPreviewer().setFile(new File(Profile.getInstance().getLibraryRoot() + relativeFilePathOfNewSelection));
		}
		else {
			logger.debug("document found for this file");
			libraryViewScreen.get().getDocumentInfoViewerWidget().setGedDocument(d); 
			libraryViewScreen.get().getDocumentPreviewer().setGedDocument(d);
		}
	}

	@Override
	public void releaseOpenedFiles() {
		libraryViewScreen.get().getDocumentPreviewer().releaseOpenedFiles();
	}

	@Override
	public void selectionChanged(LIBRARY_FILE_TYPE newSelectionType) {
		libraryViewScreen.get().getDocumentInfoViewerWidget().getBtnEditDocument().setDisable(newSelectionType != LIBRARY_FILE_TYPE.LIBRARY_FILE);
	}

}
