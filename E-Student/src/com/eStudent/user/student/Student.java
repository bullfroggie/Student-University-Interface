package com.eStudent.user.student;

import com.eStudent.user.User;
import com.eStudent.user.UserType;

public class Student extends User {

	private String email, phoneNumber;
	private RecordBook recordBook; // recordBook is somewhat similar to "Indeks" in Serbian

	public Student() {
		/*
		 * default constructor
		 */
	}

	public Student(String firstName, String lastName, String username, String password, Boolean deleted,
			UserType userType, String email, String phoneNumber, RecordBook recordBook) {
		super(firstName, lastName, username, password, deleted, userType);
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.recordBook = recordBook;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public RecordBook getRecordBook() {
		return recordBook;
	}

	public void setRecordBook(RecordBook recordBook) {
		this.recordBook = recordBook;
	}

}
