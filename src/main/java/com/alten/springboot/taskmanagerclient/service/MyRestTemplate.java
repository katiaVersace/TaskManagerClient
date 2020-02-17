package com.alten.springboot.taskmanagerclient.service;

import com.alten.springboot.taskmanagerclient.controller.CurrentSessionInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.List;

@Component
public class MyRestTemplate {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    private RestTemplate restTemplate;

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public <T> List<T> getForList(String url, Class<T> tClass) {

        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        String response = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();
        if (response != null) {
            try {
                return mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, tClass));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public <T> T getForObject(String url, Class<T> tClass) {
        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        String response = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();
        if (response != null) {
            try {
                return mapper.readValue(response, tClass);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public <T> String getResultOfOperation(String url, T input, HttpMethod httpMethod) {
        try {
            String jsonInput = mapper.writeValueAsString(input);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonInput, CurrentSessionInfo.getHeaders());
            String response = restTemplate.exchange(url, httpMethod, request, String.class).getBody();
            return response;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public <T> T postUpdatePatchObject(String url, T object, HttpMethod httpMethod) {

        try {
            String jsonEmp = mapper.writeValueAsString(object);
            CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonEmp, CurrentSessionInfo.getHeaders());
            String response = restTemplate.exchange(url, httpMethod, request, String.class).getBody();

            // String result = restTemplate.postForObject(url, request, String.class);
            if (response != null)
                return (T) mapper.readValue(response, object.getClass());
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public String deleteObject(String url) {

        CurrentSessionInfo.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
        return response.getBody();
    }

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public ResponseEntity<String> postFormEncoded(String url, MultiValueMap<String, String> map, HttpHeaders headers) {

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return response;

    }

    @Retryable(value = { ConnectException.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public String getForEntity(String url) {

        HttpEntity<String> request = new HttpEntity<String>(CurrentSessionInfo.getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        return response.getBody();
    }

}
