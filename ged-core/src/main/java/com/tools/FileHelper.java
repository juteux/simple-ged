package com.tools;

import java.io.File;

import org.apache.log4j.Logger;

import com.ged.ui.preview_widgets.FilePreviewerFactory.FileType;

/**
 * File manipulation tools
 * @author xavier
 *
 */
public class FileHelper {

	
	private static final Logger logger = Logger.getLogger(FileHelper.class);
	
	
	/**
	 * Supported file types
	 * 
	 * @author xavier
	 * 
	 */
	public enum FileType {
		OTHER_TYPE, 	// Unknown file type
		PNG_TYPE, 		// An image png
		JPG_TYPE, 		// An image jpg
		BMP_TYPE, 		// An image bmp
		GIF_TYPE, 		// An image gif
		TEXT_TYPE, 		// A brut text file
		PDF_TYPE, 		// A pdf file
		HTML_TYPE,		// An html file
		DOC_TYPE,		// A .doc doc
		DOCX_TYPE,		// A .docx doc
		PPT_TYPE,		// A ppt 
		PPTX_TYPE		// A pptx
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

		String extension = getExtension(fileName);

		switch (extension) {
		case "PNG" :
			return FileType.PNG_TYPE;
		case "JPG" :
		case "JPEG" :
			return FileType.JPG_TYPE;
		case "BMP" :
			return FileType.BMP_TYPE;
		case "GIF" :
			return FileType.GIF_TYPE;
		case "TXT" :
			return FileType.TEXT_TYPE;
		case "PDF" :
			return FileType.PDF_TYPE;
		case "HTML" :
			return FileType.HTML_TYPE;
		case "DOC" :
			return FileType.DOC_TYPE;
		case "DOCX" :
			return FileType.DOCX_TYPE;
		case "PPT" :
			return FileType.PPT_TYPE;
		case "PPTX" :
			return FileType.PPTX_TYPE;
		default :
			return FileType.OTHER_TYPE;
		}
	}
	
	
	
	/**
	 * Get the extension of the given file (without leading dot)
	 * 
	 * The extension is returned in upper case
	 */
	public static String getExtension(String fileName) {
		int mid = fileName.lastIndexOf(".");
		return fileName.substring(mid + 1, fileName.length()).toUpperCase();
	}
	
	
	/**
	 * Create the given directory if it not exists yet
	 */
	public static void createDirectoryIfNecessary(String dirPath) {
		File f = new File(dirPath);
		if ( ! f.exists() ) {
			try {
				f.mkdir();
			} catch (Exception e) {
				logger.error("Error while creating directory : " + dirPath);
			}
		}
	}
	
	/**
	 * Does this folder exits ? 
	 * 
	 * @return true if the folder exists, false otherwise.
	 */
	public static boolean folderExists(String folderAbsolutePath) {
		File f = new File(folderAbsolutePath);
		return (f.exists() && f.isDirectory());
	}
}
