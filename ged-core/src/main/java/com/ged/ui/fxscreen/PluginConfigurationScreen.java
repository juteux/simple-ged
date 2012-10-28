package com.ged.ui.fxscreen;

import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import com.ged.models.GedPlugin;
import com.ged.ui.FxMainWindow;
import com.ged.ui.fxwidgets.FxLibraryView;
import com.tools.DateHelper;

/**
 * 
 * This is the screen for configuring plugins
 * 
 * @author xavier
 *
 */
public class PluginConfigurationScreen extends FxSoftwareScreen {

	/**
	 * The library view, to chose document target
	 */
	private FxLibraryView libraryView;
	
	/**
	 * The concerned plugin
	 */
	private GedPlugin plugin; 
	
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
	
	
	public PluginConfigurationScreen(FxMainWindow mw) {
		super(mw);
		
		instanciateWidgets();
		
		VBox detailsBox = new VBox();
		detailsBox.getChildren().addAll(title, version, author, new Separator(), jarName);
		
		HBox topBox = new HBox(10);
		topBox.getChildren().addAll(detailsBox, desc);
		
		
		VBox optionsBox = new VBox();
		optionsBox.getChildren().addAll(help);
		
		HBox middleBox = new HBox();
		middleBox.getChildren().addAll(libraryView, optionsBox);
		
		VBox globalBox = new VBox();
		globalBox.getChildren().addAll(topBox, middleBox);
		
		this.getChildren().addAll(globalBox);
	}
	
	
	@Override
	public void pullExtraValues(Map<String, Object> extras) {
		plugin = (GedPlugin) extras.get("ged-plugin");
		
		title.setText(plugin.getPlugin().getPluginName());
		version.setText(properties.getProperty("Version") + " " + plugin.getPlugin().getPluginVersion() + " " + properties.getProperty("released_the") + " " + DateHelper.calendarToString(plugin.getPlugin().getPluginDate()));
		author.setText((properties.getProperty("by") + " " + plugin.getPlugin().getPluginAuthor()));
		jarName.setText(plugin.getPlugin().getJarFileName());
		desc.setText(plugin.getPlugin().getPluginDescription());
		
		if (plugin.isActivated()) {
			// fill options
		}
	}


	private void instanciateWidgets() {
		
		libraryView = new FxLibraryView(this);
		
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
		help.setText(properties.getProperty("save"));//plugin_option_user_guide"));
		
		save = new Button(properties.getProperty("save"));
		save.setPrefSize(250, 80);
		//save.setOnAction(eventHandler);
		
		Image i3 = new Image(getClass().getResourceAsStream(properties.getProperty("ico_save")));
		ImageView iv3 = new ImageView(i3);
		iv3.setSmooth(true);
		iv3.setFitWidth(64);
		iv3.setFitHeight(64);
		save.setGraphic(iv3);
		
		save.setDisable(true);
	}

}
