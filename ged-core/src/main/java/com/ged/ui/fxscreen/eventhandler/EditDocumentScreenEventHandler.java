package com.ged.ui.fxscreen.eventhandler;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.models.GedDocumentFile;
import com.ged.services.GedDocumentService;
import com.ged.tools.FileHelper;
import com.ged.ui.fxscreen.EditDocumentScreen;
import com.ged.ui.listeners.DocumentInfoEditorListener;
import com.ged.ui.listeners.DocumentPreviewListener;
import com.tools.PropertiesHelper;
import com.tools.javafx.ModalConfirm;
import com.tools.javafx.ModalConfirmResponse;

/**
 * The event handler for EditDocumentScreenEventHandler
 * 
 * @author xavier
 *
 */
public class EditDocumentScreenEventHandler implements DocumentInfoEditorListener, DocumentPreviewListener, EventHandler<ActionEvent> {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(EditDocumentScreenEventHandler.class);
	
	/**
	 * Properties
	 */
	private Properties properties = PropertiesHelper.getInstance().getProperties();
	
	/**
	 * The handled screen
	 */
	private WeakReference<EditDocumentScreen> editDocumentScreen;

	
	public EditDocumentScreenEventHandler(EditDocumentScreen editDocumentScreen) {
		this.editDocumentScreen = new WeakReference<>(editDocumentScreen);
	}


	@Override
	public void handle(ActionEvent arg0) {
		
		if (arg0.getSource() == editDocumentScreen.get().getBtnBack()) {
			
        	ModalConfirm.show(editDocumentScreen.get().getMainStage(), new ModalConfirmResponse() {
    			@Override
    			public void confirm() {
    				editDocumentScreen.get().finish();
    			}
    			@Override
    			public void cancel() {
    				// do nothing
    			}
    		}, properties.getProperty("stop_edition"));
		
		} 
		else if (arg0.getSource() == editDocumentScreen.get().getBtnSave()) {
    			
			GedDocument document = editDocumentScreen.get().getDocInfoEditor().getEventHandler().getDocument();
			
			if (editDocumentScreen.get().getDocument() != null) {
				document.setId(editDocumentScreen.get().getDocument().getId());
			}
			
			List<GedDocumentFile> attachedFiles = FileHelper.copyFilesIfNecessary(
					editDocumentScreen.get().getDocumentPreviewer().getEventHandler().getFileList(), 
					new File(Profile.getInstance().getLibraryRoot() + editDocumentScreen.get().getDocumentRelativeDirectory()), 
					editDocumentScreen.get().getDocInfoEditor().getEditDocumentTitle().getText()
				);
			document.setDocumentFiles(attachedFiles);
			
			GedDocumentService.updateDocument(document);
			
        	ModalConfirm.show(editDocumentScreen.get().getMainStage(), new ModalConfirmResponse() {
    			@Override
    			public void confirm() {
    				editDocumentScreen.get().finish();
    			}
    			@Override
    			public void cancel() {
    				editDocumentScreen.get().finish();
    			}
    		}, properties.getProperty("doc_modified"));

		} else {
			logger.warn("Not implemented yet, see AddDocumentScreenEventHandler.handle");
		}
		
	}

	
	/**
	 * Check if currents values are valid, if true, the save button is available
	 */
	public void checkValidity() {

		boolean isValid = true;

		if (!editDocumentScreen.get().getDocInfoEditor().getEventHandler().isValid()) {
			isValid = false;
		}

		if (editDocumentScreen.get().getDocumentPreviewer().getEventHandler().isFileListEmpty()) {
			isValid = false;
		}

		editDocumentScreen.get().getBtnSave().setDisable(!isValid);
	}

	/**
	 * @see com.ged.ui.listeners.DocumentInfoEditorListener#selectionChanged(boolean)
	 */
	@Override
	public void selectionChanged(boolean isValid) {
		checkValidity();
	}

	/**
	 * @see com.ged.ui.listeners.DocumentPreviewListener#fileCountChanged(int)
	 */
	@Override
	public void fileCountChanged(int newFileCount) {
		checkValidity();
	}

}
