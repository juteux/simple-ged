package com.ged.ui.listeners;

import java.util.EventListener;

/**
 * Listener for library view
 * @author xavier
 *
 */
public interface LibraryListener extends EventListener {

	/**
	 * Called when the selection changed in library view
	 * 
	 * @param relativeFilePathOfNewSelection
	 * 					The new selection, his relative path
	 */
	void selectionChanged(String relativeFilePathOfNewSelection);
	
}
