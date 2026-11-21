package Main.repository;

import Main.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE e.employeeId = :employeeId AND e.addDate = (SELECT MAX(e2.addDate) FROM Employee e2 WHERE e2.employeeId = :employeeId)")
    Employee findActualByEmployeeId(@Param("employeeId") Integer employeeId);

    @Query("SELECT e FROM Employee e WHERE " +
            "(:employeeId IS NULL OR e.employeeId = :employeeId) AND " +
            "(:fullName IS NULL OR e.fullName LIKE %:fullName%) AND " +
            "(:birthDate IS NULL OR e.birthDate = :birthDate) AND " +
            "(:passport IS NULL OR e.passport LIKE %:passport%) AND " +
            "(:snils IS NULL OR e.snils LIKE %:snils%) AND " +
            "(:inn IS NULL OR e.inn LIKE %:inn%) AND " +
            "(:phoneNumber IS NULL OR e.phoneNumber LIKE %:phoneNumber%) AND " +
            "(:mail IS NULL OR e.mail LIKE %:mail%) AND " +
            "(:address IS NULL OR e.address LIKE %:address%) AND " +
            "(:workStatus IS NULL OR e.workStatus = :workStatus)")
    List<Employee> findByFilters(@Param("employeeId") Integer employeeId,
                                 @Param("fullName") String fullName,
                                 @Param("birthDate") LocalDate birthDate,
                                 @Param("passport") String passport,
                                 @Param("snils") String snils,
                                 @Param("inn") String inn,
                                 @Param("phoneNumber") String phoneNumber,
                                 @Param("mail") String mail,
                                 @Param("address") String address,
                                 @Param("workStatus") String workStatus);

    @Query("SELECT e FROM Employee e WHERE e.addDate = " +
            "(SELECT MAX(e2.addDate) FROM Employee e2 WHERE e2.employeeId = e.employeeId)")
    List<Employee> findAllActual();

    @Query("SELECT MAX(e.employeeId) FROM Employee e")
    Integer findMaxEmployeeId();
}