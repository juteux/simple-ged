package com.ged.ui.fxscreen.eventhandler;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ged.models.GedPlugin;
import com.ged.services.PluginService;
import com.ged.ui.fxscreen.FxSoftwareScreen.Screen;
import com.ged.ui.fxscreen.PluginScreen;
import com.tools.PropertiesHelper;
import com.tools.javafx.ModalConfirm;
import com.tools.javafx.ModalConfirmResponse;

/**
 * 
 * This class catch and handle plugin screen events
 * 
 * @author xavier
 *
 */
public class PluginScreenEventHandler {

	/**
	 * Action type
	 * 
	 * @author xavier
	 *
	 */
	public enum Action {
		ACTIVATE,
		DESACTIVATE,
		MODIFY
	}

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(PluginScreenEventHandler.class);
	
	/**
	 * Properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	/**
	 * The watched screen
	 */
	private WeakReference<PluginScreen> pluginScreen;
	
	
	public PluginScreenEventHandler(PluginScreen pluginScreen) {
		this.pluginScreen = new WeakReference<>(pluginScreen);
	}
	
	
	/**
	 * 
	 * Call when the user clicked on some button (activate, desactivate, modify)
	 * 
	 * @param a
	 * 			The action nested by the user
	 * 
	 * @param pmi
	 * 			The concerned plugin
	 */
	public void pluginActionRequired(Action a, final GedPlugin pmi) {
		
		switch (a) {
		
		case ACTIVATE :
		case MODIFY :
			
			pluginScreen.get().pushScreen(Screen.PLUGIN_CONFIGURATION_SCREEN);
			
			Map<String, Object> extras = new HashMap<>();
			extras.put("ged-plugin", pmi);
			
			pluginScreen.get().pushExtraValues(extras);
			
			break;
			
		case DESACTIVATE :
			
			ModalConfirm.show(pluginScreen.get().getMainStage(), new ModalConfirmResponse() {
    			@Override
    			public void confirm() {
    				PluginService.desactivatePlugin(pmi);
    				pluginScreen.get().refreshPluginListContent();
    			}
    			@Override
    			public void cancel() {
    				// do nothing
    			}
    		}, properties.getProperty("wanna_unactivate_item_named").replace("{0}", pmi.getPlugin().getPluginName()));

			break;
			
		default :
			logger.error("Unknown action");
			break;
		}
		
	}

	
}

