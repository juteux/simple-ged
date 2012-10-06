package com.ged.ui.fxpreviewwidgets;

import java.awt.Dimension;
import java.io.File;

import org.apache.log4j.Logger;

import com.tools.FileHelper;



/**
 * Factory for document preview
 * 
 * @author xavier
 * 
 */
public class FilePreviewerFactory {
	
	private static final Logger logger = Logger.getLogger(FilePreviewerFactory.class);
	
	/**
	 * Supported file types
	 * 
	 * @author xavier
	 * 
	 */
	public enum FileType {
		OTHER_TYPE, // Unknown file type
		IMAGE_TYPE, // An image
		TEXT_TYPE, // A brut text file
		PDF_TYPE, // A pdf file
		HTML_TYPE	// An html file
	}

	/**
	 * Return the previewer which is matching with file type
	 * 
	 * @param documentFile
	 *            Full path to document
	 *            
	 * @param maximumSize
	 * 			  The maximum size of the preview. For example, an image will be scaled if it's too large
	 * 
	 * @return The appropriate viewer
	 */
	public static AbstractFilePreviewer getFilePreviewer(File documentFile) {
		
		AbstractFilePreviewer previewer = null;
	    
	    // choose the correct previewer
	    switch (FileHelper.getFileType(documentFile.getAbsolutePath()))
	    {
	    //case TEXT_TYPE :
	    //	previewer = new TextFilePreviewer(documentFile.getAbsolutePath());
	    //    break;
	    //case IMAGE_TYPE :
	    //	previewer = new ImageFilePreviewer(documentFile.getAbsolutePath(), maximumSize);
	    //    break;
	    //case HTML_TYPE :
	    //	previewer = new HtmlFilePreviewer(documentFile.getAbsolutePath());
	    //	break;
	    //case PDF_TYPE :
	    //	previewer = new PdfFilePreviewer(documentFile.getAbsolutePath(), maximumSize);
	    //	break;
	    default :
	    	previewer = new DefaultFilePreviewer(documentFile.getAbsolutePath());
	        break;
	    }

	    // loading works ?
	    try {
	    	previewer.load();    	
	    	return previewer;
	    }
	    catch (CannotCreatePreviewerException e) {
	    	logger.error("Cannot load file preview : " + documentFile.getAbsolutePath());
	    }
	    
	    // return a default previewer
		previewer = new DefaultFilePreviewer(documentFile.getAbsolutePath());
		try {
			previewer.load();
		} catch (CannotCreatePreviewerException e) { // should never happen
		}
		
		return previewer;
	}

}
