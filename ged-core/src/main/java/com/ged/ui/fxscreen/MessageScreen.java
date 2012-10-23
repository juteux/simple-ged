package com.ged.ui.fxscreen;

import javafx.scene.web.WebView;

import org.apache.log4j.Logger;

import com.ged.models.GedMessage;
import com.ged.services.MessageService;
import com.ged.ui.FxMainWindow;
import com.tools.DateHelper;

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
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(MessageScreen.class);
	
	/**
	 * the about screen is a web view
	 */
	private WebView webView;
	
	/**
	 * The webview html string content
	 */
	private String strMessage;
	
	
	public MessageScreen(FxMainWindow mw) {
		super(mw);
		
		instanciateWidget();
		
		webView.getEngine().loadContent(strMessage);

		this.getChildren().add(webView);
	}
	
	
	private void instanciateWidget() {
		webView = new WebView();
		
		StringBuilder sb = new StringBuilder();
		
		for (GedMessage m : MessageService.getMessages()) {
			
			String pre  = "";
			String post = "";
			
			if (! m.isRead()) {
				pre  = "<b>";
				post = "</b>";
			}
			
			if (m.getMessageLevel().equalsIgnoreCase("error")) {
				pre  = pre + "<font color='red'>";
				post = "</font>" + post;
			}
			else if (m.getMessageLevel().equalsIgnoreCase("info")) {
				pre  = pre + "<font color='green'>";
				post = "</font>" + post;
			}
			
			sb.append(DateHelper.calendarToString(m.getDate()) + "<br/>" + pre + m.getLabel() + post);
			sb.append("<br/><br/><hr/><br/>");
		}
		
		strMessage = sb.toString();
	}

}
