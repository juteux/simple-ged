package com.ged.ui.widgets;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.ged.models.GedDocument;
import com.ged.models.GedDocumentFile;
import com.tools.DateHelper;
import com.tools.ui.AbstractWidget;


/**
 * This is a widget which display document informations
 * 
 * @author xavier
 *
 */
public class DocumentInfoViewer extends AbstractWidget {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This container display document title and contains others informations
	 */
	private JPanel mainContainer; 
	
	/**
	 * This label contains document description
	 */
	private JLabel description;
	
	/**
	 * This label display document details
	 */
	private JLabel details;

	
	public DocumentInfoViewer() {
		super();
		instantiateWidgets();
		
		//add(mainContainer);
	}
	
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		MigLayout layout = new MigLayout(	
				"wrap",
				"[grow,fill,center]",
				"[grow,fill,center]"
			);
		
		mainContainer = new JPanel(layout);
		
		description = new JLabel();
		details = new JLabel();
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Description", new JScrollPane(description));
		tabs.addTab("Détails", details);
		
		mainContainer.add(tabs);
	}
	
	/**
	 * Display document infos
	 * @param document
	 */
	public void setGedDocument(GedDocument document) {
		
		if (document == null) {
			TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "");
			title.setTitleJustification(TitledBorder.CENTER);
			mainContainer.setBorder(title);
			description.setText("");
			details.setText("");
			return;
		}
		
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), document.getName());
		title.setTitleJustification(TitledBorder.CENTER);
		mainContainer.setBorder(title);
		
		description.setText("<html>" + document.getDescription().replace("\n", "<br/>") + "</html>");
		
		String strDetails = "<html><b>" + properties.getProperty("date_") + "</b> " + DateHelper.calendarToString(document.getDate()) + "<br/><br/>";
		
		if (document.getLocation().getId() != 1) {
			strDetails += "<b>" + properties.getProperty("location_") + "</b> " + document.getLocation().getLabel() + "<br/><br/>"; 
		}
		
		strDetails += "<b>"+ properties.getProperty("linked_files") +"</b><ul>";
		for (GedDocumentFile file : document.getDocumentFiles()) {
			strDetails += "<li>" + file.getRelativeFilePath() + "</li>";
		}
		strDetails += "</ul></html>";
		details.setText(strDetails);
	}
}


