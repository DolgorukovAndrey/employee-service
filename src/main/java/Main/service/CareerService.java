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
        career.setStartDate(LocalDate.now());
        career.setEndDate(null);
        careerRepository.save(career);
    }

    public void saveCareer(Career career){
        careerRepository.save(career);
    }

    public void update(Career newCareer) {
        Integer employeeId = newCareer.getEmployeeId();

        Career current = careerRepository.findActualByEmployeeId(employeeId);

        if (current != null) {
            current.setEndDate(LocalDate.now());
            careerRepository.save(current);
        }

        newCareer.setStartDate(LocalDate.now());
        newCareer.setEndDate(null);
        careerRepository.save(newCareer);
    }

    public Career getActualCareer(Integer employeeId){
        return careerRepository.findActualByEmployeeId(employeeId);
    }


    public List<Career> getAllActualCareer(){
        return careerRepository.findAllActual();
    }
}