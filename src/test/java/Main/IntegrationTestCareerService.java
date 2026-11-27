package Main;

import Main.entity.Career;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class IntegrationTestCareerService {

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

    private Career career(int employeeId) {
        return new Career(employeeId, "Должность", "департамент",
                200, LocalDate.of(2025, 1, 10), LocalDate.of(2025, 12,1));
    }

    @Test
    void save_invalidCareer_allnull() {
        Career invalid = new Career();

        try {
            careerRepository.saveAndFlush(invalid);
            fail("Ожидалось исключение валидации");
        } catch (ConstraintViolationException e) {
            System.out.println("Поймали: " + e.getClass().getSimpleName());
            e.getConstraintViolations().forEach(v ->
                    System.out.println("  поле: " + v.getPropertyPath() + " — " + v.getMessage()));
        }
    }

    @Test
    void save_invalidEmployee_smallsize() {
        Career invalid = new Career(-1, "Должность", "департамент",
                200, LocalDate.of(2025, 1, 10));

        try {
            careerRepository.saveAndFlush(invalid);
            fail("Ожидалось исключение валидации");
        } catch (ConstraintViolationException e) {
            System.out.println("Поймали: " + e.getClass().getSimpleName());
            e.getConstraintViolations().forEach(v ->
                    System.out.println("  поле: " + v.getPropertyPath() + " — " + v.getMessage()));
        }
    }

    @Test
    void save_invalidEmployee_bigsize() {
        String str = "f".repeat(300);
        Career invalid = new Career(1, str,"департамент",
                200, LocalDate.of(2025, 1, 10));

        try {
            careerRepository.saveAndFlush(invalid);
            fail("Ожидалось исключение валидации");
        } catch (ConstraintViolationException e) {
            System.out.println("Поймали: " + e.getClass().getSimpleName());
            e.getConstraintViolations().forEach(v ->
                    System.out.println("  поле: " + v.getPropertyPath() + " — " + v.getMessage()));
        }
    }

    @Test
    void CareerSerivce_getActualCareer_test() throws Exception{
        careerService.saveCareer(career(1));
        Career career1 = career(1);
        career1.setStartDate(LocalDate.now());
        career1.setEndDate(null);
        careerService.saveCareer(career1);
        Career fromDb = careerRepository.findActualByEmployeeId(1);
        assertEquals(LocalDate.now(), fromDb.getStartDate());
        assertNull(fromDb.getEndDate());
    }

    @Test
    void CareerSerivce_getAllActualCareer_test() throws Exception{
        careerService.saveCareer(career(1));
        Career career1 = career(1);
        career1.setStartDate(LocalDate.now());
        career1.setEndDate(null);
        careerService.saveCareer(career1);
        careerService.saveCareer(career(2));
        Career career2 = career(2);
        career2.setStartDate(LocalDate.now());
        career2.setEndDate(null);
        careerService.saveCareer(career2);
        List<Career> fromDb = careerRepository.findAllActual();
        assertEquals(2, fromDb.size());
        assertEquals(LocalDate.now(), fromDb.get(0).getStartDate());
        assertNull(fromDb.get(0).getEndDate());
        assertEquals(LocalDate.now(), fromDb.get(1).getStartDate());
        assertNull(fromDb.get(1).getEndDate());
    }
}
