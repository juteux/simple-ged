package com.ged.ui.fxscreencontroller;

import java.io.File;

import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.services.GedDocumentService;
import com.ged.ui.fxscreen.LibraryViewScreen;
import com.ged.ui.listeners.LibraryListener;

public class LibraryViewScreenController implements LibraryListener {

	/**
	 * The controlled object
	 */
	private LibraryViewScreen libraryViewScreen;
	
	public LibraryViewScreenController(LibraryViewScreen libraryViewScreen) {
		this.libraryViewScreen = libraryViewScreen;
	}
	
	@Override
	public void selectionChanged(String relativeFilePathOfNewSelection) {
	
		GedDocument d = GedDocumentService.findDocumentbyFilePath(relativeFilePathOfNewSelection);
		
		if (d == null) {
			libraryViewScreen.getDocumentInfoViewerWidget().setGedDocument(null);
			libraryViewScreen.getDocumentPreviewer().setFile(new File(Profile.getInstance().getLibraryRoot() + relativeFilePathOfNewSelection));
		}
		else {
			libraryViewScreen.getDocumentInfoViewerWidget().setGedDocument(d); 
			libraryViewScreen.getDocumentPreviewer().setGedDocument(d);
		}
	}

}
