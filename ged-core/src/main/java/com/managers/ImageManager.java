package com.managers;

import java.awt.Image;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;


/**
 * We don't wan't to load icons every time we need them.
 * This class provide a manager which keep references on icons loaded
 */
@Deprecated
public class ImageManager {

	private static final Logger logger = Logger.getLogger(ImageManager.class);
	
	/**
	 * Get an image, if this image is not loaded yet, it's done now
	 * 
	 * @param filePath
	 * 				The image path
	 * 
	 * Warning : the image can be null if it's not found
	 * 
	 * @return The required image 
	 */
	public static Image getImage(String filePath) {
		logger.trace("Required image : " + filePath);
		return new ImageIcon(ImageManager.class.getClass().getResource(filePath)).getImage();
	}
	
}

