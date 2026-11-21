package Main.repository;

import Main.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CareerRepository extends JpaRepository<Career, Long> {
    @Query("SELECT c FROM Career c WHERE c.employeeId = :employeeId AND c.endDate IS NULL")
    Career findActualByEmployeeId(@Param("employeeId") Integer employeeId);

    @Query("SELECT c FROM Career c WHERE c.endDate IS NULL")
    List<Career> findAllActual();
}
