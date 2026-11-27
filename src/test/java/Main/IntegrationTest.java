package Main;

import Main.entity.Career;
import Main.entity.Employee;
import Main.fullEmpCar.FullEmpCar;
import Main.repository.CareerRepository;
import Main.repository.EmployeeRepository;
import Main.service.CareerService;
import Main.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.validation.ConstraintViolationException;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class IntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CareerService careerService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CareerRepository careerRepository;

    @BeforeEach
    void cleanUp() {
        employeeRepository.deleteAll();
        careerRepository.deleteAll();
    }

    private Employee employee(int employeeId, String fullName) {
        return new Employee(employeeId, fullName, LocalDate.of(1990, 5, 15),
                "1234 567890", "11223344556", "123456789012",
                "89008124567", "qwerty@mail.ru", "г. Пермь, ул. Ленина, 1",
                "Трудоустроен", LocalDateTime.now());
    }

    @Test
    void save_invalidEmployee_allnull() {
        Employee invalid = new Employee();

        try {
            employeeRepository.saveAndFlush(invalid);
            fail("Ожидалось исключение валидации");
        } catch (ConstraintViolationException e) {
            System.out.println("Поймали: " + e.getClass().getSimpleName());
            e.getConstraintViolations().forEach(v ->
                    System.out.println("  поле: " + v.getPropertyPath() + " — " + v.getMessage()));
        }
    }

    @Test
    void save_invalidEmployee_smallsize() {
        Employee invalid = new Employee(
                1, "", LocalDate.of(1990, 5, 15),
                "1234567890", "11223344556", "123456789012",
                "89001274567", "qwerty@mail.ru", "г. Пермь",
                "Трудоустроен", LocalDateTime.now());

        try {
            employeeRepository.saveAndFlush(invalid);
            fail("Ожидалось исключение валидации");
        } catch (ConstraintViolationException e) {
            System.out.println("Поймали: " + e.getClass().getSimpleName());
            e.getConstraintViolations().forEach(v ->
                    System.out.println("  поле: " + v.getPropertyPath() + " — " + v.getMessage()));
        }
    }

    @Test
    void save_invalidEmployee_bigsize() {
        Employee invalid = new Employee(
                1, "qwerty", LocalDate.of(1990, 5, 15),
                "123456789012345678901234567890", "11223344556", "123456789012",
                "79001234567", "qwerty@mail.ru", "г. Пермь",
                "Трудоустроен", LocalDateTime.now());

        try {
            employeeRepository.saveAndFlush(invalid);
            fail("Ожидалось исключение валидации");
        } catch (ConstraintViolationException e) {
            System.out.println("Поймали: " + e.getClass().getSimpleName());
            e.getConstraintViolations().forEach(v ->
                    System.out.println("  поле: " + v.getPropertyPath() + " — " + v.getMessage()));
        }
    }

    @Test
    void EmployeeSerivce_findAll_test(){
        employeeService.saveEmployee(employee(1, "Ivan Ivanov"));
        employeeService.saveEmployee(employee(2, "Petr Ivanov"));
        List<Employee> fromDb = employeeRepository.findAll();
        assertEquals(2, fromDb.size());
    }

    @Test
    void EmployeeSerivce_findAllActual_test() throws Exception{
        employeeService.saveEmployee(employee(1, "Ivan Ivanov"));
        Thread.sleep(200);
        employeeService.saveEmployee(employee(1, "Petr Ivanov"));
        employeeService.saveEmployee(employee(2, "Ivan Ivanov"));
        Thread.sleep(200);
        employeeService.saveEmployee(employee(2, "Petr Ivanov"));
        List<Employee> fromDb = employeeRepository.findAllActual();
        assertEquals(2, fromDb.size());
    }

    @Test
    void EmployeeSerivce_findActualByEmployeeId_test() throws Exception{
        employeeService.saveEmployee(employee(1, "Ivan Ivanov"));
        Thread.sleep(200);
        employeeService.saveEmployee(employee(1, "Petr Ivanov"));
        Employee fromDb = employeeRepository.findActualByEmployeeId(1);
        assertEquals("Petr Ivanov", fromDb.getFullName());
    }

    @Test
    void EmployeeSerivce_getActiveEmployeeId_test() throws Exception{
        employeeService.saveEmployee(employee(1, "Ivan Ivanov"));
        employeeService.saveEmployee(employee(2, "Petr Ivanov"));
        Thread.sleep(200);
        Employee employee2 = employee(2, "Petr Ivanov");
        employee2.setWorkStatus("Уволен");
        employeeService.saveEmployee(employee2);
        List<Integer> fromDb = employeeRepository.findActiveEmployeeId();
        assertEquals(1, fromDb.size());
    }

    @Test
    void EmployeeSerivce_deleteEmployeeRecord_test() {
        employeeService.saveEmployee(employee(1, "Ivan Ivanov"));
        Employee fromDb = employeeRepository.findActualByEmployeeId(1);
        employeeRepository.deleteById(fromDb.getId());
        List<Employee> fromDb2 = employeeRepository.findAll();
        assertEquals(0, fromDb2.size());
    }

    @Test
    void EmployeeSerivce_endCareer_test() {
        employeeService.saveEmployee(employee(1, "Ivan Ivanov"));
        Career career1 = new Career(1, "Должность", "департамент",
                200, LocalDate.of(1990, 5, 15));
        careerRepository.save(career1);
        employeeService.endCareer(1,"Test");
        Employee fromDBE = employeeRepository.findActualByEmployeeId(1);
        assertEquals("Уволен", fromDBE.getWorkStatus());
        assertEquals("Test", fromDBE.getReasonDismissal());
        assertThat(careerRepository.findActualByEmployeeId(1)).isNull();
    }

    @Test
    void EmployeeSerivce_createFull_test() {
        Employee employee1 = employee(0, "Ivan Ivanov");
        Career career1 = new Career(0, "Должность", "департамент",
                200, null);
        FullEmpCar fullEmpCar = new FullEmpCar(employee1, career1);
        employeeService.createFull(fullEmpCar);
        Employee fromDBE = employeeRepository.findActualByEmployeeId(1);
        Career fromDBC = careerRepository.findActualByEmployeeId(1);
        assertThat(fromDBC.getStartDate()).isNotNull();
        assertThat(fromDBE.getAddDate()).isNotNull();
        assertEquals("Трудоустроен", fromDBE.getWorkStatus());
    }

    @Test
    void EmployeeSerivce_getAllFullInfo_test() throws Exception{
        employeeService.saveEmployee(employee(1, "Ivan Ivanov"));
        Thread.sleep(200);
        employeeService.saveEmployee(employee(1, "Ivan Ivanov new"));
        careerRepository.save(new Career(1, "Должность", "департамент",
                200, LocalDate.of(2024, 1, 10), LocalDate.of(2024, 12, 31)));
        Thread.sleep(200);
        careerRepository.save(new Career(1, "Должность new", "департамент",
                200, LocalDate.of(2025, 1, 1)));
        List<FullEmpCar> fromDb = employeeService.getAllFullInfo();
        assertEquals(1, fromDb.size());
        assertEquals("Ivan Ivanov new", fromDb.get(0).getEmployee().getFullName());
        assertEquals("Должность new", fromDb.get(0).getCareer().getPost());
    }
}