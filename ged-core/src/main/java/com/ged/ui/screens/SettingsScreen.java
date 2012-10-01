package com.ged.ui.screens;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import com.ged.Profile;
import com.ged.ui.MainWindow;
import com.ged.ui.controllers.SettingsScreenController;
import com.ged.ui.widgets.SimpleButton;
import com.tools.ui.DirectorySelector;


/**
 * This screen is used to fix software settings
 */
public class SettingsScreen extends SoftwareScreen {
	
	/**
	 * This screen buttons model
	 */
	public class SettingScreenButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public SettingScreenButton(String label, SettingsScreenController controller) {
			super(label);
			
			setIconSize(new Dimension(30, 30));
			setPreferredSize(new Dimension(200, 40));
			
			addActionListener(controller);
		}
	}
	
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Button for returning to main screen
	 */
	private SimpleButton btnReturn;
	
	/**
	 * Button for saving preferences
	 */
	private SimpleButton btnSave;
	
	/**
	 * The directory selector for library location
	 */
	private DirectorySelector libraryLocationSelector;
	
	/**
	 * The combo for theme selection
	 */
	private JComboBox comboTheme;
	
	/**
	 * The combo for printer selection
	 */
	private JComboBox comboPrinter;
	
	
	
	public SettingsScreen(MainWindow mw) {
		super(mw);
		instantiateWidgets();
		
		// library info
		MigLayout libraryInfoLayout = new MigLayout(	
				"wrap",
				"[grow,fill,center]",	// columns
				""	// rows
			);
		
		JPanel libraryInformationPanel = new JPanel(libraryInfoLayout);
		libraryInformationPanel.setBorder(BorderFactory.createTitledBorder(properties.getProperty("question_library_location")));
		libraryInformationPanel.add(libraryLocationSelector);
		
		// theme selection
		MigLayout themeLayout = new MigLayout(	
				"wrap",
				"[grow,fill,center]",	// columns
				""	// rows
			);
		
		JPanel themePanel = new JPanel(themeLayout);
		themePanel.setBorder(BorderFactory.createTitledBorder(properties.getProperty("question_skin")));
		themePanel.add(comboTheme);
		
		// printer selection
		MigLayout printerLayout = new MigLayout(	
				"wrap",
				"[grow,fill,center]",	// columns
				""	// rows
			);

		JPanel printerPanel = new JPanel(printerLayout);
		printerPanel.setBorder(BorderFactory.createTitledBorder(properties.getProperty("question_printer")));
		printerPanel.add(comboPrinter);
		
		
		// button area
		JPanel btnPanel = new JPanel();
		btnPanel.add(btnReturn);
		btnPanel.add(btnSave);
		
		// global positioning
		MigLayout globalLayout = new MigLayout(	
				"wrap",
				"[grow,fill,center]",	// columns
				""	// rows
			);
		
		JPanel container = new JPanel(globalLayout);
		container.add(libraryInformationPanel);
		container.add(themePanel);
		container.add(printerPanel);
		container.add(btnPanel);
		
		add(container);
	}


	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		SettingsScreenController controller = new SettingsScreenController(this);
		
		libraryLocationSelector = new DirectorySelector(properties.getProperty("select_root_library_directory"));
		libraryLocationSelector.setCurrentFilePath(Profile.getInstance().getLibraryRoot());
		libraryLocationSelector.addFileChangedListener(controller);
		
		// --
		
		List<String> avalaibleThemes = new ArrayList<String>();
		for (UIManager.LookAndFeelInfo i : UIManager.getInstalledLookAndFeels()) {
			avalaibleThemes.add(i.getName());
		}
		
		comboTheme = new JComboBox();
		
		for (String s : avalaibleThemes) {
			comboTheme.addItem(s);
		}
		
		comboTheme.setSelectedItem(Profile.getInstance().getThemeName());
		comboTheme.addActionListener(controller);

		// --
		
		comboPrinter = new JComboBox();
		
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(new Copies(1));
		
		for (PrintService ps : PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.JPEG, pras)) {
			comboPrinter.addItem(ps.getName());
		}
		comboPrinter.setSelectedItem(Profile.getInstance().getDefaultPrinterName());
		
		comboPrinter.addActionListener(controller);
		
		// create buttons
		btnReturn  	= new SettingScreenButton(properties.getProperty("back"), controller);
		btnSave     = new SettingScreenButton(properties.getProperty("save"), controller);
		
		btnSave.setEnabled(false);
		
		// define associated pictures
		Map<SimpleButton, String> associatedImages = new HashMap<SimpleButton, String>();
		associatedImages.put(btnReturn, 	properties.getProperty("ico_back"));
		associatedImages.put(btnSave,		properties.getProperty("ico_save"));

		// set pictures
		for (Map.Entry<SimpleButton, String> e : associatedImages.entrySet()) {
			e.getKey().setIcon(e.getValue());
		}
	}


	/**
	 * @return the btnReturn
	 */
	public SimpleButton getBtnReturn() {
		return btnReturn;
	}

	/**
	 * @return the btnSave
	 */
	public SimpleButton getBtnSave() {
		return btnSave;
	}
	
	/**
	 * @return the libraryLocationSelector
	 */
	public DirectorySelector getLibraryLocationSelector() {
		return libraryLocationSelector;
	}

	/**
	 * @return the comboTheme
	 */
	public JComboBox getComboTheme() {
		return comboTheme;
	}

	public JComboBox getComboPrinter() {
		return comboPrinter;
	}	

}

