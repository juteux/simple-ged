package com.ged.ui.fxwidgets.eventhandler;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import org.apache.log4j.Logger;

import com.ged.ui.fxpreviewwidgets.AbstractFilePreviewer;
import com.ged.ui.fxwidgets.DocumentPreviewer;
import com.ged.ui.listeners.DocumentPreviewListener;

public class FxDocumentPreviewerEventHandler implements EventHandler<ActionEvent> {

	private static final Logger logger = Logger.getLogger(FxDocumentPreviewerEventHandler.class);
	
	/**
	 * The controlled object
	 */
	private WeakReference<DocumentPreviewer> documentPreviewer;

	/**
	 * Event listener
	 */
	private final EventListenerList listeners = new EventListenerList();
	
	
	public FxDocumentPreviewerEventHandler(DocumentPreviewer documentPreviewer) {
		this.documentPreviewer = new WeakReference<>(documentPreviewer);
	}
	
	
	/**
	 * Return the list of files in the previewer
	 */
	public List<File> getFileList() {
		List<File> l = new ArrayList<File>();
		for (AbstractFilePreviewer previewer : documentPreviewer.get().getPreviewers()) {
			l.add(new File(previewer.getAbsoluteFilePath()));
		}
		return l;
	}
	
	
	@Override
	public void handle(ActionEvent arg0) {
		if (arg0.getSource() == documentPreviewer.get().getNext()) {
			documentPreviewer.get().gotoNextPreviewer();
		}
		else if (arg0.getSource() == documentPreviewer.get().getBack()) {
			documentPreviewer.get().gotoPreviousPreviewer();
		}
		else if (arg0.getSource() == documentPreviewer.get().getBtnUnlinkFile()) {
			documentPreviewer.get().removeCurrentPreviewer();
		}
		else if (arg0.getSource() == documentPreviewer.get().getBtnOpenFile()) {
			
			// see : http://java.developpez.com/faq/java/?page=systeme#ouvrirFichier
			if ( Desktop.isDesktopSupported() ) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.OPEN)) {
					try {
						desktop.open(new File(documentPreviewer.get().getCurrentPreviewerFilePath()));
					} catch (IOException e) {
						logger.error("Cannot open file : " + documentPreviewer.get().getCurrentPreviewerFilePath());
					}
				}
			}
		}
		else if (arg0.getSource() == documentPreviewer.get().getBtnPrintFile()) {
			documentPreviewer.get().getCurrentPreviewer().print();
		}
		else {
			logger.warn("Not implemented yet : FxDocumentPreviewerEventHandler.handle");
		}
		
	}
	
	
	/**
	 * Show or hide buttons, according to the current widget state
	 */
	public void fixButtonsVisibility() {
	   
		fireEvent();
		
		Button btnOpen = documentPreviewer.get().getBtnOpenFile();
		Button btnUnlink = documentPreviewer.get().getBtnUnlinkFile();
		Button btnNext = documentPreviewer.get().getNext();
		Button btnPrevious = documentPreviewer.get().getBack();
		Button btnPrint = documentPreviewer.get().getBtnPrintFile();
		//JLabel labelNavigation = documentPreviewer.getNavigationLabel();
		int currentIndex = documentPreviewer.get().getCurrentPreviewerIndex();
		int viewerCount = documentPreviewer.get().getPreviewersCount();

		btnOpen.setVisible(viewerCount != 0 && documentPreviewer.get().getCurrentPreviewer().isOpenable());
		btnPrint.setVisible(btnOpen.isVisible());
	    btnUnlink.setVisible(documentPreviewer.get().isEditionMode());
	    btnUnlink.setDisable(viewerCount == 0);

	    if ( viewerCount <= 1 ) {
	    	if (documentPreviewer.get().getCurrentPreviewer() != null) {
	    		btnPrint.setDisable(!documentPreviewer.get().getCurrentPreviewer().isPrintable());
	    	}
	    	
	    	btnPrevious.setVisible(false);
	        btnNext.setVisible(false);
	        //labelNavigation.setVisible(false);
	        return;
	    }
	    
	    //labelNavigation.setVisible(true);
	    //labelNavigation.setText( (currentIndex+1) + "/" + viewerCount );

	    btnPrevious.setVisible(true);
	    btnPrevious.setDisable(currentIndex == 0);

	    btnNext.setVisible(true);
	    btnNext.setDisable(currentIndex == viewerCount-1);
	}
	
	
	/**
	 * Is the file list empty ?
	 */
	public boolean isFileListEmpty() {
		return (documentPreviewer.get().getPreviewersCount() == 0);
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
	
	/**
	 * Fire event
	 */
	private void fireEvent() {
        for(DocumentPreviewListener listener : getDocumentPreviewListeners()) {
            listener.fileCountChanged(documentPreviewer.get().getPreviewersCount());
        }
	}
}
