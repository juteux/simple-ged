package com.ged.ui.widgets;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JComboBox;

import com.ged.models.GedDocumentPhysicalLocation;
import com.ged.services.GedDocumentLocationService;
import com.ged.ui.controllers.DocumentLocationSelectorController;
import com.ged.ui.screens.SoftwareScreen;
import com.tools.PropertiesHelper;


/**
 * This class is a combo box for selecting document location
 * 
 * You can also add some location
 * 
 * @author xavier
 *
 */
public class DocumentLocationSelector extends JComboBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * List of locations IDs
	 */
	private List<Integer> locationIDs;
	
	
	protected Properties properties;
	
	/**
	 * My container
	 */
	private WeakReference<SoftwareScreen> parentScreen;
	
	/**
	 * Does I need to show management option ?
	 */
	private boolean showManagementOption;
	
	/**
	 * My controller
	 */
	private WeakReference<DocumentLocationSelectorController> controller;
	
	
	
	public DocumentLocationSelector(SoftwareScreen parent, boolean showManagementOption) {
		super();
		
		this.showManagementOption = showManagementOption;
		
		this.parentScreen = new WeakReference<SoftwareScreen>(parent);
		this.properties = PropertiesHelper.getInstance().getProperties();
		
		fill();
		
		controller = new WeakReference<DocumentLocationSelectorController>(new DocumentLocationSelectorController(this));
		addItemListener(controller.get());
	}
	
	
	/**
	 * @return The selection id, or -1 if selection is not valid
	 */
	public int getSelectedLocationID() {
		if (getSelectedIndex() >= locationIDs.size()) {
			return -1;
		}
		return locationIDs.get(getSelectedIndex());
	}
	
	
	/**
	 * Set the selected index according to the given location
	 */
	public void setIndexFromLocation(GedDocumentPhysicalLocation gdpl) {
		setSelectedIndex(locationIDs.indexOf(gdpl.getId()));
	}
	
	
	/**
	 * Fill this object with available locations
	 */
	public void fill() {

		locationIDs = new ArrayList<Integer>();
		removeAllItems();

		for (GedDocumentPhysicalLocation l : GedDocumentLocationService.getLocations()) {
			addItem(l.getLabel());
			locationIDs.add(l.getId());
		}
		if (showManagementOption) {
			addItem(properties.getProperty("manage_locations"));
		}
	}
	
	/**
	 * Get my container
	 */
	public SoftwareScreen getParentScreen() {
		return parentScreen.get();
	}


	public boolean isShowManagementOption() {
		return showManagementOption;
	}
	
	
	public DocumentLocationSelectorController getController() {
		return controller.get();
	}

}
