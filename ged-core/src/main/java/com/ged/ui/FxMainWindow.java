package com.ged.ui;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.ged.ui.fxwidgets.FxLibraryView;
import com.tools.PropertiesHelper;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 

/**
 * The new main window, with javafx power !
 * 
 * @author xavier
 *
 */
public class FxMainWindow extends Application {

	
	private static final Logger logger = Logger.getLogger(MainWindow.class);
	
	/**
	 * Default application width
	 */
	public static final int APP_WIDTH = 900;
	
	/**
	 * Default application height
	 */
	public static final int APP_HEIGHT = 600;
	
	
	/**
	 * Proporties loaded from configuration files
	 */
	protected Properties properties;
	
	
    @Override
    public void start(Stage primaryStage) {
    	
    	properties = PropertiesHelper.getInstance().getProperties();
    	
        primaryStage.setTitle(properties.getProperty("APPLICATION_NAME"));
        
        
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        
        
        StackPane root = new StackPane();
        //root.getChildren().add(btn);
        root.getChildren().add(new FxLibraryView());
        primaryStage.setScene(new Scene(root, APP_WIDTH, APP_HEIGHT));
        primaryStage.show();
    	
    }

}
