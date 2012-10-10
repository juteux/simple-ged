package com.ged.ui.fxwidgetcontrollers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import org.apache.log4j.Logger;

import com.ged.ui.fxwidgets.DocumentPreviewer;

public class FxDocumentPreviewerController implements EventHandler<ActionEvent> {

	private static final Logger logger = Logger.getLogger(FxDocumentPreviewerController.class);
	
	/**
	 * The controlled object
	 */
	private DocumentPreviewer documentPreviewer;

	public FxDocumentPreviewerController(DocumentPreviewer documentPreviewer) {
		this.documentPreviewer = documentPreviewer;
	}
	
	
	@Override
	public void handle(ActionEvent arg0) {
		if (arg0.getSource() == documentPreviewer.getNext()) {
			documentPreviewer.gotoNextPreviewer();
		}
		else if (arg0.getSource() == documentPreviewer.getBack()) {
			documentPreviewer.gotoPreviousPreviewer();
		}
		/*else if (arg0.getSource() == documentPreview.getBtnUnlink()) {
			documentPreview.removeCurrentPreviewer();
		}*/
		else if (arg0.getSource() == documentPreviewer.getBtnOpenFile()) {
			
			// see : http://java.developpez.com/faq/java/?page=systeme#ouvrirFichier
			if ( Desktop.isDesktopSupported() ) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.OPEN)) {
					try {
						desktop.open(new File(documentPreviewer.getCurrentPreviewerFilePath()));
					} catch (IOException e) {
						logger.error("Cannot open file : " + documentPreviewer.getCurrentPreviewerFilePath());
					}
				}
			}
		}
		else if (arg0.getSource() == documentPreviewer.getBtnPrintFile()) {
			documentPreviewer.getCurrentPreviewer().print();
		}
		else {
			logger.warn("Not implemented yet : FxDocumentPreviewerController.handle");
		}
		
	}
	
	
	/**
	 * Show or hide buttons, according to the current widget state
	 */
	public void fixButtonsVisibility() {
	   
		Button btnOpen = documentPreviewer.getBtnOpenFile();
		//SimpleButton btnUnlink = documentPreviewer.getBtnUnlink();
		Button btnNext = documentPreviewer.getNext();
		Button btnPrevious = documentPreviewer.getBack();
		Button btnPrint = documentPreviewer.getBtnPrintFile();
		//JLabel labelNavigation = documentPreviewer.getNavigationLabel();
		int currentIndex = documentPreviewer.getCurrentPreviewerIndex();
		int viewerCount = documentPreviewer.getPreviewersCount();

		btnOpen.setVisible(viewerCount != 0 && documentPreviewer.getCurrentPreviewer().isOpenable());
		btnPrint.setVisible(btnOpen.isVisible());
	    //btnUnlink.setVisible( documentPreviewer.isBtnUnlinkIsAvailable() && viewerCount != 0 );

	    if ( viewerCount <= 1 ) {
	    	if (documentPreviewer.getCurrentPreviewer() != null) {
	    		btnPrint.setDisable(!documentPreviewer.getCurrentPreviewer().isPrintable());
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
	
	
	
}
