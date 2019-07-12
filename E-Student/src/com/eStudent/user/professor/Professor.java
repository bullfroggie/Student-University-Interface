package com.eStudent.user.professor;

import com.eStudent.user.User;
import com.eStudent.user.UserType;

public class Professor extends User {

	private String email;
	AcademicTitles academicTitle;

	public Professor() {
		/*
		 * default constructor
		 */
	}

	public Professor(String firstName, String lastName, String username, String password, Boolean deleted,
			UserType userType, String email, AcademicTitles academicTitle) {
		super(firstName, lastName, username, password, deleted, userType);
		this.email = email;
		this.academicTitle = academicTitle;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AcademicTitles getAcademicTitle() {
		return academicTitle;
	}

	public void setAcademicTitle(AcademicTitles academicTitle) {
		this.academicTitle = academicTitle;
	}

}
