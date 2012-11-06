package com.tools.ui;

import java.io.File;

import com.ged.Profile;

import javafx.stage.DirectoryChooser;

/**
 * Some file selector specialized for directories
 * 
 * @author xavier
 *
 */
public class DirectorySelector extends FileSelector {


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
		
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(getWindowsTitle());
		
		//directoryChooser.setInitialDirectory(new File(getDirPath()));
		
		// Show open file dialog
		File file = directoryChooser.showDialog(null);

		if (file == null) {
			return;
		}
	}

}
