package com.ged.ui.preview_widgets;

import javax.swing.JTextField;


/**
 * When no other viewer is matching, this viewer will display an error message
 * @author xavier
 *
 */
public class DefaultDocumentPreviewer extends AbstractDocumentPreviewer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public DefaultDocumentPreviewer(String absoluteFilePath) {
		super(absoluteFilePath);
	}

	
	/**
	 * This method should never fail, so never throws the exception
	 */
	@Override
	public void load() throws CannotCreatePreviewerException {
		JTextField textField = new JTextField();
		textField.setEditable(false);
		textField.setText("Pas de prévisualisation possible");
		
		add(textField);
	}


	@Override
	public boolean isPrintable() {
		return false;
	}

}

