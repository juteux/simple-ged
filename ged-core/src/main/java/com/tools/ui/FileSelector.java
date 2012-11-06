package com.tools.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import com.tools.listeners.FileChangedListener;


/**
 * A file selector, a text field for path and a browsing button
 */

public abstract class FileSelector extends AbstractWidget {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(FileSelector.class);

	/**
	 * To signal to listeners when selection changed
	 */
	protected EventListenerList listenerList;
	
	/**
	 * The popup title (when the user will click the browse button)
	 */
    private String windowsTitle;

    /**
     * Path to the default folder (when the user wants to change selected file)
     */
    private String dirPath; 

    /**
     * The widget which display current selection
     */
    private TextField selectionLineEdit; 

    /**
     * The browsing button
     */
    private Button btnBrowse;

	/**
	 * @param windowsTitle
	 * 				The popup title (when the user will click the browse button)
	 */
	public FileSelector(String windowsTitle) {
		super();
		this.windowsTitle = windowsTitle;

		listenerList = new EventListenerList();
		dirPath = ".";
		
		// Instantiate
		btnBrowse = new Button("...");
		selectionLineEdit = new TextField();
		HBox.setHgrow(selectionLineEdit, Priority.ALWAYS);
		
		// add actions
		btnBrowse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				openSelectionPopup();
			}
		});

		selectionLineEdit.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				validSelection();
			}
		});
		
		
		// layouting ...
		this.getChildren().addAll(selectionLineEdit, btnBrowse);
	}

	/**
	 * Add some listener
	 */
	public void addFileChangedListener(FileChangedListener l) {
        this.listenerList.add(FileChangedListener.class, l);
	} 
	
	/**
	 *  Remove some listener 
	 */
	public void removeFileChangedListener(FileChangedListener l) {
		listenerList.remove(FileChangedListener.class, l);
	} 
	
	/**
	 * Display the given file path in text field
	 * @param path
	 */
	public void setCurrentFilePath(String path) {
		selectionLineEdit.setText(path);
		validSelection();
	}
	
	/**
	 * Is the current file path valid ?
	 * @return
	 * 		True if the file path is valid, false otherwise
	 */
	public abstract boolean isValidPath();
	
	/**
	 * Open the file selector popup
	 */
	public abstract void openSelectionPopup();
	
	/**
	 * Update the UI, according to the value returned by isValidPath()
	 */
	protected void validSelection() {
		
		if (isValidPath()) {
			logger.debug("Valid selection");
			selectionLineEdit.getStyleClass().remove("error");
		}
		else {
			logger.debug("Invalid selection");
			selectionLineEdit.getStyleClass().add("error");
		}
		
		// send the good news to listeners
		FileChangedListener[] listeners = (FileChangedListener [])listenerList.getListeners(FileChangedListener.class);
		FileChangedEventObject e = new FileChangedEventObject(this);
		e.setValid(isValidPath());
		for (FileChangedListener fcl : listeners) {
			fcl.newFileSelected(e);
		}
	}
	
	
	/**
	 * Return the current file path, current selection
	 */
	public String getFilePath() {
		return selectionLineEdit.getText();
	}

	/**
	 * @param windowsTitle the windowsTitle to set
	 */
	public void setWindowsTitle(String windowsTitle) {
		this.windowsTitle = windowsTitle;
	}

	/**
	 * Set the default directory path (when browsing)
	 */
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

	/**
	 * Fix button browse label
	 */
	public void setBtnBrowseLabel(String label) {
		btnBrowse.setText(label);
	}

	/**
	 * @return the windowsTitle
	 */
	public String getWindowsTitle() {
		return windowsTitle;
	}

	/**
	 * Warning : This is the path where the dialog popup is opened, not the current selected path !
	 * 
	 * @return the dirPath. 
	 */
	public String getDirPath() {
		return dirPath;
	}
	
}
