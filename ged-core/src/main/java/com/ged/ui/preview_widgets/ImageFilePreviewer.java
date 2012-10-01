package com.ged.ui.preview_widgets;

import java.awt.Dimension;
import java.awt.Image;

import javax.print.DocFlavor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.ged.tools.PrintingHelper;

/**
 * A previewer for images
 * @author xavier
 *
 */
public class ImageFilePreviewer extends AbstractDocumentPreviewer {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The maximum image size
	 */
	private Dimension maximumSize;
	
	
	/**
	 * Don't forget to give the ABSOLUTE file path !
	 * 
	 * @param absoluteFilePath
	 * 				One more time, the ABSOLUTE file path
	 */
	public ImageFilePreviewer(String absoluteFilePath, Dimension maximumSize) {
		super(absoluteFilePath);
		this.maximumSize = maximumSize;
	}

	
	/**
	 * Load the image content in a label
	 */
	@Override
	public void load() throws CannotCreatePreviewerException {
		JLabel label = new JLabel(); 
		ImageIcon image = new ImageIcon(absoluteFilePath); 
		if (image.getImage() == null) {
			throw new CannotCreatePreviewerException();
		}
		
		// Scaled image size
		float newWidth = image.getIconWidth();
		float newHeight = image.getIconHeight();
		boolean hasToBeScalled = false;

		if (image.getIconHeight() > maximumSize.height && image.getIconWidth() <= maximumSize.width) {	// too height
			float scaleFactor = maximumSize.height / (float)image.getIconHeight();
			newWidth = image.getIconWidth() * scaleFactor;
			newHeight = image.getIconHeight() * scaleFactor;
			hasToBeScalled = true;
		} 
		else if (image.getIconWidth() > maximumSize.width && image.getIconHeight() <= maximumSize.getHeight()) { // too large
			float scaleFactor = maximumSize.width / (float)image.getIconWidth();
			newWidth = image.getIconWidth() * scaleFactor;
			newHeight = image.getIconHeight() * scaleFactor;
			hasToBeScalled = true;
		}
		else if (image.getIconWidth() > maximumSize.width && image.getIconHeight() > maximumSize.getWidth()){ // too height & too large
			if (image.getIconHeight() > image.getIconWidth()) { 
				float scaleFactor = (maximumSize.height / (float)image.getIconHeight())/2;
				newWidth = image.getIconWidth() * scaleFactor;
				newHeight = image.getIconHeight() * scaleFactor;
				hasToBeScalled = true;
			}
			else {
				float scaleFactor = (maximumSize.width / (float)image.getIconWidth())/2;
				newWidth = image.getIconWidth() * scaleFactor;
				newHeight = image.getIconHeight() * scaleFactor;
				hasToBeScalled = true;
			}
		}

		if (hasToBeScalled) {
			Image scalledImage = image.getImage().getScaledInstance((int)newWidth, (int)newHeight, Image.SCALE_SMOOTH);
			label.setIcon(new ImageIcon(scalledImage));
		} 
		else {
			label.setIcon(image);
		}
		
		add(label);
	}
	
	@Override
	public boolean isPrintable() {

		String extension = com.tools.FileHelper.getExtension(absoluteFilePath);
		String[] supportedExtensions = new String[] { "PNG", "JPG", "JPEG", "GIF" };
		for (String s : supportedExtensions) {
			if (extension.equals(s)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	@Override
	public void print() {
		
		String extension = com.tools.FileHelper.getExtension(absoluteFilePath);
		DocFlavor flavor = null;
		
		if (extension.equalsIgnoreCase("PNG")) {
			flavor = DocFlavor.INPUT_STREAM.PNG;
		}
		else if (extension.equalsIgnoreCase("GIF")) {
			flavor = DocFlavor.INPUT_STREAM.GIF;
		}
		else if (extension.equalsIgnoreCase("JPG") || extension.equalsIgnoreCase("JPEG")) {
			flavor = DocFlavor.INPUT_STREAM.JPEG;
		}
		
		PrintingHelper.printFile(absoluteFilePath, flavor);
	}
}
