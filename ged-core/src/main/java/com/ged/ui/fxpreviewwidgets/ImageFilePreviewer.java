package com.ged.ui.fxpreviewwidgets;

import java.awt.Dimension;
import java.awt.Toolkit;
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

			Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
			s.setSize(maximumSize.getWidth(), maximumSize.getHeight());

			double pW = image.getWidth();
			double pH = image.getHeight();
			
			double xScale = (double) s.width / pW;
			double yScale = (double) s.height / pH;
			double scale = xScale < yScale ? xScale : yScale;

			// Work out target size
			pW *= scale;
			pH *= scale;

			// Get image and set
			ImageView imageView = new ImageView(image);

			// Set size of components
			imageView.setSmooth(true);
			imageView.setFitWidth(pW);
			imageView.setFitHeight(pH);
			this.setWidth(imageView.getFitWidth() + 2);
			this.setHeight(imageView.getFitHeight() + 2);
			
			getChildren().add(imageView);
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
