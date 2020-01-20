package com.alten.springboot.taskmanagerclient.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class EmployeeDto implements Serializable {

	private int id;

	private String userName;

	private String password;

	private String firstName;

	private String lastName;

	private String email;

	private boolean topEmployee;

	private Collection<RoleDto> roles;

	private int version;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isTopEmployee() {
		return topEmployee;
	}

	public void setTopEmployee(boolean topEmployee) {
		this.topEmployee = topEmployee;
	}

	public Collection<RoleDto> getRoles() {
		if (roles == null)
			roles = new ArrayList<RoleDto>();
		return roles;
	}

	public void setRoles(Collection<RoleDto> roles) {
		this.roles = roles;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
