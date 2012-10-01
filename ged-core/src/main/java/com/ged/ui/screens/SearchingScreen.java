package com.ged.ui.screens;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import com.ged.ui.MainWindow;
import com.ged.ui.controllers.SearchScreenController;
import com.ged.ui.widgets.DocumentPreview;
import com.ged.ui.widgets.SimpleButton;

/**
 * 
 * This screen is for searching document in library
 * 
 * @author xavier
 *
 */
public class SearchingScreen extends SoftwareScreen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This screen buttons model
	 */
	public class SearchScreenButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public SearchScreenButton(String label, SearchScreenController controller) {
			super(label);
			
			setIconSize(new Dimension(30, 30));
			setPreferredSize(new Dimension(200, 40));
			
			addActionListener(controller);
		}
	}
	
	/**
	 * A field to enter the searched pattern
	 */
	private JTextField inputSearchedPattern;
	
	/**
	 * A button for saving
	 */
	private SimpleButton btnSearch;
	
	/**
	 * A button to return to main screen
	 */
	private SimpleButton btnReturn;
	
	/**
	 * Check box search in title
	 */
	private JCheckBox checkboxTitle;
	
	/**
	 * Check box search in description
	 */
	private JCheckBox checkboxDescription;
	
	/**
	 * Search results
	 */
	private JTable tableResults;
	
	/**
	 * Search results model
	 */
	private DefaultTableModel tableResultsModel;
	
	/**
	 * Document previewer
	 */
	private DocumentPreview documentPreview;
	
	
	public SearchingScreen(MainWindow mw) {
		super(mw);
		
		instantiateWidgets();
		
		// button area
		JPanel btnActionPanel = new JPanel();
		btnActionPanel.add(btnReturn);
		btnActionPanel.add(btnSearch);
		
		// search options
		MigLayout searchingOptionsLayout = new MigLayout(	
				"wrap",
				"[grow,fill,center]",	// columns
				""	// rows
			);
		
		JPanel searchingOptionPanel = new JPanel(searchingOptionsLayout);
		searchingOptionPanel.setBorder(BorderFactory.createTitledBorder(properties.getProperty("search_option")));
	
		searchingOptionPanel.add(new JLabel(properties.getProperty("search_")), "gap para");
		searchingOptionPanel.add(inputSearchedPattern, "span,growx,wrap");
		
		searchingOptionPanel.add(checkboxTitle);
		searchingOptionPanel.add(checkboxDescription);
		
		searchingOptionPanel.add(btnActionPanel);

		// table results 		
		JScrollPane tableResultsScrollPane = new JScrollPane(tableResults);
		tableResults.setFillsViewportHeight(true);
		
		// main layout
		MigLayout layout = new MigLayout(	
				"wrap",
				"[grow 50,fill,center][grow 50,fill,center]",						// columns
				"[grow 50,fill,center][grow 50,fill,center]"	// rows
			);
		
		JPanel container = new JPanel(layout);

		// add widget in main layout
		container.add(searchingOptionPanel);
		container.add(documentPreview);
		container.add(tableResultsScrollPane);

		
		// main layout
		add(container);
	}

	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		SearchScreenController controller = new SearchScreenController(this);
		
		inputSearchedPattern = new JTextField();
		inputSearchedPattern.addKeyListener(controller);
		
		checkboxTitle = new JCheckBox(properties.getProperty("search_in_title"));
		checkboxTitle.setSelected(true);
		checkboxTitle.setEnabled(false);

		checkboxDescription = new JCheckBox(properties.getProperty("search_in_description"));
		
		btnSearch = new SearchScreenButton(properties.getProperty("search"), controller);
		btnSearch.setEnabled(false);
		btnReturn = new SearchScreenButton(properties.getProperty("back"), controller);
		
		documentPreview = new DocumentPreview();
		
		String[] columnNames = {
				properties.getProperty("file"),
				properties.getProperty("document"),
				properties.getProperty("date"),
				properties.getProperty("description")
        };
		
		tableResultsModel = new DefaultTableModel(columnNames, 0) {
			
			private static final long serialVersionUID = 8062548080632065191L;
			
			@Override
		    public boolean isCellEditable(int rowIndex, int mColIndex) {
		        return false;
		    }
			
		};
		tableResults = new JTable(tableResultsModel);
		
		tableResults.addMouseListener(controller);

		// define associated pictures
		Map<SimpleButton, String> associatedImages = new HashMap<SimpleButton, String>();
		associatedImages.put(btnReturn, 				properties.getProperty("ico_back"));
		associatedImages.put(btnSearch,					properties.getProperty("ico_search"));
		
		// set pictures
		for (Map.Entry<SimpleButton, String> e : associatedImages.entrySet()) {
			e.getKey().setIcon(e.getValue());
		}
	}


	public JTextField getInputSearchedPattern() {
		return inputSearchedPattern;
	}

	public SimpleButton getBtnSearch() {
		return btnSearch;
	}

	public SimpleButton getBtnReturn() {
		return btnReturn;
	}

	public JCheckBox getCheckboxTitle() {
		return checkboxTitle;
	}

	public JCheckBox getCheckboxDescription() {
		return checkboxDescription;
	}

	public JTable getTableResults() {
		return tableResults;
	}

	public DefaultTableModel getTableResultsModel() {
		return tableResultsModel;
	}

	public DocumentPreview getDocumentPreview() {
		return documentPreview;
	}

}
