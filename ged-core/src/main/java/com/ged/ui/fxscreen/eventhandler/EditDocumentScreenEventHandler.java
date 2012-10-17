package com.ged.ui.fxscreen.eventhandler;

import java.lang.ref.WeakReference;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.apache.log4j.Logger;

import com.ged.ui.fxscreen.EditDocumentScreen;
import com.tools.PropertiesHelper;
import com.tools.javafx.ModalConfirm;
import com.tools.javafx.ModalConfirmResponse;

/**
 * The event handler for EditDocumentScreenEventHandler
 * 
 * @author xavier
 *
 */
public class EditDocumentScreenEventHandler implements EventHandler<ActionEvent> {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(EditDocumentScreenEventHandler.class);
	
	/**
	 * Properties
	 */
	private static final Properties properties = PropertiesHelper.getInstance().getProperties();
	
	/**
	 * The handled screen
	 */
	private WeakReference<EditDocumentScreen> editDocumentScreen;

	
	public EditDocumentScreenEventHandler(EditDocumentScreen editDocumentScreen) {
		this.editDocumentScreen = new WeakReference<>(editDocumentScreen);
	}


	@Override
	public void handle(ActionEvent arg0) {
		
		if (arg0.getSource() == editDocumentScreen.get().getBtnBack()) {
			
        	ModalConfirm.show(editDocumentScreen.get().getMainStage(), new ModalConfirmResponse() {
    			@Override
    			public void confirm() {
    				editDocumentScreen.get().finish();
    			}
    			@Override
    			public void cancel() {
    				// do nothing
    			}
    		}, properties.getProperty("stop_edition"));
		
		} else {
			logger.warn("Not implemented yet, see AddDocumentScreenEventHandler.handle");
		}
		
	}

}
