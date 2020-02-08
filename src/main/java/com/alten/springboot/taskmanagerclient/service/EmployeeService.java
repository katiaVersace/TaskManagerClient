package com.alten.springboot.taskmanagerclient.service;

import com.alten.springboot.taskmanagerclient.controller.CurrentSessionInfo;
import com.alten.springboot.taskmanagerclient.model.AvailabilityByEmployeeInputDto;
import com.alten.springboot.taskmanagerclient.model.EmployeeDto;
import com.alten.springboot.taskmanagerclient.model.TaskDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmployeeService {

    public static final String SERVER_URI = "http://localhost:8080/employees/";
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private RestTemplate restTemplate;

    public List<EmployeeDto> getEmployees() {

        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(SERVER_URI, HttpMethod.GET, request, String.class);
        try {
            return mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, EmployeeDto.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public EmployeeDto getEmployee(int employeeId) {

        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(SERVER_URI + employeeId + "/", HttpMethod.GET, request, String.class);
        try {
            return mapper.readValue(response.getBody(), EmployeeDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public EmployeeDto addEmployee(int admin, EmployeeDto theEmployee) {

        try {
            String jsonEmp = mapper.writeValueAsString(theEmployee);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request_post = new HttpEntity<>(jsonEmp, CurrentSessionInfo.getHeaders());
            String result = restTemplate.postForObject(SERVER_URI + admin + "/", request_post, String.class);
            return mapper.readValue(result, EmployeeDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public EmployeeDto updateEmployee(EmployeeDto theEmployee) {

        try {
            String jsonEmp = mapper.writeValueAsString(theEmployee);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request_update = new HttpEntity<>(jsonEmp, CurrentSessionInfo.getHeaders());
            HttpEntity<String> response = restTemplate.exchange(SERVER_URI, HttpMethod.PUT, request_update, String.class);
            return mapper.readValue(response.getBody(), EmployeeDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String deleteEmployee(String employeeId) {

        CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(SERVER_URI + employeeId + "/", HttpMethod.DELETE, request, String.class);
        return response.getBody();
    }

    public List<EmployeeDto> getAvailableEmployeesByTeamAndTask(int teamId, TaskDto theTask) {

        try {
            String jsonTask = mapper.writeValueAsString(theTask);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonTask, CurrentSessionInfo.getHeaders());
            HttpEntity<String> response = restTemplate.exchange(SERVER_URI + "employeesByTeamAndTask/" + teamId + "/", HttpMethod.GET, request, String.class);
            return mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, EmployeeDto.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAvailabilityByEmployee(AvailabilityByEmployeeInputDto input) {

        try {
            String jsonInput = mapper.writeValueAsString(input);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonInput, CurrentSessionInfo.getHeaders());
            HttpEntity<String> response = restTemplate.exchange(SERVER_URI + "availability/", HttpMethod.GET, request, String.class);
            return response.getBody();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}