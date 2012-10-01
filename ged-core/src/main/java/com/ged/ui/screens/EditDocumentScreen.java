package com.ged.ui.screens;

import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.models.GedDocumentFile;
import com.ged.services.GedDocumentService;
import com.ged.ui.MainWindow;
import com.ged.ui.controllers.EditDocumentScreenController;
import com.ged.ui.widgets.DocumentInfoEditor;
import com.ged.ui.widgets.SimpleButton;
import com.tools.DateHelper;

/**
 * This screen is for document edition
 * 
 * @author xavier
 *
 */
public class EditDocumentScreen extends AddDocumentScreen {

	
	/**
	 * This screen buttons model
	 */
	public class EditDocumentScreenButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public EditDocumentScreenButton(String label, EditDocumentScreenController controller) {
			super(label);
			
			setIconSize(new Dimension(30, 30));
			setPreferredSize(new Dimension(200, 50));
			
			addActionListener(controller);
		}
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The current manipulated document
	 */
	private GedDocument document;
	
	/**
	 * A button for saving
	 */
	private SimpleButton btnSave;
	
	/**
	 * A button to return to main screen
	 */
	private SimpleButton btnReturn;
	
	
	
	public EditDocumentScreen(MainWindow mw) {
		
		super(mw);	
		instantiateWidgets();
		
		btnActionPanel.removeAll();
		btnActionPanel.add(btnReturn);
		btnActionPanel.add(btnSave);
	}

	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		EditDocumentScreenController controller = new EditDocumentScreenController(this);
		
		btnSave = new EditDocumentScreenButton(properties.getProperty("save"), controller);
		btnSave.setEnabled(false);
		btnReturn = new EditDocumentScreenButton(properties.getProperty("back"), controller);
		
		// define associated pictures
		Map<SimpleButton, String> associatedImages = new HashMap<SimpleButton, String>();
		associatedImages.put(btnSave,					properties.getProperty("ico_save"));
		associatedImages.put(btnReturn, 				properties.getProperty("ico_revert"));
		
		// set pictures
		for (Map.Entry<SimpleButton, String> e : associatedImages.entrySet()) {
			e.getKey().setIcon(e.getValue());
		}
	}

	@Override
	public void receiveExtraValue(Map<String, Object> extra) {
		document = GedDocumentService.findDocumentbyFilePath((String) extra.get("document_file_path"));
		if (document == null) {
			document = new GedDocument();
			getDocumentPreview().addFile(new File(Profile.getInstance().getLibraryRoot() + (String) extra.get("document_file_path")));
		}
		
		getLibraryView().getTree().setSelectionPath(new TreePath((DefaultMutableTreeNode) extra.get("parent_node")));
		
		DocumentInfoEditor editor = getDocumentInfoEditor();
		editor.getInputName().setText(document.getName());
		
		editor.getInputDate().setText(DateHelper.calendarToString(document.getDate()));
		editor.getInputDescription().setText(document.getDescription());
		
		editor.getLocationSelector().setIndexFromLocation(document.getLocation());
		
		for (GedDocumentFile f : document.getDocumentFiles()) {
			getDocumentPreview().addFile(new File(Profile.getInstance().getLibraryRoot() + f.getRelativeFilePath()));
		}
	}

	@Override
	public SimpleButton getBtnSave() {
		return btnSave;
	}

	@Override
	public SimpleButton getBtnReturn() {
		return btnReturn;
	}

	public GedDocument getDocument() {
		return document;
	}
	
	@Override
	public void refresh() {
		getDocumentInfoEditor().reloadComboLocation();
	}
}

