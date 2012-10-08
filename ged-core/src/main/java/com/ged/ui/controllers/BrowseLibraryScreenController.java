package com.ged.ui.controllers;

import java.io.File;

import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.services.GedDocumentService;
import com.ged.ui.listeners.LibraryListener;
import com.ged.ui.screens.BrowseLibraryScreen;

public class BrowseLibraryScreenController implements LibraryListener {

	/**
	 * The controlled screen
	 */
	private BrowseLibraryScreen browseLibraryScreen;
	
	
	public BrowseLibraryScreenController(BrowseLibraryScreen browseLibraryScreen) {
		this.browseLibraryScreen = browseLibraryScreen;
	}


	@Override
	public void selectionChanged(String relativeFilePathOfNewSelection) { 
		
		GedDocument d = GedDocumentService.findDocumentbyFilePath(browseLibraryScreen.getLibraryView().getSelectedPath());
		
		if (d == null) {
			browseLibraryScreen.getDocumentInfoViewer().setGedDocument(null);
			browseLibraryScreen.getDocumentPreview().setFile(new File(Profile.getInstance().getLibraryRoot() + relativeFilePathOfNewSelection));
		}
		else {
			browseLibraryScreen.getDocumentInfoViewer().setGedDocument(d); 
			browseLibraryScreen.getDocumentPreview().setGedDocument(d);
		}
	
		// do not forget to refresh
		browseLibraryScreen.getDocumentPreview().validate();
		browseLibraryScreen.validate();
	}

	
	public void refreshScreen() {
		selectionChanged(browseLibraryScreen.getLibraryView().getController().getLastKnownNodePath());
	}


	@Override
	public void releaseOpenedFiles() {
		// TODO Auto-generated method stub
		
	}
}
