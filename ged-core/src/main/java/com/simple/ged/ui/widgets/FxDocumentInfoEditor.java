package com.simple.ged.ui.widgets;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.apache.log4j.Logger;

import com.simple.ged.models.GedDocumentPhysicalLocation;
import com.simple.ged.services.GedDocumentLocationService;
import com.simple.ged.ui.widgets.eventhandler.FxDocumentInfoEditorEventHandler;
import com.tools.PropertiesHelper;

import fr.xmichel.javafx.calendar.DatePicker;

public class FxDocumentInfoEditor extends GridPane {

	/**
	 * My logger
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FxDocumentInfoEditor.class);
	
	/**
	 * Line edit title
	 */
	private TextField editDocumentTitle;
	
	/**
	 * Date edit document date
	 */
	private DatePicker editDocumentDate;
	
	/**
	 * Text edit description
	 */
	private TextArea editDocumentDescription;
	
	/**
	 * Properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	/**
	 * Event handler
	 */
	private FxDocumentInfoEditorEventHandler eventHandler;
	
	/**
	 * The combo box to select document location
	 */
	private ComboBox<String> comboDocumentLocation;
	
	
	public FxDocumentInfoEditor() {
		instanciateWidgets();
		
		setAlignment(Pos.CENTER);
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(25, 25, 25, 25));
	
		add(editDocumentTitle, 0, 0);
		add(editDocumentDate, 0, 1);
		add(editDocumentDescription, 0, 2);
		add(comboDocumentLocation, 0, 3);
	}



	/**
	 * Instantiate children
	 */
	private void instanciateWidgets() {

		eventHandler = new FxDocumentInfoEditorEventHandler(this);
		
		editDocumentTitle = new TextField();
		editDocumentTitle.setPromptText(properties.getProperty("title_prompt"));	
		editDocumentTitle.setOnKeyReleased(eventHandler);
		
		editDocumentDate = new DatePicker();
		editDocumentDate.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
		
		editDocumentDate.getCalendarView().setShowTodayButton(true);
		editDocumentDate.getCalendarView().todayButtonTextProperty().set(properties.getProperty("today"));
		editDocumentDate.setSelectedDate(new Date());
		editDocumentDate.setOnKeyReleased(eventHandler);
		
		editDocumentDescription = new TextArea();
		editDocumentDescription.setPromptText(properties.getProperty("description_prompt"));
	
		List<String> locationList = new ArrayList<>();
		for (GedDocumentPhysicalLocation l : GedDocumentLocationService.getLocations()) {
			locationList.add(l.getLabel());
		}
		
		ObservableList<String> options = FXCollections.observableArrayList(locationList);

		comboDocumentLocation = new ComboBox<>(options);
		comboDocumentLocation.setEditable(true);   
		//HBox.setHgrow(comboDocumentLocation, Priority.ALWAYS);
	}


	/**
	 * Set default values in fields
	 */
	public void clearFields() {
		editDocumentTitle.setText("");
		editDocumentDate.getCalendarView().setCalendar(new GregorianCalendar());
		editDocumentDescription.setText("");
	}
	
	

	public TextField getEditDocumentTitle() {
		return editDocumentTitle;
	}
	
	public DatePicker getEditDocumentDate() {
		return editDocumentDate;
	}

	public TextArea getEditDocumentDescription() {
		return editDocumentDescription;
	}

	public FxDocumentInfoEditorEventHandler getEventHandler() {
		return eventHandler;
	}
	
	public ComboBox<String> getComboDocumentLocation() {
		return comboDocumentLocation;
	}

}

