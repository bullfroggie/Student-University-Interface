package com.eStudent.courses;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Exam {

	private final SimpleStringProperty recordBookNumber;
	private final SimpleIntegerProperty points;
	private final SimpleDoubleProperty grade;
	private final SimpleStringProperty examDate;
	private ExamTerm examTerm;
	private boolean annuled;
	private final SimpleStringProperty course;

	public Exam() {
		/*
		 * default constructor
		 */
		this(null, 0, 0.0, null, false, null, null);
	}

	public Exam(String recordBookNumber, Integer points, Double grade, String examDate, boolean annuled, String course,
			ExamTerm examTerm) {
		this.recordBookNumber = new SimpleStringProperty(recordBookNumber);
		this.points = new SimpleIntegerProperty(points);
		this.grade = new SimpleDoubleProperty(grade);
		this.examDate = new SimpleStringProperty(examDate);
		this.annuled = annuled;
		this.course = new SimpleStringProperty(course);
		this.examTerm = examTerm;
	}

	public SimpleStringProperty recordBookNumberProperty() {
		return recordBookNumber;
	}

	public SimpleIntegerProperty pointsProperty() {
		return points;
	}

	public SimpleDoubleProperty gradeProperty() {
		return grade;
	}

	public SimpleStringProperty examDateProperty() {
		return examDate;
	}

	public SimpleStringProperty courseProperty() {
		return course;
	}

	public int getPoints() {
		return points.get();
	}

	public void setPoints(int points) {
		this.points.set(points);
	}

	public double getGrade() {
		return grade.get();
	}

	public void setGrade(double grade) {
		this.grade.set(grade);
	}

	public String getExamDate() {
		return examDate.get();
	}

	public void setExamDate(String examDate) {
		this.examDate.set(examDate);
	}

	public boolean isAnnuled() {
		return annuled;
	}

	public void setAnnuled(boolean annuled) {
		this.annuled = annuled;
	}

	public String getCourse() {
		return course.get();
	}

	public void setCourse(String course) {
		this.course.set(course);
	}

	public ExamTerm getExamTerm() {
		return examTerm;
	}

	public void setExamTerm(ExamTerm examTerm) {
		this.examTerm = examTerm;
	}

	public String getRecordBookNumber() {
		return recordBookNumber.get();
	}

	public void setRecordBookNumber(String recordBookNumber) {
		this.recordBookNumber.set(recordBookNumber);
	}

	@Override
	public String toString() {
		return "Exam [recordBookNumber=" + recordBookNumber + ", points=" + points + ", grade=" + grade + ", examDate="
				+ examDate + ", examTerm=" + examTerm + ", annuled=" + annuled + ", course=" + course + "]";
	}

}
