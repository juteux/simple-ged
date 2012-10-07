package com.ged.ui.fxpreviewwidgets;

import java.util.Properties;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.tools.PropertiesHelper;

/**
 * Not really a previewer, I know...
 * 
 * @author xavier
 *
 */
public class AddDocumentPreviewer extends AbstractFilePreviewer {

	public AddDocumentPreviewer() {
		super("");
		
		try {
			this.load();
		} catch (CannotCreatePreviewerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load() throws CannotCreatePreviewerException {
		
		Properties properties = PropertiesHelper.getInstance().getProperties();
		
		Button b = new Button(properties.getProperty("goto_add_document"));
		
		Image i = new Image(getClass().getResourceAsStream(properties.getProperty("ico_add_doc")));
		ImageView iv = new ImageView(i);
		iv.setSmooth(true);
		iv.setFitWidth(50);
		iv.setFitHeight(50);
		b.setGraphic(iv);

		b.setPrefSize(200, 120);
		
		getChildren().add(b);
	}

	@Override
	public boolean isPrintable() {
		return false;
	}

}
