package com.eStudent.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.eStudent.courses.Course;
import com.eStudent.user.User;
import com.eStudent.user.UserType;
import com.eStudent.user.admin.Admin;
import com.eStudent.user.professor.AcademicTitles;
import com.eStudent.user.professor.Professor;
import com.eStudent.user.student.Faculty;
import com.eStudent.user.student.Majors;
import com.eStudent.user.student.RecordBook;
import com.eStudent.user.student.Student;
import com.eStudent.user.student.YearOfStudy;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/*
 * @author Nikola Lausev
 * Object Oriented Programming Class Project - Singidunum University (Freshman year)
 */

public class Main extends Application {

	public Stage window;
	public Scene scene;

	public static JSONArray users;
	public static JSONArray courses;
	public static User currentlySignedIn = null;

	public boolean isValid = false;

	public Button login;
	public Button logOut;

	public static final String PATH_TO_USERS = "data/users.json";
	public static final String PATH_TO_COURSES = "data/courses.json";

	public static void main(String[] args) {
		userDataInit(); // creates default users if needed
		courseDataInit(); // creates default courses if needed
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		dataLoader();

		window = primaryStage;
		window.setTitle("E-STUDENT / Login");
		window.getIcons().add(new Image("com/eStudent/gui/icons/icon.png"));
		window.setResizable(false);

		window.setOnCloseRequest(e -> {
			e.consume();
			closeApplication();
		});

		BorderPane mainLayout = new BorderPane();
		mainLayout.setId("main");

		Pane leftSide = new Pane();
		leftSide.setId("leftPane");

		GridPane loginLayout = new GridPane();
		loginLayout.setPadding(new Insets(150, 65, 10, 10));
		loginLayout.setAlignment(Pos.CENTER);
		loginLayout.setVgap(10);
		loginLayout.setHgap(10);

		Label greetings = new Label("Log in to access your data.");
		GridPane.setHalignment(greetings, HPos.RIGHT);
		greetings.setMaxWidth(Double.MAX_VALUE);
		greetings.setStyle("-fx-font-weight: bold;");
		greetings.setPadding(new Insets(0, 0, 5, 0));
		GridPane.setConstraints(greetings, 1, 0);

		Separator separator = new Separator();
		separator.setValignment(VPos.CENTER);
		separator.setPadding(new Insets(0, 0, 20, 0));
		GridPane.setConstraints(separator, 0, 1);
		GridPane.setColumnSpan(separator, 7);

		ImageView usrnmImg = new ImageView("com/eStudent/gui/icons/username.png");
		usrnmImg.setFitHeight(20);
		usrnmImg.setFitWidth(20);
		GridPane.setConstraints(usrnmImg, 2, 3);

		Label usernameLabel = new Label("Username");
		usernameLabel.setId("usrnm-label");
		usernameLabel.setPadding(new Insets(0, 0, 0, 30));
		GridPane.setConstraints(usernameLabel, 0, 3);

		TextField username = new TextField();
		username.setOnKeyPressed(e -> {
			username.setStyle("-fx-focus-color: purple ;");
		});
		username.setPromptText("username");
		username.setId("username");
		username.setMaxWidth(200);
		GridPane.setConstraints(username, 1, 3);

		Label passwordLabel = new Label("Password");
		passwordLabel.setId("pw-label");
		passwordLabel.setPadding(new Insets(0, 0, 0, 30));
		GridPane.setConstraints(passwordLabel, 0, 4);

		PasswordField password = new PasswordField();
		password.setOnKeyPressed(e -> {
			password.setStyle("-fx-focus-color: purple;");
		});
		password.setPromptText("password");
		password.setId("password");
		password.setMaxWidth(200);
		GridPane.setConstraints(password, 1, 4);

		ImageView pwImg = new ImageView("com/eStudent/gui/icons/password.png");
		pwImg.setFitHeight(20);
		pwImg.setFitWidth(20);
		GridPane.setConstraints(pwImg, 2, 4);

		Label error = new Label();
		error.setId("error");
		GridPane.setHalignment(error, HPos.RIGHT);
		GridPane.setConstraints(error, 1, 7);

		login = new Button("Log in");
		login.setId("login-button");
		login.setOnAction(e -> {
			userLogin(username.getText().trim(), password.getText());

			if (isValid) {
				window.setScene(Dashboard.display(currentlySignedIn.getUserType(), logOut));
				window.setTitle("Welcome");
				window.getIcons().add(new Image("com/eStudent/gui/icons/icon.png"));
				window.setResizable(false);
			} else {
				error.setVisible(true);
				error.setText("Invalid login credentials!");
				error.setGraphic(new ImageView("com/eStudent/gui/icons/error.png"));
				error.setTextFill(Color.rgb(210, 39, 30));
				username.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
				password.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
			}
		});

		GridPane.setHalignment(login, HPos.RIGHT);
		GridPane.setConstraints(login, 1, 5);

		loginLayout.getChildren().addAll(greetings, separator, usernameLabel, usrnmImg, username, passwordLabel, pwImg,
				password, login, error);

		mainLayout.setRight(loginLayout);
		mainLayout.setLeft(leftSide);

		scene = new Scene(mainLayout, 850, 550);
		scene.getStylesheets().add("com/eStudent/gui/css/login.css");

		logOut = new Button("Log out");
		logOut.setOnAction(e -> {
			window.setScene(scene);
			window.setTitle("E-Student / Login");
			error.setText("");
			error.setVisible(false);
			username.clear();
			password.clear();
		});
		logOut.setPrefSize(230, 40);

		window.setScene(scene);
		window.show();
	}

