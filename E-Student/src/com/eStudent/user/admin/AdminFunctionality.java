package com.eStudent.user.admin;

import java.io.File;
import java.time.LocalDate;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.eStudent.courses.Course;
import com.eStudent.gui.Dashboard;
import com.eStudent.gui.UserEditDialog;
import com.eStudent.user.User;
import com.eStudent.user.UserType;
import com.eStudent.user.professor.AcademicTitles;
import com.eStudent.user.professor.Professor;
import com.eStudent.user.student.Faculty;
import com.eStudent.user.student.Majors;
import com.eStudent.user.student.RecordBook;
import com.eStudent.user.student.Student;
import com.eStudent.user.student.YearOfStudy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminFunctionality extends Dashboard {

	private static GridPane layout;
	protected static TableView<User> table;

	private static TextField textField1 = new TextField();
	private static TextField textField2 = new TextField();
	private static TextField textField3 = new TextField();

	private static Label label1 = new Label();
	private static Label label2 = new Label();
	private static Label label3 = new Label();

	private static Label firstNameLabel = new Label();
	private static TextField firstName = new TextField();

	private static Label lastNameLabel = new Label();
	private static TextField lastName = new TextField();

	private static Label usernameLabel = new Label();
	private static Label usernameInfo = new Label();
	private static TextField username = new TextField();

	private static Label passwordLabel = new Label();
	private static Label passwordInfo = new Label();
	private static PasswordField password = new PasswordField();

	private static Button registrationButton = new Button();

	private static ComboBox<AcademicTitles> academicTitleChoice = new ComboBox<>();
	private static ComboBox<YearOfStudy> studyYear = new ComboBox<>();
	private static ComboBox<Faculty> faculty = new ComboBox<>();
	private static ComboBox<Majors> major = new ComboBox<>();

	private static TextField courseName = new TextField();
	private static TextArea courseSyllabus = new TextArea();
	private static DatePicker courseExecYear = new DatePicker();
	private static Spinner<Integer> classCredits = new Spinner<>();
	private static Spinner<Integer> practicalClassCredits = new Spinner<>();

	private static Alert error = new Alert(AlertType.ERROR);
	private static Alert success = new Alert(AlertType.INFORMATION);

	protected static JSONArray users = loadedUsers;
	protected static JSONArray courses = loadedCourses;

	public static GridPane userRegistrationPane() {
		layout = new GridPane();

		refreshPage();

		Label title = new Label("USER REGISTRATION");
		title.setId("title");

		Label pickUserLabel = new Label("User type");
		pickUserLabel.getStyleClass().add("labls");

		RadioButton pickStudent = new RadioButton("Student");
		pickStudent.getStyleClass().add("radio");
		RadioButton pickProfessor = new RadioButton("Professor");
		pickProfessor.getStyleClass().add("radio");
		RadioButton pickAdmin = new RadioButton("Admin");
		pickAdmin.getStyleClass().add("radio");

		Separator separator = new Separator();

		ToggleGroup group = new ToggleGroup();
		group.getToggles().addAll(pickStudent, pickProfessor, pickAdmin);

		/*
		 * Selection of user type -> adds additional fields if needed
		 */
		Button pick = new Button("Pick User Type");
		pick.setOnAction(e -> {
			if (pickStudent.isSelected()) {
				refreshPage();
				label1.setText("Email");
				label1.getStyleClass().add("labels");
				textField1.setPromptText("E-mail");

				label2.setText("Phone Number");
				label2.getStyleClass().add("labels");
				textField2.setText("06");

				label3.setText("Recordbook Number");
				label3.getStyleClass().add("labels");
				textField3.setPromptText("Recordbook number");

				studyYear.getItems().addAll(YearOfStudy.values());
				studyYear.getSelectionModel().selectFirst();
				studyYear.setMaxWidth(125);
				studyYear.setId("comboBox");

				faculty.getItems().addAll(Faculty.values());
				faculty.getSelectionModel().selectFirst();
				faculty.setMaxWidth(125);
				faculty.setId("comboBox");

				major.getItems().addAll(Majors.values());
				major.getSelectionModel().selectFirst();
				major.setMaxWidth(125);
				major.setId("comboBox");

				registrationButton = new Button("Register User");
				registrationButton.setOnAction(r -> register(UserType.STUDENT));

				createUniversalFields();
				layout.getChildren().addAll(label1, textField1, label2, textField2, label3, textField3, major,
						studyYear, faculty, registrationButton);
			}
			if (pickProfessor.isSelected()) {
				refreshPage();
				label1.setText("Email");
				label1.getStyleClass().add("labels");
				textField1.setPromptText("E-mail");

				academicTitleChoice.getItems().addAll(AcademicTitles.values());
				academicTitleChoice.setTooltip(new Tooltip("Select Academic Title"));
				academicTitleChoice.setValue(AcademicTitles.PROFESSOR);
				academicTitleChoice.setMaxWidth(150);
				academicTitleChoice.setId("comboBox");

				registrationButton = new Button("Register User");
				registrationButton.setOnAction(r -> register(UserType.PROFESSOR));

				createUniversalFields();
				GridPane.setConstraints(academicTitleChoice, 2, 7);
				layout.getChildren().addAll(label1, textField1, academicTitleChoice, registrationButton);
			}
			if (pickAdmin.isSelected()) {
				refreshPage();
				registrationButton = new Button("Register User");
				registrationButton.setOnAction(r -> register(UserType.ADMIN));

				createUniversalFields();
				layout.getChildren().add(registrationButton);
			}
		});

		GridPane.setConstraints(title, 0, 0);
		GridPane.setConstraints(pickUserLabel, 0, 1);
		GridPane.setConstraints(pickStudent, 0, 2);
		GridPane.setConstraints(pickProfessor, 0, 3);
		GridPane.setConstraints(pickAdmin, 0, 4);
		GridPane.setConstraints(separator, 0, 5);
		GridPane.setConstraints(label1, 0, 10);
		GridPane.setConstraints(textField1, 1, 10);
		GridPane.setConstraints(label2, 0, 11);
		GridPane.setConstraints(textField2, 1, 11);
		GridPane.setConstraints(label3, 0, 12);
		GridPane.setConstraints(textField3, 1, 12);
		GridPane.setConstraints(studyYear, 2, 10);
		GridPane.setConstraints(faculty, 2, 11);
		GridPane.setConstraints(major, 2, 12);
		GridPane.setConstraints(pick, 1, 5);

		layout.getChildren().addAll(title, pickUserLabel, pickStudent, pickProfessor, pickAdmin, separator, pick);
		layout.setAlignment(Pos.CENTER);
		layout.setVgap(10);
		layout.setHgap(10);
		layout.setId("rightSide");
		layout.setPrefSize(620, 550);

		return layout;
	}

	@SuppressWarnings("unchecked")
	/*
	 * Returns a table with users eligible for deletion
	 */
	public static VBox userDeletionPane() {
		dataLoader();
		table = new TableView<>();
		table.setItems(getUsers());

		TableColumn<User, String> firstNameColumn = new TableColumn<>("First Name");
		firstNameColumn.setMinWidth(140);
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

		TableColumn<User, String> lastNameColumn = new TableColumn<>("Last Name");
		lastNameColumn.setMinWidth(140);
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

		TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
		usernameColumn.setMinWidth(140);
		usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

		TableColumn<User, UserType> userTypeColumn = new TableColumn<>("User Type");
		userTypeColumn.setMinWidth(110);
		userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));

		TableColumn<User, Boolean> deletedColumn = new TableColumn<>("Deleted(?)");
		deletedColumn.setMinWidth(88);
		deletedColumn.setCellValueFactory(new PropertyValueFactory<>("deleted"));

		Label captchaLabel = new Label("Enter \"DELETE\" in order to confirm deletion");

		TextField captcha = new TextField();
		captcha.setPromptText("\"DELETE\"");

		Button deleteButton = new Button("Delete");
		deleteButton.setOnAction(e -> {
			if (table.getSelectionModel().isEmpty()) {
				return;
			}
			if (!captcha.getText().trim().equals("DELETE")) {
				captcha.setStyle("-fx-text-box-border: red ; -fx-focus-color: red;");
				return;
			} else {
				deleteUser();
				captcha.clear();
				captcha.setStyle("-fx-text-box-border: purple ; -fx-focus-color: purple;");
				table.getSelectionModel().clearSelection();
			}
		});

		HBox horizontalLayout = new HBox();
		horizontalLayout.setPadding(new Insets(15, 10, 10, 80));
		horizontalLayout.setSpacing(10);
		horizontalLayout.getChildren().addAll(captchaLabel, captcha, deleteButton);

		VBox verticalLayout = new VBox();
		verticalLayout.getChildren().addAll(table, horizontalLayout);

		table.getColumns().addAll(firstNameColumn, lastNameColumn, usernameColumn, userTypeColumn, deletedColumn);

		return verticalLayout;
	}

	@SuppressWarnings("unchecked")
	public static VBox userEditPane() {
		dataLoader();
		table = new TableView<>();
		table.getItems().clear();
		table.setItems(getUsers());

		TableColumn<User, String> firstNameColumn = new TableColumn<>("First Name");
		firstNameColumn.setMinWidth(170);
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

		TableColumn<User, String> lastNameColumn = new TableColumn<>("Last Name");
		lastNameColumn.setMinWidth(170);
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

		TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
		usernameColumn.setMinWidth(170);
		usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

		TableColumn<User, UserType> userTypeColumn = new TableColumn<>("User Type");
		userTypeColumn.setMinWidth(108);
		userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));

		table.getColumns().addAll(firstNameColumn, lastNameColumn, usernameColumn, userTypeColumn);

		Label infoLabel = new Label("Select a user to edit from the above table");

		Button editButton = new Button("Edit");
		editButton.setPrefWidth(80);
		editButton.setOnAction(e -> {
			if (table.getSelectionModel().isEmpty()) {
				return;
			}
			UserEditDialog.displayUserEditDialog("Edit User", table.getSelectionModel().getSelectedItem().getUserType(),
					table.getSelectionModel().getSelectedItem().getUsername());
		});

		HBox horizontalLayout = new HBox();
		horizontalLayout.setPadding(new Insets(15, 10, 10, 175));
		horizontalLayout.setSpacing(10);
		horizontalLayout.getChildren().addAll(infoLabel, editButton);

		VBox verticalLayout = new VBox();
		verticalLayout.getChildren().addAll(table, horizontalLayout);

		return verticalLayout;
	}

	/*
	 * Put users in an ObservableList
	 */
	public static ObservableList<User> getUsers() {
		ObservableList<User> userList = FXCollections.observableArrayList();
		for (Object obj : users) {
			JSONObject user = (JSONObject) obj;
			if (user.getString("username").equals(currentlySignedIn.getUsername()))
				continue;
			if (user.getBoolean("deleted"))
				continue;
			userList.add(new User(user.getString("firstName"), user.getString("lastName"), user.getString("username"),
					user.getString("password"), user.getBoolean("deleted"),
					UserType.valueOf(user.optString("userType"))));
		}
		return userList;
	}

	/*
	 * Removes the selected user (from the table)
	 */
	public static void deleteUser() {
		ObservableList<User> allUsers, selected;
		User userForDeletion;
		allUsers = table.getItems();
		selected = table.getSelectionModel().getSelectedItems();
		userForDeletion = selected.get(0);

		for (Object obj : users) {
			JSONObject o = (JSONObject) obj;
			if (userForDeletion.getUsername().equals(o.getString("username"))) {
				o.put("deleted", true);
			}
		}

		allUsers.removeAll(selected);

		writeData(new File(PATH_TO_USERS), users);
		Alert success = new Alert(AlertType.INFORMATION);
		success.setHeaderText("User successfully deleted!");
		success.setTitle("Successful Deletion");
		success.show();
	}

	public static void createUniversalFields() {
		/*
		 * First Name
		 */
		firstNameLabel = new Label("Enter First Name");
		firstNameLabel.getStyleClass().add("labels");
		firstName = new TextField();
		firstName.setPromptText("First name");
		firstName.setMaxWidth(200);

		/*
		 * Last Name
		 */
		lastNameLabel = new Label("Enter Last Name");
		lastNameLabel.getStyleClass().add("labels");
		lastName = new TextField();
		lastName.setPromptText("Last name");
		lastName.setMaxWidth(200);

		/*
		 * Username
		 */
		usernameLabel = new Label("Enter Username");
		usernameLabel.getStyleClass().add("labels");
		usernameInfo = new Label("(length must be >= 4)");
		usernameInfo.getStyleClass().add("info");
		username = new TextField();
		username.setPromptText("Username");
		username.setMaxWidth(200);

		/*
		 * Password
		 */
		passwordLabel = new Label("Enter Password");
		passwordLabel.getStyleClass().add("labels");
		passwordInfo = new Label("(length must be >= 6)");
		passwordInfo.getStyleClass().add("info");
		password = new PasswordField();
		password.setPromptText("Password");
		password.setMaxWidth(200);

		/*
		 * Positioning on the GridPane
		 */
		GridPane.setConstraints(firstNameLabel, 0, 6);
		GridPane.setConstraints(firstName, 1, 6);
		GridPane.setConstraints(lastNameLabel, 0, 7);
		GridPane.setConstraints(lastName, 1, 7);
		GridPane.setConstraints(usernameLabel, 0, 8);
		GridPane.setConstraints(username, 1, 8);
		GridPane.setConstraints(usernameInfo, 2, 8);
		GridPane.setConstraints(passwordLabel, 0, 9);
		GridPane.setConstraints(password, 1, 9);
		GridPane.setConstraints(passwordInfo, 2, 9);
		GridPane.setConstraints(registrationButton, 1, 13);
		GridPane.setHalignment(registrationButton, HPos.RIGHT);

		layout.getChildren().addAll(firstNameLabel, firstName, lastNameLabel, lastName, usernameLabel, usernameInfo,
				username, passwordLabel, passwordInfo, password);
	}

	public static void refreshPage() {
		layout.getChildren().remove(firstNameLabel);
		layout.getChildren().remove(firstName);
		layout.getChildren().removeAll(firstNameLabel, firstName);
		layout.getChildren().removeAll(lastNameLabel, lastName);
		layout.getChildren().removeAll(usernameLabel, usernameInfo, username);
		layout.getChildren().removeAll(passwordLabel, passwordInfo, password);
		layout.getChildren().removeAll(label1, label2, label3, textField1, textField2, textField3);
		layout.getChildren().removeAll(academicTitleChoice, studyYear, faculty, major);
		academicTitleChoice.getItems().removeAll(AcademicTitles.PROFESSOR, AcademicTitles.ASSOCIATE_PROFESSOR,
				AcademicTitles.ASSISTANT_PROFESSOR, AcademicTitles.TEACHING_ASSISTANT,
				AcademicTitles.RESEARCH_PROFESSOR);
		studyYear.getItems().removeAll(YearOfStudy.FRESHMAN, YearOfStudy.SOPHMORE, YearOfStudy.JUNIOR,
				YearOfStudy.SENIOR);
		faculty.getItems().removeAll(Faculty.FACULTY_OF_TECHNICAL_SCIENCES, Faculty.FACULTY_OF_BUSINESS,
				Faculty.FACULTY_OF_INFORMATICS_AND_COMPUTING, Faculty.FACULTY_OF_TOURISM);
		major.getItems().removeAll(Majors.BUSINESS_ECONOMICS, Majors.ELECTRICAL_ENGINEERING_AND_COMPUTING,
				Majors.INFORMATION_TECHNOLOGY, Majors.PSHYSICAL_EDUCATION_AND_SPORTS,
				Majors.SOFTWARE_AND_INFORMATION_ENGINEERING, Majors.TOURISM_AND_HOSPITALITY);
		layout.getChildren().remove(registrationButton);
	}

	public static void clearFields() {
		firstName.clear();
		lastName.clear();
		username.clear();
		password.clear();
	}

	public static void register(UserType type) {
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

		JSONObject user = null;
		switch (type) {
		case STUDENT:
			if (!textField1.getText().trim().contains("@")) {
				Alert emailError = new Alert(AlertType.WARNING, "Invalid e-mail format.");
				emailError.show();
				return;
			}
			if (!textField2.getText().trim().matches("[0-9]+")) {
				error.setTitle("PhoneNumber ERROR");
				error.setHeaderText("Invalid Phone-Number format.");
				error.setContentText("Only numbers are permitted!");
				error.show();
				return;
			}
			if (!textField3.getText().trim().matches("[0-9]+")) {
				error.setTitle("RecordbookNumber ERROR");
				error.setHeaderText("Invalid Recordbook N� format.");
				error.setContentText("Only numbers are permitted!");
				error.show();
				return;
			}

			for (Object obj : users) {
				user = (JSONObject) obj;
				if (user.get("userType").equals("STUDENT")) {
					JSONObject recordBook = user.getJSONObject("recordBook");
					if (recordBook.getString("id").equals(textField3.getText().trim())) {
						error.setTitle("Recordbook ERROR");
						error.setHeaderText("Recordbook N� already exists!");
						error.setContentText("It must be unique.");
						error.show();
						return;
					}
				}
				if (user.getString("username").equals(username.getText().trim()) && !user.getBoolean("deleted")) {
					error.setTitle("Username ERROR");
					error.setHeaderText("Username already exists!");
					error.setContentText("Try entering a slightly different one.");
					error.show();
					return;
				}
			}

			Student newStudent = new Student(firstName.getText().trim(), lastName.getText().trim(),
					username.getText().trim(), password.getText().trim(), false, UserType.STUDENT,
					textField1.getText().trim(), textField2.getText().trim(),
					new RecordBook(textField3.getText().trim(), "Singidunum University",
							String.valueOf(LocalDate.now().getYear()), firstName.getText().trim(),
							lastName.getText().trim(), faculty.getValue(), major.getValue(), studyYear.getValue(),
							new JSONArray()));
			JSONObject student = new JSONObject(newStudent);
			users.put(student);
			writeData(new File(PATH_TO_USERS), users);
			clearFields();
			textField1.clear();
			textField2.clear();
			textField2.setText("06");
			textField3.clear();
			success.setTitle("Success!");
			success.setHeaderText("You've succesfully added a new Student!");
			success.show();
			break;
		case ADMIN:
			for (Object obj : users) {
				user = (JSONObject) obj;
				if (user.getString("username").equals(username.getText().trim()) && !user.getBoolean("deleted")) {
					error.setTitle("Username ERROR");
					error.setHeaderText("Username already exists!");
					error.setContentText("Try entering a slightly different one.");
					error.show();
					return;
				}
			}
			Admin newAdmin = new Admin(firstName.getText().trim(), lastName.getText().trim(), username.getText().trim(),
					password.getText().trim(), false, UserType.ADMIN);
			JSONObject admin = new JSONObject(newAdmin);
			users.put(admin);
			writeData(new File(PATH_TO_USERS), users);
			clearFields();
			textField1.clear();
			success.setTitle("Success!");
			success.setHeaderText("You've succesfully added a new Admin!");
			success.show();
			break;
		case PROFESSOR:
			if (!textField1.getText().trim().contains("@")) {
				Alert emailError = new Alert(AlertType.WARNING, "Invalid email format.");
				emailError.show();
				return;
			}
			for (Object obj : users) {
				user = (JSONObject) obj;
				if (user.getString("username").equals(username.getText().trim()) && !user.getBoolean("deleted")) {
					error.setTitle("Username ERROR");
					error.setHeaderText("Username already exists!");
					error.setContentText("Try entering a slightly different one.");
					error.show();
					return;
				}
			}

			Professor newProfessor = new Professor(firstName.getText().trim(), lastName.getText().trim(),
					username.getText().trim(), password.getText().trim(), false, UserType.PROFESSOR,
					textField1.getText().trim(), academicTitleChoice.getValue());
			JSONObject professor = new JSONObject(newProfessor);
			users.put(professor);
			writeData(new File(PATH_TO_USERS), users);
			clearFields();
			textField1.clear();
			academicTitleChoice.getSelectionModel().selectFirst();
			success.setTitle("Success!");
			success.setHeaderText("You've succesfully added a new Professor!");
			success.show();
			break;
		}
	}

	public static GridPane addCoursePane() {
		layout = new GridPane();
		layout.getChildren().clear();

		Label title = new Label("ADD COURSE");
		title.setId("title");

		Label courseNameLabel = new Label("Course Name");
		courseName = new TextField();
		courseName.setPromptText("Course Name");

		courseSyllabus = new TextArea();
		courseSyllabus.setPromptText("Class Syllabus goes here");
		courseSyllabus.setPrefWidth(200);
		courseSyllabus.setPrefWidth(350);

		Label courseExecYearLabel = new Label("Course Year");
		courseExecYear = new DatePicker();
		courseExecYear.setEditable(false);
		courseExecYear.setValue(LocalDate.now());

		Label classCreditsLabel = new Label("Class Credits");
		classCredits = new Spinner<>(1, 8, 1);
		classCredits.setPrefWidth(60);

		Label practicalClassCreditsLabel = new Label("Practical Class Credits");
		practicalClassCredits = new Spinner<>(1, 8, 1);
		practicalClassCredits.setPrefWidth(60);

		Label studyYearLabel = new Label("Select Study Year");
		studyYear = new ComboBox<>();
		studyYear.getItems().addAll(YearOfStudy.values());
		studyYear.setValue(YearOfStudy.FRESHMAN);
		studyYear.setPrefWidth(250);
		studyYear.setId("comboBox");

		Label majorLabel = new Label("Select Major");
		major = new ComboBox<>();
		major.getItems().addAll(Majors.values());
		major.setValue(Majors.SOFTWARE_AND_INFORMATION_ENGINEERING);
		major.setPrefWidth(250);
		major.setId("comboBox");

		Button addCourse = new Button("ADD");
		addCourse.setPrefWidth(70);
		addCourse.setOnAction(e -> {
			if (courseName.getText().isBlank() || courseSyllabus.getText().isBlank()) {
				error.setTitle("FieldBlank ERROR");
				error.setHeaderText("Fields cannot be left blank!");
				error.show();
				return;
			}
			add();
		});

		GridPane.setConstraints(title, 0, 0);
		GridPane.setConstraints(courseNameLabel, 0, 1);
		GridPane.setConstraints(courseName, 1, 1);
		GridPane.setConstraints(courseExecYearLabel, 0, 2);
		GridPane.setConstraints(courseExecYear, 1, 2);
		GridPane.setConstraints(classCreditsLabel, 0, 3);
		GridPane.setConstraints(classCredits, 1, 3);
		GridPane.setConstraints(practicalClassCreditsLabel, 0, 4);
		GridPane.setConstraints(practicalClassCredits, 1, 4);
		GridPane.setConstraints(studyYearLabel, 0, 5);
		GridPane.setConstraints(studyYear, 1, 5);
		GridPane.setConstraints(majorLabel, 0, 6);
		GridPane.setConstraints(major, 1, 6);
		GridPane.setConstraints(courseSyllabus, 0, 7);
		GridPane.setConstraints(addCourse, 0, 8);

		layout.getChildren().addAll(title, courseNameLabel, courseName, courseSyllabus, courseExecYearLabel,
				courseExecYear, classCreditsLabel, classCredits, practicalClassCreditsLabel, practicalClassCredits,
				studyYearLabel, studyYear, majorLabel, major, addCourse);
		layout.setAlignment(Pos.CENTER);
		layout.setVgap(10);
		layout.setHgap(-150);
		layout.setId("rightSide");
		layout.setPrefSize(620, 550);
		return layout;
	}

	/*
	 * Adds a new course
	 */
	private static void add() {
		LocalDate localDate = courseExecYear.getValue();
		String yearFull = String.valueOf(localDate.getYear());
		String yearLastDigits = yearFull.substring(2);

		String courseID = createCourseID(yearLastDigits);

		for (Object obj : courses) {
			JSONObject o = (JSONObject) obj;
			if (o.getString("id").equals(courseID)) {
				courseID = null;
				courseID = createCourseID(yearLastDigits);
			}
			if (o.getString("name").equals(courseName.getText().trim())) {
				error.setTitle("Course ERROR");
				error.setHeaderText("Course already exists!");
				error.setContentText("Courses must be unique.");
				error.show();
				return;
			}
		}

		Course newCourse = new Course();
		newCourse.setId(courseID);
		newCourse.setName(courseName.getText().trim());
		newCourse.setSyllabus(courseSyllabus.getText().trim());
		newCourse.setCourseYear(yearFull);
		newCourse.setClassCredits(classCredits.getValue());
		newCourse.setPracticalClassCredits(practicalClassCredits.getValue());
		newCourse.setYearOfStudy(studyYear.getValue());
		newCourse.setMajor(major.getValue());

		JSONObject course = new JSONObject(newCourse);
		courses.put(course);
		writeData(new File(PATH_TO_COURSES), courses);

		courseName.clear();
		courseSyllabus.clear();
		classCredits.getValueFactory().setValue(1);
		practicalClassCredits.getValueFactory().setValue(1);
		studyYear.getSelectionModel().selectFirst();
		major.getSelectionModel().selectFirst();

		academicTitleChoice.getSelectionModel().selectFirst();
		success.setTitle("Success!");
		success.setHeaderText("You've succesfully added a new Course!");
		success.show();
	}

	/*
	 * Creates a course/class ID in this format -> two random letters + year of
	 * class execution + major name initials
	 */
	private static String createCourseID(String twoLastDigitsYear) {
		Random r = new Random();
		char randChar1 = (char) (r.nextInt(26) + 'A');
		char randChar2 = (char) (r.nextInt(26) + 'A');
		String majorInitials = "";

		for (String initial : major.getValue().toString().split("_")) {
			majorInitials += initial.charAt(0);
		}

		String courseID = "";
		courseID = String.format("%s%s%s%s", randChar1, randChar2, twoLastDigitsYear, majorInitials);

		return courseID;
	}

}
