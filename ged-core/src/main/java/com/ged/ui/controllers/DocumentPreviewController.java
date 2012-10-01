package com.ged.ui.controllers;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import com.ged.ui.listeners.DocumentPreviewListener;
import com.ged.ui.preview_widgets.AbstractDocumentPreviewer;
import com.ged.ui.widgets.DocumentPreview;
import com.ged.ui.widgets.SimpleButton;

/**
 * The controller for document preview widget
 * @author xavier
 *
 */
public class DocumentPreviewController implements ActionListener {

	private static final Logger logger = Logger.getLogger(DocumentPreviewController.class);	
	
	/**
	 * The controlled widget
	 */
	private DocumentPreview documentPreview;
	
	/**
	 * Event listener
	 */
	private final EventListenerList listeners = new EventListenerList();
	
	
	
	public DocumentPreviewController(DocumentPreview documentPreview) {
		this.documentPreview = documentPreview;
	}
	
	/**
	 * Return true if the previewer list is empty, false otherwise
	 */
	public boolean isEmpty() {
		return (documentPreview.getPreviewersCount() == 0);
	}

	/**
	 * Return the list of files attached to document
	 */
	public List<File> getFileList() {
		List<File> l = new ArrayList<File>();
		
		for (AbstractDocumentPreviewer previewer : documentPreview.getPreviewers()) {
			l.add(new File(previewer.getAbsoluteFilePath()));
		}
		
		return l;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == documentPreview.getBtnNext()) {
			documentPreview.gotoNextPreviewer();
		}
		else if (arg0.getSource() == documentPreview.getBtnPrevious()) {
			documentPreview.gotoPreviousPreviewer();
		}
		else if (arg0.getSource() == documentPreview.getBtnUnlink()) {
			documentPreview.removeCurrentPreviewer();
		}
		else if (arg0.getSource() == documentPreview.getBtnOpen()) {
			
			// see : http://java.developpez.com/faq/java/?page=systeme#ouvrirFichier
			if ( Desktop.isDesktopSupported() ) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.OPEN)) {
					try {
						desktop.open(new File(documentPreview.getCurrentPreviewerFilePath()));
					} catch (IOException e) {
						logger.error("Cannot open file : " + documentPreview.getCurrentPreviewerFilePath());
					}
				}
			}

		}
		else if (arg0.getSource() == documentPreview.getBtnPrint()) {
			documentPreview.getCurrentPreviewer().print();
		}
		else {
			logger.warn("Not implemented yet : DocumentPreviewController.actionPerformed");
		}
	}

	/**
	 * Fire event
	 */
	private void fireEvent() {
        for(DocumentPreviewListener listener : getDocumentPreviewListeners()) {
            listener.fileCountChanged(documentPreview.getPreviewersCount());
        }
	}
	
	
	/**
	 * Show or hide buttons, according to the current widget state
	 */
	public void fixButtonsVisibility() {
	   
		fireEvent();
		
		SimpleButton btnOpen = documentPreview.getBtnOpen();
		SimpleButton btnUnlink = documentPreview.getBtnUnlink();
		SimpleButton btnNext = documentPreview.getBtnNext();
		SimpleButton btnPrevious = documentPreview.getBtnPrevious();
		SimpleButton btnPrint = documentPreview.getBtnPrint();
		JLabel labelNavigation = documentPreview.getNavigationLabel();
		int currentIndex = documentPreview.getCurrentPreviewerIndex();
		int viewerCount = documentPreview.getPreviewersCount();

		btnOpen.setVisible( viewerCount != 0 );
		btnPrint.setVisible(btnOpen.isVisible());
	    btnUnlink.setVisible( documentPreview.isBtnUnlinkIsAvailable() && viewerCount != 0 );

	    if ( viewerCount <= 1 ) {
	    	if (documentPreview.getCurrentPreviewer() != null) {
	    		btnPrint.setEnabled(documentPreview.getCurrentPreviewer().isPrintable());
	    	}
	    	
	    	btnPrevious.setVisible(false);
	        btnNext.setVisible(false);
	        labelNavigation.setVisible(false);
	        return;
	    }
	    
	    labelNavigation.setVisible(true);
	    labelNavigation.setText( (currentIndex+1) + "/" + viewerCount );

	    btnPrevious.setVisible(true);
	    btnPrevious.setEnabled(currentIndex > 0);

	    btnNext.setVisible(true);
	    btnNext.setEnabled(currentIndex < viewerCount-1);
	}
	
	// for externals listeners

	public void addDocumentPreviewListener(DocumentPreviewListener listener) {
		listeners.add(DocumentPreviewListener.class, listener);
	}

	public void removeDocumentPreviewListener(DocumentPreviewListener listener) {
		listeners.remove(DocumentPreviewListener.class, listener);
	}

	public DocumentPreviewListener[] getDocumentPreviewListeners() {
		return listeners.getListeners(DocumentPreviewListener.class);
	}
	
}
