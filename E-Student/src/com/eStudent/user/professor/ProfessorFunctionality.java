package com.eStudent.user.professor;

import org.json.JSONArray;
import org.json.JSONObject;

import com.eStudent.courses.Exam;
import com.eStudent.courses.ExamTerm;
import com.eStudent.gui.AssessExam;
import com.eStudent.gui.Dashboard;
import com.eStudent.gui.PassedExams;
import com.eStudent.user.UserType;
import com.eStudent.user.student.Faculty;
import com.eStudent.user.student.Majors;
import com.eStudent.user.student.RecordBook;
import com.eStudent.user.student.Student;
import com.eStudent.user.student.YearOfStudy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProfessorFunctionality extends Dashboard {

	private static VBox layout;
	private static HBox horizontalLayout;
	private static VBox buttonHolder;
	private static Button searchButton = new Button();
	private static Button showPassed = new Button();
	private static Button gradeExam = new Button();
	private static TextField search = new TextField();
	private static TableView<Student> table = new TableView<>();
	private static TableView<Exam> examTable = new TableView<>();
	private static ComboBox<ExamTerm> examTerm = new ComboBox<>();
	private static Label searchLabel = new Label();
	private static JSONObject recordBook;
	private static Professor signedInProfessor;
	private static Separator separator = new Separator();

	public static VBox studentSearch() {
		layout = new VBox(10);

		searchLabel = new Label("Student Search");
		search = new TextField();
		search.setOnKeyReleased(e -> {
			displayInfo();
		});
		search.setPrefWidth(180);

		Tooltip tip = new Tooltip("Search using student's first name, last name, or record book number");
		Tooltip.install(search, tip);

		horizontalLayout = new HBox();
		horizontalLayout.getChildren().clear();
		horizontalLayout.getChildren().addAll(searchLabel, search);
		horizontalLayout.setPadding(new Insets(20, 20, 20, 20));
		horizontalLayout.setSpacing(20);
		horizontalLayout.setAlignment(Pos.CENTER);
		horizontalLayout.setId("rightSide");

		layout.getChildren().clear();
		layout.getChildren().addAll(horizontalLayout, separator);
		layout.setAlignment(Pos.TOP_CENTER);
		return layout;
	}

	@SuppressWarnings("unchecked")
	private static void displayInfo() {
		table = new TableView<>();
		table.getItems().clear();
		table.setItems(getStudents());

		TableColumn<Student, String> firstNameColumn = new TableColumn<>("First Name");
		firstNameColumn.setMinWidth(140);
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

		TableColumn<Student, String> lastNameColumn = new TableColumn<>("Last Name");
		lastNameColumn.setMinWidth(140);
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

		TableColumn<Student, String> usernameColumn = new TableColumn<>("Username");
		usernameColumn.setMinWidth(140);
		usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

		TableColumn<Student, String> emailColumn = new TableColumn<>("E-mail");
		emailColumn.setMinWidth(110);
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

		TableColumn<Student, String> phoneNumberColumn = new TableColumn<>("Phone Num.");
		phoneNumberColumn.setMinWidth(88);
		phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

		table.getColumns().addAll(firstNameColumn, lastNameColumn, usernameColumn, emailColumn, phoneNumberColumn);

		showPassed = new Button("Passed Exams");
		Tooltip tip = new Tooltip("Select student to view passed exams");
		Tooltip.install(showPassed, tip);
		showPassed.setOnAction(e -> {
			if (table.getSelectionModel().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Selection Error");
				alert.setHeaderText("Student must be selected first!");
				alert.show();
				return;
			}
			PassedExams.display(table.getSelectionModel().getSelectedItem());
		});
		horizontalLayout.getChildren().clear();
		horizontalLayout.getChildren().addAll(searchLabel, search, showPassed);

		layout.getChildren().clear();
		layout.getChildren().addAll(horizontalLayout, table);
	}

	public static ObservableList<Student> getStudents() {
		dataLoader();
		ObservableList<Student> studentList = FXCollections.observableArrayList();
		for (Object obj : users) {
			JSONObject user = (JSONObject) obj;
			if (!UserType.valueOf(user.optString("userType")).equals(UserType.STUDENT))
				continue;
			if (user.getBoolean("deleted"))
				continue;
			JSONObject recordBook = user.getJSONObject("recordBook");
			if (recordBook.getString("id").contains(search.getText().trim())) {
				studentList.add(new Student(user.getString("firstName"), user.getString("lastName"),
						user.getString("username"), user.getString("password"), user.getBoolean("deleted"),
						UserType.valueOf(user.optString("userType")), user.getString("email"),
						user.getString("phoneNumber"),
						new RecordBook(recordBook.getString("id"), recordBook.getString("university"),
								recordBook.getString("enrollmentYear"), recordBook.getString("firstName"),
								recordBook.getString("lastName"), Faculty.valueOf(recordBook.optString("faculty")),
								Majors.valueOf(recordBook.optString("major")),
								YearOfStudy.valueOf(recordBook.optString("yearOfStudy")),
								recordBook.getJSONArray("exams"))));
				continue;
			}
			if (user.getString("firstName").contains(search.getText().trim())
					|| user.getString("lastName").contains(search.getText().trim())
					|| user.getString("username").contains(search.getText().trim())) {
				studentList.add(new Student(user.getString("firstName"), user.getString("lastName"),
						user.getString("username"), user.getString("password"), user.getBoolean("deleted"),
						UserType.valueOf(user.optString("userType")), user.getString("email"),
						user.getString("phoneNumber"),
						new RecordBook(recordBook.getString("id"), recordBook.getString("university"),
								recordBook.getString("enrollmentYear"), recordBook.getString("firstName"),
								recordBook.getString("lastName"), Faculty.valueOf(recordBook.optString("faculty")),
								Majors.valueOf(recordBook.optString("major")),
								YearOfStudy.valueOf(recordBook.optString("yearOfStudy")),
								recordBook.getJSONArray("exams"))));
				continue;
			}
		}
		return studentList;
	}

	public static VBox examAssessment() {
		layout = new VBox(10);
		buttonHolder = new VBox(10);

		examTerm.getItems().addAll(ExamTerm.values());
		examTerm.setPromptText("Search by Exam Term");
		examTerm.setMaxWidth(125);
		examTerm.setId("comboBox");

		searchLabel = new Label("Exam Search");
		search = new TextField();
		search.setPrefWidth(180);
		searchButton = new Button("Search");
		searchButton.setPrefSize(100, 20);
		searchButton.setOnAction(e -> {
			examTable.getItems().clear();
			examTerm.setPromptText("Search by Exam Term");
			displayExams();
		});

		Tooltip tip = new Tooltip(
				"Search using an exam term or student's first and last name, as well as record book number");
		Tooltip.install(search, tip);

		buttonHolder.getChildren().clear();
		buttonHolder.getChildren().add(searchButton);
		horizontalLayout = new HBox();
		horizontalLayout.getChildren().clear();
		horizontalLayout.getChildren().addAll(searchLabel, search, examTerm, buttonHolder);
		horizontalLayout.setPadding(new Insets(20, 20, 20, 20));
		horizontalLayout.setSpacing(20);
		horizontalLayout.setAlignment(Pos.CENTER);
		horizontalLayout.setId("rightSide");

		layout.getChildren().clear();
		layout.getChildren().addAll(horizontalLayout, separator);
		layout.setAlignment(Pos.TOP_CENTER);
		return layout;
	}

	@SuppressWarnings("unchecked")
	private static void displayExams() {
		dataLoader();
		examTable = new TableView<>();
		examTable.getItems().removeAll(examTable.getItems());
		examTable.setItems(getExams());

		for (Object obj : users) {
			JSONObject o = (JSONObject) obj;
			if (o.getString("username").equals(currentlySignedIn.getUsername())) {
				signedInProfessor = new Professor(o.getString("firstName"), o.getString("lastName"),
						o.getString("username"), o.getString("password"), o.getBoolean("deleted"),
						UserType.valueOf(o.optString("userType")), o.getString("email"),
						AcademicTitles.valueOf(o.optString("academicTitle")));
				break;
			}
		}

		TableColumn<Exam, String> recordBookIDColumn = new TableColumn<>("Record Book ID");
		recordBookIDColumn.setMinWidth(140);
		recordBookIDColumn.setCellValueFactory(new PropertyValueFactory<>("recordBookNumber"));

		TableColumn<Exam, String> courseNameColumn = new TableColumn<>("Course Name");
		courseNameColumn.setMinWidth(200);
		courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("course"));

		TableColumn<Exam, Double> gradeColumn = new TableColumn<>("grade");
		gradeColumn.setMinWidth(80);
		gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

		TableColumn<Exam, Integer> pointsColumn = new TableColumn<>("Points");
		pointsColumn.setMinWidth(80);
		pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

		TableColumn<Exam, ExamTerm> examTermColumn = new TableColumn<>("Exam Term");
		examTermColumn.setMinWidth(117);
		examTermColumn.setCellValueFactory(new PropertyValueFactory<>("examTerm"));

		examTable.getColumns().addAll(recordBookIDColumn, courseNameColumn, gradeColumn, pointsColumn, examTermColumn);

		gradeExam = new Button("Assess Exam");
		Tooltip tip = new Tooltip("Select exam in order to assess it");
		Tooltip.install(showPassed, tip);
		gradeExam.setOnAction(e -> {
			if (examTable.getSelectionModel().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Selection Error");
				alert.setHeaderText("Exam must be selected first!");
				alert.show();
				return;
			}

			if (signedInProfessor.getAcademicTitle().equals(AcademicTitles.PROFESSOR)
					|| signedInProfessor.getAcademicTitle().equals(AcademicTitles.ASSISTANT_PROFESSOR)
					|| signedInProfessor.getAcademicTitle().equals(AcademicTitles.ASSOCIATE_PROFESSOR)) {
				AssessExam.display(examTable.getSelectionModel().getSelectedItem());
				examTable.getItems().clear();
				examTable.getItems().addAll(getExams());
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Ineligible Professor Error");
				alert.setHeaderText(
						"Exam can only be assessed by:\n1. Regular Professors\n2. Assistant Professors\n3. Associate Professors");
				alert.show();
				return;
			}

		});

		buttonHolder.getChildren().clear();
		buttonHolder.getChildren().addAll(searchButton, gradeExam);

		horizontalLayout.getChildren().clear();
		horizontalLayout.getChildren().addAll(searchLabel, search, examTerm, buttonHolder);

		layout.getChildren().clear();
		layout.getChildren().addAll(horizontalLayout, examTable);
	}

	public static ObservableList<Exam> getExams() {
		dataLoader();
		ObservableList<Exam> examList = FXCollections.observableArrayList();
		examList.removeAll(examList);

		JSONArray exams = new JSONArray();

		for (Object obj : users) {
			JSONObject o = (JSONObject) obj;
			if (UserType.valueOf(o.optString("userType")).equals(UserType.STUDENT) && !o.getBoolean("deleted")) {
				recordBook = o.getJSONObject("recordBook");
				exams.put(recordBook.getJSONArray("exams"));
			}

		}

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
				if (exam.getInt("points") < 51 && exam.getDouble("grade") != 0.0)
					continue;

				if (ExamTerm.valueOf(exam.optString("examTerm")).equals(examTerm.getValue())
						&& search.getText().trim().isEmpty()) {
					examList.add(new Exam(exam.getString("recordBookID"), exam.getInt("points"),
							exam.getDouble("grade"), exam.getString("examDate"), exam.getBoolean("annulled"),
							exam.getString("name"), ExamTerm.valueOf(exam.optString("examTerm"))));
					continue;
				} else if (search.getText().trim().equals(exam.getString("name"))
						&& ExamTerm.valueOf(exam.optString("examTerm")).equals(examTerm.getValue())) {
					examList.add(new Exam(exam.getString("recordBookID"), exam.getInt("points"),
							exam.getDouble("grade"), exam.getString("examDate"), exam.getBoolean("annulled"),
							exam.getString("name"), ExamTerm.valueOf(exam.optString("examTerm"))));
					continue;
				} else if (search.getText().trim().equals(exam.getString("recordBookID"))
						&& ExamTerm.valueOf(exam.optString("examTerm")).equals(examTerm.getValue())) {
					examList.add(new Exam(exam.getString("recordBookID"), exam.getInt("points"),
							exam.getDouble("grade"), exam.getString("examDate"), exam.getBoolean("annulled"),
							exam.getString("name"), ExamTerm.valueOf(exam.optString("examTerm"))));
					continue;
				} else if (search.getText().trim().equals(exam.getString("firstName"))
						&& ExamTerm.valueOf(exam.optString("examTerm")).equals(examTerm.getValue())) {
					examList.add(new Exam(exam.getString("recordBookID"), exam.getInt("points"),
							exam.getDouble("grade"), exam.getString("examDate"), exam.getBoolean("annulled"),
							exam.getString("name"), ExamTerm.valueOf(exam.optString("examTerm"))));
					continue;
				} else if (search.getText().trim().equals(exam.getString("lastName"))
						&& ExamTerm.valueOf(exam.optString("examTerm")).equals(examTerm.getValue())) {
					examList.add(new Exam(exam.getString("recordBookID"), exam.getInt("points"),
							exam.getDouble("grade"), exam.getString("examDate"), exam.getBoolean("annulled"),
							exam.getString("name"), ExamTerm.valueOf(exam.optString("examTerm"))));
					continue;
				}
			}
		}

		return examList;
	}
}
