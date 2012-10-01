package com.ged.ui.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.event.EventListenerList;

import com.ged.models.GedDocument;
import com.ged.services.GedDocumentLocationService;
import com.ged.ui.listeners.DocumentInfoEditorListener;
import com.ged.ui.widgets.DocumentInfoEditor;
import com.tools.DateHelper;

/**
 * The document info editor controller
 * 
 * @author xavier
 * 
 */
public class DocumentInfoEditorController implements KeyListener {

	/**
	 * The controlled object
	 */
	private DocumentInfoEditor documentInfoEditor;

	/**
	 * Event listener
	 */
	private final EventListenerList listeners = new EventListenerList();

	public DocumentInfoEditorController(DocumentInfoEditor documentInfoEditor) {
		this.documentInfoEditor = documentInfoEditor;
	}
	
	/**
	 * Get the document, according to the entered informations
	 */
	public GedDocument getDocument() {
		GedDocument document = new GedDocument();
		document.setName(documentInfoEditor.getInputtedName());
		document.setDate(documentInfoEditor.getInputtedDate());
		document.setDescription(documentInfoEditor.getInputtedDescription());
		document.setLocation(GedDocumentLocationService.findLocationById(documentInfoEditor.getLocationSelector().getSelectedLocationID()));
		return document;
	}
	

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		boolean valid = isValid();
		for (DocumentInfoEditorListener listener : getDocumentInfoEditorListeners()) {
			listener.selectionChanged(valid);
		}
	}

	public boolean isValid() {
		boolean valid = true;
		if (documentInfoEditor.getInputtedName().isEmpty()) {
			valid = false;
		}
		if ( ! DateHelper.isValidDate(documentInfoEditor.getInputDate().getText()) ) {
			valid = false;
		}
		return valid;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	// for externals listeners

	public void addDocumentInfoEditorListener(
			DocumentInfoEditorListener listener) {
		listeners.add(DocumentInfoEditorListener.class, listener);
	}

	public void removeDocumentInfoEditorListener(
			DocumentInfoEditorListener listener) {
		listeners.remove(DocumentInfoEditorListener.class, listener);
	}

	public DocumentInfoEditorListener[] getDocumentInfoEditorListeners() {
		return listeners.getListeners(DocumentInfoEditorListener.class);
	}
	

}
