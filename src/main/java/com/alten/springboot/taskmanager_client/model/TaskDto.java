package com.alten.springboot.taskmanager_client.model;



import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;


public class TaskDto implements Serializable{
	
	private int id;

	private String description;

	private int employeeId;
	
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone="CET")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate expectedStartTime;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone="CET")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate realStartTime;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone="CET")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate expectedEndTime;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone="CET")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
		private LocalDate realEndTime;
	
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

	
	public LocalDate getExpectedStartTime() {
		return expectedStartTime;
	}

	public void setExpectedStartTime(LocalDate expectedStartTime) {
		this.expectedStartTime = expectedStartTime;
	}

	public LocalDate getRealStartTime() {
		return realStartTime;
	}

	public void setRealStartTime(LocalDate realStartTime) {
		this.realStartTime = realStartTime;
	}

	public LocalDate getExpectedEndTime() {
		return expectedEndTime;
	}

	public void setExpectedEndTime(LocalDate expectedEndTime) {
		this.expectedEndTime = expectedEndTime;
	}

	public LocalDate getRealEndTime() {
		return realEndTime;
	}

	public void setRealEndTime(LocalDate realEndTime) {
		this.realEndTime = realEndTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	


}
