package com.simple.ged.ui.listeners;

import java.util.EventListener;

/**
 * Listener for library view
 * @author xavier
 *
 */
public interface LibraryListener extends EventListener {

	/**
	 * Enum of selection type
	 */
	public enum LIBRARY_FILE_TYPE {
		LIBRARY_ROOT, 	/** The library root */
		LIBRARY_DIR,	/** Some library directory */
		LIBRARY_FILE	/** Some file in the library */
	}
	
	
	/**
	 * Called when the selection changed in library view
	 * 
	 * @param relativeFilePathOfNewSelection
	 * 					The new selection, his relative path
	 */
	void selectionChanged(String relativeFilePathOfNewSelection);
	
	/**
	 * Called when the listeners have to closed there manipulated files 
	 */
	void releaseOpenedFiles();
	
	/**
	 * Called when the selection changed in library view
	
	 * @param newSelectionType
	 * 					The new selection item type
	 */
	void selectionChanged(LIBRARY_FILE_TYPE newSelectionType);
	
}
