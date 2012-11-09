package com.simple.ged.ui.previewwidgets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.print.DocFlavor;

import com.simple.ged.tools.PrintingHelper;

import javafx.geometry.Dimension2D;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;

/**
 * Some previewer for text files
 * 
 * @author xavier
 *
 */
public class TextFilePreviewer extends AbstractFilePreviewer {
	
	
	public TextFilePreviewer(String absoluteFilePath, Dimension2D maxSize) {
		super(absoluteFilePath, maxSize);
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

		final TextArea textArea = TextAreaBuilder.create()
				.prefWidth(maxSize.getWidth())
				.prefHeight(maxSize.getHeight())
				.wrapText(true)
				.build();
		
		textArea.setText(out.toString());
		textArea.setEditable(false);
		
		getChildren().add(textArea);
	}


	@Override
	public boolean isPrintable() {
		return true;
	}
	
	@Override
	public void print() {
		PrintingHelper.printFile(absoluteFilePath, DocFlavor.INPUT_STREAM.TEXT_PLAIN_HOST);
	}
}
