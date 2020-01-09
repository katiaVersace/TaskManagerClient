package com.alten.springboot.taskmanager_client;

import java.util.Collections;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alten.springboot.taskmanager_client.service.IEmployeeRestController;
import com.alten.springboot.taskmanager_client.service.ILoginController;
import com.alten.springboot.taskmanager_client.service.ITaskRestController;
import com.alten.springboot.taskmanager_client.service.ITeamController;

@Component
public class BeansInstantiator {

	@Bean
	public IEmployeeRestController getEmployeeClient() {

		IEmployeeRestController proxy = JAXRSClientFactory.create("http://localhost:8080/resteasy",
				IEmployeeRestController.class, Collections.singletonList(new JacksonJaxbJsonProvider()));

		WebClient.client(proxy).accept(MediaType.APPLICATION_JSON);

		WebClient.getConfig(proxy).getRequestContext().put(org.apache.cxf.message.Message.MAINTAIN_SESSION,
				Boolean.TRUE);

		return proxy;
	}

	@Bean
	public ITaskRestController getTaskClient() {
		ITaskRestController proxy = JAXRSClientFactory.create("http://localhost:8080/resteasy",
				ITaskRestController.class, Collections.singletonList(new JacksonJaxbJsonProvider()));
		WebClient.client(proxy).accept(MediaType.APPLICATION_JSON);
		WebClient.getConfig(proxy).getRequestContext().put(org.apache.cxf.message.Message.MAINTAIN_SESSION,
				Boolean.TRUE);

		WebClient.getConfig(proxy).getRequestContext().put("use.async.http.conduit", Boolean.TRUE);

		return proxy;
	}

	@Bean
	public ILoginController getLoginClient() {
		ILoginController proxy = JAXRSClientFactory.create("http://localhost:8080/resteasy", ILoginController.class,
				Collections.singletonList(new JacksonJaxbJsonProvider()));
		WebClient.client(proxy).accept(MediaType.APPLICATION_JSON);
		WebClient.getConfig(proxy).getRequestContext().put(org.apache.cxf.message.Message.MAINTAIN_SESSION,
				Boolean.TRUE);

		return proxy;
	}

	@Bean
	public ITeamController getTeamClient() {
		ITeamController proxy = JAXRSClientFactory.create("http://localhost:8080/resteasy", ITeamController.class,
				Collections.singletonList(new JacksonJaxbJsonProvider()));
		WebClient.client(proxy).accept(MediaType.APPLICATION_JSON);
		WebClient.getConfig(proxy).getRequestContext().put(org.apache.cxf.message.Message.MAINTAIN_SESSION,
				Boolean.TRUE);

		return proxy;
	}

	@Bean
	public org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider jsonProvider() {
		JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
		return jsonProvider;
	}

}
