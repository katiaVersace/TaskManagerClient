package com.alten.springboot.taskmanagerclient.service;

import com.alten.springboot.taskmanagerclient.model.RandomPopulationInputDto;
import com.alten.springboot.taskmanagerclient.model.TaskDto;
import com.alten.springboot.taskmanagerclient.model.TeamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

@Service
public class TeamService {

    public static final String SERVER_URI = "http://localhost:8080/teams/";

    @Autowired
    private MyRestTemplate restTemplate;

    @Retryable(value = {ConnectException.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public List<TeamDto> getTeams() {

        return restTemplate.getForList(SERVER_URI, TeamDto.class);
    }


    public TeamDto getTeam(int taskId) {

        return restTemplate.getForObject(SERVER_URI + taskId + "/", TeamDto.class);
    }


    public TeamDto addTeam(TeamDto theTeam) {

        return restTemplate.postUpdatePatchObject(SERVER_URI, theTeam, HttpMethod.POST);
    }


    public TeamDto updateTeam(TeamDto theTeam) {

        return restTemplate.postUpdatePatchObject(SERVER_URI, theTeam, HttpMethod.PUT);
    }


    public String deleteTeam(String teamId) {

        return restTemplate.deleteObject(SERVER_URI + teamId + "/");
    }


    public String randomPopulation(RandomPopulationInputDto input) {

        return restTemplate.getResultOfOperation(SERVER_URI + "randomPopulation/", input, HttpMethod.POST);
    }


    public TaskDto assignTaskToTeam(int teamId, TaskDto task) {

        return restTemplate.postUpdatePatchObject(SERVER_URI + "assignTaskToTeam/" + teamId + "/", task, HttpMethod.POST);
    }
}
