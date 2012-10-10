package com.ged.ui.fxscreen;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import com.ged.ui.FxMainWindow;
import com.ged.ui.fxwidgets.DocumentPreviewer;
import com.tools.javafx.calendar.DatePicker;

public class AddDocumentScreen extends FxSoftwareScreen {

	/**
	 * Button add from file system
	 */
	private Button btnAddFromFS;
	
	/**
	 * Button add from scanner
	 */
	private Button btnAddFromScanner;
	
	/**
	 * Line edit title
	 */
	private TextField editDocumentTitle;
	
	/**
	 * Date edit document date
	 */
	private DatePicker editDocumentDate;
	
	/**
	 * Button to submit new document
	 */
	private Button btnSubmit;
	
	/**
	 * Text edit description
	 */
	private TextArea editDocumentDescription;
	
	/**
	 * Document preview
	 */
	private DocumentPreviewer documentPreviewer;
	
	
	/**
	 * 
	 * @param mw
	 */
	public AddDocumentScreen(FxMainWindow mw) {
		super(mw);
	
		instanciateWidgets();
		
		VBox leftBox = new VBox();
		leftBox.setAlignment(Pos.CENTER);
		leftBox.setPadding(new Insets(25, 25, 25, 25));
		
		leftBox.getChildren().addAll(btnAddFromFS, btnAddFromScanner, new Separator(), editDocumentTitle, editDocumentDate, editDocumentDescription, btnSubmit);

		this.getChildren().addAll(leftBox);
	}

	
	private void instanciateWidgets() {

		//this.getStylesheets().add("templates/tools/calendarstyle.css");

		btnAddFromFS = new Button(properties.getProperty("add_from_hard_drive"));
		
		
		btnAddFromScanner = new Button(properties.getProperty("add_scan"));
		
		
		editDocumentTitle = new TextField();
		editDocumentTitle.setPromptText(properties.getProperty("title_prompt"));	
		
		editDocumentDate = new DatePicker();
		editDocumentDate.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
		editDocumentDate.localeProperty().set(Locale.FRANCE);
		editDocumentDate.getCalendarView().setCalendar(new GregorianCalendar());
		editDocumentDate.getCalendarView().setShowTodayButton(true);
		editDocumentDate.getCalendarView().todayButtonTextProperty().set(properties.getProperty("today"));
		
		editDocumentDescription = new TextArea();
		editDocumentDescription.setPromptText(properties.getProperty("description_prompt"));
		
		btnSubmit = new Button(properties.getProperty("save"));
	}


	public Button getBtnAddFromFS() {
		return btnAddFromFS;
	}


	public Button getBtnAddFromScanner() {
		return btnAddFromScanner;
	}


	public TextField getEditDocumentTitle() {
		return editDocumentTitle;
	}


	public DatePicker getEditDocumentDate() {
		return editDocumentDate;
	}


	public Button getBtnSubmit() {
		return btnSubmit;
	}


	public TextArea getEditDocumentDescription() {
		return editDocumentDescription;
	}


	public DocumentPreviewer getDocumentPreviewer() {
		return documentPreviewer;
	}

}
