package com.ged.services;

import java.util.List;

import com.ged.dao.DocumentLocationDAO;
import com.ged.models.GedDocument;
import com.ged.models.GedDocumentPhysicalLocation;

public class GedDocumentLocationService {

	
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
			loc.setId(1);
			loc.setLabel(" ");
			DocumentLocationDAO.saveOrUpdate(loc);		
		}
	}
	
	public static GedDocumentPhysicalLocation findLocationById(int id) {
		return DocumentLocationDAO.findLocationById(id);
	}
	
	
	public static void addLocation(GedDocumentPhysicalLocation loc) {
		DocumentLocationDAO.saveOrUpdate(loc);
	}
	
	public static void updateLocation(GedDocumentPhysicalLocation loc) {
		DocumentLocationDAO.saveOrUpdate(loc);
	}
	
	public static void deleteLocation(GedDocumentPhysicalLocation location)
	{	
		GedDocumentPhysicalLocation defaultLocation = DocumentLocationDAO.findLocationById(1);
		
		List<GedDocument> documentsWithLocationToUpdate = GedDocumentService.findDocumentbyLocation(location);
		for (GedDocument document : documentsWithLocationToUpdate) {
			document.setLocation(defaultLocation);
			GedDocumentService.updateDocument(document);
		}
		
		DocumentLocationDAO.delete(location);
	}
	
}

