package com.ged.ui.fxscreen;

import com.ged.ui.fxwidgets.FxDocumentInfoViewer;
import com.ged.ui.fxwidgets.FxLibraryView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class LibraryViewScreen extends HBox {

	
	public LibraryViewScreen() {
        this.getChildren().add(new FxLibraryView());
        this.getChildren().add(new FxDocumentInfoViewer());
	}
	
}
