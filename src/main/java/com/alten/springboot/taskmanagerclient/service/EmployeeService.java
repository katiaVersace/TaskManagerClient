package com.alten.springboot.taskmanagerclient.service;

import com.alten.springboot.taskmanagerclient.model.AvailabilityByEmployeeInputDto;
import com.alten.springboot.taskmanagerclient.model.EmployeeDto;
import com.alten.springboot.taskmanagerclient.model.TaskDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    public static final String SERVER_URI = "http://localhost:8080/employees/";
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MyRestTemplate restTemplate;


    public List<EmployeeDto> getEmployees() {

        return restTemplate.getForList(SERVER_URI, EmployeeDto.class);
    }


    public EmployeeDto getEmployee(int employeeId) {
        return restTemplate.getForObject(SERVER_URI + employeeId + "/", EmployeeDto.class);
    }


    public EmployeeDto addEmployee(int admin, EmployeeDto theEmployee) {

        return restTemplate.postUpdatePatchObject(SERVER_URI + admin + "/", theEmployee, HttpMethod.POST);
    }


    public EmployeeDto updateEmployee(EmployeeDto theEmployee) {

        return restTemplate.postUpdatePatchObject(SERVER_URI, theEmployee, HttpMethod.PUT);

    }


    public String deleteEmployee(String employeeId) {

        return restTemplate.deleteObject(SERVER_URI + employeeId + "/");
    }


    public List<EmployeeDto> getAvailableEmployeesByTeamAndTask(int teamId, TaskDto theTask) {

        return restTemplate.getForList(SERVER_URI + "employeesByTeamAndTask/" + teamId + "/", EmployeeDto.class);


    }


    public String getAvailabilityByEmployee(AvailabilityByEmployeeInputDto input) {

        return restTemplate.getResultOfOperation(SERVER_URI + "availability/", input, HttpMethod.GET);
    }
}
