package com.alten.springboot.taskmanagerclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

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
		return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}

}
