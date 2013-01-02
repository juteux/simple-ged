package com.simple.ged.services;

import java.util.List;

import com.simple.ged.dao.DocumentLocationDAO;
import com.simple.ged.models.GedDocument;
import com.simple.ged.models.GedDocumentPhysicalLocation;

/**
 * Service for document location manipulation
 * 
 * @author xavier
 *
 */
public final class GedDocumentLocationService {

	
	/**
	 * Should not be instantiated
	 */
	private GedDocumentLocationService() {
	}
	
	
	/**
	 * Get all locations
	 * 
	 * @return
	 * 			All document location available
	 */
	public static synchronized List<GedDocumentPhysicalLocation> getLocations() {
		return DocumentLocationDAO.getLocations();
	}
	
	/**
	 * If there is not at least one document location, one is created
	 */
	public static void makeSurAtLeastOneDocumentLocationExists() {
	
		List<GedDocumentPhysicalLocation> locations = DocumentLocationDAO.getLocations();
		if (locations.isEmpty()) {
			GedDocumentPhysicalLocation loc = new GedDocumentPhysicalLocation();
			loc.setLabel("");
			DocumentLocationDAO.saveOrUpdate(loc);		
		}
	}
	
	/**
	 * Get the location and create it if it doesn't exists
	 */
	public static GedDocumentPhysicalLocation getTheLocationAndCreateItIfItDoesntExists(String location) {
		
		if ( ! locationExists(location)) {
			GedDocumentPhysicalLocation loc = new GedDocumentPhysicalLocation();
			loc.setLabel(location);
			DocumentLocationDAO.saveOrUpdate(loc);
		}

		return DocumentLocationDAO.getLocationByName(location);
	}
	
	/**
	 * Does the given location exists ?
	 * 
	 * @param location
	 * 			The location we're looking for
	 * 
	 * @return
	 * 			True if the given location exists, false otherwise
	 */
	public static boolean locationExists(String location) {
		return (DocumentLocationDAO.getLocationByName(location) != null);
	}
	
	/**
	 * Find the location by id
	 * 
	 * @param id
	 * 			The id of the location we're looking for
	 * 
	 * @return
	 * 			The location associated to the given id
	 */
	public static GedDocumentPhysicalLocation findLocationById(int id) {
		return DocumentLocationDAO.findLocationById(id);
	}
	
	
	/**
	 * Save or update the given location
	 * 
	 * @param loc
	 * 			The location to save or update
	 */
	public static void saveOrUpdateLocation(GedDocumentPhysicalLocation loc) {
		DocumentLocationDAO.saveOrUpdate(loc);
	}
	
	/**
	 * Delete the given location
	 * 
	 * @param location
	 * 				The location to delete
	 */
	public static void deleteLocation(GedDocumentPhysicalLocation location)
	{	
		GedDocumentPhysicalLocation defaultLocation = DocumentLocationDAO.findLocationById(1);
		
		List<GedDocument> documentsWithLocationToUpdate = GedDocumentService.findDocumentbyLocation(location);
		for (GedDocument document : documentsWithLocationToUpdate) {
			document.setLocation(defaultLocation);
			GedDocumentService.addOrUpdateDocument(document);
		}
		
		DocumentLocationDAO.delete(location);
	}
	
}

