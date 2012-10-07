package com.ged.ui.fxpreviewwidgets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javafx.scene.control.Label;
import javafx.scene.text.Text;


public class TextFilePreviewer extends AbstractFilePreviewer {
	
	public TextFilePreviewer(String absoluteFilePath) {
		super(absoluteFilePath);
	}

	/**
	 * This method should never fail, so never throws the exception
	 */
	@Override
	public void load() throws CannotCreatePreviewerException {
		
		File f = new File(absoluteFilePath);
		StringWriter out = new StringWriter();
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
			
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			out.flush();
			out.close();
			in.close();
		} catch (IOException ie) {
			throw new CannotCreatePreviewerException();
		}

		Text t = new Text(out.toString());
		getChildren().add(t);
	}


	@Override
	public boolean isPrintable() {
		return false;
	}
}
