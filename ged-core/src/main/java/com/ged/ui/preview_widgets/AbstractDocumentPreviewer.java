package com.ged.ui.preview_widgets;

import javax.swing.JPanel;

import org.apache.log4j.Logger;


import net.miginfocom.swing.MigLayout;

/**
 * All document's previewers inherits from this class
 * @author xavier
 *
 */
public abstract class AbstractDocumentPreviewer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(AbstractDocumentPreviewer.class);
	
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
	public AbstractDocumentPreviewer(String absoluteFilePath) {
		super(
				new MigLayout(	
						"wrap",
						"[grow,fill,center]",
						"[grow,fill,center]"
					)
		);
		this.absoluteFilePath = absoluteFilePath;
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
	 * Get the absolute file path
	 */
	public String getAbsoluteFilePath() {
		return absoluteFilePath;
	}
}
