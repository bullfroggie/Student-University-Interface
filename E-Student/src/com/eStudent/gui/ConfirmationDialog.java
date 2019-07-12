package com.eStudent.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationDialog {

	static boolean choice;

	public static boolean displayConfirmationDialog(String title, String message) {

		Stage window = new Stage();

		window.getIcons().add(new Image("com/eStudent/gui/icons/icon.png"));
		window.setResizable(false);

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);

		Label info = new Label();
		info.setId("info");
		info.setText(message);

		Button yes = new Button("Yes");
		Button no = new Button("No");

		yes.setOnAction(e -> {
			choice = true;
			window.close();
		});

		no.setOnAction(e -> {
			choice = false;
			window.close();
		});

		VBox layout = new VBox(10);
		layout.getChildren().addAll(info, yes, no);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout, 300, 200);
		scene.getStylesheets().add("com/eStudent/gui/css/dialog.css");
		window.setScene(scene);

		window.showAndWait();

		return choice;
	}
}
