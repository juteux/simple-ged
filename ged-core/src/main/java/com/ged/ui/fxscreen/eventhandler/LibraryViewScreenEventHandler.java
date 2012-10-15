package com.ged.ui.fxscreen.eventhandler;

import java.io.File;
import java.lang.ref.WeakReference;

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
	 * The controlled object
	 */
	private WeakReference<LibraryViewScreen> libraryViewScreen;
	
	public LibraryViewScreenEventHandler(LibraryViewScreen libraryViewScreen) {
		this.libraryViewScreen = new WeakReference<>(libraryViewScreen);
	}
	
	@Override
	public void selectionChanged(String relativeFilePathOfNewSelection) {
	
		// special case
		if (new File(Profile.getInstance().getLibraryRoot() + relativeFilePathOfNewSelection).isDirectory()) {
			libraryViewScreen.get().getDocumentInfoViewerWidget().setGedDocument(null);
			libraryViewScreen.get().getDocumentPreviewer().setSpecialPreviewer(new AddDocumentPreviewer(libraryViewScreen.get().getLibraryWidget().getSelectionModel().getSelectedItem(), libraryViewScreen.get().getLibraryWidget().getController()));
			return;
		}
		
		// try to show doc
		
		GedDocument d = GedDocumentService.findDocumentbyFilePath(relativeFilePathOfNewSelection);
		
		if (d == null) {
			libraryViewScreen.get().getDocumentInfoViewerWidget().setGedDocument(null);
			libraryViewScreen.get().getDocumentPreviewer().setFile(new File(Profile.getInstance().getLibraryRoot() + relativeFilePathOfNewSelection));
		}
		else {
			libraryViewScreen.get().getDocumentInfoViewerWidget().setGedDocument(d); 
			libraryViewScreen.get().getDocumentPreviewer().setGedDocument(d);
		}
	}

	@Override
	public void releaseOpenedFiles() {
		libraryViewScreen.get().getDocumentPreviewer().releaseOpenedFiles();
	}

}
