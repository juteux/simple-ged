package com.ged.ui.preview_widgets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.print.DocFlavor;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import com.ged.tools.PrintingHelper;

/**
 * Previewer for HTML files
 * @author xavier
 *
 */
public class HtmlFilePreviewer extends AbstractDocumentPreviewer {

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
	public HtmlFilePreviewer(String absoluteFilePath) {
		super(absoluteFilePath);
	}

	
	/**
	 * Load the file content in a JEditorPane
	 */
	@Override
	public void load() throws CannotCreatePreviewerException {
	
		File f = new File(absoluteFilePath);
		StringWriter out = new StringWriter();
		out.write("<html>");
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
		out.write("</html>");
		
		JEditorPane textLabel = new JEditorPane("text/html", out.toString());
		
		JScrollPane scrollPane = new JScrollPane(textLabel);
		
		add(scrollPane);
	}


	@Override
	public boolean isPrintable() {
		return true;
	}
	
	@Override
	public void print() {	
		PrintingHelper.printFile(absoluteFilePath, DocFlavor.INPUT_STREAM.TEXT_HTML_HOST);
	}
}

