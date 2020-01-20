package com.alten.springboot.taskmanagerclient.service;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.alten.springboot.taskmanagerclient.model.EmployeeDto;


@Path("/auth")
public interface ILoginController {
	
	@POST
	@Path("/login")
	public EmployeeDto login( @FormParam("username") String username, @FormParam("password")  String password);

	@GET
	@Path("/logout")
	public String logout() ;

}
