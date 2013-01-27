package com.simple.ged.ui.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simple.ged.models.GedGetterPlugin;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.connector.plugins.getter.SimpleGedGetterPluginProperty;
import com.simple.ged.ui.MainWindow;
import com.simple.ged.ui.screen.eventhandler.PluginConfigurationScreenEventHandler;
import com.simple.ged.ui.widgets.LibraryView;

import fr.xmichel.toolbox.tools.DateHelper;

/**
 * 
 * This is the screen for configuring plugins
 * 
 * @author xavier
 *
 */
public class GetterPluginConfigurationScreen extends SoftwareScreen {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(GetterPluginConfigurationScreen.class);
	
	/**
	 * The library view, to chose document target
	 */
	private LibraryView libraryView;
	
	/**
	 * The concerned plugin
	 */
	private GedGetterPlugin plugin;
	
	/**
	 * The plugin name
	 */
	private Label title;
	
	/**
	 * The plugin version
	 */
	private Label version;

	/**
	 * The plugin author
	 */
	private Label author;
	
	/**
	 * The plugin jar name
	 */
	private Label jarName;
	
	/**
	 * The plugin description
	 */
	private Text desc;
	
	/**
	 * Help text
	 */
	private Text help;
	
	/**
	 * Save button
	 */
	private Button save;
	
	/**
	 * The options layout (will be fill after receiving extra values)
	 */
	private GridPane optionLayout;
	
	/**
	 * The day selector
	 */
	private ComboBox<Integer> comboDayOfMonthForUpdate;
	
	/**
	 * The interval between update selector
	 */
	private ComboBox<Integer> comboIntervalBetweenUpdateInMonth;
	
	/**
	 * Map of properties
	 */
	private Map<SimpleGedGetterPluginProperty, TextField> propertiesFieldsMap;
	
	/**
	 * The destination file name pattern
	 */
	private TextField fieldNamePattern;
	
	/**
	 * My event handler
	 */
	private PluginConfigurationScreenEventHandler eventHandler;
	
	
	public GetterPluginConfigurationScreen(MainWindow mw) {
		super(mw);
		
		instanciateWidgets();
		
		GridPane globalLayout = new GridPane();
		globalLayout.setHgap(20);
		globalLayout.setVgap(20);

		VBox detailsBox = new VBox();
		detailsBox.getChildren().addAll(title, version, author, new Separator(), jarName);
		
		optionLayout.add(new Label(properties.getProperty("destination_file_name")), 0, 0);
		optionLayout.add(fieldNamePattern, 1, 0);

		optionLayout.add(new Label(properties.getProperty("day_of_getting")), 0, 1);
		optionLayout.add(comboDayOfMonthForUpdate, 1, 1);
		
		optionLayout.add(new Label(properties.getProperty("time_between")), 0, 2);
		optionLayout.add(comboIntervalBetweenUpdateInMonth, 1, 2);
		
		VBox optionsBox = new VBox(30);
		optionsBox.getChildren().addAll(help, optionLayout);
		
		globalLayout.add(detailsBox, 0, 0);
		globalLayout.add(desc, 1, 0);
		globalLayout.add(libraryView, 0, 1);
		globalLayout.add(optionsBox, 1, 1);
		globalLayout.add(save, 1, 2);
		
		this.getChildren().addAll(globalLayout);
	}
	
	
	@Override
	public void pullExtraValues(Map<String, Object> extras) {
		plugin = (GedGetterPlugin) extras.get("ged-plugin");
		
		title.setText(plugin.getPlugin().getPluginName());
		version.setText(properties.getProperty("Version") + " " + plugin.getPlugin().getPluginVersion() + " " + properties.getProperty("released_the") + " " + DateHelper.calendarToString(plugin.getPlugin().getPluginDate()));
		author.setText((properties.getProperty("by") + " " + plugin.getPlugin().getPluginAuthor()));
		jarName.setText(plugin.getPlugin().getJarFileName());
		desc.setText(plugin.getPlugin().getPluginDescription());
		
		
		if (plugin.isActivated()) {
			fieldNamePattern.setText(plugin.getDestinationFilePattern());
			comboDayOfMonthForUpdate.getSelectionModel().select(plugin.getDayOfMonthForUpdate() - 1);
			comboIntervalBetweenUpdateInMonth.getSelectionModel().select(plugin.getIntervalBetweenUpdates() - 1);
		}
		
		
		int currentRowNumber = 3;
		
		for (SimpleGedGetterPluginProperty property : plugin.getPlugin().getProperties()) {
			
			TextField field = new TextField();
			if (property.isHidden()) {
				field = new PasswordField();
			}
		
			if (plugin.isActivated()) {
				logger.debug("Plugin is activated, setting value");
				for (SimpleGedGetterPluginProperty pr : plugin.getPluginProperties()) {
					if (pr.getPropertyKey().equals(property.getPropertyKey())) {
						field.setText(pr.getPropertyValue());
						break;
					}
				}
			}
			
			field.setOnKeyReleased(eventHandler);
			
			propertiesFieldsMap.put(property, field);
			
			optionLayout.add(new Label(property.getPropertyLabel()), 0, currentRowNumber);
			optionLayout.add(field, 1, currentRowNumber);
			
			++currentRowNumber;
		}
	}


