package com.simple.ged.ui.widgets.eventhandler;

import java.lang.ref.WeakReference;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.ui.listeners.DocumentInfoViewerListener;
import com.simple.ged.ui.widgets.DocumentInfoViewer;

/**
 * The event handler for document info viewer
 * 
 * @author xavier
 *
 */
public class DocumentInfoViewerEventHandler implements EventHandler<ActionEvent> {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(DocumentInfoViewerEventHandler.class);
	
	/**
	 * Event listener
	 */
	private final EventListenerList listeners = new EventListenerList();
	
	/**
	 * The listened object
	 */
	private WeakReference<DocumentInfoViewer> documentInfoViewer;
	
	
	public DocumentInfoViewerEventHandler(DocumentInfoViewer documentInfoViewer) {
		this.documentInfoViewer = new WeakReference<>(documentInfoViewer);
	}
	
	
	@Override
	public void handle(ActionEvent arg0) {
		if (arg0.getSource() == documentInfoViewer.get().getBtnEditDocument()) {
			for (DocumentInfoViewerListener listener : getDocumentInfoViewerListeners()) {
				listener.askForDocumentEdition();
			}
		}
		else {
			logger.warn("Not implemented yet, see DocumentInfoViewerEventHandler.handle");
		}
	}

	
	// for externals listeners

	public void addDocumentInfoViewerListener(DocumentInfoViewerListener listener) {
		listeners.add(DocumentInfoViewerListener.class, listener);
	}

	public void removeDocumentInfoViewerListener(DocumentInfoViewerListener listener) {
		listeners.remove(DocumentInfoViewerListener.class, listener);
	}

	public DocumentInfoViewerListener[] getDocumentInfoViewerListeners() {
		return listeners.getListeners(DocumentInfoViewerListener.class);
	}
}
