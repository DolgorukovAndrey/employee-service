package unit;

import Main.entity.Career;
import Main.entity.Employee;
import Main.fullEmpCar.FullEmpCar;
import Main.repository.CareerRepository;
import Main.repository.EmployeeRepository;
import Main.service.CareerService;
import Main.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private CareerRepository careerRepository;

    @Mock
    private CareerService careerService;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void employeeService_endCareer_exceptionNoCareer_test() {
        when(careerService.getActualCareer(1)).thenReturn(null);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> employeeService.endCareer(1, "Причина"));
        assertEquals("Карьера не найдена", exception.getMessage());
        verify(employeeRepository, never()).save(any());
        verify(careerService, never()).saveCareer(any());
    }

    @Test
    void employeeService_endCareer_exceptionNoEmployee_test() {
        Career career1 = new Career(1, "Должность", "департамент",
                200, LocalDate.of(1990, 5, 15));
        when(careerService.getActualCareer(1)).thenReturn(career1);
        when(employeeService.getActualEmployee(1)).thenReturn(null);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> employeeService.endCareer(1, "Причина"));
        assertEquals("Сотрудник не найден", exception.getMessage());
        verify(employeeRepository, never()).save(any());
        verify(careerService, never()).saveCareer(any());
    }

    @Test
    void employeeService_saveEmployee_test() {
        Employee input = new Employee(1, "Ivan Ivanov", LocalDate.now(), "0000000000",
                "-", "-","88888888888", "qwerty@mail.ru", "address",
                "Трудоустроен", LocalDateTime.now());
        Employee saved = new Employee(1L, 1, "Ivan Ivanov", LocalDate.now(), "0000000000",
                "-", "-","88888888888", "qwerty@mail.ru", "address",
                "Трудоустроен", null,LocalDateTime.now());
        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);
        employeeService.saveEmployee(input);
        verify(employeeRepository).save(input);
    }

    @Test
    void employeeService_createFull_assignsNextIdAfterMax_test() {
        when(employeeRepository.findMaxEmployeeId()).thenReturn(5);
        Employee employee = new Employee();
        FullEmpCar fullEmpCar = new FullEmpCar(employee, new Career());
        employeeService.createFull(fullEmpCar);
        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());
        assertEquals(6, captor.getValue().getEmployeeId());
    }

    @Test
    void employeeService_createFull_assignsNextIdAfterNull_test() {
        when(employeeRepository.findMaxEmployeeId()).thenReturn(null);
        Employee employee = new Employee();
        FullEmpCar fullEmpCar = new FullEmpCar(employee, new Career());
        employeeService.createFull(fullEmpCar);
        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());
        assertEquals(1, captor.getValue().getEmployeeId());
    }

    @Test
    void employeeService_createFull_usesSameIdForCareer_test() {
        when(employeeRepository.findMaxEmployeeId()).thenReturn(5);
        Employee employee = new Employee();
        Career career = new Career();
        FullEmpCar fullEmpCar = new FullEmpCar(employee, career);
        employeeService.createFull(fullEmpCar);
        verify(careerService).create(career);
        assertEquals(6, career.getEmployeeId());
    }

    @Test
    void employeeService_createFull_setsWorkStatusAndAddDate_test() {
        when(employeeRepository.findMaxEmployeeId()).thenReturn(5);
        Employee employee = new Employee();
        FullEmpCar fullEmpCar = new FullEmpCar(employee, new Career());
        employeeService.createFull(fullEmpCar);
        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());
        Employee saved = captor.getValue();
        assertEquals("Трудоустроен", saved.getWorkStatus());
        assertNotNull(saved.getAddDate());
    }
}