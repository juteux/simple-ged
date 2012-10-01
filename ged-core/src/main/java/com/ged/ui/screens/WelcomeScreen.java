package com.ged.ui.screens;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.ged.ui.MainWindow;
import com.ged.ui.controllers.WelcomeScreenController;
import com.ged.ui.widgets.SimpleButton;


/**
 * The welcome screen contains buttons for main useful actions
 */
public class WelcomeScreen extends SoftwareScreen {
	
	
	/**
	 * All buttons in this screen have the same size, so they are WelcomeScreenButton
	 */
	public class WelcomeScreenButton extends SimpleButton {

		private static final long serialVersionUID = 1L;
		
		public WelcomeScreenButton(String label, WelcomeScreenController controller) {
			super(label);
			
			setIconSize(new Dimension(50, 50));
			setMaximumSize(new Dimension(200, 140));
			setPreferredSize(new Dimension(200, 140));
			
			addActionListener(controller);
		}
	}
	
	
	private static final long serialVersionUID = 1L;

	/**
	 * Button for browsing document library
	 */
	private SimpleButton btnBrowse; 
	
	/**
	 * Button for adding document in library
	 */
	private SimpleButton btnAdd;
	
	/**
	 * Button for searching document in library
	 */
	private SimpleButton btnSearch;
	
	/**
	 * Button for plugin management
	 */
	private SimpleButton btnPluginsManagement;
	
	
	public WelcomeScreen(MainWindow mw) {
		
		super(mw);
		instantiateWidgets();
		
		MigLayout layout = new MigLayout(	
				"wrap",
				"[grow,center][grow,center]",
				"[grow,center][grow,center]"
			);
		
		JPanel container = new JPanel(layout);

		// add buttons in main layout
		container.add(btnBrowse, 			"c");
		container.add(btnSearch, 			"c");
		container.add(btnAdd   , 			"c");
		container.add(btnPluginsManagement, "c");

		// main layout
        add(container);
	}
	
	
	/**
	 * Instantiate attributes (widgets), according to application settings (language... (for future releases !))
	 */
	private void instantiateWidgets() {
		
		WelcomeScreenController controller = new WelcomeScreenController(this);
		
		btnBrowse  				= new WelcomeScreenButton(properties.getProperty("goto_browse"), controller);
		btnAdd     				= new WelcomeScreenButton(properties.getProperty("goto_add_document"), controller);
		btnSearch  				= new WelcomeScreenButton(properties.getProperty("goto_search"), controller);
		btnPluginsManagement	= new WelcomeScreenButton(properties.getProperty("goto_plugin_management"), controller);
		
		// define associated pictures
		Map<SimpleButton, String> associatedImages = new HashMap<SimpleButton, String>();
		associatedImages.put(btnBrowse,				properties.getProperty("ico_library"));
		associatedImages.put(btnAdd,				properties.getProperty("ico_add_doc"));
		associatedImages.put(btnSearch,				properties.getProperty("ico_search"));
		associatedImages.put(btnPluginsManagement,  properties.getProperty("ico_plugin_management"));

		// set pictures
		for (Map.Entry<SimpleButton, String> e : associatedImages.entrySet()) {
			e.getKey().setIcon(e.getValue());
		}
	}


	/**
	 * @return the btnBrowse
	 */
	public SimpleButton getBtnBrowse() {
		return btnBrowse;
	}

	/**
	 * @return the btnAdd
	 */
	public SimpleButton getBtnAdd() {
		return btnAdd;
	}

	/**
	 * @return the btnSearch
	 */
	public SimpleButton getBtnSearch() {
		return btnSearch;
	}
	
	/**
	 * @return the btnPluginsManagement
	 */
	public SimpleButton getBtnPluginsManagement() {
		return btnPluginsManagement;
	}
}