	private void closeApplication() {
		Boolean choice = ConfirmationDialog.displayConfirmationDialog("Close application",
				"Are you sure you want to exit?");
		if (choice)
			window.close();
	}

	/*
	 * Creates json files with default user objects if they don't exist already
	 */
	private static void userDataInit() {

		Student defaultStudent1 = new Student("Nikola", "Lausev", "lausevnikola", "password123", false,
				UserType.STUDENT, "nikola.lausev.18@singidunum.com", "0650000000",
				new RecordBook("270243", "Singidunum University", "2018", "Nikola", "Lausev",
						Faculty.FACULTY_OF_TECHNICAL_SCIENCES, Majors.SOFTWARE_AND_INFORMATION_ENGINEERING,
						YearOfStudy.FRESHMAN, new JSONArray()));
		Student defaultStudent2 = new Student("Jane", "Doe", "janeDoe", "qwerty123", false, UserType.STUDENT,
				"jane.doe@gmail.com", "0640000000",
				new RecordBook("270001", "Singidunum University", "2019", "Jane", "Doe", Faculty.FACULTY_OF_BUSINESS,
						Majors.BUSINESS_ECONOMICS, YearOfStudy.SOPHMORE, new JSONArray()));
		Student defaultStudent3 = new Student("John", "Doe", "johnDoe", "ytrewq321", false, UserType.STUDENT,
				"john.doe@yahoo.com", "0620000000",
				new RecordBook("270002", "Singidunum University", "2077", "John", "Doe",
						Faculty.FACULTY_OF_INFORMATICS_AND_COMPUTING, Majors.ELECTRICAL_ENGINEERING_AND_COMPUTING,
						YearOfStudy.SENIOR, new JSONArray()));

		Admin defaultAdmin = new Admin("Root", "Root", "root", "root", false, UserType.ADMIN);

		Professor defaultProfessor1 = new Professor("Steve", "Wozniak", "steveW", "elppa321", false, UserType.PROFESSOR,
				"steve.wozniak@apple.com", AcademicTitles.RESEARCH_PROFESSOR);
		Professor defaultProfessor2 = new Professor("Donald", "Knuth", "donaldK", "donaldEK", false, UserType.PROFESSOR,
				"donald.knuth@stanford.edu", AcademicTitles.PROFESSOR);
		Professor defaultProfessor3 = new Professor("Nick", "Parlante", "nickP", "parlante101", false,
				UserType.PROFESSOR, "nick.parlante@stanford.edu", AcademicTitles.PROFESSOR);

		JSONObject ds1 = new JSONObject(defaultStudent1);
		JSONObject ds2 = new JSONObject(defaultStudent2);
		JSONObject ds3 = new JSONObject(defaultStudent3);

		JSONObject da = new JSONObject(defaultAdmin);

		JSONObject dp1 = new JSONObject(defaultProfessor1);
		JSONObject dp2 = new JSONObject(defaultProfessor2);
		JSONObject dp3 = new JSONObject(defaultProfessor3);

		JSONArray defaultUsers = new JSONArray();
		defaultUsers.put(ds1);
		defaultUsers.put(ds2);
		defaultUsers.put(ds3);

		defaultUsers.put(da);

		defaultUsers.put(dp1);
		defaultUsers.put(dp2);
		defaultUsers.put(dp3);

		File users = new File(PATH_TO_USERS);

		if (!users.exists()) {
			writeData(users, defaultUsers);
		}
	}

