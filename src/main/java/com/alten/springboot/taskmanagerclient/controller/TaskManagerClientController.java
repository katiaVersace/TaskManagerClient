package com.alten.springboot.taskmanagerclient.controller;

import com.alten.springboot.taskmanagerclient.model.EmployeeDto;
import com.alten.springboot.taskmanagerclient.model.RandomPopulationInputDto;
import com.alten.springboot.taskmanagerclient.model.TaskDto;
import com.alten.springboot.taskmanagerclient.model.TeamDto;
import com.alten.springboot.taskmanagerclient.service.EmployeeService;
import com.alten.springboot.taskmanagerclient.service.LoginService;
import com.alten.springboot.taskmanagerclient.service.TaskService;
import com.alten.springboot.taskmanagerclient.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TaskManagerClientController {

    private static CurrentSessionInfo session;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private TeamService teamService;

    @GetMapping("/home")
    public String sayHello(Model theModel) {

        if (session == null) {
            return "redirect:/showLoginForm";
        }

        theModel.addAttribute("user", session.getUser());
        theModel.addAttribute("admin", session.isAdmin());

        return "Home";
    }

    @GetMapping("/employees")
    public String getEmployees(Model theModel) {

        if (session == null) {
            return "redirect:/showLoginForm";
        }

        theModel.addAttribute("user", session.getUser());
        theModel.addAttribute("employees", employeeService.getEmployees());
        theModel.addAttribute("admin", session.isAdmin());
        return "Employees";
    }

    @GetMapping("/tasks")
    public String getTasks(Model theModel) {

        if (session == null) {
            return "redirect:/showLoginForm";
        }
        theModel.addAttribute("user", session.getUser());
        theModel.addAttribute("tasks", taskService.getTasks());
        theModel.addAttribute("admin", session.isAdmin());

        return "Tasks";
    }

    @GetMapping("/teams")
    public String getTeams(Model theModel) {

        if (session == null) {
            return "redirect:/showLoginForm";
        }

        theModel.addAttribute("user", session.getUser());
        theModel.addAttribute("teams", teamService.getTeams());
        theModel.addAttribute("admin", session.isAdmin());
        return "Teams";
    }

    @GetMapping("/tasks/{employeeId}")
    public String getTasks(@PathVariable("employeeId") int employeeId, Model theModel) {
        if (session == null) {
            return "redirect:/showLoginForm";
        }

        theModel.addAttribute("user", session.getUser());
        theModel.addAttribute("tasksOwner", employeeService.getEmployee(employeeId));
        theModel.addAttribute("tasks", taskService.getTasksByEmployeeId(employeeId));
        theModel.addAttribute("admin", session.isAdmin());
        return "TasksByEmployee";
    }

    @GetMapping("/teams/{teamId}")
    public String getEmployeesByTeam(@PathVariable("teamId") int teamId, Model theModel) {
        if (session == null) {
            return "redirect:/showLoginForm";
        }

        TeamDto theTeam = teamService.getTeam(teamId);

        theModel.addAttribute("user", session.getUser());
        theModel.addAttribute("team", theTeam);
        theModel.addAttribute("employees", theTeam.getEmployees());
        theModel.addAttribute("admin", session.isAdmin());
        return "EmployeesByTeam";
    }

    @GetMapping("/showLoginForm")
    public String showLoginForm(Model theModel) {

        return "Login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        Model theModel) {
        HttpHeaders headers = new HttpHeaders();

        EmployeeDto result = loginService.login(username, password, headers);
        if (result != null) {

            session = new CurrentSessionInfo(headers, result);

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

        loginService.logout();
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
            return "AccessDenied";
        }
        TaskDto theTask = new TaskDto();
        theTask.setEmployeeId(employeeId);

        EmployeeDto theEmployee = employeeService.getEmployee(employeeId);
        theModel.addAttribute("taskOwnerUserName", theEmployee.getUserName());
        theModel.addAttribute("task", theTask);
        theModel.addAttribute("user", session.getUser());
        theModel.addAttribute("admin", session.isAdmin());

        return "TaskForm";
    }

    @GetMapping("/addTeamForm")
    public String addTeamForm(Model theModel) {
        if (session == null) {
            return "redirect:/showLoginForm";
        }
        if (!session.isAdmin()) {
            return "AccessDenied";
        }
        TeamDto team = new TeamDto();

        theModel.addAttribute("team", team);
        theModel.addAttribute("user", session.getUser());
        theModel.addAttribute("admin", session.isAdmin());

        theModel.addAttribute("employeesNotInTeam", employeeService.getEmployees());

        return "TeamForm";
    }

    @GetMapping("/deleteTask/{taskId}")
    public String deleteTaskForm(@PathVariable("taskId") int taskId, Model theModel) {
        if (session == null) {
            return "redirect:/showLoginForm";
        }
        if (!session.isAdmin()) {
            return "AccessDenied";
        }
        taskService.deleteTask(Integer.toString(taskId));
        return "redirect:/home";

    }

    @GetMapping("/updateTaskForm/{taskId}")
    public String updateTaskForm(@PathVariable("taskId") int taskId, Model theModel) {
        if (session == null) {
            return "redirect:/showLoginForm";
        }

        TaskDto theTask = taskService.getTask(taskId);

        if (!(session.isAdmin() || session.getUser().getId() == theTask.getEmployeeId())) {
            return "AccessDenied";
        }

        EmployeeDto theEmployee = employeeService.getEmployee(theTask.getEmployeeId());

        theModel.addAttribute("taskOwnerUserName", theEmployee.getUserName());

        theModel.addAttribute("task", theTask);
        theModel.addAttribute("user", session.getUser());
        theModel.addAttribute("admin", session.isAdmin());

        return "TaskForm";
    }

    @GetMapping("/updateTeamForm/{teamId}")
    public String updateTeamForm(@PathVariable("teamId") int teamId, Model theModel) {
        if (session == null) {
            return "redirect:/showLoginForm";
        }

        if (!session.isAdmin()) {
            return "AccessDenied";
        }
        TeamDto theTeam = teamService.getTeam(teamId);
        List<EmployeeDto> notInTeam = employeeService.getEmployees();

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

        return "TeamForm";
    }

    @PostMapping("/saveTask")
    public String saveTask(@ModelAttribute("task") TaskDto theTaskDto, Model theModel) {

        if (session == null) {
            return "redirect:/showLoginForm";
        }

        if (session.isAdmin()) {
            if (theTaskDto.getId() == 0) {
                if (taskService.addTask(theTaskDto) == null) {
                    theModel.addAttribute("error", true);
                    return addTaskForm(theTaskDto.getEmployeeId(), theModel);
                }
            } else
                taskService.updateTaskAdmin(theTaskDto);
        } else if (session.getUser().getId() == theTaskDto.getEmployeeId()) {
            taskService.updateTask(theTaskDto);
        } else {
            return "AccessDenied";
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
                teamService.addTeam(theTeamDto);
            } else {
                theTeamDto.setEmployees(teamService.getTeam(theTeamDto.getId()).getEmployees());
                teamService.updateTeam(theTeamDto);

            }
        } else {
            return "AccessDenied";
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

            TeamDto team = teamService.getTeam(teamId);

            EmployeeDto employeeInTeam = team.getEmployees().stream().filter(e -> e.getId() == employeeId).findFirst().orElse(null);
            if (employeeInTeam == null) {
                EmployeeDto theEmployee = employeeService.getEmployee(employeeId);
                team.getEmployees().add(theEmployee);
                teamService.updateTeam(team);
            }

            return "redirect:/updateTeamForm/" + teamId;
        } else {
            return "AccessDenied";
        }

    }

    @GetMapping("/deleteTeam/{teamId}")
    public String deleteTeamForm(@PathVariable("teamId") String teamId, Model theModel) {
        if (session == null) {
            return "redirect:/showLoginForm";
        }
        if (!session.isAdmin()) {
            return "AccessDenied";
        }
        teamService.deleteTeam(teamId);
        return "redirect:/teams";

    }

    @GetMapping("/removeEmployeeFromTeam/{teamId}/{employeeId}")
    public String removeEmployeeFromTeam(@PathVariable("teamId") int teamId, @PathVariable("employeeId") int employeeId,
                                         Model theModel) {
        if (session == null) {
            return "redirect:/showLoginForm";
        }
        if (session.isAdmin()) {

            TeamDto team = teamService.getTeam(teamId);

            EmployeeDto employeeToRemove = team.getEmployees().stream().filter(e -> e.getId() == employeeId).findFirst().orElse(null);

            if (employeeToRemove != null) {

                team.getEmployees().remove(employeeToRemove);
                teamService.updateTeam(team);
            }

            return "redirect:/updateTeamForm/" + teamId;
        } else {
            return "AccessDenied";
        }

    }

    @GetMapping("/addTaskToTeamForm")
    public String addTaskToTeamForm(Model theModel) {
        if (session == null) {
            return "redirect:/showLoginForm";
        }
        if (!session.isAdmin()) {
            return "AccessDenied";
        }

        TaskDto theTask = (TaskDto) theModel.getAttribute("task");
        if (theTask == null) {
            theTask = new TaskDto();
        }

        theModel.addAttribute("task", theTask);
        theModel.addAttribute("user", session.getUser());
        theModel.addAttribute("admin", session.isAdmin());
        theModel.addAttribute("teams", teamService.getTeams());

        return "TaskToTeamForm";
    }

    @PostMapping("assignTaskToTeam/{teamId}")
    public String assignTaskToTeam(@ModelAttribute("task") TaskDto theTaskDto, Model theModel,
                                   @PathVariable("teamId") int teamId) {
        if (session == null) {
            return "redirect:/showLoginForm";
        }
        if (!session.isAdmin()) {
            return "AccessDenied";
        }

        TaskDto assignedTask = teamService.assignTaskToTeam(teamId, theTaskDto);
        if (assignedTask != null) {

            return "redirect:/updateTaskForm/" + assignedTask.getId();
        } else {
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
        if (!session.isAdmin()) {
            return "AccessDenied";
        }

        RandomPopulationInputDto input = new RandomPopulationInputDto();

        theModel.addAttribute("user", session.getUser());
        theModel.addAttribute("admin", session.isAdmin());
        theModel.addAttribute("input", input);

        return "RandomPopulationForm";
    }

    @PostMapping("/randomPopulation")
    public String randomPopulation(@ModelAttribute("input") RandomPopulationInputDto input, Model theModel) {

        if (session == null) {
            return "redirect:/showLoginForm";
        }

        if (!session.isAdmin()) {
            return "AccessDenied";
        }

        String result = teamService.randomPopulation(input);

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

        if (!session.isAdmin()) {
            return "AccessDenied";
        }
        EmployeeDto employee = new EmployeeDto();

        theModel.addAttribute("employee", employee);
        theModel.addAttribute("user", session.getUser());
        theModel.addAttribute("admin", session.isAdmin());

        return "EmployeeForm";
    }

    @PostMapping("/saveEmployee/{admin}")
    public String saveEmployee(@ModelAttribute("employee") EmployeeDto employeeDto, Model theModel,
                               @PathVariable("admin") int admin) {

        if (session == null) {
            return "redirect:/showLoginForm";
        }
        if (!session.isAdmin()) {
            return "AccessDenied";
        }

        employeeService.addEmployee(admin, employeeDto);

        return "redirect:/employees";

    }

    @GetMapping("/deleteEmployee/{employeeId}")
    public String deleteEmployee(@PathVariable("employeeId") int employeeId, Model theModel) {
        if (session == null) {
            return "redirect:/showLoginForm";
        }
        if (!session.isAdmin()) {
            return "AccessDenied";
        }
        String result = employeeService.deleteEmployee(String.valueOf(employeeId));
        if(result.contains("logout"))
            return "redirect:/logout";
        return "redirect:/employees";

    }
}
