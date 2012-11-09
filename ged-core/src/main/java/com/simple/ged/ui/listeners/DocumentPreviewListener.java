package com.simple.ged.ui.listeners;

import java.util.EventListener;

/**
 * The listener for the widget named "DocumentPreview"
 * @author xavier
 *
 */
public interface DocumentPreviewListener extends EventListener {

	/**
	 * Fire when the file count changed
	 * 
	 * @param newFileCount
	 * 				The new file count
	 */
	void fileCountChanged(int newFileCount);
	
}
