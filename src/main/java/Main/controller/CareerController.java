package Main.controller;

import Main.entity.Career;
import Main.service.CareerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/careers")
public class CareerController {

    private final CareerService service;

    public CareerController(CareerService service) {
        this.service = service;
    }

    @GetMapping("/actual")
    public List<Career> getAllActual() {
        return service.getAllActualCareer();
    }

    @GetMapping("/actual/{employeeId}")
    public Career getActual(@PathVariable Integer employeeId) {
        return service.getActualCareer(employeeId);
    }

    @PostMapping
    public void create(@RequestBody Career career) {
        service.create(career);
    }

    @PutMapping
    public void update(@RequestBody Career career) {
        service.update(career);
    }

    @GetMapping("/filter")
    public List<Career> filter(
            @RequestParam(required = false) Integer employeeId,
            @RequestParam(required = false) String post,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Integer salary,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return service.findByFilters(employeeId, post, department, salary, startDate, endDate);
    }
}