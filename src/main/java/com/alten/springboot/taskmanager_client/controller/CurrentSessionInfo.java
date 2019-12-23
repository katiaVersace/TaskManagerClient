package com.alten.springboot.taskmanager_client.controller;

import org.apache.cxf.transport.http.HTTPConduit;

import com.alten.springboot.taskmanager_client.model.EmployeeDto;
import com.alten.springboot.taskmanager_client.model.RoleDto;

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
		for (RoleDto role : user.getRoles()) {
			if (role.getName().equals("ROLE_ADMIN")) {

				return true;
			}
		}
		return false;
	}

}
