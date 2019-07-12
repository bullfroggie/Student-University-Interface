package com.eStudent.user.admin;

import com.eStudent.user.User;
import com.eStudent.user.UserType;

public class Admin extends User {

	public Admin() {
		/*
		 * default constructor
		 */
	}

	public Admin(String firstName, String lastName, String username, String password, Boolean deleted,
			UserType userType) {
		super(firstName, lastName, username, password, deleted, userType);
	}

}
