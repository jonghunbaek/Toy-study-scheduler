package toyproject.studyscheduler.domain.study.reading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReadingRepository extends JpaRepository<Reading, Long> {

    @Query("select r from Reading r where (r.startDate >= :startDate and r.startDate <= :endDate)" +
        "or (r.endDate >= :startDate and r.endDate <= :endDate) ")
    List<Reading> findAllByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
