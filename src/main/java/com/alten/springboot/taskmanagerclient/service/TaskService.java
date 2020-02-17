package com.alten.springboot.taskmanagerclient.service;

import com.alten.springboot.taskmanagerclient.model.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    public static final String SERVER_URI = "http://localhost:8080/tasks/";

    @Autowired
    private MyRestTemplate restTemplate;


    public List<TaskDto> getTasks() {

        return restTemplate.getForList(SERVER_URI, TaskDto.class);
    }


    public TaskDto getTask(int taskId) {

        return restTemplate.getForObject(SERVER_URI + taskId + "/", TaskDto.class);
    }


    public TaskDto addTask(TaskDto theTask) {

        return restTemplate.postUpdatePatchObject(SERVER_URI, theTask, HttpMethod.POST);
    }


    public TaskDto updateTaskAdmin(TaskDto theTask) {

        return restTemplate.postUpdatePatchObject(SERVER_URI, theTask, HttpMethod.PUT);
    }


    public TaskDto updateTask(TaskDto theTask) {

        return restTemplate.postUpdatePatchObject(SERVER_URI, theTask, HttpMethod.PATCH);
    }


    public String deleteTask(String taskId) {

        return restTemplate.deleteObject(SERVER_URI + taskId + "/");
    }


    public List<TaskDto> getTasksByEmployeeId(int employeeId) {

        return restTemplate.getForList(SERVER_URI + "tasksByEmployee/" + employeeId + "/", TaskDto.class);
    }

}
