package com.tools.ui;


import java.util.Properties;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import com.tools.PropertiesHelper;


/**
 * Abstract widget
 *  
 * @author xavier
 *
 */
public abstract class AbstractWidget extends HBox {

	/**
	 * Golbal properties
	 */
	protected static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	
	public AbstractWidget() {
		super();
		HBox.setHgrow(this, Priority.ALWAYS);
	}
}


