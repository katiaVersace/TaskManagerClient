package com.alten.springboot.taskmanager_client.model;

import java.io.Serializable;



public class AssignTaskToTeamInputDto implements Serializable{
	
	private String start;

	
	private String end;

	
	private int team_id;
	
	
	private TaskDto task;
	
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

	public TaskDto getTask() {
		return task;
	}

	public void setTask(TaskDto theTask) {
		this.task = theTask;
	}

	public int getTeam_id() {
		return team_id;
	}

	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}

	
}
