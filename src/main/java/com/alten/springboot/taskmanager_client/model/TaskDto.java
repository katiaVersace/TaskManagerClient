package com.alten.springboot.taskmanager_client.model;

import java.io.Serializable;

public class TaskDto implements Serializable {

	private int id;

	private String description;

	private int employeeId;

	private String expectedStartTime;

	private String realStartTime;

	private String expectedEndTime;

	private String realEndTime;

	private int version;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getExpectedStartTime() {
		return expectedStartTime;
	}

	public void setExpectedStartTime(String expectedStartTime) {
		this.expectedStartTime = expectedStartTime;
	}

	public String getRealStartTime() {
		return realStartTime;
	}

	public void setRealStartTime(String realStartTime) {
		this.realStartTime = realStartTime;
	}

	public String getExpectedEndTime() {
		return expectedEndTime;
	}

	public void setExpectedEndTime(String expectedEndTime) {
		this.expectedEndTime = expectedEndTime;
	}

	public String getRealEndTime() {
		return realEndTime;
	}

	public void setRealEndTime(String realEndTime) {
		this.realEndTime = realEndTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
