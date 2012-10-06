package com.ged.ui.fxwidgets;

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
 * A view of the library, a tree with files in GED directory
 * 
 */

public class FxLibraryView extends TreeView<String> {

	/*
 	private final Node rootIcon = new ImageView(
        new Image(getClass().getResourceAsStream("folder_16.png"))
    );
    */
 	
    public FxLibraryView() {

        TreeItem<String> rootItem = new TreeItem<String> ("Inbox"/*, rootIcon*/);
        rootItem.setExpanded(true);
        for (int i = 1; i < 6; i++) {
            TreeItem<String> item = new TreeItem<String> ("Message" + i);            
            rootItem.getChildren().add(item);
        }        
        
        this.setRoot(rootItem);
    }

}
