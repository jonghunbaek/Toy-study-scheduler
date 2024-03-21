package toyproject.studyscheduler.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.auth.application.dto.MemberInfo;
import toyproject.studyscheduler.auth.web.dto.Tokens;
import toyproject.studyscheduler.common.jwt.JwtProvider;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtProvider jwtProvider;

    @Transactional
    public Tokens createTokens(MemberInfo memberInfo) {
        String accessToken = jwtProvider.createAccessToken(memberInfo.getMemberId(), memberInfo.getRole());
        String refreshToken = jwtProvider.createRefreshToken();

        return Tokens.of(accessToken, refreshToken);
    }
}
