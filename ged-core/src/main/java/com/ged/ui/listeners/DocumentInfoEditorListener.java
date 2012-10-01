package com.ged.ui.listeners;

import java.util.EventListener;

/**
 * Listen for changes on DocumentInfoEditor
 * @author xavier
 *
 */
public interface DocumentInfoEditorListener extends EventListener {

	/**
	 * Fired when some input has changed 
	 * 
	 * @param isValid
	 * 				True if values aren't correct, false otherwise
	 */
	void selectionChanged(boolean isValid);
	
}
