package com.eStudent.user.student;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.eStudent.courses.Exam;
import com.eStudent.courses.ExamTerm;
import com.eStudent.gui.ConfirmationDialog;
import com.eStudent.gui.Dashboard;
import com.eStudent.user.UserType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StudentFunctionality extends Dashboard {
	private static HBox horizontalLayout;
	private static VBox verticalLayout;
	private static TableView<Exam> table;
	private static JSONArray users = loadedUsers;
	private static JSONArray courses = loadedCourses;
	private static ComboBox<ExamTerm> examTerm = new ComboBox<>();
	private static ListView<String> list1;
	private static ListView<String> list2;
	private static Student signedInStudent;
	private static RecordBook recordBook = new RecordBook();
	private static JSONArray exams;

	private static Label firstName = new Label();
	private static Label lastName = new Label();
	private static Label username = new Label();
	private static Label email = new Label();
	private static Label phoneNumber = new Label();
	private static Label university = new Label();
	private static Label faculty = new Label();
	private static Label major = new Label();
	private static Label yearOfStudy = new Label();
	private static Label list2Label = new Label();
	private static Separator separator = new Separator();

	private static Button registerExam = new Button();
	private static Button examAnnulling = new Button();

	public static HBox display() {
		dataLoader();

		horizontalLayout = new HBox();
		horizontalLayout.getChildren().clear();

		for (Object obj : users) {
			JSONObject o = (JSONObject) obj;
			if (o.getString("username").equals(currentlySignedIn.getUsername())) {
				JSONObject x = o.getJSONObject("recordBook");
				recordBook = new RecordBook(x.getString("id"), x.getString("university"), x.getString("enrollmentYear"),
						x.getString("firstName"), x.getString("lastName"), Faculty.valueOf(x.optString("faculty")),
						Majors.valueOf(x.optString("major")), YearOfStudy.valueOf(x.optString("yearOfStudy")),
						x.getJSONArray("exams"));
				signedInStudent = new Student(o.getString("firstName"), o.getString("lastName"),
						o.getString("username"), o.getString("password"), o.getBoolean("deleted"),
						UserType.valueOf(o.optString("userType")), o.getString("email"), o.getString("phoneNumber"),
						recordBook);
			}
		}

		registerExam.setText("Register Exam");
		registerExam.setPrefWidth(250);
		registerExam.setOnAction(e -> {
			if (list1.getItems().isEmpty()) {
				Alert info = new Alert(AlertType.WARNING);
				info.setTitle("No Exams");
				info.setHeaderText("You don't have any exams to register!");
				info.show();
				return;
			}
			if (examTerm.getValue() == null) {
				Alert info = new Alert(AlertType.WARNING);
				info.setTitle("No Exam Term Selected");
				info.setHeaderText("You haven't selected an exam term!");
				info.show();
				return;
			}
			if (list1.getSelectionModel().getSelectedItem() == null) {
				Alert info = new Alert(AlertType.WARNING);
				info.setTitle("No Exams Selected");
				info.setHeaderText("You have to select an exam!");
				info.show();
				return;
			} else {
				examRegistration();
			}
		});

		examAnnulling.setText("Annul Exam");
		examAnnulling.setPrefWidth(250);
		examAnnulling.setOnAction(e -> {
			if (list2.getItems().isEmpty()) {
				Alert info = new Alert(AlertType.WARNING);
				info.setTitle("No Exams");
				info.setHeaderText("You don't have any exams to annul!");
				info.show();
				return;
			}
			if (list2.getSelectionModel().getSelectedItem() == null) {
				Alert info = new Alert(AlertType.WARNING);
				info.setTitle("No Exams Selected");
				info.setHeaderText("You have to select an exam!");
				info.show();
				return;
			} else {
				boolean confirm = ConfirmationDialog.displayConfirmationDialog("Annul Exam Confirmation",
						"Annul the selected exam?");
				if (confirm)
					annulExam();
			}
		});

		verticalLayout = new VBox(10);
		verticalLayout.getChildren().clear();

		list1 = new ListView<>();
		list1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		list1.getItems().removeAll(list1.getItems());
		list1.refresh();

		list2Label.setText("Registered/Passed exams ⤵");
		list2Label.setAlignment(Pos.BASELINE_CENTER);
		list2 = new ListView<>();
		list2.getItems().removeAll(list2.getItems());
		list2.refresh();

		Tooltip tip1 = new Tooltip("Annulled/Not Passed Exams");
		Tooltip.install(list1, tip1);

		Tooltip tip2 = new Tooltip("If an exam is registered or passed it will appear here");
		Tooltip.install(list2, tip2);

		exams = recordBook.getExams();
		exams.forEach(e -> {
			JSONObject exam = (JSONObject) e;
			for (Object obj : courses) {
				JSONObject o = (JSONObject) obj;
				if (exam != null && exam.getString("name").equals(o.getString("name")) && !exam.getBoolean("annulled")
						&& (exam.getInt("points") == 0 || exam.getInt("points") >= 51))
					list2.getItems().add(o.getString("name"));
			}
		});

		courses.forEach(e -> {
			JSONObject o = (JSONObject) e;
			list1.getItems().add(o.getString("name"));
			if (!YearOfStudy.valueOf(o.optString("yearOfStudy")).equals(recordBook.getYearOfStudy())
					|| !Majors.valueOf(o.optString("major")).equals(recordBook.getMajor())) {
				list1.getItems().remove(o.getString("name"));
			}
			for (String name : list2.getItems()) {
				if (name.equals(o.getString("name"))) {
					list1.getItems().remove(name);
				}
			}
		});

		list1.setPrefWidth(350);

		verticalLayout = new VBox(10);
		verticalLayout.getChildren().clear();

		examTerm.getItems().removeAll(ExamTerm.values());
		examTerm.getItems().addAll(ExamTerm.values());
		examTerm.getSelectionModel().selectFirst();
		examTerm.setId("comboBox");
		examTerm.setPrefWidth(270);

		Tooltip tip = new Tooltip("Here you can select an Exam Term");
		Tooltip.install(examTerm, tip);

		String facultyInitials = "";
		for (String initial : recordBook.getFaculty().toString().split("_")) {
			facultyInitials += initial.charAt(0);
		}

		String majorInitials = "";
		for (String initial : recordBook.getMajor().toString().split("_")) {
			majorInitials += initial.charAt(0);
		}

		firstName.setText("First Name:  " + signedInStudent.getFirstName());
		firstName.getStyleClass().add("studentInfo");

		lastName.setText("Last Name:  " + signedInStudent.getLastName());
		lastName.getStyleClass().add("studentInfo");

		username.setText("Username:  " + signedInStudent.getUsername());
		username.getStyleClass().add("studentInfo");

		email.setText("Email:  " + signedInStudent.getEmail());
		email.getStyleClass().add("studentInfo");

		phoneNumber.setText("Phone Number:  " + signedInStudent.getPhoneNumber());
		phoneNumber.getStyleClass().add("studentInfo");

		university.setText("University:  " + recordBook.getUniversity());
		university.getStyleClass().add("studentInfo");

		faculty.setText("Faculty:  " + facultyInitials);
		faculty.getStyleClass().add("studentInfo");

		major.setText("Major:  " + majorInitials);
		major.getStyleClass().add("studentInfo");

		yearOfStudy.setText("Study Year:  " + recordBook.getYearOfStudy());
		yearOfStudy.getStyleClass().add("studentInfo");

		verticalLayout.setPadding(new Insets(5, 10, 0, 10));
		verticalLayout.setPrefWidth(270);
		verticalLayout.getChildren().addAll(firstName, lastName, username, email, phoneNumber, university, faculty,
				major, yearOfStudy, separator, examTerm, registerExam, examAnnulling, list2Label, list2);
		horizontalLayout.setPrefSize(620, 550);
		horizontalLayout.setId("rightSide");
		horizontalLayout.getChildren().addAll(list1, verticalLayout);

		return horizontalLayout;
	}

	private static void examRegistration() {
		dataLoader();
		String exam = list1.getSelectionModel().getSelectedItem();
		list1.getItems().remove(exam);
		list2.getItems().add(exam);

		for (Object obj : courses) {
			JSONObject o = (JSONObject) obj;
			if (o.getString("name").equals(exam)) {
				JSONObject newExam = new JSONObject();
				newExam.accumulate("recordBookID", signedInStudent.getRecordBook().getId());
				newExam.accumulate("firstName", signedInStudent.getRecordBook().getFirstName());
				newExam.accumulate("lastName", signedInStudent.getRecordBook().getLastName());
				newExam.accumulate("id", o.getString("id"));
				newExam.accumulate("name", o.getString("name"));
				newExam.accumulate("classCredits", o.getInt("classCredits"));
				newExam.accumulate("practicalClassCredits", o.getInt("practicalClassCredits"));
				newExam.accumulate("examDate", "");
				newExam.accumulate("examTerm", examTerm.getValue());
				newExam.accumulate("points", 0);
				newExam.accumulate("grade", 0.0);
				newExam.accumulate("annulled", false);
				exams.put(newExam);
				break;
			}
		}
		writeData(new File(PATH_TO_USERS), users);
		examTerm.getItems().removeAll(ExamTerm.values());
		examTerm.getItems().addAll(ExamTerm.values());
		examTerm.getSelectionModel().selectFirst();
	}

	private static void annulExam() {
		String selected = list2.getSelectionModel().getSelectedItem();
		boolean eligible = false;
		for (Object obj : exams) {
			JSONObject o = (JSONObject) obj;
			if (o.getString("name").equals(selected) && !o.getBoolean("annulled") && o.getDouble("grade") >= 6) {
				o.put("annulled", true);
				eligible = true;
				break;
			}
		}

		if (!eligible) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Exam Annulling Error");
			error.setHeaderText("Only passed exams can be annulled!");
			error.show();
			return;
		}

		writeData(new File(PATH_TO_USERS), users);
		list2.getItems().remove(selected);
		list1.getItems().add(selected);
	}

	@SuppressWarnings("unchecked")
	public static VBox displayPassedExams() {
		dataLoader();

		table = new TableView<>();
		table.getItems().removeAll(table.getItems());
		table.setItems(getPassed());
		table.refresh();

		TableColumn<Exam, String> courseName = new TableColumn<>("Course Name");
		courseName.setMinWidth(200);
		courseName.setCellValueFactory(new PropertyValueFactory<>("course"));

		TableColumn<Exam, Integer> points = new TableColumn<>("Points");
		points.setMinWidth(140);
		points.setCellValueFactory(new PropertyValueFactory<>("points"));

		TableColumn<Exam, Double> grade = new TableColumn<>("Grade");
		grade.setMinWidth(140);
		grade.setCellValueFactory(new PropertyValueFactory<>("grade"));

		TableColumn<Exam, String> examDate = new TableColumn<>("Exam Date");
		examDate.setMinWidth(137);
		examDate.setCellValueFactory(new PropertyValueFactory<>("examDate"));

		table.getColumns().addAll(courseName, points, grade, examDate);

		Label gpa = new Label();
		gpa.setId("gpa");
		double gradePointAverage = 0.0;
		List<Double> grades = new ArrayList<>();
		for (Exam item : table.getItems()) {
			grades.add(grade.getCellObservableValue(item).getValue());
		}

		for (Double i : grades) {
			gradePointAverage += i;
		}

		gpa.setText(String.format("GPA: %.2f", gradePointAverage / grades.size()));
		gpa.setPadding(new Insets(0, 0, 0, 500));

		Tooltip tip = new Tooltip("Grade Point Average on a 10.0 scale");
		Tooltip.install(gpa, tip);

		horizontalLayout = new HBox();
		horizontalLayout.getChildren().clear();
		horizontalLayout.getChildren().add(gpa);
		horizontalLayout.setPadding(new Insets(20, 20, 20, 20));
		horizontalLayout.setSpacing(20);

		verticalLayout = new VBox();
		verticalLayout.getChildren().clear();
		verticalLayout.getChildren().addAll(table, horizontalLayout, separator);

		return verticalLayout;
	}

	public static ObservableList<Exam> getPassed() {
		ObservableList<Exam> examList = FXCollections.observableArrayList();
		examList.removeAll(examList);
		dataLoader();
		for (Object obj : users) {
			JSONObject o = (JSONObject) obj;
			if (o.getString("username").equals(currentlySignedIn.getUsername())) {
				JSONObject x = o.getJSONObject("recordBook");
				recordBook = new RecordBook(x.getString("id"), x.getString("university"), x.getString("enrollmentYear"),
						x.getString("firstName"), x.getString("lastName"), Faculty.valueOf(x.optString("faculty")),
						Majors.valueOf(x.optString("major")), YearOfStudy.valueOf(x.optString("yearOfStudy")),
						x.getJSONArray("exams"));
				signedInStudent = new Student(o.getString("firstName"), o.getString("lastName"),
						o.getString("username"), o.getString("password"), o.getBoolean("deleted"),
						UserType.valueOf(o.optString("userType")), o.getString("email"), o.getString("phoneNumber"),
						recordBook);
			}
		}

		exams = recordBook.getExams();

		for (Object obj : exams) {
			JSONObject exam = (JSONObject) obj;
			if (exam.getDouble("grade") < 6.0)
				continue;
			if (exam.getBoolean("annulled"))
				continue;
			examList.add(new Exam(signedInStudent.getRecordBook().getId(), exam.getInt("points"),
					exam.getDouble("grade"), exam.getString("examDate"), exam.getBoolean("annulled"),
					exam.getString("name"), ExamTerm.valueOf(exam.optString("examTerm"))));
		}
		return examList;
	}
}
