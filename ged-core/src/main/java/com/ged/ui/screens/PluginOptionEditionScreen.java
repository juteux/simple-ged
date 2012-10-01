package com.ged.ui.screens;

import java.awt.Dimension;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.ged.connector.plugins.SimpleGedPlugin;
import com.ged.connector.plugins.SimpleGedPluginProperty;
import com.ged.ui.MainWindow;
import com.ged.ui.controllers.PluginOptionEditionScreenController;
import com.ged.ui.widgets.LibraryView;
import com.ged.ui.widgets.SimpleButton;
import com.tools.DateHelper;

/**
 * This screen is for fixing some plugin options
 * 
 * @author xavier
 *
 */
public class PluginOptionEditionScreen extends SoftwareScreen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This screen buttons model
	 */
	public class PluginOptionEdtionScreenButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public PluginOptionEdtionScreenButton(String label, PluginOptionEditionScreenController controller) {
			super(label);
			
			setIconSize(new Dimension(30, 30));
			setPreferredSize(new Dimension(180, 50));
			
			addActionListener(controller);
		}
	}
	
	/**
	 * Button return
	 */
	private SimpleButton btnReturn;
	
	/**
	 * Button save
	 */
	private SimpleButton btnSave;

	/**
	 * Plugin presentation
	 */
	private JLabel pluginTitle;
	
	/**
	 * Plugin description
	 */
	private JLabel pluginDescription;
	
	/**
	 * Library view for plugin's downloaded file target
	 */
	private LibraryView libraryView;
	
	/**
	 * The destination file name pattern
	 */
	private JTextField fieldNamePattern;
	
	/**
	 * Day of month for update
	 */
	private JComboBox comboDayOfMonthForUpdate;
	
	/**
	 * Interval between update (in month)
	 */
	private JComboBox comboIntervalBetweenUpdateInMonth;
	
	/**
	 * Map of properties
	 */
	private Map<SimpleGedPluginProperty, JTextField> propertiesFieldsMap;
	
	/**
	 * Fields properties componment
	 */
	private JPanel propertiesInputArea;
	
	/**
	 * My controller
	 */
	private WeakReference<PluginOptionEditionScreenController> controller;
	
	/**
	 * The plugin file name
	 */
	private String pluginFileName;
	
	
	public PluginOptionEditionScreen(MainWindow mw) {
		super(mw);
		
		instantiateWidgets();
		
		// buttons panel
		JPanel btnPanel = new JPanel();
		btnPanel.add(btnReturn);
		btnPanel.add(btnSave);
		
		// main layout
		MigLayout layout = new MigLayout(	
				"wrap",
				"[grow 20,fill,center][grow 80,fill,center]",						// columns
				"[grow 20,fill,center][grow 50,fill,center][grow 30,fill,center]"	// rows
			);
		
		JPanel container = new JPanel(layout);

		// add widget in main layout
		container.add(pluginTitle);
		container.add(pluginDescription);
		
		container.add(libraryView);
		container.add(propertiesInputArea);
		
		container.add(btnPanel, "span 2");
		
		// main layout
		add(container);
	}


	@Override
	public void receiveExtraValue(Map<String, Object> extra) {

		SimpleGedPlugin p = (SimpleGedPlugin)extra.get("plugin");

		this.pluginFileName = p.getJarFileName();
		
		pluginTitle.setText(
				"<html>" +
						"<center><h2>" + p.getPluginName() + "</h2></center>" + 
						properties.getProperty("version") + p.getPluginVersion() + 
						properties.getProperty("released_the") + DateHelper.calendarToString(p.getPluginDate()) + "<br/>" +
						properties.getProperty("by") + p.getPluginAuthor() +
						"<br/><center><i>(" + p.getJarFileName() + ")</i></center>" 
					+ "</html>"
		);
		
		pluginDescription.setText(
				"<html>" +
						p.getPluginDescription()
					+ "</html>"	
		);
		
		for (SimpleGedPluginProperty property : p.getProperties()) {
			
			JTextField field = new JTextField();
			if (property.isHidden()) {
				field = new JPasswordField();
			}
		
			field.addKeyListener(controller.get());
			
			propertiesFieldsMap.put(property, field);
			
			propertiesInputArea.add(new JLabel(property.getPropertyLabel()), "gap para");
			propertiesInputArea.add(field, "span,growx,wrap");
		}
	}
	
	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {

		controller = new WeakReference<PluginOptionEditionScreenController>(new PluginOptionEditionScreenController(this));

		propertiesFieldsMap = new HashMap<SimpleGedPluginProperty, JTextField>();
		propertiesInputArea = new JPanel(new MigLayout());
		
		btnReturn	= new PluginOptionEdtionScreenButton("Retour", controller.get());
		btnSave		= new PluginOptionEdtionScreenButton("Sauvegarder", controller.get());
	
		btnSave.setEnabled(false);
	
		pluginTitle 		= new JLabel();
		pluginDescription 	= new JLabel();
		
		libraryView = new LibraryView(true, this);
		libraryView.getController().addLibraryListener(controller.get());	
		
		fieldNamePattern = new JTextField();
		fieldNamePattern.addKeyListener(controller.get());
		
		Vector<Integer> vDays = new Vector<Integer>();
		for (int i = 1; i <= 30; ++i) {
			vDays.add(i);
		}
		comboDayOfMonthForUpdate = new JComboBox(vDays);
		
		Vector<Integer> vMonth = new Vector<Integer>();
		for (int i = 1; i <= 12; ++i) {
			vMonth.add(i);
		}
		comboIntervalBetweenUpdateInMonth = new JComboBox(vMonth);
		
		propertiesInputArea.add(new JLabel(properties.getProperty("plugin_option_user_guide")), "span 2,growx,wrap");
		
		propertiesInputArea.add(new JLabel(properties.getProperty("destination_file_name")), "gap para");
		propertiesInputArea.add(fieldNamePattern, "span,growx,wrap");

		propertiesInputArea.add(new JLabel(properties.getProperty("day_of_getting")), "gap para");
		propertiesInputArea.add(comboDayOfMonthForUpdate, "span,growx,wrap");

		propertiesInputArea.add(new JLabel(properties.getProperty("time_between")), "gap para");
		propertiesInputArea.add(new JScrollPane(comboIntervalBetweenUpdateInMonth), "span,growx,wrap");
		
		propertiesInputArea.add(new JLabel("<html><br/></html>"), "span 2,growx,wrap");
		
		// define associated pictures
		Map<SimpleButton, String> associatedImages = new HashMap<SimpleButton, String>();
		associatedImages.put(btnSave,					properties.getProperty("ico_save"));
		associatedImages.put(btnReturn, 				properties.getProperty("ico_back"));
		
		// set pictures
		for (Map.Entry<SimpleButton, String> e : associatedImages.entrySet()) {
			e.getKey().setIcon(e.getValue());
		}
	}


	
	public SimpleButton getBtnReturn() {
		return btnReturn;
	}

	public SimpleButton getBtnSave() {
		return btnSave;
	}

	public LibraryView getLibraryView() {
		return libraryView;
	}

	public JTextField getFieldNamePattern() {
		return fieldNamePattern;
	}

	public JComboBox getComboDayOfMonthForUpdate() {
		return comboDayOfMonthForUpdate;
	}
	
	public JComboBox getComboIntervalBetweenUpdateInMonth() {
		return comboIntervalBetweenUpdateInMonth;
	}

	public Map<SimpleGedPluginProperty, JTextField> getPropertiesFieldsMap() {
		return propertiesFieldsMap;
	}

	public String getPluginFileName() {
		return pluginFileName;
	}
}
