package Main.service;

import Main.entity.Career;
import Main.entity.Employee;
import Main.fullEmpCar.FullEmpCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Main.repository.EmployeeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final CareerService careerService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, CareerService careerService) {
        this.employeeRepository = employeeRepository;
        this.careerService = careerService;
    }

    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    public List<Employee> getAllActualEmployee(){
        return employeeRepository.findAllActual();
    }

    public Employee getActualEmployee(Integer employeeId){
        return employeeRepository.findActualByEmployeeId(employeeId);
    }

    public List<Integer> getActiveEmployeeId() {
        return employeeRepository.findActiveEmployeeId();
    }

    public List<FullEmpCar> getAllFullInfo() {
        return employeeRepository.findAllActualWithCareerObjects();
    }

    public void saveEmployee(Employee employee) {
        employee.setAddDate(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    public void createFull(FullEmpCar fullEmpCar) {
        Employee employee = fullEmpCar.getEmployee();
        Career career = fullEmpCar.getCareer();
        Integer maxId = employeeRepository.findMaxEmployeeId();
        int nextId = (maxId == null) ? 1 : maxId + 1;
        employee.setEmployeeId(nextId);
        employee.setWorkStatus("Трудоустроен");
        saveEmployee(employee);
        career.setEmployeeId(nextId);
        careerService.create(career);
    }

    public void deleteEmployeeRecord(Long id) {
        employeeRepository.deleteById(id);
    }

    public void endCareer(Integer employeeId, String reasonDismissal){
        Career currentCareer = careerService.getActualCareer(employeeId);
        Employee currentEmployee = employeeRepository.findActualByEmployeeId(employeeId);
        if (currentCareer == null) {
            throw new RuntimeException("Карьера не найдена");
        }
        if (currentEmployee == null) {
            throw new RuntimeException("Сотрудник не найден");
        }
        currentEmployee.setReasonDismissal(reasonDismissal);
        currentEmployee.setWorkStatus("Уволен");
        currentCareer.setEndDate(LocalDate.now());
        careerService.saveCareer(currentCareer);
        employeeRepository.save(currentEmployee);
    }

    public List<Employee> findByFilters(Integer employeeId,
                                          String fullName,
                                          LocalDate birthDate,
                                          String passport,
                                          String snils,
                                          String inn,
                                          String phoneNumber,
                                          String mail,
                                          String address,
                                          String workStatus){
        return employeeRepository.findByFilters(employeeId, fullName, birthDate,
                passport, snils, inn, phoneNumber, mail, address, workStatus);
    }

    public List<FullEmpCar> filterFullInfo(
            Integer employeeId, String fullName, LocalDate birthDate, String passport,
            String snils, String inn, String phoneNumber, String mail, String address,
            String workStatus, String reasonDismissal, String post, String department,
            Integer salary, LocalDate startDate) {

        return employeeRepository.findByFullFilters(employeeId, fullName, birthDate, passport,
                snils, inn, phoneNumber, mail, address, workStatus, reasonDismissal,
                post, department, salary, startDate);
    }
}
