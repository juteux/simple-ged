package com.ged.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.models.GedDocumentFile;
import com.ged.services.GedDocumentService;
import com.ged.tools.FileHelper;
import com.ged.ui.screens.EditDocumentScreen;
import com.tools.PropertiesHelper;

/**
 * This is the EditDocumentScreen controller
 * 
 * @author xavier
 *
 */
public class EditDocumentScreenController implements ActionListener {

	private static final Logger logger = Logger.getLogger(EditDocumentScreenController.class);
	
	/**
	 * The controlled screen
	 */
	EditDocumentScreen editDocumentScreen;
	
	protected Properties properties;
	
	
	public EditDocumentScreenController(EditDocumentScreen editDocumentScreen) {
		this.editDocumentScreen = editDocumentScreen;
		this.properties = PropertiesHelper.getInstance().getProperties();
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == editDocumentScreen.getBtnReturn()) {
			int option = JOptionPane.showConfirmDialog(null, properties.getProperty("stop_edition"), properties.getProperty("stop_edition_title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (option == JOptionPane.YES_OPTION) {
				editDocumentScreen.finish();
			}
		} 
		else if (e.getSource() == editDocumentScreen.getBtnSave()) {
			
			GedDocument document = editDocumentScreen.getDocumentInfoEditor().getController().getDocument();
			document.setId(editDocumentScreen.getDocument().getId());
			
			List<GedDocumentFile> attachedFiles = FileHelper.copyFilesIfNecessary(
					editDocumentScreen.getDocumentPreview().getController().getFileList(), 
					new File(Profile.getInstance().getLibraryRoot() + editDocumentScreen.getLibraryView().getSelectedPath()), 
					editDocumentScreen.getDocumentInfoEditor().getInputtedName()
				);
			document.setDocumentFiles(attachedFiles);
			
			if (document.getId() == -1) {
				GedDocumentService.addDocument(document);
			}
			else {
				GedDocumentService.updateDocument(document);
			}
			
			JOptionPane.showMessageDialog(null, properties.getProperty("doc_modified"), properties.getProperty("information"), JOptionPane.INFORMATION_MESSAGE);
			editDocumentScreen.refreshScreens();
			editDocumentScreen.finish();
		}
		else {
			logger.warn("Not implemented yet, see EditDocumentScreenController.actionPerformed");
		}
	}

}
