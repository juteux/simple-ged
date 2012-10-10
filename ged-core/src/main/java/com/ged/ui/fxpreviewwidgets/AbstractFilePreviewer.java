package com.ged.ui.fxpreviewwidgets;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import org.apache.log4j.Logger;



/**
 * All file's previewers inherits from this class
 * 
 * @author xavier
 *
 */
public abstract class AbstractFilePreviewer extends HBox {

	private static final Logger logger = Logger.getLogger(AbstractFilePreviewer.class);
	
	/**
	 * The ABSOLUTE file path
	 */
	protected String absoluteFilePath;

	
	/**
	 * Don't forget to give the ABSOLUTE file path !
	 * 
	 * @param absoluteFilePath
	 * 				One more time, the ABSOLUTE file path
	 */
	public AbstractFilePreviewer(String absoluteFilePath) {
		this.absoluteFilePath = absoluteFilePath;
		setAlignment(Pos.CENTER);
	}
	
	/**
	 * Try to load the current file
	 * 
	 * @throws CannotCreatePreviewerException
	 */
	public abstract void load() throws CannotCreatePreviewerException;
	
	/**
	 * Is file printable ?
	 */
	public abstract boolean isPrintable();
	
	/**
	 * Print the document, override this method if it is possible
	 */
	public void print() {
		logger.warn("Generic method of print, should be re-implemented");
	}
	
	/**
	 * Is the file openable ?
	 * 
	 * Should always be true, exepted for special previewer like "AddDocumentPreviewer"
	 */
	public boolean isOpenable() {
		return true;
	}
	
	/**
	 * Get the absolute file path
	 */
	public String getAbsoluteFilePath() {
		return absoluteFilePath;
	}

	/**
	 * Close some file resource, if opened.
	 * 
	 * Override this method if you need to release your resource !
	 */
	public void closeFile() {
	}
}
