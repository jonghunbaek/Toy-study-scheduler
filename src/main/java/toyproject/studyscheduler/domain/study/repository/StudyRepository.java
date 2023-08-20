package toyproject.studyscheduler.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import toyproject.studyscheduler.domain.study.Study;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {

    @Query("select s from Study s where s.startDate >= :checkStartDate and s.startDate <= :checkEndDate ")
    List<Study> findAllByPeriodIn(@Param("checkStartDate") LocalDate checkStartDate, @Param("checkEndDate") LocalDate checkEndDate);
}
