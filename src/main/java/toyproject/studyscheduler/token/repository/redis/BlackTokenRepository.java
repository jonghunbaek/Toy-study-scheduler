package toyproject.studyscheduler.token.repository.redis;

import org.springframework.data.repository.CrudRepository;
import toyproject.studyscheduler.token.domain.BlackToken;

public interface BlackTokenRepository extends CrudRepository<BlackToken, String> {
}
