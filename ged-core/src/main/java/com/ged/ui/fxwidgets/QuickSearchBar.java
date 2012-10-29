package com.ged.ui.fxwidgets;

import java.util.Properties;

import com.tools.PropertiesHelper;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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
	
	
	public QuickSearchBar() {
		
		instanciateWidgets();
		
		getChildren().addAll(seachPatternInput);
	}
	
	
	private void instanciateWidgets() {
		seachPatternInput = new TextField();
		seachPatternInput.setPromptText(properties.getProperty("search_prompt"));
	}
	
}
