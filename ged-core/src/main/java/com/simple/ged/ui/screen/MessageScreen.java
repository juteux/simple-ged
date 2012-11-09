package com.simple.ged.ui.screen;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

import org.apache.log4j.Logger;

import com.simple.ged.models.GedMessage;
import com.simple.ged.services.MessageService;
import com.simple.ged.ui.MainWindow;
import com.tools.DateHelper;
import com.tools.FileHelper;

/**
 * This screen show message about plugins comportement
 * 
 * @author xavier
 *
 */
public class MessageScreen extends FxSoftwareScreen {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(MessageScreen.class);
	
	/**
	 * the about screen is a web view
	 */
	private WebView webView;
	
	/**
	 * The webview html string content
	 */
	private String strMessage;
	
	
	public MessageScreen(MainWindow mw) {
		super(mw);
		
		instanciateWidget();
		
		String header = "";
		String footer = "";
		Reader reader = new InputStreamReader(AboutScreen.class.getResourceAsStream("/html/message-head.html"));
		try {
			header = FileHelper.readAllStringContent(reader);
			reader.close();
		} catch (IOException e) {
			logger.error("Failed to read /html/message-head.html");
			e.printStackTrace();
		}
		
		Reader reader2 = new InputStreamReader(AboutScreen.class.getResourceAsStream("/html/message-foot.html"));
		try {
			footer = FileHelper.readAllStringContent(reader2);
			reader2.close();
		} catch (IOException e) {
			logger.error("Failed to read /html/message-foot.html");
			e.printStackTrace();
		}
		

		webView.getEngine().loadContent(header + strMessage + footer);

		HBox.setHgrow(webView, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
		
		this.getChildren().add(webView);
		
		MessageService.markAllMessagesAsRead();
	}
	
	
	private void instanciateWidget() {
		webView = new WebView();
		
		StringBuilder sb = new StringBuilder();
		
		for (GedMessage m : MessageService.getMessages()) {
			
			List<String> classes = new ArrayList<>();
			classes.add("alert");
			
			if (! m.isRead()) {
				classes.add("unread");
			}
			
			if (m.getMessageLevel().equalsIgnoreCase("error")) {
				classes.add("alert-error");
			}
			else if (m.getMessageLevel().equalsIgnoreCase("info")) {
				classes.add("alert-success");
			}
			else {
				classes.add("alert-info");	
			}
			
			String strClass = "";
			for (String s : classes) {
				strClass += s + " ";
			}
			
			sb.append("<div class=\"");
			sb.append(strClass);
			sb.append("\">");
			sb.append(DateHelper.calendarToString(m.getDate()));
			sb.append("<hr/>");
			sb.append(m.getLabel());
			sb.append("</div>");
		}
		
		strMessage = sb.toString();
	}

}
