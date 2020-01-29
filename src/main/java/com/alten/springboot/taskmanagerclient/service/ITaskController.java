package com.alten.springboot.taskmanagerclient.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.RequestBody;

import com.alten.springboot.taskmanagerclient.model.TaskDto;



/*
 * To enable PATCH for RestEasy we need to define a annotation annotated with @HttpMethod
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("PATCH")
@interface PATCH {
}


@Path("/tasks")
public interface ITaskController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<TaskDto> getTasks();

	@GET
	@Path("/{taskId}")
	@Produces(MediaType.APPLICATION_JSON)
	public TaskDto getTask(@PathParam("taskId") String taskId);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TaskDto addTask(TaskDto theTask);

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TaskDto updateTaskAdmin(@RequestBody TaskDto theTask);

	@PATCH
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TaskDto updateTask(@RequestBody TaskDto theTask);

	@DELETE
	@Path("/{taskId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteTask(@PathParam("taskId") String taskId);

	@GET
	@Path("/tasksByEmployee/{employeeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TaskDto> getTasksByEmployeeId(@PathParam("employeeId") String employeeId);

}
