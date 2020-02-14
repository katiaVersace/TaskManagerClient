package com.alten.springboot.taskmanagerclient.service;

import com.alten.springboot.taskmanagerclient.controller.CurrentSessionInfo;
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
public class TaskService {

    public static final String SERVER_URI = "http://localhost:8080/tasks/";
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private RestTemplate restTemplate;

    public List<TaskDto> getTasks() {

        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(SERVER_URI, HttpMethod.GET, request, String.class);
        if(response.getBody()!= null) {
            try {
                return mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, TaskDto.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public TaskDto getTask(int taskId) {

        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(SERVER_URI + taskId + "/", HttpMethod.GET, request, String.class);
        if(response.getBody()!= null) {
            try {
                return mapper.readValue(response.getBody(), TaskDto.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public TaskDto addTask(TaskDto theTask) {

        try {
            String jsonTask = mapper.writeValueAsString(theTask);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request_post = new HttpEntity<>(jsonTask, CurrentSessionInfo.getHeaders());
            String result = restTemplate.postForObject(SERVER_URI, request_post, String.class);
            if(result!= null)
            return mapper.readValue(result, TaskDto.class);
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TaskDto updateTaskAdmin(TaskDto theTask) {

        try {
            String jsonTask = mapper.writeValueAsString(theTask);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request_update = new HttpEntity<>(jsonTask, CurrentSessionInfo.getHeaders());
            HttpEntity<String> response = restTemplate.exchange(SERVER_URI, HttpMethod.PUT, request_update, String.class);
            if(response.getBody()!= null)
            return mapper.readValue(response.getBody(), TaskDto.class);
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TaskDto updateTask(TaskDto theTask) {

        try {
            String jsonTask = mapper.writeValueAsString(theTask);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request_update = new HttpEntity<>(jsonTask, CurrentSessionInfo.getHeaders());
            HttpEntity<String> response = restTemplate.exchange(SERVER_URI, HttpMethod.PATCH, request_update, String.class);
            if(response.getBody()!= null)
            return mapper.readValue(response.getBody(), TaskDto.class);
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String deleteTask(String taskId) {

        CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(SERVER_URI + taskId + "/", HttpMethod.DELETE, request, String.class);
        return response.getBody();
    }

    public List<TaskDto> getTasksByEmployeeId(int employeeId) {

        HttpEntity<String> request = new HttpEntity<>(CurrentSessionInfo.getHeaders());
        HttpEntity<String> response = restTemplate.exchange(SERVER_URI + "tasksByEmployee/" + employeeId + "/", HttpMethod.GET, request, String.class);
        if(response.getBody()!= null) {
            try {
                return mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, TaskDto.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

}
