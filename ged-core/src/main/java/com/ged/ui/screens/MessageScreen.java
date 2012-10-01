package com.ged.ui.screens;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.ged.models.GedMessage;
import com.ged.services.MessageService;
import com.ged.ui.MainWindow;
import com.ged.ui.controllers.MessageScreenController;
import com.ged.ui.widgets.SimpleButton;
import com.tools.DateHelper;

/**
 * This screen provide message view
 * 
 * @author xavier
 *
 */
public class MessageScreen extends SoftwareScreen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * This screen buttons model
	 */
	public class MessageScreenButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public MessageScreenButton(String label, MessageScreenController controller) {
			super(label);
			
			setIconSize(new Dimension(30, 30));
			setPreferredSize(new Dimension(200, 40));
			
			addActionListener(controller);
		}
	}
	
	
	/**
	 * The label which contains messages
	 */
	private JLabel labelMessages;
	
	/**
	 * The title label
	 */
	private JLabel titleLabel;
	
	/**
	 * Return button
	 */
	private SimpleButton btnReturn;
	
	
	
	public MessageScreen(MainWindow mw) {
		
		super(mw);
		instantiateWidgets();
		
		// say all messages are reads
		MessageService.markAllMessagesAsRead();
		notifyNoNewMessagesAvailable();
		
		// layouting
		MigLayout mainLayout = new MigLayout(	
				"wrap",
				"[grow,fill,center]",	// columns
				"[center][grow,fill,center][center]"	// rows
			);
		
		JPanel container = new JPanel(mainLayout);

		// add widgets in main layout
		container.add(titleLabel);
		container.add(new JScrollPane(labelMessages));
		container.add(btnReturn);
		
		// main layout
        add(container);
		
	}

	
	/**
	 * Instantiate attributes
	 */
	private void instantiateWidgets() {
		
		MessageScreenController controller = new MessageScreenController(this);
		
		titleLabel = new JLabel(properties.getProperty("message_screen_title"));
		btnReturn  = new MessageScreenButton(properties.getProperty("back"), controller);
		
		labelMessages = new JLabel();
		
		String message = "<html>";
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
			
			message += DateHelper.calendarToString(m.getDate()) + "<br/>" + pre + m.getLabel() + post;
			message += "<br/><br/><hr/><br/>";
		}
		message += "</html>";
		
		labelMessages.setText(message);
		
		// define associated pictures
		Map<SimpleButton, String> associatedImages = new HashMap<SimpleButton, String>();
		associatedImages.put(btnReturn, 	properties.getProperty("ico_back"));

		// set pictures
		for (Map.Entry<SimpleButton, String> e : associatedImages.entrySet()) {
			e.getKey().setIcon(e.getValue());
		}
	}

	public SimpleButton getBtnReturn() {
		return btnReturn;
	}

}
