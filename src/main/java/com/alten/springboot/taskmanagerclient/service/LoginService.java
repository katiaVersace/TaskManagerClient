package com.alten.springboot.taskmanagerclient.service;

import com.alten.springboot.taskmanagerclient.model.EmployeeDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService {

    public static final String SERVER_URI = "http://localhost:8080/auth/";

    @Autowired
    private MyRestTemplate restTemplate;
    @Autowired
    ObjectMapper mapper;


    public EmployeeDto login(String username, String password, HttpHeaders headers) {

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", username);
        map.add("password", password);
        ResponseEntity<String> response = restTemplate.postFormEncoded(SERVER_URI + "/login", map, headers);

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

        return restTemplate.getForEntity(SERVER_URI + "logout/");
    }
}
