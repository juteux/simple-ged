package com.tools.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Demonstrates a modal confirm box in JavaFX. Dialog is rendered upon a blurred
 * background. Dialog is translucent.
 * 
 * https://gist.github.com/1887631
 * 
 * Modified by Xavier
 */
public class ModalConfirm {

	// --
	
	public static ModalConfirm show(Stage parent, ModalConfirmResponse response, String text) {
		ModalConfirm mc = new ModalConfirm(parent, response, text);
		mc.showDialog();
		return mc;
	}
	
	// --
	
	
	private Stage parent;
	
	private ModalConfirmResponse response;
	
	private String text;
	
	private ModalConfirm(Stage parent, ModalConfirmResponse response, String text) {
		this.parent = parent;
		this.response = response;
		this.text = text;
	}
	
	public void showDialog() {

		// initialize the confirmation dialog
		final Stage dialog = new Stage(StageStyle.TRANSPARENT);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(parent);
		dialog.setScene(
				new Scene(VBoxBuilder.create().styleClass("modal-dialog").children(
					LabelBuilder.create().text(text).build(), 
					HBoxBuilder.create().alignment(Pos.CENTER_RIGHT).children(
							ButtonBuilder.create().text("Oui").defaultButton(true).onAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent actionEvent) {
									response.confirm();
									dialog.close();
								}
							}).build(), ButtonBuilder.create().text("Non").cancelButton(true).onAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent actionEvent) {
									response.cancel();
									dialog.close();
							}
		}).build()).build()).build(), Color.TRANSPARENT));
		
		dialog.getScene().getStylesheets().add("templates/modal-dialog.css");

		// allow the dialog to be dragged around.
		final Node root = dialog.getScene().getRoot();
		root.getStyleClass().add("modal-dialog-root");
		
		final Delta dragDelta = new Delta();
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// record a delta distance for the drag and drop operation.
				dragDelta.x = dialog.getX() - mouseEvent.getScreenX();
				dragDelta.y = dialog.getY() - mouseEvent.getScreenY();
			}
		});
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				dialog.setX(mouseEvent.getScreenX() + dragDelta.x);
				dialog.setY(mouseEvent.getScreenY() + dragDelta.y);
			}
		});

		
		dialog.show();
	}

	// records relative x and y co-ordinates.
	class Delta {
		double x, y;
	}
}
