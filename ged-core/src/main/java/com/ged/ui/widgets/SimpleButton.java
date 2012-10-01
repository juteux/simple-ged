package com.ged.ui.widgets;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.managers.ImageManager;

/**
 * This class inherits from JButton, but has the advantage
 * to redefine the method for setting icon. Just give the file
 * path and the icon will have the good dimension.
 * 
 * Notice that this class is strongly linked with the image manager.
 */
public class SimpleButton extends JButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Size of the button, child class should fix that
	 */
	private Dimension iconSize = new Dimension(10, 10);
	
	public SimpleButton() {
		super();
	}
	
	public SimpleButton(String label) {
		super(label);
	}
	
	public void setIcon(String filePath) {
		// get & resize image
		Image image = ImageManager.getImage(filePath)
				.getScaledInstance(iconSize.width, iconSize.height, java.awt.Image.SCALE_SMOOTH);
		// set image on button
		setIcon(new ImageIcon(image));
	}

	
	/**
	 * @return the iconSize
	 */
	public Dimension getIconSize() {
		return iconSize;
	}

	/**
	 * @param iconSize the iconSize to set
	 */
	public void setIconSize(Dimension iconSize) {
		this.iconSize = iconSize;
	}

}


