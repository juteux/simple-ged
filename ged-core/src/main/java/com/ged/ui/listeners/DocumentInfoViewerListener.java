package com.ged.ui.listeners;

import java.util.EventListener;

/**
 * Listen for changes on DocumentInfoViewer
 * 
 * @author xavier
 *
 */
public interface DocumentInfoViewerListener extends EventListener {

	/**
	 * Ask for document edition (the user clicked on the edit button)
	 * 
	 */
	void askForDocumentEdition();
	
}
