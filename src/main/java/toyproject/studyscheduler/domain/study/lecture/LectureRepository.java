package toyproject.studyscheduler.domain.study.lecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Query("select l from Lecture l where (l.startDate >= :startDate and l.startDate <= :endDate)" +
        "or (l.expectedEndDate >= :startDate and l.expectedEndDate <= :endDate) ")
    List<Lecture> findAllByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
