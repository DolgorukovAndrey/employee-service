package Main.service;

import Main.entity.Career;
import Main.repository.CareerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CareerService {
    private final CareerRepository careerRepository;

    @Autowired
    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    public void create(Career career) {
        if (career == null) {
            throw new RuntimeException("Пустая запись не может быть создана");
        }
        career.setStartDate(LocalDate.now());
        career.setEndDate(null);
        careerRepository.save(career);
    }

    public void saveCareer(Career career){
        if (career == null) {
            throw new RuntimeException("Пустая запись не может быть создана");
        }
        careerRepository.save(career);
    }

    public void update(Career newCareer) {
        if (newCareer == null) {
            throw new RuntimeException("Пустая запись не может быть создана");
        }
        Integer employeeId = newCareer.getEmployeeId();
        Career current = careerRepository.findActualByEmployeeId(employeeId);
        if (current != null) {
            current.setEndDate(LocalDate.now());
            careerRepository.save(current);
        }
        create(newCareer);
    }

    public Career getActualCareer(Integer employeeId){
        return careerRepository.findActualByEmployeeId(employeeId);
    }


    public List<Career> getAllActualCareer(){
        return careerRepository.findAllActual();
    }

    public List<Career> findByFilters(Integer employeeId, String post, String department,
                                      Integer salary, LocalDate startDate, LocalDate endDate) {
        return careerRepository.findByFilters(employeeId, post, department, salary, startDate, endDate);
    }
}