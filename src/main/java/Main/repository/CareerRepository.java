package Main.repository;

import Main.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CareerRepository extends JpaRepository<Career, Long> {
    @Query("SELECT c FROM Career c WHERE c.employeeId = :employeeId AND c.endDate IS NULL")
    Career findActualByEmployeeId(@Param("employeeId") Integer employeeId);

    @Query("SELECT c FROM Career c WHERE c.endDate IS NULL")
    List<Career> findAllActual();

    @Query("SELECT c FROM Career c WHERE " +
            "(:employeeId IS NULL OR c.employeeId = :employeeId) AND " +
            "(:post IS NULL OR c.post LIKE %:post%) AND " +
            "(:department IS NULL OR c.department LIKE %:department%) AND " +
            "(:salary IS NULL OR c.salary = :salary) AND " +
            "(:startDate IS NULL OR c.startDate = :startDate) AND " +
            "(:endDate IS NULL OR c.endDate = :endDate)")
    List<Career> findByFilters(@Param("employeeId") Integer employeeId,
                               @Param("post") String post,
                               @Param("department") String department,
                               @Param("salary") Integer salary,
                               @Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate);
}
