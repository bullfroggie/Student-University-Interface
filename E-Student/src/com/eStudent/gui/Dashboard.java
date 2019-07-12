package com.eStudent.gui;

import org.json.JSONArray;

import com.eStudent.user.UserType;
import com.eStudent.user.admin.AdminFunctionality;
import com.eStudent.user.professor.ProfessorFunctionality;
import com.eStudent.user.student.StudentFunctionality;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class Dashboard extends Main {

	public static AnchorPane layout;
	private static Pane rightPane;
	public static VBox rightTableView;
	public static HBox rightRecordBookView;

	protected static JSONArray loadedUsers = users;
	protected static JSONArray loadedCourses = courses;

	public static Scene display(UserType userType, Button logOut) {
		Button button1 = new Button();
		Button button2 = new Button();
		Button button3 = new Button();
		Button button4 = new Button();
		Button button5 = new Button();

		Separator separator = new Separator();
		separator.getStyleClass().add("separator");

		VBox leftMenu = new VBox();
		leftMenu.setId("leftMenu");
		leftMenu.setAlignment(Pos.CENTER_LEFT);
		leftMenu.setPrefSize(230, 550);
		leftMenu.getChildren().addAll(button1, button2, button3, button4, button5, separator, logOut);

		layout = new AnchorPane();
		AnchorPane.setLeftAnchor(leftMenu, 0.0);

		dataLoader();

		/*
		 * creates a left menu in correlation with the person that is logged in
		 */
		switch (userType) {
		case STUDENT:
			button1.setText("View Record Book");
			button1.setPrefSize(230, 40);
			button1.setTextAlignment(TextAlignment.CENTER);
			button1.setOnAction(e -> {
				layout.getChildren().removeAll(rightPane, rightTableView, rightRecordBookView);
				rightRecordBookView = StudentFunctionality.display();
				AnchorPane.setRightAnchor(rightRecordBookView, 0.0);
				AnchorPane.setTopAnchor(rightRecordBookView, 0.0);
				layout.setStyle("-fx-background-image: null;");
				layout.getChildren().add(rightRecordBookView);
			});

			button2.setText("View Passed Exams");
			button2.setPrefSize(230, 40);
			button2.setTextAlignment(TextAlignment.CENTER);
			button2.setOnAction(e -> {
				layout.getChildren().removeAll(rightPane, rightTableView, rightRecordBookView);
				rightTableView = StudentFunctionality.displayPassedExams();
				AnchorPane.setRightAnchor(rightTableView, 0.0);
				AnchorPane.setTopAnchor(rightTableView, 0.0);
				rightTableView.setPrefHeight(550);
				rightTableView.setPrefWidth(620);
				layout.setStyle("-fx-background-image: null;");
				layout.getChildren().add(rightTableView);
			});

			leftMenu.getChildren().removeAll(button3, button4, button5);
			break;

		case PROFESSOR:
			button1.setText("Exam Assessment");
			button1.setPrefSize(230, 40);
			button1.setTextAlignment(TextAlignment.CENTER);
			button1.setOnAction(e -> {
				layout.getChildren().removeAll(rightPane, rightTableView);
				rightTableView = ProfessorFunctionality.examAssessment();
				AnchorPane.setRightAnchor(rightTableView, 0.0);
				AnchorPane.setTopAnchor(rightTableView, 0.0);
				rightTableView.setPrefHeight(550);
				rightTableView.setPrefWidth(620);
				layout.setStyle("-fx-background-image: null;");
				layout.getChildren().add(rightTableView);
			});

			button2.setText("Student Search");
			button2.setPrefSize(230, 40);
			button2.setTextAlignment(TextAlignment.CENTER);
			button2.setOnAction(e -> {
				layout.getChildren().removeAll(rightPane, rightTableView);
				rightTableView = ProfessorFunctionality.studentSearch();
				AnchorPane.setRightAnchor(rightTableView, 0.0);
				AnchorPane.setTopAnchor(rightTableView, 0.0);
				rightTableView.setPrefHeight(550);
				rightTableView.setPrefWidth(620);
				layout.setStyle("-fx-background-image: null;");
				layout.getChildren().add(rightTableView);
			});

			leftMenu.getChildren().removeAll(button3, button4, button5);
			break;

		case ADMIN:
			button1.setText("User Registration");
			button1.setPrefSize(230, 40);
			button1.setTextAlignment(TextAlignment.CENTER);
			button1.setOnAction(e -> {
				layout.getChildren().removeAll(rightPane, rightTableView);
				rightPane = AdminFunctionality.userRegistrationPane();
				AnchorPane.setRightAnchor(rightPane, 0.0);
				AnchorPane.setTopAnchor(rightPane, 0.0);
				layout.setStyle("-fx-background-image: null;");
				layout.getChildren().add(rightPane);
			});

			button2.setText("User Deletion");
			button2.setPrefSize(230, 40);
			button2.setTextAlignment(TextAlignment.CENTER);
			button2.setOnAction(e -> {
				layout.getChildren().removeAll(rightPane, rightTableView);
				rightTableView = AdminFunctionality.userDeletionPane();
				AnchorPane.setRightAnchor(rightTableView, 0.0);
				AnchorPane.setTopAnchor(rightTableView, 0.0);
				rightTableView.setPrefHeight(550);
				rightTableView.setPrefWidth(620);
				layout.setStyle("-fx-background-image: null;");
				layout.getChildren().add(rightTableView);
			});

			button3.setText("Edit User");
			button3.setPrefSize(230, 40);
			button3.setTextAlignment(TextAlignment.CENTER);
			button3.setOnAction(e -> {
				layout.getChildren().removeAll(rightPane, rightTableView);
				rightTableView = AdminFunctionality.userEditPane();
				AnchorPane.setRightAnchor(rightTableView, 0.0);
				AnchorPane.setTopAnchor(rightTableView, 0.0);
				rightTableView.setPrefHeight(550);
				rightTableView.setPrefWidth(620);
				layout.setStyle("-fx-background-image: null;");
				layout.getChildren().add(rightTableView);
			});

			button4.setText("Add Class");
			button4.setPrefSize(230, 40);
			button4.setTextAlignment(TextAlignment.CENTER);
			button4.setOnAction(e -> {
				layout.getChildren().removeAll(rightPane, rightTableView);
				rightPane = AdminFunctionality.addCoursePane();
				AnchorPane.setRightAnchor(rightPane, 0.0);
				AnchorPane.setTopAnchor(rightPane, 0.0);
				layout.setStyle("-fx-background-image: null;");
				layout.getChildren().add(rightPane);
			});

			button5.setText("Student Search");
			button5.setPrefSize(230, 40);
			button5.setTextAlignment(TextAlignment.CENTER);
			button5.setOnAction(e -> {
				layout.getChildren().removeAll(rightPane, rightTableView);
				rightTableView = ProfessorFunctionality.studentSearch();
				AnchorPane.setRightAnchor(rightTableView, 0.0);
				AnchorPane.setTopAnchor(rightTableView, 0.0);
				rightTableView.setPrefHeight(550);
				rightTableView.setPrefWidth(620);
				layout.setStyle("-fx-background-image: null;");
				layout.getChildren().add(rightTableView);
			});
			break;
		}

		layout.getChildren().addAll(leftMenu);

		Scene scene = new Scene(layout, 850, 550);
		scene.getStylesheets().add("com/eStudent/gui/css/dashboard.css");

		return scene;
	}
}
