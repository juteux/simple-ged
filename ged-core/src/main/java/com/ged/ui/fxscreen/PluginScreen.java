package com.ged.ui.fxscreen;

import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import org.apache.log4j.Logger;

import com.ged.connector.plugins.SimpleGedPlugin;
import com.ged.plugins.PluginManagementInformations;
import com.ged.plugins.PluginManager;
import com.ged.ui.FxMainWindow;
import com.tools.DateHelper;

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
	private TableView<PluginManagementInformations> table;

	/**
	 * The plugin list
	 */
	private ObservableList<PluginManagementInformations> pluginsList;

	public PluginScreen(FxMainWindow mw) {
		super(mw);

		instanciateWidgets();
		fillPluginList();

		table.setItems(pluginsList);

		HBox.setHgrow(table, Priority.ALWAYS);

		this.getChildren().addAll(table);
	}

	/**
	 * Create widgets
	 */
	private void instanciateWidgets() {

		pluginsList = FXCollections.observableArrayList();
		table = new TableView<>();
	
		TableColumn<PluginManagementInformations, VBox> colName = new TableColumn<>(properties.getProperty("plugin_name"));
		colName.setCellValueFactory(new Callback<CellDataFeatures<PluginManagementInformations, VBox>, ObservableValue<VBox>>() {
			public ObservableValue<VBox> call(CellDataFeatures<PluginManagementInformations, VBox> p) {
				VBox box = new VBox();

				SimpleGedPlugin plugin = p.getValue().getPlugin();
				
				Label title = new Label(plugin.getPluginName());
				title.getStyleClass().add("list-plugin-title");
				
				Label version = new Label(properties.getProperty("Version") + " " + plugin.getPluginVersion() + " " + properties.getProperty("released_the") + " " + DateHelper.calendarToString(plugin.getPluginDate()));
				version.getStyleClass().add("list-plugin-version");
				
				Label author = new Label(properties.getProperty("by") + " " + plugin.getPluginAuthor());
				author.getStyleClass().add("list-plugin-author");
				
				Label jarName = new Label(plugin.getJarFileName());
				jarName.getStyleClass().add("list-plugin-jarname");
				
				box.getChildren().addAll(title, version, author, new Separator(), jarName);

				return new SimpleObjectProperty<>(box);
			}
		});

		TableColumn<PluginManagementInformations, VBox> colDesc = new TableColumn<>(properties.getProperty("plugin_description"));
		colDesc.setCellValueFactory(new Callback<CellDataFeatures<PluginManagementInformations, VBox>, ObservableValue<VBox>>() {
			public ObservableValue<VBox> call(CellDataFeatures<PluginManagementInformations, VBox> p) {
				VBox box = new VBox();

				SimpleGedPlugin plugin = p.getValue().getPlugin();
				
				Text desc = new Text(plugin.getPluginDescription());
				desc.getStyleClass().add("list-plugin-desc");
				
				box.getChildren().addAll(desc);

				return new SimpleObjectProperty<>(box);
			}
		});
		
		TableColumn<PluginManagementInformations, VBox> colMang = new TableColumn<>(properties.getProperty("plugin_activated"));

		table.getColumns().addAll(colName, colDesc, colMang);
	}

	/**
	 * Fill the table with the plugin list
	 */
	private void fillPluginList() {

		List<PluginManagementInformations> plugins = PluginManager.getPluginList();

		logger.info("Plugin count : " + plugins.size());

		// show only activated plugins
		for (PluginManagementInformations p : plugins) {
			if (p.isActivated()) {
				pluginsList.add(p);
			}
		}

		// show non activated plugins
		for (PluginManagementInformations p : plugins) {
			if (!p.isActivated()) {
				pluginsList.add(p);
			}
		}

	}
}
