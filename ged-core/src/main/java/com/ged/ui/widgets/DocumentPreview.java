package com.ged.ui.widgets;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.ged.Profile;
import com.ged.models.GedDocument;
import com.ged.models.GedDocumentFile;
import com.ged.ui.controllers.DocumentPreviewController;
import com.ged.ui.preview_widgets.AbstractDocumentPreviewer;
import com.ged.ui.preview_widgets.FilePreviewerFactory;
import com.tools.ui.AbstractWidget;

/**
 * This is a widget which provide a graphical preview of the document
 * 
 * In fact, this is a container which contains file previewers
 * 
 * @author xavier
 *
 */
public class DocumentPreview extends AbstractWidget {

	/**
	 * Buttons next & previous
	 */
	public class NavigationButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public NavigationButton(DocumentPreviewController controller) {
			super("");
	
			setIconSize(new Dimension(30, 30));
			setPreferredSize(new Dimension(40, 40));
			
			addActionListener(controller);
		}
	}
	
	/**
	 * All actions buttons in right bar
	 */
	public class DocumentPreviewButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public DocumentPreviewButton(String label, DocumentPreviewController controller) {
			super(label);
			
			setIconSize(new Dimension(30, 30));
			setPreferredSize(new Dimension(100, 40));
			
			addActionListener(controller);
		}
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Current document previewer
	 */
	private AbstractDocumentPreviewer currentPreviewer;
	
	/**
	 * The maximum size for previewer, initialized when the first file preview is required
	 */
	private Dimension maximumPreviewerSize;
	
	/**
	 * The previewer list
	 */
	private List<AbstractDocumentPreviewer> previewers;
	
	/**
	 * Index of the current showed previewer
	 */
	private int currentPreviewerIndex;
	
	/**
	 * Button next previewer
	 */
	private NavigationButton btnNext;
	
	/**
	 * Button previous previewer
	 */
	private NavigationButton btnPrevious;
	
	/**
	 * Label navigation info
	 */
	private JLabel navigationLabel;
	
	/**
	 * Main container
	 */
	private JPanel mainContainer;
	
	/**
	 * Open document
	 */
	private DocumentPreviewButton btnOpen;
	
	/**
	 * Print document
	 */
	private DocumentPreviewButton btnPrint;
	
	/**
	 * Unlink file button
	 */
	private DocumentPreviewButton btnUnlink;
	
	/**
	 * Button unlink is available ?
	 * 
	 * Default value : false
	 */
	private boolean btnUnlinkIsAvailable;
	
	/**
	 * My controller
	 */
	private DocumentPreviewController controller;
	
	
	public DocumentPreview() {
		super();
		btnUnlinkIsAvailable = false;
		instantiateWidgets();

		// navigation buttons layout
		MigLayout navigationLayout = new MigLayout(	
				"wrap",
				"[left]15[grow,center]15[right]",	// columns
				""	// rows
			);
		
		JPanel btnPanel = new JPanel(navigationLayout);
		btnPanel.add(btnPrevious);
		btnPanel.add(navigationLabel);
		btnPanel.add(btnNext);
		
		// actions buttons (left panel)
		MigLayout actionLayout = new MigLayout(	
				"wrap",
				"[grow,fill,center]",	// columns
				""	// rows
			);
		
		JPanel actionPanel = new JPanel(actionLayout);
		actionPanel.add(btnOpen);
		actionPanel.add(btnPrint);
		actionPanel.add(btnUnlink);
		
		// main layout
		MigLayout layout = new MigLayout(	
				"wrap",
				"[grow 90,fill,center][grow 10,fill,center]",	// columns
				"[grow 90,fill,center][grow 10,fill,center]"	// rows
			);
		
		mainContainer = new JPanel(layout);
		
		mainContainer.add(actionPanel, "cell 1 0 1 2");
		mainContainer.add(btnPanel, "cell 0 1");
		
		//add(mainContainer);
	}
	

	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		previewers = new ArrayList<AbstractDocumentPreviewer>();
		currentPreviewer = null;
		maximumPreviewerSize = null;
		
		navigationLabel = new JLabel();
		
		controller = new DocumentPreviewController(this);
		
		btnNext = new NavigationButton(controller);
		btnPrevious = new NavigationButton(controller);
		
		btnOpen = new DocumentPreviewButton(properties.getProperty("open"), controller);
		btnUnlink = new DocumentPreviewButton(properties.getProperty("unlink"), controller);
		btnPrint = new DocumentPreviewButton(properties.getProperty("print"), controller);
		
		// define associated pictures
		Map<SimpleButton, String> associatedImages = new HashMap<SimpleButton, String>();
		associatedImages.put(btnNext,		properties.getProperty("ico_next"));
		associatedImages.put(btnPrevious,	properties.getProperty("ico_previous"));
		associatedImages.put(btnOpen,		properties.getProperty("ico_open"));
		associatedImages.put(btnUnlink,		properties.getProperty("ico_remove"));

		// set pictures
		for (Map.Entry<SimpleButton, String> e : associatedImages.entrySet()) {
			e.getKey().setIcon(e.getValue());
		}
		
		controller.fixButtonsVisibility();
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
	 * Add a document to display list without forgetting previous elements
	 * 
	 * The insert file is showed
	 */
	public void addFile(final File file) {
		if (maximumPreviewerSize == null) {
			//maximumPreviewerSize = getSize();
			maximumPreviewerSize.setSize(maximumPreviewerSize.width, maximumPreviewerSize.height-50);
		}
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {				
				previewers.add(FilePreviewerFactory.getFilePreviewer(file, maximumPreviewerSize));
				gotoIndex(previewers.size()-1);
			}
		});
		t.start();
	}
	
	
	/**
	 * Display the given viewer. If another viewer were here, he's freed
	 */
	private void refreshDocumentViewer() {
		if (currentPreviewer != null) {
			mainContainer.remove(currentPreviewer);
		}
		
		if (previewers.size() != 0) {
			currentPreviewer = previewers.get(currentPreviewerIndex);
			mainContainer.add(currentPreviewer, "cell 0 0");
		}
		
		controller.fixButtonsVisibility();
		
		mainContainer.repaint();
		mainContainer.validate();
		//repaint();
		//validate();
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


	public NavigationButton getBtnNext() {
		return btnNext;
	}


	public NavigationButton getBtnPrevious() {
		return btnPrevious;
	}

	public int getCurrentPreviewerIndex() {
		return currentPreviewerIndex;
	}

	public JLabel getNavigationLabel() {
		return navigationLabel;
	}

	public DocumentPreviewButton getBtnOpen() {
		return btnOpen;
	}
	
	public DocumentPreviewButton getBtnUnlink() {
		return btnUnlink;
	}
	
	/**
	 * Get the absolute file path of the current viewer
	 */
	public String getCurrentPreviewerFilePath() {
		return currentPreviewer.getAbsoluteFilePath();
	}

	/**
	 * Get the current previewer
	 */
	public AbstractDocumentPreviewer getCurrentPreviewer() {
		return currentPreviewer;
	}
	
	public boolean isBtnUnlinkIsAvailable() {
		return btnUnlinkIsAvailable;
	}

	public void setBtnUnlinkAvailable(boolean btnUnlinkIsAvailable) {
		this.btnUnlinkIsAvailable = btnUnlinkIsAvailable;
		controller.fixButtonsVisibility();
	}


	public DocumentPreviewController getController() {
		return controller;
	}


	public List<AbstractDocumentPreviewer> getPreviewers() {
		return previewers;
	}

	public DocumentPreviewButton getBtnPrint() {
		return btnPrint;
	}
	
}
