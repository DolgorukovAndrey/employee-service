package Main.controller;

import Main.entity.Employee;
import Main.fullEmpCar.FullEmpCar;
import Main.service.EmployeeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Employee> getAll() {
        return service.getAllEmployee();
    }

    @GetMapping("/actual")
    public List<Employee> getAllActual() {
        return service.getAllActualEmployee();
    }

    @GetMapping("/actual/{employeeId}")
    public Employee getActual(@PathVariable Integer employeeId) {
        return service.getActualEmployee(employeeId);
    }

    @PostMapping
    public void create(@RequestBody Employee employee) {
        service.saveEmployee(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteRecord(@PathVariable Long id) {
        service.deleteEmployeeRecord(id);
    }

    @GetMapping("/filter")
    public List<Employee> filter(
            @RequestParam(required = false) Integer employeeId,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(required = false) String passport,
            @RequestParam(required = false) String snils,
            @RequestParam(required = false) String inn,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String mail,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String workStatus) {
        return service.filtersEmployee(employeeId, fullName, birthDate, passport, snils,
                inn, phoneNumber, mail, address, workStatus);
    }

    @PostMapping("/new")
    public void createFull(@RequestBody FullEmpCar fullEmpCar) {
        service.createFull(fullEmpCar);
    }

    @PostMapping("/{employeeId}/dismiss")
    public void endCareer(@PathVariable Integer employeeId, @RequestParam String reasonDismissal) {
        service.endCareer(employeeId, reasonDismissal);
    }

}