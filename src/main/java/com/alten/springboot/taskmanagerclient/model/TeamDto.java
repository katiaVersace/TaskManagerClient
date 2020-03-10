package com.alten.springboot.taskmanagerclient.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class TeamDto implements Serializable {

    private int id;

    private String name;

    private Set<EmployeeDto> employees;

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

    public Set<EmployeeDto> getEmployees() {
        if (employees == null)
            employees = new HashSet<EmployeeDto>();
        return employees;
    }

    public void setEmployees(Set<EmployeeDto> employees) {
        this.employees = employees;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
