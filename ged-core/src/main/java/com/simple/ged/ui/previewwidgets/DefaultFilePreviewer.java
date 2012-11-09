package com.simple.ged.ui.previewwidgets;

import javafx.geometry.Dimension2D;
import javafx.scene.control.Label;

/**
 * 
 * The default previewer just show an generic message "cannot show previewer"
 * 
 * @author xavier
 *
 */
public class DefaultFilePreviewer extends AbstractFilePreviewer {
	
	public DefaultFilePreviewer(String absoluteFilePath, Dimension2D maxSize) {
		super(absoluteFilePath, maxSize);
	}

	/**
	 * This method should never fail, so never throws the exception
	 */
	@Override
	public void load() throws CannotCreatePreviewerException {
		Label l = new Label("Pas de prévisualisation possible");
		getChildren().add(l);
	}


	@Override
	public boolean isPrintable() {
		return false;
	}
}
