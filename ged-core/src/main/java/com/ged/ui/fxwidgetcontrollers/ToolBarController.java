package com.ged.ui.fxwidgetcontrollers;

import org.apache.log4j.Logger;

import com.ged.ui.fxscreen.FxSoftwareScreen.Screen;
import com.ged.ui.fxwidgets.FxToolBar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Tool bar controller
 * 
 * @author xavier
 *
 */
public class ToolBarController implements EventHandler<ActionEvent> {

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
	public void handle(ActionEvent action) {
	
		if (action.getSource() == toolBar.getBtnHome()) {
			toolBar.gotoScreen(Screen.BROWSING_SCREEN);
		}
		else {
			logger.warn("Not implemented yet, see ToolBarController.handle");
		}
		
	}
	
}
