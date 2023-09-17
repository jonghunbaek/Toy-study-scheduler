package toyproject.studyscheduler.domain.techstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.studyscheduler.domain.techstack.TechStack;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {
}
