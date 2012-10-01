package com.ged.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.ged.Constants;
import com.ged.ui.widgets.SimpleButton;
import com.managers.ImageManager;
import com.tools.PropertiesHelper;

/**
 * The about popup window
 * 
 * @author xavier
 *
 */
public class AboutPopup extends JFrame {

	
	/**
	 * This screen buttons model
	 */
	public class AboutPopupButton extends SimpleButton {
		
		private static final long serialVersionUID = 1L;
		
		public AboutPopupButton(String label) {
			super(label);
			
			setIconSize(new Dimension(30, 30));
			setPreferredSize(new Dimension(200, 40));
		}
	}
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Label software name + version
	 */
	private JLabel labelNameAndVersion;
	
	/**
	 * Label about text
	 */
	private JLabel labelAboutText;
	
	/**
	 * Software logo
	 */
	private Image imgLogo;
	
	/**
	 * Button close
	 */
	AboutPopupButton btnClose;
	
	
	protected Properties properties;
	
	
	
	public AboutPopup() {
		super();
		
		properties = PropertiesHelper.getInstance().getProperties();
		
		instantiateWidgets();
		
	    setTitle("A propos de " + Constants.applicationName);
	    setSize(550,650);
	    setResizable(false);
	    setIconImage(imgLogo);
	    setDefaultLookAndFeelDecorated(true);
	    setLocationRelativeTo(this.getParent());
	 
	    MigLayout mainLayout = new MigLayout(
	    		"wrap",
				"[grow]",
				"[grow]"
		);
	    
		JPanel rootPanel = new JPanel(mainLayout);

		rootPanel.add(labelNameAndVersion, "center");
		
		rootPanel.add(new JLabel("<html><hr></html>"));

		rootPanel.add(labelAboutText);
		
		rootPanel.add(btnClose, "center");
		
		setContentPane(rootPanel);
		
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	
	/**
	 * Instantiate attributes (widgets)
	 */
	private void instantiateWidgets() {
		
		imgLogo = ImageManager.getImage(properties.getProperty("ico_ico"));
		
		labelNameAndVersion = new JLabel();
		labelNameAndVersion.setText("<html><b>" + Constants.applicationName + "</b> - <i>version " + Constants.applicationVersion + "</i></html>");
		labelNameAndVersion.setIcon(new ImageIcon(imgLogo.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		
		labelAboutText = new JLabel();
		labelAboutText.setText(properties.getProperty("about_content"));
			
		btnClose = new AboutPopupButton("Fermer");
		
		// define associated pictures
		Map<SimpleButton, String> associatedImages = new HashMap<SimpleButton, String>();
		associatedImages.put(btnClose, 	properties.getProperty("ico_back"));

		// set pictures
		for (Map.Entry<SimpleButton, String> e : associatedImages.entrySet()) {
			e.getKey().setIcon(e.getValue());
		}
	}
	
}
