package com.tools.ui;

import java.io.File;

import javax.swing.JFileChooser;

/**
 * Some file selector specialized for directories
 * 
 * @author xavier
 *
 */
public class DirectorySelector extends FileSelector {

	private static final long serialVersionUID = 1L;

	
	public DirectorySelector(String windowsTitle) {
		super(windowsTitle);;
	}

	@Override
	public boolean isValidPath() {
		File f = new File(getFilePath());
		return ( f.exists() && f.isDirectory() );
	}

	@Override
	public void openSelectionPopup() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(getDirPath()));
		chooser.setDialogTitle(getWindowsTitle());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	setCurrentFilePath(chooser.getSelectedFile().toString());
	    }
	}

}
