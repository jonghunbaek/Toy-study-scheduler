package toyproject.studyscheduler.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.studyscheduler.token.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String refreshToken);
}
