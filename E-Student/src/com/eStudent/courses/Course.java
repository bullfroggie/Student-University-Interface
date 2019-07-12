package com.eStudent.courses;

import com.eStudent.user.student.Majors;
import com.eStudent.user.student.YearOfStudy;

public class Course {

	private String id, name, syllabus, courseYear;
	private int classCredits, practicalClassCredits;
	private YearOfStudy yearOfStudy;
	private Majors major;

	public Course() {
		/*
		 * default constructor
		 */
	}

	public Course(String id, String name, String syllabus, String courseYear, int classCredits,
			int practicalClassCredits, YearOfStudy yearOfStudy, Majors major) {

		this.id = id;
		this.name = name;
		this.syllabus = syllabus;
		this.courseYear = courseYear;
		this.classCredits = classCredits;
		this.practicalClassCredits = practicalClassCredits;
		this.yearOfStudy = yearOfStudy;
		this.major = major;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSyllabus() {
		return syllabus;
	}

	public void setSyllabus(String syllabus) {
		this.syllabus = syllabus;
	}

	public String getCourseYear() {
		return courseYear;
	}

	public void setCourseYear(String courseYear) {
		this.courseYear = courseYear;
	}

	public int getClassCredits() {
		return classCredits;
	}

	public void setClassCredits(int classCredits) {
		this.classCredits = classCredits;
	}

	public int getPracticalClassCredits() {
		return practicalClassCredits;
	}

	public void setPracticalClassCredits(int practicalClassCredits) {
		this.practicalClassCredits = practicalClassCredits;
	}

	public YearOfStudy getYearOfStudy() {
		return yearOfStudy;
	}

	public void setYearOfStudy(YearOfStudy yearOfStudy) {
		this.yearOfStudy = yearOfStudy;
	}

	public Majors getMajor() {
		return major;
	}

	public void setMajor(Majors major) {
		this.major = major;
	}

}
