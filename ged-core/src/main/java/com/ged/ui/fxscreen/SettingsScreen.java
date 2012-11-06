package com.ged.ui.fxscreen;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.ged.Profile;
import com.ged.ui.FxMainWindow;
import com.ged.ui.fxscreen.eventhandler.SettingsScreenEventHandler;
import com.tools.ui.DirectorySelector;

/**
 * This screen is for global configuration
 *
 * @author xavier
 */
public class SettingsScreen extends FxSoftwareScreen {

	/**
	 * The directory selector for library location
	 */
	private DirectorySelector libraryLocationSelector;
	
	/**
	 * The combo for printer selection
	 */
	private ComboBox<String> comboPrinter;
	
	/**
	 * Save button 
	 */
	private Button btnSubmit;
	
	/**
	 * The event handler
	 */
	private SettingsScreenEventHandler eventHandler;
	
	
	public SettingsScreen(FxMainWindow mw) {
		super(mw);

		instanciateWidgets();
		
		//JPanel libraryInformationPanel = new JPanel(libraryInfoLayout);
		//libraryInformationPanel.setBorder(BorderFactory.createTitledBorder(properties.getProperty("question_library_location")));
		
		//JPanel printerPanel = new JPanel(printerLayout);
		//printerPanel.setBorder(BorderFactory.createTitledBorder(properties.getProperty("question_printer")));
		//printerPanel.add(comboPrinter);
		HBox.setHgrow(comboPrinter, Priority.ALWAYS);
		
		VBox mainLayout = new VBox(20);
		HBox.setHgrow(mainLayout, Priority.ALWAYS);
		
		mainLayout.getChildren().addAll(libraryLocationSelector, comboPrinter, btnSubmit);
		
		this.getChildren().addAll(mainLayout);
	}
	
	
	private void instanciateWidgets() {
		
		libraryLocationSelector = new DirectorySelector(properties.getProperty("select_root_library_directory"));
		libraryLocationSelector.setCurrentFilePath(Profile.getInstance().getLibraryRoot());
		libraryLocationSelector.addFileChangedListener(eventHandler);

		
		comboPrinter = new ComboBox<>();

		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(new Copies(1));
		
		for (PrintService ps : PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.JPEG, pras)) {
			comboPrinter.getItems().add(ps.getName());
		}
		comboPrinter.getSelectionModel().select(Profile.getInstance().getDefaultPrinterName());
		
		comboPrinter.setOnAction(eventHandler);
		
		
		btnSubmit = new Button(properties.getProperty("save"));
		btnSubmit.setPrefSize(250, 80);
		btnSubmit.setOnAction(eventHandler);
		
		Image i3 = new Image(getClass().getResourceAsStream(properties.getProperty("ico_save")));
		ImageView iv3 = new ImageView(i3);
		iv3.setSmooth(true);
		iv3.setFitWidth(64);
		iv3.setFitHeight(64);
		btnSubmit.setGraphic(iv3);
		
		btnSubmit.setDisable(true);
	}

}
