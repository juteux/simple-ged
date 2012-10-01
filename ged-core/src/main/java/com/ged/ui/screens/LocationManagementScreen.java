package com.ged.ui.screens;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.ged.ui.MainWindow;
import com.ged.ui.controllers.LocationManagementScreenController;
import com.ged.ui.widgets.DocumentLocationSelector;
import com.ged.ui.widgets.SimpleButton;


/**
 * This screen is for managing available locations 
 * 
 * @author xavier
 *
 */
public class LocationManagementScreen extends SoftwareScreen {

	/**
	 * This screen buttons model
	 */
	public class LocationManagementScreenButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public LocationManagementScreenButton(String label, LocationManagementScreenController controller) {
			super(label);
			
			setIconSize(new Dimension(30, 30));
			setPreferredSize(new Dimension(200, 40));
			
			addActionListener(controller);
		}
	}
	
	/**
	 * This same, but a little bit smaller
	 */
	public class LocationManagementScreenMiniButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public LocationManagementScreenMiniButton(String label, LocationManagementScreenController controller) {
			super(label);
			
			setIconSize(new Dimension(15, 15));
			setPreferredSize(new Dimension(200, 20));
			
			addActionListener(controller);
		}
	}

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Button for returning to previous screen
	 */
	private SimpleButton btnReturn;
	
	/**
	 * A field to enter the new label
	 */
	private JTextField inputNewLabel;
	
	/**
	 * A label for the new label preview
	 */
	private JLabel newLabelPreview;
	
	/**
	 * A button for adding the new label
	 */
	private SimpleButton btnAddNewLabel;
	
	/**
	 * A button for deleting current label
	 */
	private SimpleButton btnDeleteCurrentLabel;
	
	/**
	 * Locations list
	 */
	private DocumentLocationSelector documentLocationSelector;
	
	
	public LocationManagementScreen(MainWindow mw) {
		super(mw);
		instantiateWidgets();
	
		// form add
		JPanel addContainer = new JPanel(new MigLayout(				"wrap",
				"[grow,fill,center][]",	// columns
				"[grow,fill,center][]"	// rows));
				));

		addContainer.add(documentLocationSelector, "gap para");
		addContainer.add(btnDeleteCurrentLabel, "span, growx, wrap");
		
		addContainer.add(inputNewLabel, "gap para");
		addContainer.add(btnAddNewLabel, "span,growx,wrap");
		addContainer.add(newLabelPreview, "gap para");
		
		
		// button area
		JPanel btnPanel = new JPanel();
		btnPanel.add(btnReturn);
		
		
		// global positioning
		MigLayout globalLayout = new MigLayout(	
				"wrap",
				"[grow,fill,center]",	// columns
				""	// rows
			);
		
		JPanel container = new JPanel(globalLayout);
		container.add(new JLabel(properties.getProperty("loc_management_help")));
		container.add(addContainer);
		container.add(btnPanel);
		
		add(container);
	}
	
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		LocationManagementScreenController controller = new LocationManagementScreenController(this);
		
		// new label 
		inputNewLabel = new JTextField(100);
		inputNewLabel.addKeyListener(controller);
		
		newLabelPreview = new JLabel();

		btnAddNewLabel = new LocationManagementScreenMiniButton(properties.getProperty("add"), controller);
		btnAddNewLabel.setEnabled(false);
		
		btnDeleteCurrentLabel = new LocationManagementScreenMiniButton(properties.getProperty("delete"), controller);
		btnDeleteCurrentLabel.setEnabled(false);
		
		documentLocationSelector = new DocumentLocationSelector(this, false);
		documentLocationSelector.getController().addDocumentLocationSelectorListener(controller);
		
		// create buttons
		btnReturn  	= new LocationManagementScreenButton(properties.getProperty("back"), controller);
		
		// define associated pictures
		Map<SimpleButton, String> associatedImages = new HashMap<SimpleButton, String>();
		associatedImages.put(btnReturn, 	properties.getProperty("ico_back"));
		associatedImages.put(btnAddNewLabel, 	properties.getProperty("ico_add"));
		associatedImages.put(btnDeleteCurrentLabel, properties.getProperty("ico_remove"));

		// set pictures
		for (Map.Entry<SimpleButton, String> e : associatedImages.entrySet()) {
			e.getKey().setIcon(e.getValue());
		}
	}


	public SimpleButton getBtnReturn() {
		return btnReturn;
	}


	public JTextField getInputNewLabel() {
		return inputNewLabel;
	}


	public JLabel getNewLabelPreview() {
		return newLabelPreview;
	}


	public SimpleButton getBtnAddNewLabel() {
		return btnAddNewLabel;
	}


	public SimpleButton getBtnDeleteCurrentLabel() {
		return btnDeleteCurrentLabel;
	}


	public DocumentLocationSelector getDocumentLocationSelector() {
		return documentLocationSelector;
	}

}
