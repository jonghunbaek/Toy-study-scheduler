package toyproject.studyscheduler.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyproject.studyscheduler.domain.study.StudyTime;

@Repository
public interface StudyTimeRepository extends JpaRepository<StudyTime, Long> {

}
