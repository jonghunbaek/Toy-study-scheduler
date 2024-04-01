package toyproject.studyscheduler.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {

    // TODO : expectedEndDate -> realEndDate로 변경하기
    @Query("select s from Study s where (s.studyPeriod.startDate >= :startDate and s.studyPeriod.startDate <= :endDate)" +
        "or (s.studyPeriod.endDate >= :startDate and s.studyPeriod.endDate <= :endDate) ")
    List<Study> findAllByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
