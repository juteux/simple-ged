package com.ged.ui.fxscreen;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

import org.apache.log4j.Logger;

import com.ged.ui.FxMainWindow;
import com.tools.FileHelper;

/**
 * The screen about
 * 
 * @author xavier
 *
 */
public class AboutScreen extends FxSoftwareScreen {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(AboutScreen.class);
	
	/**
	 * the about screen is a web view
	 */
	private WebView webView;
	
	
	public AboutScreen(FxMainWindow mw) {
		super(mw);
		instanciateWidget();
		
		try {
			Reader reader = new InputStreamReader(AboutScreen.class.getResourceAsStream("/html/about.html"));
			String content = FileHelper.readAllStringContent(reader);
			//logger.debug("content : " + content);
			webView.getEngine().loadContent(content);
		} catch (IOException|NullPointerException e) {
			logger.error("Failed to read file : html/about.html");
			e.printStackTrace();
		}
		
		HBox.setHgrow(webView, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
		
		this.getChildren().add(webView);
	}
	
	
	private void instanciateWidget() {
		webView = new WebView();
	}

}
