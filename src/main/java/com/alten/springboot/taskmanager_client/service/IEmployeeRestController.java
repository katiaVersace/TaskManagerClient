package com.alten.springboot.taskmanager_client.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.RequestBody;

import com.alten.springboot.taskmanager_client.model.EmployeeDto;


@Path("/employees")
public interface IEmployeeRestController {

//	@ApiOperation(value = "Home", response = String.class)
//	@GetMapping("/")
//	public String getHello();

	@GET
	//@Path("/employees")
	@Produces(MediaType.APPLICATION_JSON)
	public List<EmployeeDto> getEmployees();

	@GET
	@Path("/{employeeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public EmployeeDto getEmployee(
			@PathParam("employeeId") int employeeId);

	
	@POST
	//@Path("/employees")
	@Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON)
	public EmployeeDto addEmployee(
			@RequestBody EmployeeDto theEmployee);

	
	@PUT
	//@Path("/employees")
	@Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.APPLICATION_JSON)
	public EmployeeDto updateEmployee(
			@RequestBody EmployeeDto theEmployee);

	
	@DELETE
	@Path("/{employeeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteEmployee(
			 @PathParam("employeeId") String employeeId);

	 


}
