package com.simple.ged.ui.screen;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.simple.ged.Profile;
import com.simple.ged.ui.MainWindow;
import com.simple.ged.ui.screen.eventhandler.SettingsScreenEventHandler;

import fr.xmichel.javafx.fieldset.FxFieldSet;
import fr.xmichel.javafx.fileselector.DirectorySelector;

/**
 * This screen is for global configuration
 *
 * @author xavier
 */
public class SettingsScreen extends SoftwareScreen {

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
	
	
	public SettingsScreen(MainWindow mw) {
		super(mw);

		instanciateWidgets();
		
		VBox fieldSetLibraryLocationBox = new VBox();
		//fieldSetLibraryLocationBox.getStyleClass().add("debug-layout");
		HBox.setHgrow(fieldSetLibraryLocationBox, Priority.ALWAYS);
		fieldSetLibraryLocationBox.setSpacing(10);
		FxFieldSet fieldSetLibraryLocation = new FxFieldSet(fieldSetLibraryLocationBox);
		HBox.setHgrow(fieldSetLibraryLocation, Priority.ALWAYS);
		fieldSetLibraryLocation.setStyleClassForBorder("fieldSet");
		fieldSetLibraryLocationBox.getChildren().addAll(new Label(properties.getProperty("question_library_location")), libraryLocationSelector);
	

		VBox fieldSetPrinterBox = new VBox();
		fieldSetPrinterBox.setSpacing(10);
		FxFieldSet fieldSetPrinter = new FxFieldSet(fieldSetPrinterBox);
		fieldSetPrinter.setStyleClassForBorder("fieldSet");
		fieldSetPrinterBox.getChildren().addAll(new Label(properties.getProperty("question_printer")), comboPrinter);
		
		VBox mainLayout = new VBox(20);
		HBox.setHgrow(mainLayout, Priority.ALWAYS);

		mainLayout.getChildren().addAll(fieldSetLibraryLocation, fieldSetPrinter, btnSubmit);
		
		this.getChildren().addAll(mainLayout);
	}
	
	
	private void instanciateWidgets() {
		
		eventHandler = new SettingsScreenEventHandler(this);
		
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


	public DirectorySelector getLibraryLocationSelector() {
		return libraryLocationSelector;
	}

	public ComboBox<String> getComboPrinter() {
		return comboPrinter;
	}

	public Button getBtnSubmit() {
		return btnSubmit;
	}

}
