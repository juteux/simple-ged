package com.ged.ui.fxscreen;

import org.apache.log4j.Logger;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import com.ged.ui.FxMainWindow;

/**
 * 
 * The plugin management screen allow you to enable/disable some plugin
 * 
 * @author xavier
 * 
 */
public class PluginScreen extends FxSoftwareScreen {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(PluginScreen.class);

	/**
	 * The table view contains the list of plugins
	 */
	private TableView<String> table;

	public PluginScreen(FxMainWindow mw) {
		super(mw);

		instanciateWidgets();

		HBox.setHgrow(table, Priority.ALWAYS);
		
		this.getChildren().addAll(table);
	}

	/**
	 * Create widgets
	 */
	private void instanciateWidgets() {

		table = new TableView<>();

		TableColumn<String, String> colName = new TableColumn<>(properties.getProperty("plugin_name"));
		TableColumn<String, String> colDesc = new TableColumn<>(properties.getProperty("plugin_description"));
		TableColumn<String, String> colMang = new TableColumn<>(properties.getProperty("plugin_activated"));

		table.getColumns().addAll(colName, colDesc, colMang);
	}
}
