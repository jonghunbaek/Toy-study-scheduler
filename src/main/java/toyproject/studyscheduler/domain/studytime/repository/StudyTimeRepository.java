package toyproject.studyscheduler.domain.studytime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.studytime.StudyTime;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {

    @Query("select s from StudyTime s where s.date >= :startDate and s.date <= :endDate ")
    List<StudyTime> findAllByPeriod(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

    StudyTime findFirstByStudyOrderByDateDesc(Study study);
}
