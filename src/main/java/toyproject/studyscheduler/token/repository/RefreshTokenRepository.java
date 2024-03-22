package toyproject.studyscheduler.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.studyscheduler.token.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
