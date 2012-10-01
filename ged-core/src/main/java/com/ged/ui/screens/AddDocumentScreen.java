package com.ged.ui.screens;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.ged.ui.MainWindow;
import com.ged.ui.controllers.AddDocumentScreenController;
import com.ged.ui.widgets.DocumentInfoEditor;
import com.ged.ui.widgets.DocumentPreview;
import com.ged.ui.widgets.LibraryView;
import com.ged.ui.widgets.SimpleButton;


/**
 * The screen which allow you to add document in library
 * 
 * @author xavier
 *
 */
public class AddDocumentScreen extends SoftwareScreen {

	/**
	 * This screen buttons model
	 */
	public class AddDocumentScreenButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public AddDocumentScreenButton(String label, AddDocumentScreenController controller) {
			super(label);
			
			setIconSize(new Dimension(30, 30));
			setPreferredSize(new Dimension(200, 60));
			
			addActionListener(controller);
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The view of the library (the tree)
	 */
	private LibraryView libraryView;
	
	/**
	 * A button for adding a file from hard drive
	 */
	private SimpleButton btnAddFileFromHardDrive;
	
	/**
	 * A button for adding a file from scanner
	 */
	private AddDocumentScreenButton btnScan;
	
	/**
	 * A button for saving
	 */
	private SimpleButton btnSave;
	
	/**
	 * A button to return to main screen
	 */
	private SimpleButton btnReturn;
	
	/**
	 * The document files preview
	 */
	private DocumentPreview documentPreview; 
	
	/**
	 * Form to enter document informations
	 */
	private DocumentInfoEditor documentInfoEditor;
	
	/**
	 * The button panel (contains button submit and back)
	 * 
	 * The child widget can rewrite the content, that's why this attribute exists
	 */
	protected JPanel btnActionPanel;
	
	
	public AddDocumentScreen(MainWindow mw) {
		
		super(mw);
		instantiateWidgets();
		
		// top right buttons panel
		JPanel btnAddPanel = new JPanel(new MigLayout("wrap", "[grow, center]"));
		btnAddPanel.add(btnAddFileFromHardDrive);
		
		if (btnScan != null ) {
			btnAddPanel.add(btnScan);
		}
		
		// button area
		btnActionPanel = new JPanel();
		btnActionPanel.add(btnReturn);
		btnActionPanel.add(btnSave);
		
		// main layout
		MigLayout layout = new MigLayout(	
				"wrap",
				"[grow 15,fill,center][grow 85,fill,center]",						// columns
				"[grow 75,fill,center][grow 10,fill,center][grow 5,fill,center][grow 5,fill,center]"	// rows
			);
		
		JPanel container = new JPanel(layout);

		// add widget in main layout
		container.add(btnAddPanel);
		container.add(documentPreview, "span 1 2");
		container.add(libraryView, "span 1 3");
		container.add(documentInfoEditor);
		container.add(btnActionPanel);
		
		// main layout
		add(container);
	}

	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		AddDocumentScreenController controller = new AddDocumentScreenController(this);
		
		libraryView = new LibraryView(true, this);
		libraryView.getController().addLibraryListener(controller);	
		
		documentPreview = new DocumentPreview();
		documentPreview.setBtnUnlinkAvailable(true);
		documentPreview.getController().addDocumentPreviewListener(controller);
		
		documentInfoEditor = new DocumentInfoEditor(this);
		documentInfoEditor.getController().addDocumentInfoEditorListener(controller);
		
		btnAddFileFromHardDrive = new AddDocumentScreenButton(properties.getProperty("add_from_hard_drive"), controller);
		btnScan 				= new AddDocumentScreenButton(properties.getProperty("add_scan"), controller);

		btnSave = new AddDocumentScreenButton(properties.getProperty("save"), controller);
		btnSave.setEnabled(false);
		btnReturn  	= new AddDocumentScreenButton(properties.getProperty("back"), controller);
				
		// define associated pictures
		Map<SimpleButton, String> associatedImages = new HashMap<SimpleButton, String>();
		associatedImages.put(btnAddFileFromHardDrive, 	properties.getProperty("ico_add"));
		associatedImages.put(btnSave,					properties.getProperty("ico_save"));
		associatedImages.put(btnReturn, 				properties.getProperty("ico_back"));
		associatedImages.put(btnScan,					properties.getProperty("ico_scan"));
		
		// set pictures
		for (Map.Entry<SimpleButton, String> e : associatedImages.entrySet()) {
			if (e.getKey() != null) {
				e.getKey().setIcon(e.getValue());
			}
		}
	}

	
	
	public LibraryView getLibraryView() {
		return libraryView;
	}

	public SimpleButton getBtnAddFileFromHardDrive() {
		return btnAddFileFromHardDrive;
	}

	public SimpleButton getBtnSave() {
		return btnSave;
	}

	public DocumentPreview getDocumentPreview() {
		return documentPreview;
	}

	public DocumentInfoEditor getDocumentInfoEditor() {
		return documentInfoEditor;
	}
	
	public SimpleButton getBtnReturn() {
		return btnReturn;
	}

	public AddDocumentScreenButton getBtnScan() {
		return btnScan;
	}
	
	@Override
	public void refresh() {
		getDocumentInfoEditor().reloadComboLocation();
	}
	
}
