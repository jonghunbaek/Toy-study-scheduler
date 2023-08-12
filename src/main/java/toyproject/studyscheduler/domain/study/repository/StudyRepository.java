package toyproject.studyscheduler.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyproject.studyscheduler.domain.study.Study;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
}
