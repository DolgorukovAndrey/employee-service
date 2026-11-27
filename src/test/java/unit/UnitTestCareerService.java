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
public class UnitTestCareerService {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private CareerRepository careerRepository;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private CareerService careerService;

    @Test
    void careerService_update_currentIsNull_test() {
        when(careerRepository.findActualByEmployeeId(1)).thenReturn(null);
        Career newCareer = new Career(1, "Должность", "департамент",
                200, LocalDate.of(2024, 1, 10), LocalDate.of(2024, 12, 31));
        careerService.update(newCareer);
        verify(careerRepository, times(1)).save(any(Career.class));
        assertEquals(LocalDate.now(), newCareer.getStartDate());
        assertNull(newCareer.getEndDate());
    }

    @Test
    void careerService_update_currentNotNull_test() {
        Career current = new Career(1, "Должность", "департамент",
                200, LocalDate.of(2024, 1, 10));
        when(careerRepository.findActualByEmployeeId(1)).thenReturn(current);
        Career newCareer = new Career(1, "ДолжностьNew", "департамент",
                200, LocalDate.of(2025, 1, 10), LocalDate.of(2025, 12, 31));
        careerService.update(newCareer);
        verify(careerRepository, times(2)).save(any(Career.class));
        assertEquals(LocalDate.now(), newCareer.getStartDate());
        assertNull(newCareer.getEndDate());
        assertEquals(LocalDate.now(), current.getEndDate());
    }

    @Test
    void careerService_update_newCareerIsNull_test() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> careerService.update(null));
        assertEquals("Пустая запись не может быть создана", exception.getMessage());
        verify(careerRepository, never()).save(any());
    }

    @Test
    void careerService_create_careerIsNull_test() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> careerService.create(null));
        assertEquals("Пустая запись не может быть создана", exception.getMessage());
        verify(careerRepository, never()).save(any());
    }

    @Test
    void careerService_create_careerNotNull_test() {
        Career career = new Career(1, "Должность", "департамент",
                200, LocalDate.of(2025, 1, 10), LocalDate.of(2025, 12, 31));
        careerService.create(career);
        verify(careerRepository, times(1)).save(any(Career.class));
        assertEquals(LocalDate.now(), career.getStartDate());
        assertNull(career.getEndDate());
    }

    @Test
    void careerService_saveCareer_careerIsNull_test() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> careerService.saveCareer(null));
        assertEquals("Пустая запись не может быть создана", exception.getMessage());
        verify(careerRepository, never()).save(any());
    }

    @Test
    void careerService_saveCareer_careerNotNull_test() {
        Career career = new Career(1, "Должность", "департамент",
                200, LocalDate.now());
        careerService.saveCareer(career);
        verify(careerRepository, times(1)).save(any(Career.class));
    }


}
