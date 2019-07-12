package com.eStudent.user.student;

import org.json.JSONArray;

public class RecordBook extends Student {

	private String id, university, enrollmentYear, firstName, lastName;
	private Faculty faculty;
	private Majors major;
	private YearOfStudy yearOfStudy;
	private JSONArray exams;

	public RecordBook() {
		/*
		 * Default constructor
		 */
	}

	public RecordBook(String id, String university, String enrollmentYear, String firstName, String lastName,
			Faculty faculty, Majors major, YearOfStudy yearOfStudy, JSONArray exams) {
		super();
		this.id = id;
		this.university = university;
		this.enrollmentYear = enrollmentYear;
		this.firstName = firstName;
		this.lastName = lastName;
		this.faculty = faculty;
		this.major = major;
		this.yearOfStudy = yearOfStudy;
		this.exams = exams;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public Majors getMajor() {
		return major;
	}

	public void setMajor(Majors major) {
		this.major = major;
	}

	public String getEnrollmentYear() {
		return enrollmentYear;
	}

	public void setEnrollmentYear(String enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}

	public YearOfStudy getYearOfStudy() {
		return yearOfStudy;
	}

	public void setYearOfStudy(YearOfStudy yearOfStudy) {
		this.yearOfStudy = yearOfStudy;
	}

	public JSONArray getExams() {
		return exams;
	}

	public void setExams(JSONArray exams) {
		this.exams = exams;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
