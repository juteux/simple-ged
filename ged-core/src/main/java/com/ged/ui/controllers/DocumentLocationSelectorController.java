package com.ged.ui.controllers;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import com.ged.ui.listeners.DocumentLocationSelectorListener;
import com.ged.ui.screens.SoftwareScreen.Screen;
import com.ged.ui.widgets.DocumentLocationSelector;


/**
 * This class is the DocumentLocationSelector controller
 * 
 * @author xavier
 *
 */
public class DocumentLocationSelectorController implements ItemListener{

	private static final Logger logger = Logger.getLogger(DocumentLocationSelectorController.class);
	
	/**
	 * The controlled widget
	 */
	private DocumentLocationSelector documentLocationSelector;
	
	/**
	 * Event listener
	 */
	private final EventListenerList listeners = new EventListenerList();
	
	
	public DocumentLocationSelectorController(DocumentLocationSelector documentLocationSelector) {
		this.documentLocationSelector = documentLocationSelector;
	}


	@Override
	public void itemStateChanged(ItemEvent arg0) {

		logger.debug("item state changed");
		
		if (arg0.getStateChange() == ItemEvent.SELECTED) {
			logger.debug("firing event !");
			for (DocumentLocationSelectorListener dlsl : getDocumentLocationSelectorListeners()) {
				dlsl.selectionChanged();
			}
		}
		
		if ( ! documentLocationSelector.isShowManagementOption() ) {
			return;
		}
		
		if (arg0.getStateChange() == ItemEvent.SELECTED && documentLocationSelector.getItemCount() > 1) {
			// last element = add location
			if (documentLocationSelector.getSelectedIndex() == documentLocationSelector.getItemCount() -1) {
				documentLocationSelector.getParentScreen().pushScreen(Screen.LOCATION_MANAGEMENT_SCREEN);
			}
		}
		
	}

	
	// for externals listeners

	public void addDocumentLocationSelectorListener(DocumentLocationSelectorListener listener) {
		listeners.add(DocumentLocationSelectorListener.class, listener);
	}

	public void removeDocumentLocationSelectorListener(DocumentLocationSelectorListener listener) {
		listeners.remove(DocumentLocationSelectorListener.class, listener);
	}

	public DocumentLocationSelectorListener[] getDocumentLocationSelectorListeners() {
		return listeners.getListeners(DocumentLocationSelectorListener.class);
	}
	
}
