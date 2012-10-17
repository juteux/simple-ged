package com.ged.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.asprise.util.jtwain.Source;
import com.asprise.util.jtwain.SourceManager;
import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.models.GedDocumentFile;
import com.ged.services.GedDocumentService;
import com.ged.tools.FileHelper;
import com.ged.ui.listeners.DocumentInfoEditorListener;
import com.ged.ui.listeners.DocumentPreviewListener;
import com.ged.ui.listeners.LibraryListener;
import com.ged.ui.screens.AddDocumentScreen;
import com.ged.ui.screens.SoftwareScreen;
import com.tools.DateTokenGetter;
import com.tools.OSHelper;
import com.tools.PropertiesHelper;

/**
 * The controller for AddDocumentScreen
 * 
 * @author xavier
 *
 */
public class AddDocumentScreenController implements ActionListener,
		LibraryListener, DocumentInfoEditorListener, DocumentPreviewListener {

	private static final Logger logger = Logger.getLogger(AddDocumentScreenController.class);
	
	/**
	 * The controlled window
	 */
	private AddDocumentScreen addDocumentScreen;

	protected Properties properties;
	
	
	public AddDocumentScreenController(AddDocumentScreen addDocumentScreen) {
		this.addDocumentScreen = addDocumentScreen;
		this.properties = PropertiesHelper.getInstance().getProperties();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (arg0.getSource() == addDocumentScreen.getBtnSave()) {

			GedDocument document = addDocumentScreen.getDocumentInfoEditor().getController().getDocument();
			List<GedDocumentFile> attachedFiles = FileHelper.copyFilesIfNecessary(
					addDocumentScreen.getDocumentPreview().getController().getFileList(), 
					new File(Profile.getInstance().getLibraryRoot() + addDocumentScreen.getLibraryView().getSelectedPath()), 
					addDocumentScreen.getDocumentInfoEditor().getInputtedName()
				);
			document.setDocumentFiles(attachedFiles);
			
			GedDocumentService.addOrUpdateDocument(document);
			
			JOptionPane.showMessageDialog(null, properties.getProperty("doc_added"), properties.getProperty("information"), JOptionPane.INFORMATION_MESSAGE);
			
			addDocumentScreen.getDocumentInfoEditor().clearFields();
			addDocumentScreen.getDocumentPreview().clearPreviews();
			
		} 
		
		else if (arg0.getSource() == addDocumentScreen.getBtnReturn()) {
			addDocumentScreen.gotoScreen(SoftwareScreen.Screen.WELCOME_SCEEN);
		} 
		
		else if (arg0.getSource() == addDocumentScreen
				.getBtnAddFileFromHardDrive()) {

			// add a file from hard drive
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle(properties.getProperty("select_some_file_to_add"));

			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				addDocumentScreen.getDocumentPreview().addFile(new File(chooser.getSelectedFile().toString()));
				addDocumentScreen.getDocumentPreview().validate();
			}
		} 
		
		else if (arg0.getSource() == addDocumentScreen.getBtnScan()) {
			
			if ( OSHelper.isWindowsOs() ) {
			
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
	
						try { 
							Source source = SourceManager.instance().getDefaultSource(); 
							source.open(); 
							source.acquireImage(); 
							
							File f = File.createTempFile(DateTokenGetter.getToken(), ".jpg");
							source.saveLastAcquiredImageIntoFile(f);
							addDocumentScreen.getDocumentPreview().addFile(f);
							addDocumentScreen.getDocumentPreview().validate();
							
						} catch(Exception e) { 
								e.printStackTrace(); 
						} finally { 
								SourceManager.closeSourceManager(); 
						}
						
					}
					
				});
				
				t.start();
				
			}
			else { // maybe an unix
				/*
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
				
							JSaneDialog dialog = new JSaneDialog(JSaneDialog.CP_START_SANED_LOCALHOST, addDocumentScreen.getMainFrame(), properties.getProperty("scan"), true, null);
							Image i = dialog.openDialog();
			
							if (i == null) {
								return;
							}
							
					        try {
					        	File f = File.createTempFile(DateTokenGetter.getToken(), ".jpg");
					        	ImageIO.write(toBufferedImage(i), "jpeg", f);
					        	
								addDocumentScreen.getDocumentPreview().addFile(f);
								addDocumentScreen.getDocumentPreview().validate();
								
					        } catch (IOException ex) {
					            ex.printStackTrace();
					        }
						}
				});
				
				t.start();
				*/
			}
			
			
		}
		
		else {
			logger.warn("Not implemented yet, see AddDocumentScreenController.actionPerformed");
		}
	}
	
	/**
	 * Convert an image to a buffered image
	 * @see http://www.java-forums.org/new-java/2790-how-save-image-jpg.html
	 */
	/*
	private static BufferedImage toBufferedImage(Image src) {
		int w = src.getWidth(null);
		int h = src.getHeight(null);
		int type = BufferedImage.TYPE_INT_RGB;
		BufferedImage dest = new BufferedImage(w, h, type);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(src, 0, 0, null);
		g2.dispose();
		return dest;
	}
	*/

	/**
	 * Check if currents values are valid, if true, the save button is available
	 */
	public void checkValidity() {
		addDocumentScreen.getBtnSave().setEnabled(
				addDocumentScreen.getDocumentInfoEditor().getController().isValid()
			&&  addDocumentScreen.getLibraryView().getController().isElementSelected()
			&& !addDocumentScreen.getDocumentPreview().getController().isEmpty()
		);
	}

	
	@Override
	public void selectionChanged(String relativeFilePathOfNewSelection) {
		checkValidity();
	}

	@Override
	public void selectionChanged(boolean isValid) {
		checkValidity();
	}

	@Override
	public void fileCountChanged(int newFileCount) {
		checkValidity();
	}

	@Override
	public void releaseOpenedFiles() {
		//  Auto-generated method stub
		
	}

	@Override
	public void selectionChanged(LIBRARY_FILE_TYPE newSelectionType) {
		//  Auto-generated method stub
		
	}

}
