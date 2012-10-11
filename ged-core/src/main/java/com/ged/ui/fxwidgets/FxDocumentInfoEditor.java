package com.ged.ui.fxwidgets;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ged.ui.fxscreen.AddDocumentScreen;
import com.tools.PropertiesHelper;
import com.tools.javafx.calendar.DatePicker;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class FxDocumentInfoEditor extends GridPane {

	/**
	 * My logger
	 */
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
	 * My parent
	 */
	private WeakReference<AddDocumentScreen> addDocumentScreen;
	
	/**
	 * Properties
	 */
	private Properties properties = PropertiesHelper.getInstance().getProperties();
	
	
	public FxDocumentInfoEditor(AddDocumentScreen addDocumentScreen) {
		this.addDocumentScreen = new WeakReference<>(addDocumentScreen);
		
		instanciateWidgets();
		
		setAlignment(Pos.CENTER);
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(25, 25, 25, 25));
	
		add(editDocumentTitle, 0, 0);
		add(editDocumentDate, 0, 1);
		add(editDocumentDescription, 0, 2);
	}



	/**
	 * Instantiate children
	 */
	private void instanciateWidgets() {

		editDocumentTitle = new TextField();
		editDocumentTitle.setPromptText(properties.getProperty("title_prompt"));	
		
		editDocumentDate = new DatePicker();
		editDocumentDate.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
		editDocumentDate.getCalendarView().setCalendar(new GregorianCalendar());
		editDocumentDate.getCalendarView().setShowTodayButton(true);
		editDocumentDate.getCalendarView().todayButtonTextProperty().set(properties.getProperty("today"));
		editDocumentDate.setSelectedDate(new Date());
		
		editDocumentDescription = new TextArea();
		editDocumentDescription.setPromptText(properties.getProperty("description_prompt"));
	}
	
	
}

