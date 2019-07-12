package com.eStudent.gui;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import com.eStudent.user.UserType;
import com.eStudent.user.admin.Admin;
import com.eStudent.user.admin.AdminFunctionality;
import com.eStudent.user.professor.AcademicTitles;
import com.eStudent.user.professor.Professor;
import com.eStudent.user.student.Faculty;
import com.eStudent.user.student.Majors;
import com.eStudent.user.student.RecordBook;
import com.eStudent.user.student.Student;
import com.eStudent.user.student.YearOfStudy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserEditDialog extends AdminFunctionality {

	private static JSONArray loadedUsers = users;

	private static JSONObject recordBookJSON = new JSONObject();
	private static RecordBook recordBook = new RecordBook();

	private static Label header = new Label();
	private static Label firstNameLabel = new Label("First Name");
	private static Label lastNameLabel = new Label("Last Name");
	private static Label usernameLabel = new Label("Username");
	private static Label passwordLabel = new Label("Password");
	private static Label emailLabel = new Label("E-mail");
	private static Label phoneNumLabel = new Label("Phone Number");
	private static Label facultyLabel = new Label("Faculty");
	private static Label majorLabel = new Label("Major");

	private static TextField firstName = new TextField();
	private static TextField lastName = new TextField();
	private static TextField username = new TextField();
	private static TextField password = new TextField();
	private static TextField email = new TextField();
	private static TextField phoneNumber = new TextField();

	private static Button confirmButton = new Button();

	private static ComboBox<AcademicTitles> professorTitle = new ComboBox<>();
	private static ComboBox<YearOfStudy> studyYear = new ComboBox<>();
	private static ComboBox<Faculty> faculty = new ComboBox<>();
	private static ComboBox<Majors> major = new ComboBox<>();

	private static Admin selectedAdmin = null;
	private static Professor selectedProfessor = null;
	private static Student selectedStudent = null;

	private static Alert error = new Alert(AlertType.ERROR);

	public static void displayUserEditDialog(String title, UserType userType, String usernameFromSelected) {
		GridPane layout = new GridPane();

		Stage window = new Stage();
		window.getIcons().add(new Image("com/eStudent/gui/icons/icon.png"));
		window.setResizable(false);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);

		header = new Label("Edit User");

		switch (userType) {
		/*
		 * ADMIN EDIT
		 */
		case ADMIN:
			selectedAdmin = null;
			layout.getChildren().clear();
			for (Object obj : loadedUsers) {
				JSONObject o = (JSONObject) obj;
				if (o.getString("username").equals(usernameFromSelected))
					selectedAdmin = new Admin(o.getString("firstName"), o.getString("lastName"),
							o.getString("username"), o.getString("password"), o.getBoolean("deleted"),
							UserType.valueOf(o.optString("userType")));
			}

			firstName.setText(selectedAdmin.getFirstName());
			firstName.setMaxWidth(200);

			lastName.setText(selectedAdmin.getLastName());
			lastName.setMaxWidth(200);

			username.setText(selectedAdmin.getUsername());
			username.setMaxWidth(200);

			password.setText(selectedAdmin.getPassword());
			password.setMaxWidth(200);

			confirmButton = new Button("Confirm");

			GridPane.setConstraints(header, 1, 0);
			GridPane.setConstraints(firstNameLabel, 0, 1);
			GridPane.setConstraints(firstName, 1, 1);
			GridPane.setConstraints(lastNameLabel, 0, 2);
			GridPane.setConstraints(lastName, 1, 2);
			GridPane.setConstraints(usernameLabel, 0, 3);
			GridPane.setConstraints(username, 1, 3);
			GridPane.setConstraints(passwordLabel, 0, 4);
			GridPane.setConstraints(password, 1, 4);
			GridPane.setConstraints(confirmButton, 1, 5);

			layout.getChildren().addAll(firstNameLabel, firstName, lastNameLabel, lastName, usernameLabel, username,
					passwordLabel, password, confirmButton);
			layout.setVgap(10);
			layout.setHgap(10);

			confirmButton.setOnAction(e -> {
				if (username.getText().trim().length() < 4 || password.getText().trim().length() < 6) {
					error.setTitle("Length ERROR");
					error.setHeaderText("Username or Password is too short.");
					error.setContentText("Mind the length requirements!");
					error.show();
					return;
				}
				if (firstName.getText().isBlank() || lastName.getText().isBlank()) {
					error.setTitle("FieldBlank ERROR");
					error.setHeaderText("Fields cannot be left blank!");
					error.show();
					return;
				}

				for (Object obj : loadedUsers) {
					JSONObject o = (JSONObject) obj;
					if (username.getText().trim().equals(o.getString("username"))
							&& !username.getText().trim().equals(selectedAdmin.getUsername())) {
						error.setTitle("Username ERROR");
						error.setHeaderText("Username already exists!");
						error.setContentText("Try entering a slightly different one.");
						error.show();
						return;
					}
					if (selectedAdmin.getUsername().equals(o.getString("username"))) {
						o.put("firstName", firstName.getText().trim());
						o.put("lastName", lastName.getText().trim());
						o.put("username", username.getText().trim());
						o.put("password", password.getText());
					}
				}
				writeData(new File(PATH_TO_USERS), loadedUsers);
				table.getItems().clear();
				table.setItems(getUsers());
				Alert success = new Alert(AlertType.INFORMATION);
				success.setHeaderText("Admin successfully edited!");
				success.setTitle("Successful Edit");
				window.close();
				success.show();
			});
			break;
		/*
		 * PROFESSOR EDIT
		 */
		case PROFESSOR:
			selectedProfessor = null;
			layout.getChildren().clear();
			professorTitle.getItems().removeAll(AcademicTitles.PROFESSOR, AcademicTitles.ASSOCIATE_PROFESSOR,
					AcademicTitles.ASSISTANT_PROFESSOR, AcademicTitles.TEACHING_ASSISTANT,
					AcademicTitles.RESEARCH_PROFESSOR);
			for (Object obj : loadedUsers) {
				JSONObject o = (JSONObject) obj;
				if (o.getString("username").equals(usernameFromSelected))
					selectedProfessor = new Professor(o.getString("firstName"), o.getString("lastName"),
							o.getString("username"), o.getString("password"), o.getBoolean("deleted"),
							UserType.valueOf(o.optString("userType")), o.getString("email"),
							AcademicTitles.valueOf(o.optString("academicTitle")));
			}

			firstName.setText(selectedProfessor.getFirstName());
			firstName.setMaxWidth(200);

			lastName.setText(selectedProfessor.getLastName());
			lastName.setMaxWidth(200);

			username.setText(selectedProfessor.getUsername());
			username.setMaxWidth(200);

			password.setText(selectedProfessor.getPassword());
			password.setMaxWidth(200);

			email.setText(selectedProfessor.getEmail());
			email.setMaxWidth(200);

			professorTitle.getItems().addAll(AcademicTitles.values());
			professorTitle.setTooltip(new Tooltip("Select Academic Title"));
			professorTitle.setValue(selectedProfessor.getAcademicTitle());
			professorTitle.setMaxWidth(150);
			professorTitle.getStyleClass().add("comboBox");

			confirmButton = new Button("Confirm");

			GridPane.setConstraints(header, 1, 0);
			GridPane.setConstraints(firstNameLabel, 0, 1);
			GridPane.setConstraints(firstName, 1, 1);
			GridPane.setConstraints(lastNameLabel, 0, 2);
			GridPane.setConstraints(lastName, 1, 2);
			GridPane.setConstraints(usernameLabel, 0, 3);
			GridPane.setConstraints(username, 1, 3);
			GridPane.setConstraints(passwordLabel, 0, 4);
			GridPane.setConstraints(password, 1, 4);
			GridPane.setConstraints(emailLabel, 0, 5);
			GridPane.setConstraints(email, 1, 5);
			GridPane.setConstraints(professorTitle, 1, 6);
			GridPane.setConstraints(confirmButton, 1, 7);

			layout.getChildren().addAll(firstNameLabel, firstName, lastNameLabel, lastName, usernameLabel, username,
					passwordLabel, password, emailLabel, email, professorTitle, confirmButton);
			layout.setVgap(10);
			layout.setHgap(30);

			confirmButton.setOnAction(e -> {
				if (username.getText().trim().length() < 4 || password.getText().trim().length() < 6) {
					error.setTitle("Length ERROR");
					error.setHeaderText("Username or Password is too short.");
					error.setContentText("Mind the length requirements!");
					error.show();
					return;
				}
				if (firstName.getText().isBlank() || lastName.getText().isBlank()) {
					error.setTitle("FieldBlank ERROR");
					error.setHeaderText("Fields cannot be left blank!");
					error.show();
					return;
				}
				if (!email.getText().trim().contains("@")) {
					Alert emailError = new Alert(AlertType.WARNING, "Invalid email format.");
					emailError.show();
					return;
				}
				for (Object obj : loadedUsers) {
					JSONObject o = (JSONObject) obj;
					if (username.getText().trim().equals(o.getString("username"))
							&& !username.getText().trim().equals(selectedProfessor.getUsername())) {
						error.setTitle("Username ERROR");
						error.setHeaderText("Username already exists!");
						error.setContentText("Try entering a slightly different one.");
						error.show();
						return;
					}
					if (selectedProfessor.getUsername().equals(o.getString("username"))) {
						o.put("firstName", firstName.getText().trim());
						o.put("lastName", lastName.getText().trim());
						o.put("username", username.getText().trim());
						o.put("password", password.getText());
						o.put("email", email.getText().trim());
						o.put("academicTitle", professorTitle.getValue());
					}
				}
				writeData(new File(PATH_TO_USERS), loadedUsers);
				table.getItems().clear();
				table.setItems(getUsers());
				Alert success = new Alert(AlertType.INFORMATION);
				success.setHeaderText("Professor successfully edited!");
				success.setTitle("Successful Edit");
				window.close();
				success.show();
			});
			break;
		/*
		 * STUDENT EDIT
		 */
		case STUDENT:
			layout.getChildren().clear();
			studyYear.getItems().removeAll(YearOfStudy.values());
			faculty.getItems().removeAll(Faculty.values());
			major.getItems().removeAll(Majors.values());

			for (Object obj : loadedUsers) {
				JSONObject o = (JSONObject) obj;
				if (o.getString("username").equals(usernameFromSelected)) {
					recordBookJSON = o.getJSONObject("recordBook");

					recordBook = new RecordBook(recordBookJSON.getString("id"), recordBookJSON.getString("university"),
							recordBookJSON.getString("enrollmentYear"), recordBookJSON.getString("firstName"),
							recordBookJSON.getString("lastName"), Faculty.valueOf(recordBookJSON.optString("faculty")),
							Majors.valueOf(recordBookJSON.optString("major")),
							YearOfStudy.valueOf(recordBookJSON.optString("yearOfStudy")),
							recordBookJSON.getJSONArray("exams"));

					selectedStudent = new Student(o.getString("firstName"), o.getString("lastName"),
							o.getString("username"), o.getString("password"), o.getBoolean("deleted"),
							UserType.valueOf(o.optString("userType")), o.getString("email"), o.getString("phoneNumber"),
							recordBook);
				}
			}

			firstName.setText(selectedStudent.getFirstName());
			firstName.setMaxWidth(200);

			lastName.setText(selectedStudent.getLastName());
			lastName.setMaxWidth(200);

			username.setText(selectedStudent.getUsername());
			username.setMaxWidth(200);

			password.setText(selectedStudent.getPassword());
			password.setMaxWidth(200);

			email.setText(selectedStudent.getEmail());
			email.setMaxWidth(200);

			phoneNumber.setText(selectedStudent.getPhoneNumber().toString());
			phoneNumber.setMaxWidth(200);

			faculty.getItems().addAll(Faculty.values());
			faculty.setValue(recordBook.getFaculty());
			faculty.setMaxWidth(150);
			faculty.getStyleClass().add("comboBox");

			major.getItems().addAll(Majors.values());
			major.setValue(recordBook.getMajor());
			major.setMaxWidth(150);
			major.getStyleClass().add("comboBox");

			studyYear.getItems().addAll(YearOfStudy.values());
			studyYear.setValue(recordBook.getYearOfStudy());
			studyYear.setMaxWidth(150);
			studyYear.getStyleClass().add("comboBox");

			confirmButton = new Button("Confirm");

			GridPane.setConstraints(header, 1, 0);
			GridPane.setConstraints(firstNameLabel, 0, 1);
			GridPane.setConstraints(firstName, 1, 1);
			GridPane.setConstraints(lastNameLabel, 0, 2);
			GridPane.setConstraints(lastName, 1, 2);
			GridPane.setConstraints(usernameLabel, 0, 3);
			GridPane.setConstraints(username, 1, 3);
			GridPane.setConstraints(passwordLabel, 0, 4);
			GridPane.setConstraints(password, 1, 4);
			GridPane.setConstraints(emailLabel, 0, 5);
			GridPane.setConstraints(email, 1, 5);
			GridPane.setConstraints(phoneNumLabel, 0, 6);
			GridPane.setConstraints(phoneNumber, 1, 6);
			GridPane.setConstraints(facultyLabel, 0, 7);
			GridPane.setConstraints(faculty, 1, 7);
			GridPane.setConstraints(majorLabel, 0, 8);
			GridPane.setConstraints(major, 1, 8);
			GridPane.setConstraints(studyYear, 1, 9);
			GridPane.setConstraints(confirmButton, 1, 10);

			layout.getChildren().addAll(firstNameLabel, firstName, lastNameLabel, lastName, usernameLabel, username,
					passwordLabel, password, emailLabel, email, phoneNumLabel, phoneNumber, facultyLabel, faculty,
					majorLabel, major, studyYear, confirmButton);
			layout.setVgap(10);
			layout.setHgap(10);

			confirmButton.setOnAction(e -> {
				if (username.getText().trim().length() < 4 || password.getText().trim().length() < 6) {
					error.setTitle("Length ERROR");
					error.setHeaderText("Username or Password is too short.");
					error.setContentText("Mind the length requirements!");
					error.show();
					return;
				}
				if (firstName.getText().isBlank() || lastName.getText().isBlank()) {
					error.setTitle("FieldBlank ERROR");
					error.setHeaderText("Fields cannot be left blank!");
					error.show();
					return;
				}
				if (!email.getText().trim().contains("@")) {
					Alert emailError = new Alert(AlertType.WARNING, "Invalid email format.");
					emailError.show();
					return;
				}
				if (!phoneNumber.getText().trim().matches("[0-9]+")) {
					error.setTitle("PhoneNumber ERROR");
					error.setHeaderText("Invalid Phone-Number format.");
					error.setContentText("Only numbers are permitted!");
					error.show();
					return;
				}

				for (Object obj : loadedUsers) {
					JSONObject o = (JSONObject) obj;
					if ((username.getText().trim().equals(o.getString("username")))
							&& (!username.getText().trim().equals(selectedStudent.getUsername()))) {
						error.setTitle("Username ERROR");
						error.setHeaderText("Username already exists!");
						error.setContentText("Try entering a slightly different one.");
						error.show();
						return;
					}
				}

				for (Object obj : loadedUsers) {
					JSONObject o = (JSONObject) obj;
					if (selectedStudent.getUsername().equals(o.getString("username"))) {
						o.put("firstName", firstName.getText().trim());
						o.put("lastName", lastName.getText().trim());
						o.put("username", username.getText().trim());
						o.put("password", password.getText());
						o.put("email", email.getText().trim());
						o.put("phoneNumber", phoneNumber.getText().trim());
						recordBook.setYearOfStudy(studyYear.getValue());
						recordBook.setFaculty(faculty.getValue());
						recordBook.setMajor(major.getValue());
						o.put("recordBook", new JSONObject(recordBook));
					}
				}
				writeData(new File(PATH_TO_USERS), loadedUsers);
				table.getItems().clear();
				table.setItems(getUsers());
				Alert success = new Alert(AlertType.INFORMATION);
				success.setHeaderText("Student successfully edited!");
				success.setTitle("Successful Edit");
				window.close();
				success.show();
			});
			break;
		}

		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout, 400, 430);
		scene.getStylesheets().add("com/eStudent/gui/css/dialog.css");
		window.setScene(scene);
		window.showAndWait();
	}
}
