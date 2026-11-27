package Main.repository;

import Main.entity.Employee;
import Main.fullEmpCar.FullEmpCar;
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

    @Query("SELECT e.employeeId FROM Employee e WHERE e.workStatus = 'Трудоустроен' " +
            "AND e.addDate = (SELECT MAX(e2.addDate) FROM Employee e2 WHERE e2.employeeId = e.employeeId)")
    List<Integer> findActiveEmployeeId();

    @Query("SELECT new Main.fullEmpCar.FullEmpCar(e, c) " +
            "FROM Employee e " +
            "JOIN Career c ON e.employeeId = c.employeeId " +
            "WHERE e.addDate = (SELECT MAX(e2.addDate) FROM Employee e2 WHERE e2.employeeId = e.employeeId) " +
            "AND c.endDate IS NULL")
    List<FullEmpCar> findAllActualWithCareerObjects();

    @Query("SELECT new Main.fullEmpCar.FullEmpCar(e, c) " +
            "FROM Employee e " +
            "JOIN Career c ON e.employeeId = c.employeeId " +
            "WHERE e.addDate = (SELECT MAX(e2.addDate) FROM Employee e2 WHERE e2.employeeId = e.employeeId) " +
            "AND c.endDate IS NULL " +
            // --- Поля Employee ---
            "AND (:employeeId IS NULL OR e.employeeId = :employeeId) " +
            "AND (:fullName IS NULL OR e.fullName LIKE %:fullName%) " +
            "AND (:birthDate IS NULL OR e.birthDate = :birthDate) " +
            "AND (:passport IS NULL OR e.passport LIKE %:passport%) " +
            "AND (:snils IS NULL OR e.snils LIKE %:snils%) " +
            "AND (:inn IS NULL OR e.inn LIKE %:inn%) " +
            "AND (:phoneNumber IS NULL OR e.phoneNumber LIKE %:phoneNumber%) " +
            "AND (:mail IS NULL OR e.mail LIKE %:mail%) " +
            "AND (:address IS NULL OR e.address LIKE %:address%) " +
            "AND (:workStatus IS NULL OR e.workStatus = :workStatus) " +
            "AND (:reasonDismissal IS NULL OR e.reasonDismissal LIKE %:reasonDismissal%) " +
            // --- Поля Career ---
            "AND (:post IS NULL OR c.post LIKE %:post%) " +
            "AND (:department IS NULL OR c.department LIKE %:department%) " +
            "AND (:salary IS NULL OR c.salary = :salary) " +
            "AND (:startDate IS NULL OR c.startDate = :startDate)")
    List<FullEmpCar> findByFullFilters(
            // Employee
            @Param("employeeId") Integer employeeId,
            @Param("fullName") String fullName,
            @Param("birthDate") LocalDate birthDate,
            @Param("passport") String passport,
            @Param("snils") String snils,
            @Param("inn") String inn,
            @Param("phoneNumber") String phoneNumber,
            @Param("mail") String mail,
            @Param("address") String address,
            @Param("workStatus") String workStatus,
            @Param("reasonDismissal") String reasonDismissal,
            // Career
            @Param("post") String post,
            @Param("department") String department,
            @Param("salary") Integer salary,
            @Param("startDate") LocalDate startDate
    );
}