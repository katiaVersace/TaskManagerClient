package com.alten.springboot.taskmanager_client.model;

import java.io.Serializable;

public class AvailabilityByEmployeeInputDto implements Serializable {

	private String start;

	private String end;

	private int employee_id;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

}
