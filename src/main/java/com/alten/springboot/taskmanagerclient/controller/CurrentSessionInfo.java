package com.alten.springboot.taskmanagerclient.controller;

import org.apache.cxf.transport.http.HTTPConduit;

import com.alten.springboot.taskmanagerclient.model.EmployeeDto;
import com.alten.springboot.taskmanagerclient.model.RoleDto;

public class CurrentSessionInfo {

	private HTTPConduit sourceConduit;
	private EmployeeDto user;

	public CurrentSessionInfo(HTTPConduit sourceConduit, EmployeeDto user) {
		super();
		this.sourceConduit = sourceConduit;
		this.user = user;

	}

	public HTTPConduit getSourceConduit() {
		return sourceConduit;
	}

	public void setSourceConduit(HTTPConduit sourceConduit) {
		this.sourceConduit = sourceConduit;
	}

	public EmployeeDto getUser() {
		return user;
	}

	public void setUser(EmployeeDto user) {
		this.user = user;
	}

	public boolean isAdmin() {
		return user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
	}

}
