package com.ged.ui.preview_widgets;

import java.awt.Dimension;
import java.io.File;

import org.apache.log4j.Logger;



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
	public static AbstractDocumentPreviewer getFilePreviewer(File documentFile, Dimension maximumSize) {
		
	    AbstractDocumentPreviewer previewer = null;
	    
	    // choose the correct previewer
	    switch (getFileType(documentFile.getAbsolutePath()))
	    {
	    case TEXT_TYPE :
	    	previewer = new TextFilePreviewer(documentFile.getAbsolutePath());
	        break;
	    case IMAGE_TYPE :
	    	previewer = new ImageFilePreviewer(documentFile.getAbsolutePath(), maximumSize);
	        break;
	    case HTML_TYPE :
	    	previewer = new HtmlFilePreviewer(documentFile.getAbsolutePath());
	    	break;
	    case PDF_TYPE :
	    	previewer = new PdfFilePreviewer(documentFile.getAbsolutePath(), maximumSize);
	    	break;
	    default :
	    	previewer = new DefaultDocumentPreviewer(documentFile.getAbsolutePath());
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
		previewer = new DefaultDocumentPreviewer(documentFile.getAbsolutePath());
		try {
			previewer.load();
		} catch (CannotCreatePreviewerException e) { // should never happen
		}
		
		return previewer;
	}

	/**
	 * Deduce the file type from the given file
	 * 
	 * @param fileName
	 *            The file which you wan't to know type. 
	 *            
	 * @return The associated file type
	 */
	public static FileType getFileType(String fileName) {

		int mid = fileName.lastIndexOf(".");
		String extension = fileName.substring(mid + 1, fileName.length()).toUpperCase();

		if (extension.equals("PDF")) {
			return FileType.PDF_TYPE;
		}
		else if (extension.equals("HTML")) {
			return FileType.HTML_TYPE;
		}
		if (extension.equals("TXT")) {
			return FileType.TEXT_TYPE;
		}
		else { // more complex types
			
			String[] imagesExtensions = new String[] { "PNG", "JPG", "JPEG", "BMP", "GIF" };
			for (String s : imagesExtensions) {
				if (extension.equals(s)) {
					return FileType.IMAGE_TYPE; 
				}
			}
			
		}
		
		return FileType.OTHER_TYPE;
	}

}