	private void instanciateWidgets() {
		
		eventHandler = new PluginConfigurationScreenEventHandler(this);
		
		
		libraryView = new LibraryView(this, true);
		
		title = new Label();
		title.getStyleClass().add("list-plugin-title");
		
		version = new Label();
		version.getStyleClass().add("list-plugin-version");
		
		author = new Label();
		author.getStyleClass().add("list-plugin-author");
		
		jarName = new Label();
		jarName.getStyleClass().add("list-plugin-jarname");
		
		desc = new Text();
		desc.getStyleClass().add("list-plugin-desc");
		
		help = new Text();
		help.setText(properties.getProperty("plugin_option_user_guide"));
		
		save = new Button(properties.getProperty("save"));
		save.setPrefSize(250, 80);
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				eventHandler.setOnActionEvent(arg0);
			}
		});
		
		Image i3 = new Image(getClass().getResourceAsStream(properties.getProperty("ico_save")));
		ImageView iv3 = new ImageView(i3);
		iv3.setSmooth(true);
		iv3.setFitWidth(64);
		iv3.setFitHeight(64);
		save.setGraphic(iv3);
		
		save.setDisable(true);
		
		
		optionLayout = new GridPane();
		optionLayout.setHgap(10);
		optionLayout.setVgap(10);
		
		comboDayOfMonthForUpdate = new ComboBox<>();
		
		List<Integer> vDays = new ArrayList<>();
		for (int i = 1; i <= 30; ++i) {
			vDays.add(i);
		}
		comboDayOfMonthForUpdate.getItems().addAll(vDays);
		comboDayOfMonthForUpdate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				eventHandler.checkValidity();
			}
		});
		
		comboIntervalBetweenUpdateInMonth = new ComboBox<>();
		List<Integer> vMonth = new ArrayList<>();
		for (int i = 1; i <= 12; ++i) {
			vMonth.add(i);
		}
		comboIntervalBetweenUpdateInMonth.getItems().addAll(vMonth);
		comboIntervalBetweenUpdateInMonth.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				eventHandler.checkValidity();
			}
		});
		
		propertiesFieldsMap = new HashMap<>();
		
		fieldNamePattern = new TextField();
		fieldNamePattern.setOnKeyReleased(eventHandler);
	}


	public LibraryView getLibraryView() {
		return libraryView;
	}


	public GedGetterPlugin getPlugin() {
		return plugin;
	}


	public Button getSave() {
		return save;
	}


	public ComboBox<Integer> getComboDayOfMonthForUpdate() {
		return comboDayOfMonthForUpdate;
	}


	public ComboBox<Integer> getComboIntervalBetweenUpdateInMonth() {
		return comboIntervalBetweenUpdateInMonth;
	}


	public Map<SimpleGedGetterPluginProperty, TextField> getPropertiesFieldsMap() {
		return propertiesFieldsMap;
	}


	public TextField getFieldNamePattern() {
		return fieldNamePattern;
	}

}
