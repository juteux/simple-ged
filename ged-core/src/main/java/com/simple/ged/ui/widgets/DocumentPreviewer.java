package com.simple.ged.ui.widgets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.simple.ged.Profile;
import com.simple.ged.models.GedDocument;
import com.simple.ged.models.GedDocumentFile;
import com.simple.ged.ui.previewwidgets.AbstractFilePreviewer;
import com.simple.ged.ui.previewwidgets.FilePreviewerFactory;
import com.simple.ged.ui.widgets.eventhandler.DocumentPreviewerEventHandler;

import fr.xmichel.toolbox.tools.PropertiesHelper;


/**
 * The document previewer is a previewer for document. Notice that a document can have multiple attached files
 * 
 * @author xavier
 *
 */
public class DocumentPreviewer extends HBox {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(DocumentPreviewer.class);
	
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
	private DocumentPreviewerEventHandler eventHandler;
	
	/**
	 * Button for opening document
	 */
	private Button btnOpenFile;
	
	/**
	 * Button for printing document
	 */
	private Button btnPrintFile;
	
	/**
	 * Button for unlinking files
	 */
	private Button btnUnlinkFile;
	
	/**
	 * Edition mode is on ? Default false
	 * Edition mode enable you to unlink document files for example
	 */
	private boolean editionMode;
	
	
	/**
	 * Properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	
	public DocumentPreviewer() {
		
		editionMode = false;
		
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
		leftBox.setMinSize(100, 100);
		//leftBox.setStyle("-fx-background-color:orange");
		HBox.setHgrow(leftBox, Priority.ALWAYS);
		VBox.setVgrow(leftBox, Priority.ALWAYS);

		
		VBox topRightBox = new VBox(10);
		
		HBox topRightBoxTop = new HBox(5);
		topRightBoxTop.getChildren().addAll(back, next);
		
		topRightBox.getChildren().addAll(topRightBoxTop, btnUnlinkFile);
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
		
		eventHandler = new DocumentPreviewerEventHandler(this);
		
		previewers = new ArrayList<>();
		
		back = new Button("<");
		back.setOnAction(eventHandler);
		back.setPrefSize(50, 50);

		
		next = new Button(">");
		next.setOnAction(eventHandler);
		next.setPrefSize(50, 50);
		
		
		btnOpenFile = new Button(properties.getProperty("open"));
		btnOpenFile.setOnAction(eventHandler);
		btnOpenFile.setPrefSize(160, 50);

		Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_open_file")));
		ImageView iv = new ImageView(i);
		iv.setSmooth(true);
		iv.setFitWidth(32);
		iv.setFitHeight(32);
		btnOpenFile.setGraphic(iv);
		
		
		btnPrintFile = new Button(properties.getProperty("print"));
		btnPrintFile.setOnAction(eventHandler);
		btnPrintFile.setPrefSize(160, 50);
		
		Image i2 = new Image(getClass().getResourceAsStream(properties.getProperty("ico_print")));
		ImageView iv2 = new ImageView(i2);
		iv2.setSmooth(true);
		iv2.setFitWidth(32);
		iv2.setFitHeight(32);
		btnPrintFile.setGraphic(iv2);
		
		
		btnUnlinkFile = new Button(properties.getProperty("unlink"));
		btnUnlinkFile.setOnAction(eventHandler);
		btnUnlinkFile.setPrefSize(160, 50);
		
		Image i3 = new Image(getClass().getResourceAsStream(properties.getProperty("ico_remove")));
		ImageView iv3 = new ImageView(i3);
		iv3.setSmooth(true);
		iv3.setFitWidth(32);
		iv3.setFitHeight(32);
		btnUnlinkFile.setGraphic(iv3);
		
		
		eventHandler.fixButtonsVisibility();
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
		logger.debug("Max size : " + maximumPreviewerSize.getWidth() + " x " + maximumPreviewerSize.getHeight());
		
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
		
		eventHandler.fixButtonsVisibility();
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

	// Get back Jojo =)
	public Button getBack() {
		return back;
	}

	public Button getBtnUnlinkFile() {
		return btnUnlinkFile;
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

	public DocumentPreviewerEventHandler getEventHandler() {
		return eventHandler;
	}

	public boolean isEditionMode() {
		return editionMode;
	}

	public void setEditionMode(boolean editionMode) {
		this.editionMode = editionMode;
		eventHandler.fixButtonsVisibility();
	}
	
	
}
