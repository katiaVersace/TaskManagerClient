package com.alten.springboot.taskmanagerclient.service;

import com.alten.springboot.taskmanagerclient.controller.CurrentSessionInfo;
import com.alten.springboot.taskmanagerclient.model.EmployeeDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService {

    public static final String SERVER_URI = "http://localhost:8080/auth/";
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private RestTemplate restTemplate;

    public EmployeeDto login(String username, String password, HttpHeaders headers) {

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", username);
        map.add("password", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.exchange(SERVER_URI + "/login", HttpMethod.POST, request, String.class);
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode == HttpStatus.OK && response.getBody() != null) {

            try {
                EmployeeDto user = mapper.readValue(response.getBody(), EmployeeDto.class);
                List<String> cookies = response.getHeaders().get("Set-Cookie");
                headers.set("Cookie", cookies.stream().collect(Collectors.joining(";")));
                return user;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        } else
            return null;
    }

    public String logout() {

        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(SERVER_URI + "logout/", HttpMethod.GET, request, String.class);
        return response.getBody();
    }
}