	/*
	 * Creates json files with default courses if they don't exist already
	 */
	private static void courseDataInit() {
		Course defaultCourse1 = new Course("AB18SOOP", "Object Oriented Programming",
				"Principles and implementation issues in object-oriented programming languages,"
						+ " including: memory and run-time models; encapsulation, inheritance and polymorphism;"
						+ " generics. Collections and other frameworks and hierarchies. Effects of binding time"
						+ " considerations on language design and implementation. Introduction to design patterns,"
						+ " such as adapter, singleton, and model-view-controller. Language used - Java EE",
				"2018", 4, 4, YearOfStudy.FRESHMAN, Majors.SOFTWARE_AND_INFORMATION_ENGINEERING);

		Course defaultCourse2 = new Course("BA19SFP", "Functional Programming",
				"This course introduces the student to the functional programming paradigm."
						+ " Functional programming techniques are illustrated in the Haskell language."
						+ " A significant portion of the learning occurs through the programming examples"
						+ " that are discussed in class. Programming projects will be required.",
				"2019", 3, 5, YearOfStudy.SOPHMORE, Majors.SOFTWARE_AND_INFORMATION_ENGINEERING);

		Course defaultCourse3 = new Course("SH77DM", "Discrete Mathematics",
				"This course covers widely applicable mathematical tools for computer science,"
						+ " including topics from logic, set theory, combinatorics, number theory,"
						+ " probability theory, and graph theory."
						+ " It includes practice in reasoning formally and proving theorems.",
				"2077", 4, 2, YearOfStudy.JUNIOR, Majors.ELECTRICAL_ENGINEERING_AND_COMPUTING);

		JSONObject course1 = new JSONObject(defaultCourse1);
		JSONObject course2 = new JSONObject(defaultCourse2);
		JSONObject course3 = new JSONObject(defaultCourse3);

		JSONArray defaultCourses = new JSONArray();
		defaultCourses.put(course1);
		defaultCourses.put(course2);
		defaultCourses.put(course3);

		File courses = new File(PATH_TO_COURSES);

		if (!courses.exists()) {
			writeData(new File(PATH_TO_COURSES), defaultCourses);
		}
	}

	public static void writeData(File file, JSONArray itemToWrite) {
		try {
			PrintWriter write = new PrintWriter(file);
			write.write(itemToWrite.toString(4));
			write.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Loads users and courses
	 */
	protected static void dataLoader() {
		try {
			users = new JSONArray(new JSONTokener(new FileInputStream(PATH_TO_USERS)));
			courses = new JSONArray(new JSONTokener(new FileInputStream(PATH_TO_COURSES)));
		} catch (JSONException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void userLogin(String username, String password) {
		dataLoader();
		for (Object obj : users) {
			JSONObject o = (JSONObject) obj;

			if (o.getString("username").equals(username) && o.getString("password").equals(password)
					&& o.get("userType").equals("STUDENT") && !o.getBoolean("deleted")) {
				JSONObject recordBook = o.getJSONObject("recordBook");
				currentlySignedIn = new Student(o.getString("firstName"), o.getString("lastName"),
						o.getString("username"), o.getString("password"), o.getBoolean("deleted"), UserType.STUDENT,
						o.getString("email"), o.getString("phoneNumber"),
						new RecordBook(recordBook.getString("id"), recordBook.getString("university"),
								recordBook.getString("enrollmentYear"), recordBook.getString("firstName"),
								recordBook.getString("lastName"), Faculty.valueOf(recordBook.optString("faculty")),
								Majors.valueOf(recordBook.optString("major")),
								YearOfStudy.valueOf(recordBook.optString("yearOfStudy")),
								recordBook.getJSONArray("exams")));
				isValid = true;
				break;
			} else if (o.getString("username").equals(username) && o.getString("password").equals(password)
					&& o.get("userType").equals("PROFESSOR") && !o.getBoolean("deleted")) {
				currentlySignedIn = new Professor(o.getString("firstName"), o.getString("lastName"),
						o.getString("username"), o.getString("password"), o.getBoolean("deleted"), UserType.PROFESSOR,
						o.getString("email"), AcademicTitles.valueOf((String) o.get("academicTitle")));
				isValid = true;
				break;
			} else if (o.getString("username").equals(username) && o.getString("password").equals(password)
					&& o.get("userType").equals("ADMIN") && !o.getBoolean("deleted")) {
				currentlySignedIn = new Admin(o.getString("firstName"), o.getString("lastName"),
						o.getString("username"), o.getString("password"), o.getBoolean("deleted"), UserType.ADMIN);
				isValid = true;
				break;
			} else {
				isValid = false;
				currentlySignedIn = null;
			}
		}
	}
}
