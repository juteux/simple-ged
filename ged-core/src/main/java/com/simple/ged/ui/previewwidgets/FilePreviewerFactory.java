package com.simple.ged.ui.previewwidgets;

import java.io.File;

import org.apache.log4j.Logger;

import javafx.geometry.Dimension2D;
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
	public static AbstractFilePreviewer getFilePreviewer(File documentFile, Dimension2D maxSize) {
		
		AbstractFilePreviewer previewer = null;
		
	    // choose the correct previewer
	    switch (FileHelper.getFileType(documentFile.getAbsolutePath()))
	    {
	    case TEXT_TYPE :
	    	previewer = new TextFilePreviewer(documentFile.getAbsolutePath(), maxSize);
	        break;
	    case PNG_TYPE :
	    case JPG_TYPE :
	    case BMP_TYPE :
	    case GIF_TYPE :
	    	previewer = new ImageFilePreviewer(documentFile.getAbsolutePath(), maxSize);
	        break;
	    case HTML_TYPE :
	    	previewer = new HtmlFilePreviewer(documentFile.getAbsolutePath(), maxSize);
	    	break;
	    case PDF_TYPE :
	    	previewer = new PdfFilePreviewer(documentFile.getAbsolutePath(), maxSize);
	    	break;
	    default :
	    	previewer = new DefaultFilePreviewer(documentFile.getAbsolutePath(), maxSize);
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
		previewer = new DefaultFilePreviewer(documentFile.getAbsolutePath(), maxSize);
		try {
			previewer.load();
		} catch (CannotCreatePreviewerException e) { // should never happen
		}
		
		return previewer;
	}

}
