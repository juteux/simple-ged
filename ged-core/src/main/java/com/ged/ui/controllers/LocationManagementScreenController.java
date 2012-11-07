package com.ged.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.ged.models.GedDocument;
import com.ged.models.GedDocumentPhysicalLocation;
import com.ged.services.GedDocumentLocationService;
import com.ged.services.GedDocumentService;
import com.ged.ui.listeners.DocumentLocationSelectorListener;
import com.ged.ui.screens.LocationManagementScreen;
import com.tools.PropertiesHelper;

/**
 * This class is the LocationManagementScreen controller
 * 
 * @author xavier
 *
 */
public class LocationManagementScreenController implements ActionListener, KeyListener, DocumentLocationSelectorListener {

	private static final Logger logger = Logger.getLogger(LocationManagementScreenController.class);
	
	/**
	 * The controlled screen
	 */
	private LocationManagementScreen locationManagementScreen;
	
	private Properties properties;

	
	public LocationManagementScreenController(LocationManagementScreen locationManagementScreen) {
		this.locationManagementScreen = locationManagementScreen;
		properties = PropertiesHelper.getInstance().getProperties();
	}

	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		boolean refresh = false;
		
		if (arg0.getSource() == locationManagementScreen.getBtnReturn()) {
			locationManagementScreen.refreshScreens();
			locationManagementScreen.finish();
		}
		else if (arg0.getSource() == locationManagementScreen.getBtnAddNewLabel()) {
			
			// we're adding or editing document
			int currentItemID = locationManagementScreen.getDocumentLocationSelector().getSelectedLocationID();
			if (currentItemID == -1) {
				return;
			}
			
			if (currentItemID == 1) { // adding
				GedDocumentPhysicalLocation gdpl = new GedDocumentPhysicalLocation();
				gdpl.setLabel(locationManagementScreen.getInputNewLabel().getText());
				//GedDocumentLocationService.addLocation(gdpl);
			}
			else { // modify
				GedDocumentPhysicalLocation gdpl = GedDocumentLocationService.findLocationById(currentItemID);
				gdpl.setLabel(locationManagementScreen.getInputNewLabel().getText());
				//GedDocumentLocationService.updateLocation(gdpl);
			}
				
			refresh = true;
		}
		else if (arg0.getSource() == locationManagementScreen.getBtnDeleteCurrentLabel()) {
			int currentItemID = locationManagementScreen.getDocumentLocationSelector().getSelectedLocationID();
			if (currentItemID == -1) {
				return;
			}
			
			GedDocumentPhysicalLocation gdpl = GedDocumentLocationService.findLocationById(currentItemID);
			
			List<GedDocument> docs = GedDocumentService.findDocumentbyLocation(gdpl);
			if (docs.size() > 0) {
				int option = JOptionPane.showConfirmDialog(null,
						"<html>" +
						properties.getProperty("confirm_delete_location").replaceFirst("\\{\\{item_count\\}\\}", String.valueOf(docs.size()))
						+ "</html>",
						properties.getProperty("delete"), JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					GedDocumentLocationService.deleteLocation(gdpl);
					refresh = true;
				}
			} else {
				GedDocumentLocationService.deleteLocation(gdpl);
				refresh = true;
			}
		}
		else {
			logger.warn("Not implemented yet, see LocationManagementScreenController.actionPerformed");
		}
		
		if (refresh) {
			locationManagementScreen.getInputNewLabel().setText("");
			locationManagementScreen.getNewLabelPreview().setText("");
			
			locationManagementScreen.getDocumentLocationSelector().fill();
		}
	}



	@Override
	public void keyReleased(KeyEvent e) {
		locationManagementScreen.getNewLabelPreview().setText(locationManagementScreen.getInputNewLabel().getText());
		locationManagementScreen.getBtnAddNewLabel().setEnabled(! locationManagementScreen.getInputNewLabel().getText().trim().isEmpty());
	}


	@Override
	public void keyPressed(KeyEvent e) {
	}


	@Override
	public void keyTyped(KeyEvent e) {
	}


	@Override
	public void selectionChanged() {

		int currentItemID = locationManagementScreen.getDocumentLocationSelector().getSelectedLocationID();
		if (currentItemID == -1) {
			return;
		}
		
		if (currentItemID == 1) {
			locationManagementScreen.getBtnAddNewLabel().setText(properties.getProperty("add"));
			locationManagementScreen.getInputNewLabel().setText("");
			locationManagementScreen.getNewLabelPreview().setText("");
			locationManagementScreen.getBtnDeleteCurrentLabel().setEnabled(false);
			locationManagementScreen.getBtnAddNewLabel().setEnabled(false);
		}
		else {
			locationManagementScreen.getBtnAddNewLabel().setText(properties.getProperty("modify"));
			locationManagementScreen.getInputNewLabel().setText(locationManagementScreen.getDocumentLocationSelector().getSelectedItem().toString());
			locationManagementScreen.getNewLabelPreview().setText(locationManagementScreen.getInputNewLabel().getText());
			locationManagementScreen.getBtnDeleteCurrentLabel().setEnabled(true);
			locationManagementScreen.getBtnAddNewLabel().setEnabled(false);
		}
		
	}

}
