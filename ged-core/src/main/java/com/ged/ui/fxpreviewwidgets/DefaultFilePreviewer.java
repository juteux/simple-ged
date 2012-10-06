package com.ged.ui.fxpreviewwidgets;

import javafx.scene.control.Label;


public class DefaultFilePreviewer extends AbstractFilePreviewer {
	
	public DefaultFilePreviewer(String absoluteFilePath) {
		super(absoluteFilePath);
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
