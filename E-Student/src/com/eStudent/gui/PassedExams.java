package com.eStudent.gui;

import org.json.JSONArray;
import org.json.JSONObject;

import com.eStudent.courses.Exam;
import com.eStudent.courses.ExamTerm;
import com.eStudent.user.professor.ProfessorFunctionality;
import com.eStudent.user.student.Faculty;
import com.eStudent.user.student.Majors;
import com.eStudent.user.student.RecordBook;
import com.eStudent.user.student.Student;
import com.eStudent.user.student.YearOfStudy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PassedExams extends ProfessorFunctionality {

	private static TableView<Exam> table;
	private static HBox horizontalLayout;
	private static RecordBook recordBook;
	private static JSONArray exams;
	private static Student selected;

	@SuppressWarnings({ "unchecked" })
	public static void display(Student student) {
		Stage window = new Stage();
		window.getIcons().add(new Image("com/eStudent/gui/icons/icon.png"));
		window.setResizable(false);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Passed Exams");

		selected = student;

		table = new TableView<>();
		table.getItems().clear();
		table.setItems(getPassed());

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

		horizontalLayout = new HBox();
		horizontalLayout.getChildren().clear();
		horizontalLayout.setPadding(new Insets(20, 20, 20, 20));
		horizontalLayout.setSpacing(20);

		table.getItems().clear();
		table.setItems(getPassed());
		table.setId("passedTable");
		Scene scene = new Scene(table);
		scene.getStylesheets().add("com/eStudent/gui/css/dialog.css");
		window.setScene(scene);
		window.showAndWait();
	}

	public static ObservableList<Exam> getPassed() {
		dataLoader();
		ObservableList<Exam> examList = FXCollections.observableArrayList();
		for (Object obj : users) {
			JSONObject o = (JSONObject) obj;
			if (o.getString("username").equals(selected.getUsername())) {
				JSONObject x = o.getJSONObject("recordBook");
				recordBook = new RecordBook(x.getString("id"), x.getString("university"), x.getString("enrollmentYear"),
						x.getString("firstName"), x.getString("lastName"), Faculty.valueOf(x.optString("faculty")),
						Majors.valueOf(x.optString("major")), YearOfStudy.valueOf(x.optString("yearOfStudy")),
						x.getJSONArray("exams"));
			}
		}

		exams = recordBook.getExams();

		for (Object obj : exams) {
			JSONObject exam = (JSONObject) obj;
			if (exam.getInt("grade") < 6)
				continue;
			if (exam.getBoolean("annulled"))
				continue;
			examList.add(new Exam(selected.getRecordBook().getId(), exam.getInt("points"), exam.getDouble("grade"),
					exam.getString("examDate"), exam.getBoolean("annulled"), exam.getString("name"),
					ExamTerm.valueOf(exam.optString("examTerm"))));
		}
		return examList;
	}
}
