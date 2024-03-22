package toyproject.studyscheduler.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.auth.application.dto.MemberInfo;
import toyproject.studyscheduler.auth.web.dto.Tokens;
import toyproject.studyscheduler.common.jwt.JwtManager;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtManager jwtManager;

    @Transactional
    public Tokens createTokens(MemberInfo memberInfo) {
        String accessToken = jwtManager.createAccessToken(memberInfo.getMemberId(), memberInfo.getRole());
        String refreshToken = jwtManager.createRefreshToken();

        return Tokens.of(accessToken, refreshToken);
    }
}
