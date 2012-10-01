package com.ged.ui.listeners;

import java.util.EventListener;

/**
 * Listen for changes on DocumentLocationSelector
 * @author xavier
 *
 */
public interface DocumentLocationSelectorListener extends EventListener {

	/**
	 * Fired when selection changed
	 * 
	 */
	void selectionChanged();
	
}
