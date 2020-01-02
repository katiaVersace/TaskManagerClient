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

import com.alten.springboot.taskmanager_client.model.AvailabilityByEmployeeInputDto;
import com.alten.springboot.taskmanager_client.model.EmployeeDto;
import com.alten.springboot.taskmanager_client.model.TaskDto;

@Path("/employees")
public interface IEmployeeRestController {

	@GET
	// @Path("/employees")
	@Produces(MediaType.APPLICATION_JSON)
	public List<EmployeeDto> getEmployees();

	@GET
	@Path("/{employeeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public EmployeeDto getEmployee(@PathParam("employeeId") int employeeId);

	@POST
	// @Path("/employees")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public EmployeeDto addEmployee(@RequestBody EmployeeDto theEmployee);

	@PUT
	// @Path("/employees")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public EmployeeDto updateEmployee(@RequestBody EmployeeDto theEmployee);

	@DELETE
	@Path("/{employeeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteEmployee(@PathParam("employeeId") String employeeId);

	@GET
	@Path("/employeesByTeamAndTask/{teamId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<EmployeeDto> getAvailableEmployeesByTeamAndTask(@PathParam("teamId") int teamId, TaskDto theTask);

	@GET
	@Path("/availability")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAvailabilityByEmployee(AvailabilityByEmployeeInputDto input);

}
