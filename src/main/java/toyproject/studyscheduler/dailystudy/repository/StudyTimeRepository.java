package toyproject.studyscheduler.dailystudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import toyproject.studyscheduler.study.domain.entity.Study;
import toyproject.studyscheduler.dailystudy.domain.StudyTime;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {

    @Query("select s from StudyTime s where s.date >= :startDate and s.date <= :endDate ")
    List<StudyTime> findAllByPeriod(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

    StudyTime findFirstByStudyOrderByDateDesc(Study study);
}
