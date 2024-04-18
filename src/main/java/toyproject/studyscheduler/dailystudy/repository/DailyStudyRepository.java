package toyproject.studyscheduler.dailystudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import toyproject.studyscheduler.study.domain.entity.Study;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyStudyRepository extends JpaRepository<DailyStudy, Long> {

    List<DailyStudy> findAllByStudy(Study study);

//    @Query("select s from DailyStudy s where s.studyDate >= :startDate and s.studyDate <= :endDate ")
//    List<DailyStudy> findAllByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
