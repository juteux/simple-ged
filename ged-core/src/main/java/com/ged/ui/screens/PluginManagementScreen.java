package com.ged.ui.screens;

import java.awt.Component;
import java.awt.Dimension;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;

import com.ged.ui.MainWindow;
import com.ged.ui.controllers.PluginManagementScreenController;
import com.ged.ui.widgets.SimpleButton;

/**
 * This is the screen for plugin management
 * 
 * @author xavier
 *
 */
public class PluginManagementScreen extends SoftwareScreen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * This screen buttons model
	 */
	public class PluginManagementScreenButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public PluginManagementScreenButton(String label, PluginManagementScreenController controller) {
			super(label);
			
			setIconSize(new Dimension(30, 30));
			setPreferredSize(new Dimension(200, 40));
			
			addActionListener(controller);
		}
	}
	
	
	/**
	 * Table of plugins model
	 */
	private DefaultTableModel tablePluginModel;
	
	/**
	 * Table of plugins 
	 */
	private JTable tablePlugin;
	
	/**
	 * A button to return to main screen
	 */
	private SimpleButton btnReturn;
	
	/**
	 * A button for opening plugins directory
	 */
	private SimpleButton btnOpenPluginDirectory;
	
	/**
	 * A button for refreshing plugins list
	 */
	private SimpleButton btnRefresh;
	
	/**
	 * My controller
	 */
	private WeakReference<PluginManagementScreenController> controller;
	
	
	public PluginManagementScreen(MainWindow mw) {
		super(mw);

		instantiateWidgets();
		
		// table results 		
		JScrollPane tablePluginScrollPane = new JScrollPane(tablePlugin);
		tablePlugin.setFillsViewportHeight(true);
		
		// button area
		JPanel btnActionPanel = new JPanel();
		btnActionPanel.add(btnReturn);
		btnActionPanel.add(btnOpenPluginDirectory);
		btnActionPanel.add(btnRefresh);
		
		// main layout
		MigLayout layout = new MigLayout(	
				"wrap",
				"[grow,fill,center]",						// columns
				"[grow 80,fill,center][grow 20,fill,center]"	// rows
			);
	
		JPanel container = new JPanel(layout);
		
		container.add(tablePluginScrollPane);
		container.add(btnActionPanel);
		
		add(container);
	}

	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		controller = new WeakReference<PluginManagementScreenController>(new PluginManagementScreenController(this));
		
		btnReturn = new PluginManagementScreenButton(properties.getProperty("back"), controller.get());
		btnOpenPluginDirectory = new PluginManagementScreenButton(properties.getProperty("open_plugin_directory"), controller.get());
		btnRefresh = new PluginManagementScreenButton(properties.getProperty("list_refresh"), controller.get());
		
		String[] columnNames = {
				properties.getProperty("plugin_name"),
                properties.getProperty("plugin_description"),
                properties.getProperty("plugin_activated")
        };
		
		tablePluginModel = new DefaultTableModel(columnNames, 0) {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -7790866228028051683L;

			@Override
		    public boolean isCellEditable(int rowIndex, int mColIndex) {
				return (getValueAt(0, mColIndex) instanceof Boolean);
		    }
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Class getColumnClass(int col) {
				return ((Vector)getDataVector().elementAt(0)).elementAt(col).getClass();
			}
		};
		
		tablePlugin = new JTable(tablePluginModel);

		tablePlugin.setRowHeight(150);
		tablePluginModel.addTableModelListener(controller.get());
		
		tablePlugin.setDefaultRenderer(JLabel.class, new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				if (value instanceof JLabel){
					((JLabel) value).setHorizontalAlignment(JLabel.CENTER);
					return (JLabel) value;
				}
				return null;
			}
		});
				
		

		// define associated pictures
		Map<SimpleButton, String> associatedImages = new HashMap<SimpleButton, String>();
		associatedImages.put(btnReturn, 				properties.getProperty("ico_back"));
		associatedImages.put(btnOpenPluginDirectory, 	properties.getProperty("ico_open_dir"));
		associatedImages.put(btnRefresh, 				properties.getProperty("ico_refresh"));
		
		// set pictures
		for (Map.Entry<SimpleButton, String> e : associatedImages.entrySet()) {
			e.getKey().setIcon(e.getValue());
		}
	
		controller.get().fillPluginTable();
	}


	@Override
	public void refresh() {
		controller.get().fillPluginTable();
	}


	public DefaultTableModel getTablePluginModel() {
		return tablePluginModel;
	}

	public JTable getTablePlugin() {
		return tablePlugin;
	}

	public SimpleButton getBtnReturn() {
		return btnReturn;
	}

	public SimpleButton getBtnOpenPluginDirectory() {
		return btnOpenPluginDirectory;
	}

	public SimpleButton getBtnRefresh() {
		return btnRefresh;
	}
	
}

