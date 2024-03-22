package toyproject.studyscheduler.token.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.member.entity.Role;
import toyproject.studyscheduler.token.application.dto.TokenCreationInfo;
import toyproject.studyscheduler.auth.web.dto.Tokens;
import toyproject.studyscheduler.common.jwt.JwtManager;
import toyproject.studyscheduler.token.entity.RefreshToken;
import toyproject.studyscheduler.token.repository.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class TokenService {

    // TODO :: 추후 시간되면 토큰 매니저를 추상화하기
    private final JwtManager jwtManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Tokens createTokens(TokenCreationInfo tokenCreationInfo) {
        long memberId = tokenCreationInfo.getMemberId();
        Role role = tokenCreationInfo.getRole();

        String accessToken = jwtManager.createAccessToken(memberId, role);
        String refreshToken = jwtManager.createRefreshToken();

        saveRefreshToken(memberId, refreshToken);

        return Tokens.of(accessToken, refreshToken);
    }

    private void saveRefreshToken(Long memberId, String refreshToken) {
        refreshTokenRepository.findById(memberId)
            .ifPresentOrElse(
                refresh -> refresh.updateNewToken(refreshToken),
                () -> refreshTokenRepository.save(new RefreshToken(memberId, refreshToken))
            );
    }
}
