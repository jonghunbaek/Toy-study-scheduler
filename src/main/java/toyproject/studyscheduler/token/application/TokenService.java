package toyproject.studyscheduler.token.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.token.application.dto.TokenCreationInfo;
import toyproject.studyscheduler.auth.web.dto.Tokens;
import toyproject.studyscheduler.common.jwt.JwtManager;

@RequiredArgsConstructor
@Service
public class TokenService {

    // TODO :: 추후 시간되면 토큰 매니저를 추상화하기
    private final JwtManager jwtManager;

    @Transactional
    public Tokens createTokens(TokenCreationInfo tokenCreationInfo) {
        String accessToken = jwtManager.createAccessToken(tokenCreationInfo.getMemberId(), tokenCreationInfo.getRole());
        String refreshToken = jwtManager.createRefreshToken();

        return Tokens.of(accessToken, refreshToken);
    }
}
