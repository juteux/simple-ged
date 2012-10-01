package com.tools.ui;


import java.util.Properties;

import javax.swing.JPanel;

import com.tools.PropertiesHelper;

import net.miginfocom.swing.MigLayout;


/**
 * Abstract widget, define a main layout which take all place available 
 * @author xavier
 *
 */
public abstract class AbstractWidget extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Properties properties;
	
	
	public AbstractWidget() {
		super(
				new MigLayout(	
						"wrap",
						"[grow,fill,center]",
						"[grow,fill,center]"
					)
		);
		
		properties = PropertiesHelper.getInstance().getProperties();
	}
}


