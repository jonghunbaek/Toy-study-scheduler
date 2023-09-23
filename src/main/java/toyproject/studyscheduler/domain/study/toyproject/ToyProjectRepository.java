package toyproject.studyscheduler.domain.study.toyproject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ToyProjectRepository extends JpaRepository<ToyProject, Long> {

    @Query("select t from ToyProject t where (t.startDate >= :startDate and t.startDate <= :endDate)" +
        "or (t.endDate >= :startDate and t.endDate <= :endDate) ")
    List<ToyProject> findAllByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
