package com.alten.springboot.taskmanagerclient.controller

import com.alten.springboot.taskmanagerclient.model.EmployeeDto
import com.alten.springboot.taskmanagerclient.model.RandomPopulationInputDto
import com.alten.springboot.taskmanagerclient.model.TaskDto
import com.alten.springboot.taskmanagerclient.model.TeamDto
import com.alten.springboot.taskmanagerclient.service.EmployeeService
import com.alten.springboot.taskmanagerclient.service.LoginService
import com.alten.springboot.taskmanagerclient.service.TaskService
import com.alten.springboot.taskmanagerclient.service.TeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@Controller
class TaskManagerClientController (@Autowired private val employeeService: EmployeeService,
                                   @Autowired private val taskService: TaskService,
                                   @Autowired  private val loginService: LoginService,
                                   @Autowired private val teamService: TeamService){

    @GetMapping("/home")
    fun sayHello(theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["admin"] = CurrentSessionInfo.isAdmin()

        return "Home"
    }

    @GetMapping("/employees")
    fun getEmployees(theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["employees"] = employeeService!!.employees
        theModel["admin"] = CurrentSessionInfo.isAdmin()
        return "Employees"
    }

    @GetMapping("/tasks")
    fun getTasks(theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["tasks"] = taskService!!.tasks
        theModel["admin"] = CurrentSessionInfo.isAdmin()
        return "Tasks"
    }

    @GetMapping("/teams")
    fun getTeams(theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["teams"] = teamService!!.teams
        theModel["admin"] = CurrentSessionInfo.isAdmin()
        return "Teams"
    }

    @GetMapping("/tasks/{employeeId}")
    fun getTasks(@PathVariable("employeeId") employeeId: Int, theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["tasksOwner"] = employeeService!!.getEmployee(employeeId)
        theModel["tasks"] = taskService!!.getTasksByEmployeeId(employeeId)
        theModel["admin"] = CurrentSessionInfo.isAdmin()
        return "TasksByEmployee"
    }

    @GetMapping("/teams/{teamId}")
    fun getEmployeesByTeam(@PathVariable("teamId") teamId: Int, theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        val theTeam = teamService!!.getTeam(teamId)
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["team"] = theTeam
        theModel["employees"] = theTeam.employees
        theModel["admin"] = CurrentSessionInfo.isAdmin()
        return "EmployeesByTeam"
    }

    @GetMapping("/showLoginForm")
    fun showLoginForm(theModel: Model?): String {
        return "Login"
    }

    @PostMapping("/login")
    fun login(@RequestParam("username") username: String?, @RequestParam("password") password: String?,
              theModel: Model): String {
        val headers = HttpHeaders()
        val result = loginService!!.login(username, password, headers)
        return if (result != null) {
            session = CurrentSessionInfo(headers, result)
            "redirect:/home"
        } else {
            theModel["error"] = true
            showLoginForm(theModel)
        }
    }

    @GetMapping("/logout")
    fun logout(theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        loginService!!.logout()
        session = null
        theModel["logout"] = true
        return showLoginForm(theModel)
    }

    @GetMapping("/addTaskForm/{employeeId}")
    fun addTaskForm(@PathVariable("employeeId") employeeId: Int, theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (!CurrentSessionInfo.isAdmin()) {
            return "AccessDenied"
        }
        val theTask = TaskDto()
        theTask.employeeId = employeeId
        val theEmployee = employeeService!!.getEmployee(employeeId)
        theModel["taskOwnerUserName"] = theEmployee.userName
        theModel["task"] = theTask
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["admin"] = CurrentSessionInfo.isAdmin()
        return "TaskForm"
    }

    @GetMapping("/addTeamForm")
    fun addTeamForm(theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (!CurrentSessionInfo.isAdmin()) {
            return "AccessDenied"
        }
        val team = TeamDto()
        theModel["team"] = team
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["admin"] = CurrentSessionInfo.isAdmin()
        theModel["employeesNotInTeam"] = employeeService!!.employees
        return "TeamForm"
    }

    @GetMapping("/deleteTask/{taskId}")
    fun deleteTaskForm(@PathVariable("taskId") taskId: Int, theModel: Model?, request: HttpServletRequest?): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (!CurrentSessionInfo.isAdmin()) {
            return "AccessDenied"
        }
        taskService!!.deleteTask(Integer.toString(taskId))
        return "redirect:/home"
    }

    @GetMapping("/updateTaskForm/{taskId}")
    fun updateTaskForm(@PathVariable("taskId") taskId: Int, theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        val theTask = taskService!!.getTask(taskId)
        if (!(CurrentSessionInfo.isAdmin() || CurrentSessionInfo.getUser().id == theTask.employeeId)) {
            return "AccessDenied"
        }
        val theEmployee = employeeService!!.getEmployee(theTask.employeeId)
        theModel["taskOwnerUserName"] = theEmployee.userName
        theModel["task"] = theTask
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["admin"] = CurrentSessionInfo.isAdmin()
        return "TaskForm"
    }

    @GetMapping("/updateTeamForm/{teamId}")
    fun updateTeamForm(@PathVariable("teamId") teamId: Int, theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (!CurrentSessionInfo.isAdmin()) {
            return "AccessDenied"
        }
        val theTeam = teamService!!.getTeam(teamId)
        val notInTeam = employeeService!!.employees
        for (employeeInTeam in theTeam.employees) {
            for (employee in notInTeam) {
                if (employee.id == employeeInTeam.id) {
                    notInTeam.remove(employee)
                    break
                }
            }
        }
        theModel["team"] = theTeam
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["admin"] = CurrentSessionInfo.isAdmin()
        theModel["employeesNotInTeam"] = notInTeam
        return "TeamForm"
    }

    @PostMapping("/saveTask")
    fun saveTask(@ModelAttribute("task") theTaskDto: TaskDto, theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (CurrentSessionInfo.isAdmin()) {
            if (theTaskDto.id == 0) {
                if (taskService!!.addTask(theTaskDto) == null) {
                    theModel["error"] = true
                    return addTaskForm(theTaskDto.employeeId, theModel)
                }
            } else taskService!!.updateTaskAdmin(theTaskDto)
        } else if (CurrentSessionInfo.getUser().id == theTaskDto.employeeId) {
            taskService!!.updateTask(theTaskDto)
        } else {
            return "AccessDenied"
        }
        return getTasks(theTaskDto.employeeId, theModel)
    }

    @PostMapping("/saveTeam")
    fun saveTeam(@ModelAttribute("team") theTeamDto: TeamDto, theModel: Model?): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (CurrentSessionInfo.isAdmin()) {
            if (theTeamDto.id == 0) {
                teamService!!.addTeam(theTeamDto)
            } else {
                theTeamDto.employees = teamService!!.getTeam(theTeamDto.id).employees
                teamService.updateTeam(theTeamDto)
            }
        } else {
            return "AccessDenied"
        }
        return "redirect:/teams"
    }

    @PostMapping("/addEmployee/{teamId}/{employeeId}")
    fun addEmployeeToTeam(@PathVariable("teamId") teamId: Int, @PathVariable("employeeId") employeeId: Int,
                          theModel: Model?): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        return if (CurrentSessionInfo.isAdmin()) {
            val team = teamService!!.getTeam(teamId)
            val employeeInTeam = team.employees.stream().filter { e: EmployeeDto -> e.id == employeeId }.findFirst().orElse(null)
            if (employeeInTeam == null) {
                val theEmployee = employeeService!!.getEmployee(employeeId)
                team.employees.add(theEmployee)
                teamService.updateTeam(team)
            }
            "redirect:/updateTeamForm/$teamId"
        } else {
            "AccessDenied"
        }
    }

    @GetMapping("/deleteTeam/{teamId}")
    fun deleteTeamForm(@PathVariable("teamId") teamId: String?, theModel: Model?): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (!CurrentSessionInfo.isAdmin()) {
            return "AccessDenied"
        }
        teamService!!.deleteTeam(teamId)
        return "redirect:/teams"
    }

    @GetMapping("/removeEmployeeFromTeam/{teamId}/{employeeId}")
    fun removeEmployeeFromTeam(@PathVariable("teamId") teamId: Int, @PathVariable("employeeId") employeeId: Int,
                               theModel: Model?): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        return if (CurrentSessionInfo.isAdmin()) {
            val team = teamService!!.getTeam(teamId)
            val employeeToRemove = team.employees.stream().filter { e: EmployeeDto -> e.id == employeeId }.findFirst().orElse(null)
            if (employeeToRemove != null) {
                team.employees.remove(employeeToRemove)
                teamService.updateTeam(team)
            }
            "redirect:/updateTeamForm/$teamId"
        } else {
            "AccessDenied"
        }
    }

    @GetMapping("/addTaskToTeamForm")
    fun addTaskToTeamForm(theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (!CurrentSessionInfo.isAdmin()) {
            return "AccessDenied"
        }
        var theTask = theModel.getAttribute("task") as? TaskDto
        if (theTask == null) {
            theTask = TaskDto()
        }
        theModel["task"] = theTask
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["admin"] = CurrentSessionInfo.isAdmin()
        theModel["teams"] = teamService!!.teams
        return "TaskToTeamForm"
    }

    @PostMapping("assignTaskToTeam/{teamId}")
    fun assignTaskToTeam(@ModelAttribute("task") theTaskDto: TaskDto?, theModel: Model,
                         @PathVariable("teamId") teamId: Int): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (!CurrentSessionInfo.isAdmin()) {
            return "AccessDenied"
        }
        val assignedTask = teamService!!.assignTaskToTeam(teamId, theTaskDto)
        return if (assignedTask != null) {
            "redirect:/updateTaskForm/" + assignedTask.id
        } else {

            if (theTaskDto != null) {
                theModel["task"] = theTaskDto
            }
           theModel["error"] = true
           addTaskToTeamForm(theModel)
        }
    }

    @GetMapping("/randomPopulationForm")
    fun randomPopulationForm(theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (!CurrentSessionInfo.isAdmin()) {
            return "AccessDenied"
        }
        val input = RandomPopulationInputDto()
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["admin"] = CurrentSessionInfo.isAdmin()
        theModel["input"] = input
        return "RandomPopulationForm"
    }

    @PostMapping("/randomPopulation")
    fun randomPopulation(@ModelAttribute("input") input: RandomPopulationInputDto?, theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (!CurrentSessionInfo.isAdmin()) {
            return "AccessDenied"
        }
        val result = teamService!!.randomPopulation(input)
        if (result == "invalid input") {
            theModel["error"] = true
            return randomPopulationForm(theModel)
        }
        return "redirect:/home"
    }

    @GetMapping("/addEmployeeForm")
    fun addEmployeeForm(theModel: Model): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (!CurrentSessionInfo.isAdmin()) {
            return "AccessDenied"
        }
        val employee = EmployeeDto()
        theModel["employee"] = employee
        theModel["user"] = CurrentSessionInfo.getUser()
        theModel["admin"] = CurrentSessionInfo.isAdmin()
        return "EmployeeForm"
    }

    @PostMapping("/saveEmployee/{admin}")
    fun saveEmployee(@ModelAttribute("employee") employeeDto: EmployeeDto?, theModel: Model?,
                     @PathVariable("admin") admin: Int): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (!CurrentSessionInfo.isAdmin()) {
            return "AccessDenied"
        }
        employeeService!!.addEmployee(admin, employeeDto)
        return "redirect:/employees"
    }

    @GetMapping("/deleteEmployee/{employeeId}")
    fun deleteEmployee(@PathVariable("employeeId") employeeId: Int, theModel: Model?): String {
        if (session == null) {
            return "redirect:/showLoginForm"
        }
        if (!CurrentSessionInfo.isAdmin()) {
            return "AccessDenied"
        }
        val result = employeeService!!.deleteEmployee(employeeId.toString())
        return if (result.contains("logout")) "redirect:/logout" else "redirect:/employees"
    }

    companion object {
        private var session: CurrentSessionInfo? = null
    }
}