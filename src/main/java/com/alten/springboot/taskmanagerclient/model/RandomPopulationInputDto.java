package com.alten.springboot.taskmanagerclient.model;

import java.io.Serializable;

public class RandomPopulationInputDto implements Serializable {

    private String start;

    private String end;

    private int teams_size;

    private int employees_size;

    private int tasks_size;

    private int task_max_duration;

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

    public int getTeams_size() {
        return teams_size;
    }

    public void setTeams_size(int teams_size) {
        this.teams_size = teams_size;
    }

    public int getEmployees_size() {
        return employees_size;
    }

    public void setEmployees_size(int employees_size) {
        this.employees_size = employees_size;
    }

    public int getTasks_size() {
        return tasks_size;
    }

    public void setTasks_size(int tasks_size) {
        this.tasks_size = tasks_size;
    }

    public int getTask_max_duration() {
        return task_max_duration;
    }

    public void setTask_max_duration(int task_max_duration) {
        this.task_max_duration = task_max_duration;
    }

}
