package com.alten.springboot.taskmanager_client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TeamDto implements Serializable{
	
	private int id;
	
	private String name;
	
	private List<EmployeeDto> employees;
	
	private int version;

	public TeamDto() {
		super();
	}

	public TeamDto(String name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EmployeeDto> getEmployees() {
		if(employees==null)
			employees = new ArrayList<EmployeeDto>();
		return employees;
	}

	public void setEmployees(List<EmployeeDto> employees) {
		this.employees = employees;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}


	
	
	

}
