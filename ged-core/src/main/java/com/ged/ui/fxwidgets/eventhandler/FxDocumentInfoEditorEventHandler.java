package com.ged.ui.fxwidgets.eventhandler;

import java.lang.ref.WeakReference;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import javax.swing.event.EventListenerList;

import com.ged.models.GedDocument;
import com.ged.ui.fxwidgets.FxDocumentInfoEditor;
import com.ged.ui.listeners.DocumentInfoEditorListener;


public class FxDocumentInfoEditorEventHandler implements EventHandler<KeyEvent> {

	/**
	 * The object we're listerning to
	 */
	private WeakReference<FxDocumentInfoEditor> documentInfoEditor;

	/**
	 * Event listener
	 */
	private final EventListenerList listeners = new EventListenerList();

	public FxDocumentInfoEditorEventHandler(FxDocumentInfoEditor documentInfoEditor) {
		this.documentInfoEditor = new WeakReference<>(documentInfoEditor);
	}
	
	
	/**
	 * Get the document, according to the entered informations
	 */
	public GedDocument getDocument() {
		GedDocument document = new GedDocument();
		document.setName(documentInfoEditor.get().getEditDocumentTitle().getText());
		document.setDate(documentInfoEditor.get().getEditDocumentDate().getSelectedDate());
		document.setDescription(documentInfoEditor.get().getEditDocumentDescription().getText());
		//document.setLocation(GedDocumentLocationService.findLocationById(documentInfoEditor.getLocationSelector().getSelectedLocationID()));
		return document;
	}
	
	
	
	@Override
	public void handle(KeyEvent arg0) {
		boolean valid = isValid();
		for (DocumentInfoEditorListener listener : getDocumentInfoEditorListeners()) {
			listener.selectionChanged(valid);
		}
	}
	
	

	public boolean isValid() {
		boolean valid = true;
		if (documentInfoEditor.get().getEditDocumentTitle().getText().isEmpty()) {
			valid = false;
		}
		return valid;
	}

	
	
	// for externals listeners

	public void addDocumentInfoEditorListener(DocumentInfoEditorListener listener) {
		listeners.add(DocumentInfoEditorListener.class, listener);
	}

	public void removeDocumentInfoEditorListener(DocumentInfoEditorListener listener) {
		listeners.remove(DocumentInfoEditorListener.class, listener);
	}

	public DocumentInfoEditorListener[] getDocumentInfoEditorListeners() {
		return listeners.getListeners(DocumentInfoEditorListener.class);
	}

}
