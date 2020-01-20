package com.alten.springboot.taskmanagerclient.service;

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

import com.alten.springboot.taskmanagerclient.model.RandomPopulationInputDto;
import com.alten.springboot.taskmanagerclient.model.TaskDto;
import com.alten.springboot.taskmanagerclient.model.TeamDto;

@Path("/teams")
public interface ITeamController {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<TeamDto> getTeams();

	@GET
	@Path("/{teamId}")
	@Produces(MediaType.APPLICATION_JSON)
	public TeamDto getTeam(@PathParam("teamId") int teamId);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TeamDto addTeam(@RequestBody TeamDto theTeam);

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TeamDto updateTeam(@RequestBody TeamDto theTeam);

	@DELETE
	@Path("/{teamId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteTeam(@PathParam("teamId") int teamId);

	@POST
	@Path("/randomPopulation")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String randomPopulation(RandomPopulationInputDto input);

	@POST
	@Path("/assignTaskToTeam/{teamId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TaskDto assignTaskToTeam(@PathParam("teamId") int teamId,
			TaskDto task);

}
