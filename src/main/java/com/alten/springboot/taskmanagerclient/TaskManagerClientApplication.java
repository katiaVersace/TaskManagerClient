package com.alten.springboot.taskmanagerclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

@EnableRetry
@SpringBootApplication
public class TaskManagerClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerClientApplication.class, args);
    }

    @Bean
    public org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider jsonProvider() {
        JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
        return jsonProvider;
    }

    @Bean
    public RestTemplate getRestTemplate() {

        RestTemplate restTemplate = new RestTemplate();
        HttpClient httpClient = HttpClientBuilder.create()
                .disableCookieManagement()
                .build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        return restTemplate;
    }


    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

}
