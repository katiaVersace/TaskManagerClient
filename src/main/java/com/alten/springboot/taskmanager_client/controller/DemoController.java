package com.alten.springboot.taskmanager_client.controller;

import java.util.List;

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
import com.alten.springboot.taskmanager_client.model.RandomPopulationInputDto;
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

	@GetMapping("/home")
	public String sayHello(Model theModel) {

		if (session == null) {
			return "redirect:/showLoginForm";
		}

		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("admin", session.isAdmin());

		return "home";
	}

	@GetMapping("/employees")
	public String getEmployees(Model theModel) {

		if (session == null) {
			return "redirect:/showLoginForm";
		}

		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("employees", employeeClient.getEmployees());
		theModel.addAttribute("admin", session.isAdmin());
		return "list-employees";
	}

	@GetMapping("/tasks")
	public String getTasks(Model theModel) {

		if (session == null) {
			return "redirect:/showLoginForm";
		}
		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("tasks", taskClient.getTasks());
		theModel.addAttribute("admin", session.isAdmin());

		return "list-tasks";
	}

	@GetMapping("/teams")
	public String getTeams(Model theModel) {

		if (session == null) {
			return "redirect:/showLoginForm";
		}

		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("teams", teamClient.getTeams());
		theModel.addAttribute("admin", session.isAdmin());
		return "list-teams";
	}

	@GetMapping("/tasks/{employeeId}")
	public String getTasks(@PathVariable("employeeId") int employeeId, Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}

		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("tasksOwner", employeeClient.getEmployee(employeeId));
		theModel.addAttribute("tasks", taskClient.getTasksByEmployeeId(Integer.toString(employeeId)));
		theModel.addAttribute("admin", session.isAdmin());
		return "list-tasks_by_employee";
	}

	@GetMapping("/teams/{teamId}")
	public String getEmployeesByTeam(@PathVariable("teamId") int teamId, Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
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

			return "redirect:/home";
		} else {
			theModel.addAttribute("error", true);
			return showLoginForm(theModel);
		}

	}

	@GetMapping("/logout")
	public String logout(Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}

		loginClient.logout();
		session = null;
		theModel.addAttribute("logout", true);
		return showLoginForm(theModel);
	}

	@GetMapping("/addTaskForm/{employeeId}")
	public String addTaskForm(@PathVariable("employeeId") int employeeId, Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}

		if (!session.isAdmin()) {
			return "access-denied";
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
			return "redirect:/showLoginForm";
		}
		if (!session.isAdmin()) {
			return "access-denied";
		}
		TeamDto team = new TeamDto();

		theModel.addAttribute("team", team);
		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("admin", session.isAdmin());

		theModel.addAttribute("employeesNotInTeam", employeeClient.getEmployees());

		return "team-form";
	}

	@GetMapping("/deleteTask/{taskId}")
	public String deleteTaskForm(@PathVariable("taskId") int taskId, Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}
		if (!session.isAdmin()) {
			return "access-denied";
		}
		taskClient.deleteTask(Integer.toString(taskId));
		return "redirect:/home";

	}

	@GetMapping("/updateTaskForm/{taskId}")
	public String updateTaskForm(@PathVariable("taskId") String taskId, Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}

		TaskDto theTask = taskClient.getTask(taskId);

		if (!(session.isAdmin() || session.getUser().getId() == theTask.getEmployeeId())) {
			return "access-denied";
		}

		EmployeeDto theEmployee = employeeClient.getEmployee(theTask.getEmployeeId());

		theModel.addAttribute("taskOwnerUserName", theEmployee.getUserName());

		theModel.addAttribute("task", theTask);
		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("admin", session.isAdmin());

		return "task-form";
	}

	@GetMapping("/updateTeamForm/{teamId}")
	public String updateTeamForm(@PathVariable("teamId") int teamId, Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}

		if (!session.isAdmin()) {
			return "access-denied";
		}
		TeamDto theTeam = teamClient.getTeam(teamId);
		List<EmployeeDto> notInTeam = employeeClient.getEmployees();

		for (EmployeeDto employeeInTeam : theTeam.getEmployees()) {
			for (EmployeeDto employee : notInTeam) {
				if (employee.getId() == employeeInTeam.getId()) {
					notInTeam.remove(employee);
					break;
				}
			}
		}

		theModel.addAttribute("team", theTeam);
		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("admin", session.isAdmin());
		theModel.addAttribute("employeesNotInTeam", notInTeam);

		return "team-form";
	}

	@PostMapping("/saveTask")
	public String saveTask(@ModelAttribute("task") TaskDto theTaskDto, Model theModel) {

		if (session == null) {
			return "redirect:/showLoginForm";
		}

		if (session.isAdmin()) {
			if (theTaskDto.getId() == 0) {
				if (taskClient.addTask(theTaskDto) == null) {
					theModel.addAttribute("error", true);
					return addTaskForm(theTaskDto.getEmployeeId(), theModel);
				}
			} else
				taskClient.updateTaskAdmin(theTaskDto);
		}

		else if (session.getUser().getId() == theTaskDto.getEmployeeId()) {
			taskClient.updateTask(theTaskDto);
		} else {
			return "access-denied";
		}

		return getTasks(theTaskDto.getEmployeeId(), theModel);

	}

	@PostMapping("/saveTeam")
	public String saveTeam(@ModelAttribute("team") TeamDto theTeamDto, Model theModel) {

		if (session == null) {
			return "redirect:/showLoginForm";
		}

		if (session.isAdmin()) {
			if (theTeamDto.getId() == 0) {
				teamClient.addTeam(theTeamDto);
			} else {
				theTeamDto.setEmployees(teamClient.getTeam(theTeamDto.getId()).getEmployees());
				teamClient.updateTeam(theTeamDto);

			}
		} else {
			return "access-denied";
		}

		return "redirect:/teams";

	}

	@PostMapping("/addEmployee/{teamId}/{employeeId}")
	public String addEmployeeToTeam(@PathVariable("teamId") int teamId, @PathVariable("employeeId") int employeeId,
			Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}
		if (session.isAdmin()) {

			TeamDto team = teamClient.getTeam(teamId);

			boolean alreadyInTeam = false;

			for (EmployeeDto e : team.getEmployees()) {
				if (e.getId() == employeeId)
					alreadyInTeam = true;
			}
			if (!alreadyInTeam) {
				EmployeeDto theEmployee = employeeClient.getEmployee(employeeId);
				team.getEmployees().add(theEmployee);
				teamClient.updateTeam(team);
			}

			return "redirect:/updateTeamForm/" + teamId;
		} else {
			return "access-denied";
		}

	}

	@GetMapping("/deleteTeam/{teamId}")
	public String deleteTeamForm(@PathVariable("teamId") int teamId, Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}
		if (!session.isAdmin()) {
			return "access-denied";
		}
		teamClient.deleteTeam(teamId);
		return "redirect:/teams";

	}

	@GetMapping("/removeEmployeeFromTeam/{teamId}/{employeeId}")
	public String removeEmployeeFromTeam(@PathVariable("teamId") int teamId, @PathVariable("employeeId") int employeeId,
			Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}
		if (session.isAdmin()) {

			TeamDto team = teamClient.getTeam(teamId);

			EmployeeDto employeeToRemove = null;
			for (EmployeeDto e : team.getEmployees()) {
				if (e.getId() == employeeId)
					employeeToRemove = e;
			}
			if (employeeToRemove != null) {

				team.getEmployees().remove(employeeToRemove);
				teamClient.updateTeam(team);
			}

			return "redirect:/updateTeamForm/" + teamId;
		} 
		else {
			return "access-denied";
		}

	}

	@GetMapping("/addTaskToTeamForm")
	public String addTaskToTeamForm(Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}
		if (!session.isAdmin() ) {
			return "access-denied";
		}

		TaskDto theTask = (TaskDto) theModel.getAttribute("task");
		if (theTask == null) {
			theTask = new TaskDto();
		}

		theModel.addAttribute("task", theTask);
		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("admin", session.isAdmin());
		theModel.addAttribute("teams", teamClient.getTeams());

		return "task_to_team_form";
	}

	@PostMapping("assignTaskToTeam/{teamId}")
	public String assignTaskToTeam(@ModelAttribute("task") TaskDto theTaskDto, Model theModel,
			@PathVariable("teamId") int teamId) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}
		if (!session.isAdmin() ) {
			return "access-denied";
		}

		TaskDto assignedTask = teamClient.assignTaskToTeam(teamId, theTaskDto);
		if (assignedTask != null) {

			return "redirect:/updateTaskForm/" + assignedTask.getId();
		}

		else {
			theModel.addAttribute("task", theTaskDto);
			theModel.addAttribute("error", true);

			return addTaskToTeamForm(theModel);
		}

	}

	@GetMapping("/randomPopulationForm")
	public String randomPopulationForm(Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}
		if (!session.isAdmin() ) {
			return "access-denied";
		}

		RandomPopulationInputDto input = new RandomPopulationInputDto();

		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("admin", session.isAdmin());
		theModel.addAttribute("input", input);

		return "random_population_form";
	}

	@PostMapping("/randomPopulation")
	public String randomPopulation(@ModelAttribute("input") RandomPopulationInputDto input, Model theModel) {
		
		if (session == null) {
			return "redirect:/showLoginForm";
		}
		
		if (!session.isAdmin() ) {
			return "access-denied";
		}

		String result = teamClient.randomPopulation(input);

		if (result.equals("invalid input")) {
			theModel.addAttribute("error", true);
			return randomPopulationForm(theModel);
		}

		return "redirect:/home";
	}

	@GetMapping("/addEmployeeForm")
	public String addEmployeeForm(Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}
		
		if (!session.isAdmin() ) {
			return "access-denied";
		}
		EmployeeDto employee = new EmployeeDto();

		theModel.addAttribute("employee", employee);
		theModel.addAttribute("user", session.getUser());
		theModel.addAttribute("admin", session.isAdmin());

		return "employee-form";
	}

	@PostMapping("/saveEmployee/{admin}")
	public String saveEmployee(@ModelAttribute("employee") EmployeeDto employeeDto, Model theModel,
			@PathVariable("admin") int admin) {

		if (session == null) {
			return "redirect:/showLoginForm";
		}
		if (!session.isAdmin() ) {
			return "access-denied";
		}

		employeeClient.addEmployee(admin, employeeDto);

		return "redirect:/employees";

	}

	@GetMapping("/deleteEmployee/{employeeId}")
	public String deleteEmployee(@PathVariable("employeeId") int employeeId, Model theModel) {
		if (session == null) {
			return "redirect:/showLoginForm";
		}
		if (!session.isAdmin() ) {
			return "access-denied";
		}
		employeeClient.deleteEmployee(String.valueOf(employeeId));
		return "redirect:/employees";

	}

}
