package com.alten.springboot.taskmanager_client.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.RequestBody;

import com.alten.springboot.taskmanager_client.model.TaskDto;




@Path("/tasks")
public interface ITaskRestController {
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<TaskDto> getTasks();
	
	@GET
	@Path("/{taskId}")
	@Produces(MediaType.APPLICATION_JSON)
	public TaskDto getTask(
			@PathParam("taskId") String taskId) ;
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON)
	public TaskDto addTask(
			TaskDto theTask);
	
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON)
	public TaskDto updateTaskAdmin(
			 @RequestBody TaskDto theTask);
	
	
	@PATCH
	@Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON)
	public TaskDto updateTask(
			 @RequestBody TaskDto theTask);
	
	
	@DELETE
	@Path("/{taskId}") 
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteTask(
			 @PathParam("taskId") String taskId);
	
	
	@GET
	@Path("/tasksByEmployee/{employeeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TaskDto> getTasksByEmployeeId(
			@PathParam("employeeId") String employeeId) ;

	
	


}
