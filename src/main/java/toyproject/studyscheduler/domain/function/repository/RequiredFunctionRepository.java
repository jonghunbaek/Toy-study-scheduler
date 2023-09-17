package toyproject.studyscheduler.domain.function.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.studyscheduler.domain.function.RequiredFunction;

public interface RequiredFunctionRepository extends JpaRepository<RequiredFunction, Long> {
}
