package toyproject.studyscheduler.token.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.common.exception.ResponseCode;
import toyproject.studyscheduler.member.entity.Role;
import toyproject.studyscheduler.token.application.dto.TokenCreationInfo;
import toyproject.studyscheduler.auth.web.dto.Tokens;
import toyproject.studyscheduler.common.jwt.JwtManager;
import toyproject.studyscheduler.token.entity.RefreshToken;
import toyproject.studyscheduler.token.exception.TokenException;
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

    /**
     * Access Token, Refresh Token 재발행
     * Refresh Token은 jjwt라이브러리를 이용해 파싱하면서 만료시간, 위조 여부 1차검증
     * DB에 저장된 Refresh Token과 비교해 2차 검증을 한다. - DB에 저장된 값과 다르면 이미 재발행된 토큰이기 때문에 위조의 가능성이 생김
     * @return 새로운 access token과 기존 refresh token
     */
    @Transactional
    public Tokens reissueTokens(String accessTokens, String refreshToken) {
        String newRefreshToken = reissueRefreshToken(refreshToken);
        String newAccessToken = jwtManager.reissueAccessToken(accessTokens);

        return Tokens.of(newAccessToken, newRefreshToken);
    }

    private String reissueRefreshToken(String refreshToken) {
        jwtManager.validateRefreshToken(refreshToken);
        RefreshToken entity = findRefreshToken(refreshToken);

        String newRefresh = jwtManager.createRefreshToken();

        entity.updateNewToken(newRefresh);
        return newRefresh;
    }

    private RefreshToken findRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new TokenException(ResponseCode.E20000));
    }

}
