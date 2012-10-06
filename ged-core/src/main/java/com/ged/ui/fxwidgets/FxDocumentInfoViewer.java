package com.ged.ui.fxwidgets;

import java.awt.Color;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import com.ged.models.GedDocument;
import com.ged.models.GedDocumentFile;
import com.tools.DateHelper;
import com.tools.PropertiesHelper;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class FxDocumentInfoViewer extends VBox {

	private static final Logger logger = Logger.getLogger(FxDocumentInfoViewer.class);
	
	/**
	 * The software properties
	 */
	Properties properties = PropertiesHelper.getInstance().getProperties();

	
	
	/**
	 * This label contains document description
	 */
	private Label title;
	
	/**
	 * This label display document details
	 */
	private Label description;
	
	/**
	 * This label display document date
	 */
	private Label date;
	
	
	public FxDocumentInfoViewer() {
		instantiateWidgets();
		
		this.getChildren().add(title);
		this.getChildren().add(date);
		this.getChildren().add(description);
	}
	
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		title = new Label("Titre de test");
		description = new Label("Description de test");
		date = new Label("00/00/0000");
	}
	
	
	/**
	 * Display document infos
	 * @param document
	 */
	public void setGedDocument(GedDocument document) {
		
		if (document == null) {
			title.setText("");
			description.setText("");
			date.setText("");
			return;
		}
		
		title.setText(document.getName());
		date.setText(DateHelper.calendarToString(document.getDate()));
		description.setText(document.getDescription());
	}
}

