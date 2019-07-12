package com.eStudent.gui;

import java.io.File;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;

import com.eStudent.courses.Exam;
import com.eStudent.user.UserType;
import com.eStudent.user.professor.ProfessorFunctionality;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AssessExam extends ProfessorFunctionality {

	private static Label recordBookNumberLabel = new Label();
	private static Label courseNameLabel = new Label();
	private static Label pointsLabel = new Label("Points");
	private static Label titleLabel = new Label("Exam Assessment");

	private static Spinner<Integer> points = new Spinner<>(0, 100, 0);

	private static Button confirmButton = new Button();

	private static JSONObject recordBook;

	private static GridPane layout;
	private static Stage window;

	public static void display(Exam exam) {
		window = new Stage();
		window.getIcons().add(new Image("com/eStudent/gui/icons/icon.png"));
		window.setResizable(false);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Exam Assessment");

		titleLabel.setId("title");
		titleLabel.setPadding(new Insets(0, 0, 10, 0));

		recordBookNumberLabel.setText(String.format("Record Book Number:  %s", exam.getRecordBookNumber()));
		courseNameLabel.setText(String.format("Course Name:  %s", exam.getCourse()));

		pointsLabel.setPadding(new Insets(0, -200, 0, 0));
		points.setMaxWidth(60);

		confirmButton = new Button("Confirm");
		confirmButton.setOnAction(e -> Assess(exam));

		layout = new GridPane();
		layout.getChildren().clear();
		layout.getChildren().addAll(titleLabel, recordBookNumberLabel, courseNameLabel, pointsLabel, points,
				confirmButton);
		layout.setAlignment(Pos.CENTER);
		layout.setVgap(10);

		GridPane.setConstraints(titleLabel, 0, 0);
		GridPane.setConstraints(recordBookNumberLabel, 0, 1);
		GridPane.setConstraints(courseNameLabel, 0, 2);
		GridPane.setConstraints(pointsLabel, 0, 3);
		GridPane.setConstraints(points, 0, 4);
		GridPane.setConstraints(confirmButton, 0, 5);

		Scene scene = new Scene(layout, 350, 270);
		scene.getStylesheets().add("com/eStudent/gui/css/dialog.css");
		window.setScene(scene);
		window.showAndWait();

	}

	private static void Assess(Exam selected) {
		JSONArray exams = new JSONArray();

		for (Object obj : users) {
			JSONObject o = (JSONObject) obj;
			if (UserType.valueOf(o.optString("userType")).equals(UserType.STUDENT)) {
				recordBook = o.getJSONObject("recordBook");
				exams.put(recordBook.getJSONArray("exams"));
			}

		}

		double grade = 0.0;

		/*
		 * Grading System - grade gets assigned accordingly to the amount of points a
		 * student receives
		 */
		if (points.getValue() >= 51 && points.getValue() < 61)
			grade = 6.0;
		else if (points.getValue() >= 61 && points.getValue() < 71)
			grade = 7.0;
		else if (points.getValue() >= 71 && points.getValue() < 81)
			grade = 8.0;
		else if (points.getValue() >= 81 && points.getValue() < 91)
			grade = 9.0;
		else if (points.getValue() >= 91 && points.getValue() <= 100)
			grade = 10.0;
		else
			grade = 5.0;

		for (Object obj : exams) {
			JSONArray o = (JSONArray) obj;
			for (Object x : o) {
				JSONObject exam = (JSONObject) x;

				if (exam.optBoolean("annulled"))
					continue;
				if (exam.isEmpty())
					continue;
				if (exam.getDouble("grade") >= 6.0)
					continue;

				if (selected.getRecordBookNumber().equals(exam.getString("recordBookID"))
						&& selected.getCourse().equals(exam.getString("name")) && !exam.getBoolean("annulled")
						&& exam.getDouble("grade") != 5) {
					exam.put("examDate", String.valueOf(LocalDate.now()));
					exam.put("points", points.getValue());
					exam.put("grade", grade);
					break;
				}
			}
		}

		writeData(new File(PATH_TO_USERS), users);
		points.getValueFactory().setValue(0);
		dataLoader();
		Alert success = new Alert(AlertType.INFORMATION);
		success.setHeaderText("Exam successfully assessed!");
		success.setTitle("Successful Assessment");
		window.close();
		success.show();
	}
}
