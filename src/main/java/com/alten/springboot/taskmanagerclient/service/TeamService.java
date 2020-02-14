package com.alten.springboot.taskmanagerclient.service;

import com.alten.springboot.taskmanagerclient.controller.CurrentSessionInfo;
import com.alten.springboot.taskmanagerclient.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.List;

@Service
public class TeamService {

    public static final String SERVER_URI = "http://localhost:8080/teams/";
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private RestTemplate restTemplate;

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public List<TeamDto> getTeams() {

        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(SERVER_URI, HttpMethod.GET, request, String.class);
        if(response.getBody()!= null) {
            try {
                return mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, TeamDto.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        } return null;
    }

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public TeamDto getTeam(int taskId) {

        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(SERVER_URI + taskId + "/", HttpMethod.GET, request, String.class);
        if(response.getBody()!= null) {
            try {
                return mapper.readValue(response.getBody(), TeamDto.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        } return null;
    }

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public TeamDto addTeam(TeamDto theTeam) {

        try {
            String jsonTeam = mapper.writeValueAsString(theTeam);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request_post = new HttpEntity<>(jsonTeam, CurrentSessionInfo.getHeaders());
            String result = restTemplate.postForObject(SERVER_URI, request_post, String.class);
            if(result!= null)
            return mapper.readValue(result, TeamDto.class);
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public TeamDto updateTeam(TeamDto theTeam) {

        try {
            String jsonEmp = mapper.writeValueAsString(theTeam);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request_update = new HttpEntity<>(jsonEmp, CurrentSessionInfo.getHeaders());
            HttpEntity<String> response = restTemplate.exchange(SERVER_URI, HttpMethod.PUT, request_update, String.class);
            if(response.getBody()!= null)
            return mapper.readValue(response.getBody(), TeamDto.class);
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public String deleteTeam(String teamId) {

        CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(SERVER_URI + teamId + "/", HttpMethod.DELETE, request, String.class);
        return response.getBody();
    }

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public String randomPopulation(RandomPopulationInputDto input) {

        try {
            String jsonInput = mapper.writeValueAsString(input);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonInput, CurrentSessionInfo.getHeaders());
            HttpEntity<String> response = restTemplate.exchange(SERVER_URI + "randomPopulation/", HttpMethod.POST, request, String.class);
            return response.getBody();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public TaskDto assignTaskToTeam(int teamId, TaskDto task) {

        try {
            String jsonTask = mapper.writeValueAsString(task);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonTask, CurrentSessionInfo.getHeaders());
            HttpEntity<String> response = restTemplate.exchange(SERVER_URI + "assignTaskToTeam/"+teamId+"/", HttpMethod.POST, request, String.class);
           if(response.getBody()!=null)
            return mapper.readValue(response.getBody(), TaskDto.class);
           else return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
