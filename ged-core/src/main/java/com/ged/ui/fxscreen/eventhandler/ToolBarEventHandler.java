package com.ged.ui.fxscreen.eventhandler;

import java.lang.ref.WeakReference;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.apache.log4j.Logger;

import com.ged.ui.fxscreen.FxToolBar;

/**
 * Tool bar event handler
 * 
 * @author xavier
 *
 */
public class ToolBarEventHandler implements EventHandler<Event> {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(ToolBarEventHandler.class);
	
	/**
	 * The controlled object
	 */
	private WeakReference<FxToolBar> toolBar;
	
	
	
	public ToolBarEventHandler(FxToolBar toolBar) {
		this.toolBar = new WeakReference<>(toolBar);
	}
	
	
	/**
	 * Handle the event
	 */
	@Override
	public void handle(Event action) {
	
		if (action.getSource() == toolBar.get().getBtnBack()) {
			toolBar.get().finish();
		}
		else {
			logger.warn("Not implemented yet, see ToolBarController.handle");
		}
		
	}
	
}
