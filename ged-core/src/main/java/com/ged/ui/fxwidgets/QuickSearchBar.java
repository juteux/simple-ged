package com.ged.ui.fxwidgets;

import java.util.Properties;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import com.ged.ui.fxwidgets.eventhandler.QuickSearchBarEventHandler;
import com.tools.PropertiesHelper;

/**
 * 
 * A simple text edit for quick search in documents
 * 
 * @author xavier
 *
 */
public class QuickSearchBar extends HBox {

	/**
	 * Line for searching pattern
	 */
	private TextField seachPatternInput;
	
	/**
	 * Properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	/**
	 * Event handler
	 */
	private QuickSearchBarEventHandler eventHandler;
	
	public QuickSearchBar() {
		
		instanciateWidgets();
		
		HBox.setHgrow(seachPatternInput, Priority.ALWAYS);
		
		getChildren().addAll(seachPatternInput);
	}
	
	
	private void instanciateWidgets() {
		
		eventHandler = new QuickSearchBarEventHandler(this);
		
		seachPatternInput = new TextField();
		seachPatternInput.setPromptText(properties.getProperty("search_prompt"));
		seachPatternInput.setOnKeyReleased(eventHandler);
	}


	public TextField getSeachPatternInput() {
		return seachPatternInput;
	}


	public QuickSearchBarEventHandler getEventHandler() {
		return eventHandler;
	}
	
	
}
