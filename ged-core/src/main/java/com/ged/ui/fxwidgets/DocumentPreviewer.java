package com.ged.ui.fxwidgets;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.models.GedDocumentFile;
import com.ged.ui.fxpreviewwidgets.AbstractFilePreviewer;
import com.ged.ui.fxpreviewwidgets.FilePreviewerFactory;
import com.ged.ui.fxwidgetcontrollers.FxDocumentPreviewerController;
import com.tools.PropertiesHelper;


/**
 * The document previewer is a previewer for document. Notice that a document can have multiple attached files
 * 
 * @author xavier
 *
 */
public class DocumentPreviewer extends HBox {

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
	
	/**
	 * Next file button
	 */
	private Button next;
	
	/**
	 * Previous file button
	 */
	private Button back;
	
	/**
	 * The right box contains actions buttons
	 */
	private VBox rightBox;
	
	/**
	 * The current previewer container
	 */
	private VBox leftBox;
	
	/**
	 * My controller
	 */
	private WeakReference<FxDocumentPreviewerController> controller;
	
	/**
	 * Button for opening document
	 */
	private Button btnOpenFile;
	
	/**
	 * Button for printing document
	 */
	private Button btnPrintFile;
	
	/**
	 * Properties
	 */
	Properties properties = PropertiesHelper.getInstance().getProperties();
	
	
	public DocumentPreviewer() {
		instantiateWidgets();
		
		//	+-----------+-------------------+
		//	|			|					|
		//	|			| Top right Box		|
		//	|			|					|
		//	|			|					|
		//	| left Box	+------right-box----+
		//	|			|					|
		//	|			| Bottom right Box	|
		//	|			|					|
		//	+-----------+-------------------+
		
		leftBox = new VBox();
		//leftBox.setStyle("-fx-background-color:orange");
		HBox.setHgrow(leftBox, Priority.ALWAYS);
		VBox.setVgrow(leftBox, Priority.ALWAYS);

		
		HBox topRightBox = new HBox(5);
		topRightBox.getChildren().addAll(back, next);
		VBox.setVgrow(topRightBox, Priority.ALWAYS);
		
		VBox bottomRightBox = new VBox(10);
		bottomRightBox.getChildren().addAll(btnOpenFile, btnPrintFile);
		VBox.setVgrow(bottomRightBox, Priority.NEVER);
		
		rightBox = new VBox();
		rightBox.getChildren().addAll(topRightBox, bottomRightBox);
		//rightBox.setStyle("-fx-background-color:green");
		
		
		setPadding(new Insets(5,5,5,5));
		
		HBox.setHgrow(this, Priority.ALWAYS);
		VBox.setVgrow(this, Priority.ALWAYS);
		
		HBox.setHgrow(rightBox, Priority.NEVER);
		
		this.getChildren().addAll(leftBox, rightBox);
	}
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		controller = new WeakReference<>(new FxDocumentPreviewerController(this));
		
		previewers = new ArrayList<>();
		
		back = new Button("<");
		back.setOnAction(controller.get());
		back.setPrefSize(50, 50);

		
		next = new Button(">");
		next.setOnAction(controller.get());
		next.setPrefSize(50, 50);
		
		
		btnOpenFile = new Button(properties.getProperty("open"));
		btnOpenFile.setOnAction(controller.get());
		btnOpenFile.setPrefSize(160, 50);

		Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_open_file")));
		ImageView iv = new ImageView(i);
		iv.setSmooth(true);
		iv.setFitWidth(32);
		iv.setFitHeight(32);
		btnOpenFile.setGraphic(iv);
		
		
		btnPrintFile = new Button(properties.getProperty("print"));
		btnPrintFile.setOnAction(controller.get());
		btnPrintFile.setPrefSize(160, 50);
		
		Image i2 = new Image(getClass().getResourceAsStream(properties.getProperty("ico_print")));
		ImageView iv2 = new ImageView(i2);
		iv2.setSmooth(true);
		iv2.setFitWidth(32);
		iv2.setFitHeight(32);
		btnPrintFile.setGraphic(iv2);
		
		
		controller.get().fixButtonsVisibility();
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
		Dimension2D maximumPreviewerSize = new Dimension2D(leftBox.getWidth() - 10, leftBox.getHeight() - 10);
		
		previewers.add(FilePreviewerFactory.getFilePreviewer(file, maximumPreviewerSize));
		gotoIndex(previewers.size()-1);
	}
	
	/**
	 * Display the given viewer. If another viewer were here, he's freed
	 */
	private void refreshDocumentViewer() {
		if (currentPreviewer != null) {
			leftBox.getChildren().clear();//remove(currentPreviewer);
		}
		
		if (previewers.size() != 0) {
			currentPreviewer = previewers.get(currentPreviewerIndex);
			leftBox.getChildren().add(currentPreviewer);
		}
		
		controller.get().fixButtonsVisibility();
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
	
	public int getPreviewersCount() {
		return previewers.size();
	}

	public List<AbstractFilePreviewer> getPreviewers() {
		return previewers;
	}

	public AbstractFilePreviewer getCurrentPreviewer() {
		return currentPreviewer;
	}

	public int getCurrentPreviewerIndex() {
		return currentPreviewerIndex;
	}

	public Button getNext() {
		return next;
	}

	public Button getBack() {
		return back;
	}

	/**
	 * Ask to close all opened files in previewers
	 */
	public void releaseOpenedFiles() {
		for (AbstractFilePreviewer p : previewers) {
			p.closeFile();
		}
	}

	public Button getBtnOpenFile() {
		return btnOpenFile;
	}

	public Button getBtnPrintFile() {
		return btnPrintFile;
	}
	
	
	/**
	 * Get the absolute file path of the current viewer
	 */
	public String getCurrentPreviewerFilePath() {
		return currentPreviewer.getAbsoluteFilePath();
	}
	
}
