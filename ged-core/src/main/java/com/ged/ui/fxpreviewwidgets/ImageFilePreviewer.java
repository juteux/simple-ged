package com.ged.ui.fxpreviewwidgets;

import java.io.File;

import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.apache.log4j.Logger;

public class ImageFilePreviewer extends AbstractFilePreviewer {

	private static final Logger logger = Logger.getLogger(ImageFilePreviewer.class);
	
	private Dimension2D maximumSize;
	
	public ImageFilePreviewer(String absoluteFilePath, Dimension2D maxSize) {
		super(absoluteFilePath);
		this.maximumSize = maxSize;
		
		logger.debug("Max size : " + maxSize.getWidth() + " x " + maxSize.getHeight());
	}

	@Override
	public void load() throws CannotCreatePreviewerException {
		try {
			File file = new File(absoluteFilePath);
			Image image = new Image(file.toURI().toURL().toExternalForm());
		
			
			// Scaled image size
			double newWidth = image.getWidth();
			double newHeight = image.getHeight();
			boolean hasToBeScalled = false;

			if (image.getHeight() > maximumSize.getHeight() && image.getWidth() <= maximumSize.getWidth()) {	// too height
				double scaleFactor = maximumSize.getHeight() / (double)image.getHeight();
				newWidth = image.getWidth() * scaleFactor;
				newHeight = image.getHeight() * scaleFactor;
				hasToBeScalled = true;
			} 
			else if (image.getWidth() > maximumSize.getWidth() && image.getHeight() <= maximumSize.getHeight()) { // too large
				double scaleFactor = maximumSize.getWidth() / (double)image.getWidth();
				newWidth = image.getWidth() * scaleFactor;
				newHeight = image.getHeight() * scaleFactor;
				hasToBeScalled = true;
			}
			else if (image.getWidth() > maximumSize.getWidth() && image.getHeight() > maximumSize.getWidth()){ // too height & too large
				if (image.getHeight() > image.getWidth()) { 
					double scaleFactor = (maximumSize.getHeight() / (double)image.getHeight())/2;
					newWidth = image.getWidth() * scaleFactor;
					newHeight = image.getHeight() * scaleFactor;
					hasToBeScalled = true;
				}
				else {
					double scaleFactor = (maximumSize.getWidth() / (double)image.getWidth())/2;
					newWidth = image.getWidth() * scaleFactor;
					newHeight = image.getHeight() * scaleFactor;
					hasToBeScalled = true;
				}
			}

			ImageView iv = new ImageView(image);
			
			if (hasToBeScalled) {
				iv.setSmooth(true);
				iv.setFitHeight(newHeight);
				iv.setFitWidth(newWidth);
			} 

			getChildren().add(iv);
		} 
		catch (Exception e) {
			logger.error("Failed to load " + absoluteFilePath + " : " + e.getMessage());
			logger.error(e.getStackTrace().toString());
			throw new CannotCreatePreviewerException();
		}
	}

	@Override
	public boolean isPrintable() {
		return true;
	}

}
