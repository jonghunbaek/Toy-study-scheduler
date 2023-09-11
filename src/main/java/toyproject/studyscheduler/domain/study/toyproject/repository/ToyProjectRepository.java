package toyproject.studyscheduler.domain.study.toyproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;

@Repository
public interface ToyProjectRepository extends JpaRepository<ToyProject, Long> {
}
