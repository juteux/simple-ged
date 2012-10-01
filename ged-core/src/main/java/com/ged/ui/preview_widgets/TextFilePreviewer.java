package com.ged.ui.preview_widgets;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.print.DocFlavor;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.ged.tools.PrintingHelper;

/**
 * Previewer for plain text file
 * @author xavier
 *
 */
public class TextFilePreviewer extends AbstractDocumentPreviewer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Don't forget to give the ABSOLUTE file path !
	 * 
	 * @param absoluteFilePath
	 * 				One more time, the ABSOLUTE file path
	 */
	public TextFilePreviewer(String absoluteFilePath) {
		super(absoluteFilePath);
	}

	
	/**
	 * Load the file content in a text area
	 */
	@Override
	public void load() throws CannotCreatePreviewerException {
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);

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

		textArea.setText(out.toString());
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.scrollRectToVisible(new Rectangle());
		scrollPane.scrollRectToVisible(new Rectangle());
		
		add(scrollPane);
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
