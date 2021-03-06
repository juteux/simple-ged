package com.simple.ged.ui.previewwidgets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javafx.geometry.Dimension2D;
import javafx.scene.web.WebView;
import javafx.scene.web.WebViewBuilder;

import javax.print.DocFlavor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.tools.PrintingHelper;

import fr.xmichel.toolbox.tools.FileHelper;

/**
 * 
 * Previewer for html files
 * 
 * @author xavier
 *
 */
public class HtmlFilePreviewer extends AbstractFilePreviewer {

	/**
	 * My logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(HtmlFilePreviewer.class);
	
	
	public HtmlFilePreviewer(String absoluteFilePath, Dimension2D maxSize) {
		super(absoluteFilePath, maxSize);
	}

	@Override
	public void load() throws CannotCreatePreviewerException {

		final WebView webView = WebViewBuilder.create()
				.prefWidth(maxSize.getWidth())
				.prefHeight(maxSize.getHeight())
				.build();
		
		try {
			InputStream inputStream = new FileInputStream(absoluteFilePath);
			Reader      reader      = new InputStreamReader(inputStream);
			String content = FileHelper.readAllStringContent(reader);
			logger.trace("content : " + content);
			webView.getEngine().loadContent(content);
		} catch (IOException e) {
			logger.error("Failed to read file : " + absoluteFilePath, e);
			throw new CannotCreatePreviewerException();
		}
		
		getChildren().add(webView);
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
