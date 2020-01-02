package com.alten.springboot.taskmanager_client.controller;


import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alten.springboot.taskmanager_client.model.EmployeeDto;
import com.alten.springboot.taskmanager_client.model.TaskDto;
import com.alten.springboot.taskmanager_client.model.TeamDto;
import com.alten.springboot.taskmanager_client.service.IEmployeeRestController;
import com.alten.springboot.taskmanager_client.service.ILoginController;
import com.alten.springboot.taskmanager_client.service.ITaskRestController;
import com.alten.springboot.taskmanager_client.service.ITeamController;

@Controller
public class DemoController {

	@Autowired
	private IEmployeeRestController employeeClient;

	@Autowired
	private ITaskRestController taskClient;

	@Autowired
	private ILoginController loginClient;
	
	@Autowired
	private ITeamController teamClient;

	private static CurrentSessionInfo session;

	// create a mapping for "/hello"

	@GetMapping("/hello")
	public String sayHello(Model theModel) {

		if (session == null) {
			return "fancy-login";
		}

		theModel.addAttribute("theDate", new java.util.Date());

		return "helloworld";
	}

	@GetMapping("/employees")
	public String getEmployees(Model theModel) {

		if (session == null) {
			return "fancy-login";
		}

		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("employees", employeeClient.getEmployees());
		return "list-employees";
	}

	@GetMapping("/tasks")
	public String getTasks(Model theModel) {

		if (session == null) {
			return "fancy-login";
		}

		theModel.addAttribute("tasks", taskClient.getTasks());
		theModel.addAttribute("admin", session.isAdmin());

		return "list-tasks";
	}

	@GetMapping("/tasks/{employeeId}")
	public String getTasks(@PathVariable("employeeId") int employeeId, Model theModel) {
		if (session == null) {
			return "fancy-login";
		}

		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("tasksOwner", employeeId);
		theModel.addAttribute("tasks", taskClient.getTasksByEmployeeId(Integer.toString(employeeId)));
		theModel.addAttribute("admin", session.isAdmin());
		return "list-tasks";
	}
	
	@GetMapping("/teams/{teamId}")
	public String getEmployeesByTeam(@PathVariable("teamId") int teamId, Model theModel) {
		if (session == null) {
			return "fancy-login";
		}
		
		TeamDto theTeam = teamClient.getTeam(teamId);

		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("team", theTeam);
		theModel.addAttribute("employees", theTeam.getEmployees());
		theModel.addAttribute("admin", session.isAdmin());
		return "list-employees_by_team";
	}

	@GetMapping("/showLoginForm")
	public String showLoginForm(Model theModel) {

		return "fancy-login";
	}

	@PostMapping("/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password,
			Model theModel) {

		EmployeeDto result = loginClient.login(username, password);
		if (result != null) {

			HTTPConduit loginConduit = WebClient.getConfig(loginClient).getHttpConduit();
			session = new CurrentSessionInfo(loginConduit, result);
			HTTPConduit taskConduit = WebClient.getConfig(taskClient).getHttpConduit();
			taskConduit.getCookies().putAll(loginConduit.getCookies());
			HTTPConduit employeeConduit = WebClient.getConfig(employeeClient).getHttpConduit();
			employeeConduit.getCookies().putAll(loginConduit.getCookies());
			HTTPConduit teamConduit = WebClient.getConfig(teamClient).getHttpConduit();
			teamConduit.getCookies().putAll(loginConduit.getCookies());

			theModel.addAttribute("user", session.getUser());
			theModel.addAttribute("teams", teamClient.getTeams());
			theModel.addAttribute("admin", session.isAdmin());
			return "list-teams";
		} else {
			theModel.addAttribute("error",true);
			return "fancy-login";
		}

	}

	@GetMapping("/logout")
	public String logout(Model theModel) {
		if (session == null) {
			return "fancy-login";
		}

		loginClient.logout();
		session = null;
		theModel.addAttribute("logout",true);
		return "fancy-login";
	}

	@GetMapping("/addTaskForm/{employeeId}")
	public String addTaskForm(@PathVariable("employeeId") int employeeId, Model theModel) {
		if (session == null) {
			return "fancy-login";
		}
		TaskDto theTask = new TaskDto();
		theTask.setEmployeeId(employeeId);
		
		EmployeeDto theEmployee = employeeClient.getEmployee(employeeId);
		theModel.addAttribute("taskOwnerUserName", theEmployee.getUserName());
		theModel.addAttribute("task", theTask);
		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("admin", session.isAdmin());

		return "task-form";
	}
	
	@GetMapping("/addTeamForm")
	public String addTeamForm(Model theModel) {
		if (session == null) {
			return "fancy-login";
		}
		TeamDto team = new TeamDto();
		
		theModel.addAttribute("team", team);
		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("admin", session.isAdmin());

		return "team-form";
	}

	@GetMapping("/deleteTask/{taskId}")
	public String deleteTaskForm(@PathVariable("taskId") int taskId, Model theModel) {
		if (session == null) {
			return "fancy-login";
		}
		taskClient.deleteTask(Integer.toString(taskId));

		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("employees", employeeClient.getEmployees());
		return "list-employees";

	}

	@GetMapping("/updateTaskForm/{taskId}")
	public String updateTaskForm(@PathVariable("taskId") String taskId, Model theModel) {
		if (session == null) {
			return "fancy-login";
		}
		
		
		TaskDto theTask = taskClient.getTask(taskId);
		
		EmployeeDto theEmployee = employeeClient.getEmployee(theTask.getEmployeeId());
		
		theModel.addAttribute("taskOwnerUserName", theEmployee.getUserName());

		theModel.addAttribute("task", theTask);
		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("admin", session.isAdmin());

		return "task-form";
	}

	@PostMapping("/saveTask")
	public String saveTask(@ModelAttribute("task") TaskDto theTaskDto, Model theModel) {

		// EmployeeDtoLight e = employeeClient.getEmployee(theTaskDto.getEmployee().getId());
		// theTaskDto.setEmployee(e);

		if (session == null) {
			return "fancy-login";
		}

		if (session.isAdmin()) {
			if (theTaskDto.getId() == 0) {
				taskClient.addTask(theTaskDto);
			} else
				taskClient.updateTaskAdmin(theTaskDto);
		}

		else {
			taskClient.updateTask(theTaskDto);
		}
		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("tasksOwner", theTaskDto.getEmployeeId());
		theModel.addAttribute("tasks",
				taskClient.getTasksByEmployeeId(Integer.toString(theTaskDto.getEmployeeId())));
		theModel.addAttribute("admin", session.isAdmin());
		return "list-tasks";

			}

}
