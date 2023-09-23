package toyproject.studyscheduler.domain.function;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.studyscheduler.domain.function.RequiredFunction;

public interface RequiredFunctionRepository extends JpaRepository<RequiredFunction, Long> {
}
