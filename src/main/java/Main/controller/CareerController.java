package Main.controller;

import Main.entity.Career;
import Main.service.CareerService;
import org.springframework.web.bind.annotation.*;

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
}