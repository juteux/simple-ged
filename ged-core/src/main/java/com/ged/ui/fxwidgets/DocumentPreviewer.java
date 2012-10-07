package com.ged.ui.fxwidgets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.models.GedDocumentFile;
import com.ged.ui.fxpreviewwidgets.AbstractFilePreviewer;
import com.ged.ui.fxpreviewwidgets.FilePreviewerFactory;


/**
 * The document previewer is a previewer for document. Notice that a document can have multiple attached files
 * 
 * @author xavier
 *
 */
public class DocumentPreviewer extends VBox {

	/**
	 * Every previewers attached to the current document
	 */
	private List<AbstractFilePreviewer> previewers;

	/**
	 * Current document previewer
	 */
	private AbstractFilePreviewer currentPreviewer;
	
	/**
	 * Index of the current showed previewer
	 */
	private int currentPreviewerIndex;
	
	
	public DocumentPreviewer() {
		instantiateWidgets();
	}
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		previewers = new ArrayList<>();
		
		// TODO : add previous & next buttons
		
		setPadding(new Insets(5,5,5,5));
		setVgrow(this, Priority.ALWAYS);
	}
	
	
	/**
	 * We can find files to load with the given document
	 */
	public void setGedDocument(GedDocument document) {
		previewers.clear();
		for (GedDocumentFile file : document.getDocumentFiles()) {
			addFile(new File(Profile.getInstance().getLibraryRoot() + file.getRelativeFilePath()));
		}
		gotoIndex(0);
	}
	
	/**
	 * Sometimes a file haven't associated document, that's why we can preview him without ged document 
	 * 
	 * Notice that the given file will be the only one in preview list
	 * 
	 * @param file
	 */
	public void setFile(File file) {
		previewers.clear();
		addFile(file);
		gotoIndex(0);
	}
	
	/**
	 * Set a special previewer, which is a previewer for add new document (used on directories)
	 */
	public void setSpecialPreviewer(AbstractFilePreviewer p) {
		previewers.clear();
		previewers.add(p);
		gotoIndex(0);
	}
	
	/**
	 * Add a document to display list without forgetting previous elements
	 * 
	 * The insert file is showed
	 */
	public void addFile(final File file) {
		
		// always choose the best size for you !
		Dimension2D maximumPreviewerSize = new Dimension2D(this.getWidth() - 10, this.getHeight() - 10);
		
		previewers.add(FilePreviewerFactory.getFilePreviewer(file, maximumPreviewerSize));
		gotoIndex(previewers.size()-1);
	}
	
	/**
	 * Display the given viewer. If another viewer were here, he's freed
	 */
	private void refreshDocumentViewer() {
		if (currentPreviewer != null) {
			this.getChildren().remove(currentPreviewer);
		}
		
		if (previewers.size() != 0) {
			currentPreviewer = previewers.get(currentPreviewerIndex);
			this.getChildren().add(currentPreviewer);
		}
		
		//controller.fixButtonsVisibility();
	}
	
	/**
	 * Remove the current previewer
	 */
	public void removeCurrentPreviewer() {
		previewers.remove(currentPreviewer);
		if (currentPreviewerIndex < previewers.size()-1 && previewers.size() != 0) {
			gotoIndex(currentPreviewerIndex);
		}
		else {
			gotoIndex(0);
		}
	}
	
	/**
	 * Remove all items in previewer
	 */
	public void clearPreviews() {
		previewers.clear();
		gotoIndex(0);
	}
	
	
	/**
	 * Move to next previewer
	 */
	public void gotoNextPreviewer() {
		currentPreviewerIndex++;
		refreshDocumentViewer();
	}
	
	/**
	 * Move to previous previewer
	 */
	public void gotoPreviousPreviewer() {
		currentPreviewerIndex--;
		refreshDocumentViewer();
	}

	/**
	 * Move to the given index
	 */
	public void gotoIndex(int index) {
		currentPreviewerIndex = index;
		refreshDocumentViewer();
	}
	
	
	
	
}
