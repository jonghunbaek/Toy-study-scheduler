package toyproject.studyscheduler.dailystudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;

import java.util.List;

@Repository
public interface DailyStudyRepository extends JpaRepository<DailyStudy, Long>, DailyStudyQuerydslRepository {

    List<DailyStudy> findAllByStudyId(Long studyId);

//    @Query("select s from DailyStudy s where s.studyDate >= :startDate and s.studyDate <= :endDate ")
//    List<DailyStudy> findAllByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
