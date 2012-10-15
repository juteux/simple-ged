package com.ged.ui.fxscreen.eventhandler;

import java.io.File;
import java.lang.ref.WeakReference;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;

import org.apache.log4j.Logger;

import com.asprise.util.jtwain.Source;
import com.asprise.util.jtwain.SourceManager;
import com.ged.ui.fxscreen.AddDocumentScreen;
import com.ged.ui.listeners.DocumentInfoEditorListener;
import com.ged.ui.listeners.DocumentPreviewListener;
import com.tools.DateTokenGetter;
import com.tools.OSHelper;

public class AddDocumentScreenEventHandler implements DocumentInfoEditorListener, DocumentPreviewListener, EventHandler<ActionEvent> {

	/**
	 * My logger
	 */
	private static final Logger logger = Logger.getLogger(AddDocumentScreenEventHandler.class);

	/**
	 * The watched screen
	 */
	private WeakReference<AddDocumentScreen> addDocumentScreen;

	public AddDocumentScreenEventHandler(AddDocumentScreen addDocumentScreen) {
		this.addDocumentScreen = new WeakReference<>(addDocumentScreen);
	}

	/**
	 * Check if currents values are valid, if true, the save button is available
	 */
	public void checkValidity() {

		boolean isValid = true;

		if (!addDocumentScreen.get().getDocInfoEditor().getEventHandler().isValid()) {
			isValid = false;
		}

		if (addDocumentScreen.get().getDocumentPreviewer().getEventHandler().isFileListEmpty()) {
			isValid = false;
		}

		addDocumentScreen.get().getBtnSubmit().setDisable(!isValid);
	}

	/**
	 * @see com.ged.ui.listeners.DocumentInfoEditorListener#selectionChanged(boolean)
	 */
	@Override
	public void selectionChanged(boolean isValid) {
		checkValidity();
	}

	/**
	 * @see com.ged.ui.listeners.DocumentPreviewListener#fileCountChanged(int)
	 */
	@Override
	public void fileCountChanged(int newFileCount) {
		checkValidity();
	}

	/**
	 * @see javafx.event.EventHandler#handle(javafx.event.Event)
	 */
	@Override
	public void handle(ActionEvent arg0) {

		if (arg0.getSource() == addDocumentScreen.get().getBtnAddFromFS()) {
			FileChooser fileChooser = new FileChooser();

			// Show open file dialog
			File file = fileChooser.showOpenDialog(null);

			addDocumentScreen.get().getDocumentPreviewer().addFile(file);
		} else if (arg0.getSource() == addDocumentScreen.get().getBtnAddFromScanner()) {

			if (OSHelper.isWindowsOs()) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Source source = SourceManager.instance().getDefaultSource();
							source.open();
							source.acquireImage();

							File f = File.createTempFile(DateTokenGetter.getToken(), ".jpg");
							source.saveLastAcquiredImageIntoFile(f);
							addDocumentScreen.get().getDocumentPreviewer().addFile(f);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							SourceManager.closeSourceManager();
						}
					}
				});
				t.start();
			}

		} else if (arg0.getSource() == addDocumentScreen.get().getBtnSubmit()) {

		} else {
			logger.warn("Not implemented yet, see AddDocumentScreenEventHandler.handle");
		}
	}

}
