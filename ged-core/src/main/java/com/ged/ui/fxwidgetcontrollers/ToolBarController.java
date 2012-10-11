package com.ged.ui.fxwidgetcontrollers;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.apache.log4j.Logger;

import com.ged.ui.fxwidgets.FxToolBar;

/**
 * Tool bar controller
 * 
 * @author xavier
 *
 */
public class ToolBarController implements EventHandler<Event> {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(ToolBarController.class);
	
	/**
	 * The controlled object
	 */
	private FxToolBar toolBar;
	
	
	
	public ToolBarController(FxToolBar toolBar) {
		this.toolBar = toolBar;
	}
	
	
	/**
	 * Handle the event
	 */
	@Override
	public void handle(Event action) {
	
		if (action.getSource() == toolBar.getBtnBack()) {
			toolBar.finish();
		}
		else {
			logger.warn("Not implemented yet, see ToolBarController.handle");
		}
		
	}
	
}
