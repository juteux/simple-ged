package com.ged.ui.widgets;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.ged.ui.controllers.DocumentInfoEditorController;
import com.ged.ui.screens.SoftwareScreen;
import com.tools.DateHelper;
import com.tools.ui.AbstractWidget;

/**
 * An editor for document informations
 * 
 * @author xavier
 * 
 */
public class DocumentInfoEditor extends AbstractWidget {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A field to enter the document name
	 */
	private JTextField inputName;

	/**
	 * A field to enter the date of the document
	 */
	private JFormattedTextField inputDate;

	/**
	 * A field to enter the document description
	 */
	private JTextArea inputDescription;

	/**
	 * A field for document location
	 */
	private DocumentLocationSelector locationSelector;
	
	/**
	 * My container
	 */
	private WeakReference<SoftwareScreen> parentScreen;
	
	
	/**
	 * The controller
	 */
	private DocumentInfoEditorController controller;

	public DocumentInfoEditor(SoftwareScreen parent) {
		
		super();
		this.parentScreen = new WeakReference<SoftwareScreen>(parent);
		
		instantiateWidgets();

		JPanel container = new JPanel(new MigLayout());

		container.add(new JLabel(properties.getProperty("name_")), "gap para");
		container.add(inputName, "span,growx,wrap");

		container.add(new JLabel(properties.getProperty("date_")), "gap para");
		container.add(inputDate, "span,growx,wrap");

		container.add(new JLabel(properties.getProperty("description_")), "gap para");
		container.add(new JScrollPane(inputDescription), "span,growx,wrap");

		container.add(new JLabel(properties.getProperty("location_")), "gap para");
		container.add(locationSelector, "span,growx,wrap");
		
		//add(container);
	}

	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {

		controller = new DocumentInfoEditorController(this);

		inputName = new JTextField(30);
		inputDescription = new JTextArea(5, 30);
		inputDate = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
		locationSelector = new DocumentLocationSelector(parentScreen.get(), true);
		
		inputName.addKeyListener(controller);
		inputDate.addKeyListener(controller);

		clearFields();
	}

	public JTextField getInputName() {
		return inputName;
	}

	public JFormattedTextField getInputDate() {
		return inputDate;
	}

	public JTextArea getInputDescription() {
		return inputDescription;
	}
	
	/**
	 * Get the current name
	 */
	public String getInputtedName() {
		return inputName.getText().trim();
	}

	/**
	 * Get the current date
	 * 
	 * @warning Can be null if date isn't valid
	 */
	public Date getInputtedDate() {

		if ( ! DateHelper.isValidDate(inputDate.getText()) ) {
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date d = null;
		try {
			d = dateFormat.parse(inputDate.getText());
		} catch (ParseException e) {
		}
		return d;
	}

	/**
	 * Get the current description
	 */
	public String getInputtedDescription() {
		return inputDescription.getText().trim();
	}

	public DocumentInfoEditorController getController() {
		return controller;
	}
	
	public DocumentLocationSelector getLocationSelector() {
		return locationSelector;
	}

	/**
	 * Clean all fields
	 */
	public void clearFields() {
		inputName.setText("");
		inputDescription.setText("");
		inputDate.setText(DateHelper.calendarToString(new GregorianCalendar()));
		locationSelector.setSelectedIndex(0);
	}
	
	public void reloadComboLocation() {
		locationSelector.fill();
	}
	
}
