package com.tools.ui;

import java.util.EventObject;

/**
 * The event is fired when the selected file changed 
 * 
 * @author xavier
 *
 */
public class FileChangedEventObject extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private boolean isValid;
	
	public FileChangedEventObject(Object source) {
		super(source);
		setValid(false);
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
}
